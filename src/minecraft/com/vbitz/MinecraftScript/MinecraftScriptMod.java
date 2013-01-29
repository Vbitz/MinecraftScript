package com.vbitz.MinecraftScript;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.Configuration;

import com.vbitz.MinecraftScript.blocks.ScriptedBlock;
import com.vbitz.MinecraftScript.commands.APIKeyCommand;
import com.vbitz.MinecraftScript.commands.KeyValueStoreCommand;
import com.vbitz.MinecraftScript.commands.MinecraftScriptHelpCommand;
import com.vbitz.MinecraftScript.docs.HelpRegistry;
import com.vbitz.MinecraftScript.docs.KeyValueStore;
import com.vbitz.MinecraftScript.extend.BlockFunctions;
import com.vbitz.MinecraftScript.extend.IInternalExtendApi;
import com.vbitz.MinecraftScript.items.JSStick;
import com.vbitz.MinecraftScript.items.ScriptedItem;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingCommand;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;
import com.vbitz.MinecraftScript.web.MinecraftScriptHTTPServer;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="MinecraftScript", name="MinecraftScript", version="1.2.1") // mental note, update this loads
@NetworkMod(clientSideRequired=false, serverSideRequired=true)
public class MinecraftScriptMod {
	@Instance("MinecraftScriptMod")
	public static MinecraftScriptMod instance;
	
	@SidedProxy(clientSide="com.vbitz.MinecraftScript.ClientProxy", serverSide="com.vbitz.MinecraftScript.CommonProxy")
	public static CommonProxy proxy;

	private static MinecraftScriptMod _singilton = null;
	
	private static File scriptsDirectory = null;
	private static File publicHtmlDirectory = null;
	public static File loggingDirectory = null;
	
	private static final DateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	
	private static Logger mcLogger = Logger.getLogger("MinecraftScriptMod");
	
	private static ScriptedBlock blocks[];
	private static ScriptedItem items[];
	
	private boolean webServerStarted = false;
	
	private static boolean clientSideEnabled = true;
	private static boolean unsafeModeEnabled = false;
	
	private int scriptedBlockIdStart = 1400;
	private int scriptedBlockCount = 16;
	
	private int scriptedItemIdStart = 7312;
	private int scriptedItemIdCount = 16;
	
	private int jsStickId = 7311;
	
	private static IInternalExtendApi[] _internalApis = new IInternalExtendApi[] {
		new BlockFunctions()
	};
	
	@PreInit
	public void preInit(FMLPreInitializationEvent e) {
		File baseDirectory = new File(e.getModConfigurationDirectory(), "minecraftScript");
		scriptsDirectory = new File(baseDirectory, "scripts");
		publicHtmlDirectory = new File(baseDirectory, "public_html");
		loggingDirectory = new File(baseDirectory, "logs");
		KeyValueStore.load(new File(baseDirectory, "mcsKeyValueStore.dat"));
		
		if (!baseDirectory.exists()) {
			baseDirectory.mkdir();
		}
		
		if (!scriptsDirectory.exists()) {
			scriptsDirectory.mkdir();
		}
		if (!publicHtmlDirectory.exists()) {
			publicHtmlDirectory.mkdir();
		}
		if (!loggingDirectory.exists()) {
			loggingDirectory.mkdir();
		}
		
		Configuration config = new Configuration(e.getSuggestedConfigurationFile());
		
		config.load();
		
		clientSideEnabled = config.get(Configuration.CATEGORY_GENERAL, "clientSideEnabled", true).getBoolean(true);
		unsafeModeEnabled = config.get(Configuration.CATEGORY_GENERAL, "unsafeEnabled", false).getBoolean(false);
		
		scriptedBlockIdStart = config.get(Configuration.CATEGORY_BLOCK, "scriptedBlockIdStart", 1400).getInt(1400);
		scriptedBlockCount = config.get(Configuration.CATEGORY_BLOCK, "scriptedBlockCount", 16).getInt(16);
		
		scriptedItemIdStart = config.get(Configuration.CATEGORY_ITEM, "scriptedItemIdStart", 7312).getInt(7312);
		scriptedItemIdCount = config.get(Configuration.CATEGORY_ITEM, "scriptedItemIdCount", 16).getInt(16);
		
		jsStickId = config.get(Configuration.CATEGORY_ITEM, "jsStickId", 7311).getInt(7311);
		
		config.save();
	}
	
	@Init
	public void load(FMLInitializationEvent e) {
		proxy.registerRenderers();
		
		this.mcLogger.setParent(FMLLog.getLogger());
		
		createScriptedObjects();
		
		JSScriptingManager.getInstance().onServerLoad();
		
		HelpRegistry.load();
		
		if (clientSideEnabled) {
			JSStick.instance = new JSStick(jsStickId);
			
			EntityRegistry.registerModEntity(ScriptedThrowable.class, "scriptedThrowable",
					1234, this, 50, 1, true); // need to change the id maybe
		}
		
		new MinecraftScriptHTTPServer(publicHtmlDirectory);
		
		TickRegistry.registerTickHandler(MinecraftScriptedTickManager.getInstance(), Side.SERVER);
		TickRegistry.registerTickHandler(MinecraftScriptHTTPServer.getInstance(), Side.SERVER);
		
		GameRegistry.registerWorldGenerator(new MinecraftScriptWorldGen());
	}
	
	private void createScriptedObjects() {
		if (!getClientSideEnabled()) {
			this.mcLogger.fine("Client Side Disabled");
			return;
		}
		blocks = new ScriptedBlock[scriptedBlockCount]; // this will get bigger in the future
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new ScriptedBlock(scriptedBlockIdStart + i);
			GameRegistry.registerBlock(blocks[i],"Scripted Block " + i);
			LanguageRegistry.addName(blocks[i], "Scripted Block " + i);
		}
		items = new ScriptedItem[scriptedItemIdCount]; // this will get bigger in the future
		for (int i = 0; i < items.length; i++) {
			items[i] = new ScriptedItem(scriptedItemIdStart + i); // allow people to configure this value
			LanguageRegistry.addName(items[i], "Scripted Item " + i);
		}
	}
	
	public ScriptedBlock getScriptedBlock(int id) {
		return blocks[id];
	}
	
	public ScriptedItem getScriptedItem(int id) {
		return items[id];
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent e) {
		for (IInternalExtendApi exApi : _internalApis) {
			exApi.init();
		}
	}
	
	@ServerStarting
	public void serverStarting(FMLServerStartingEvent e) {
		CommandHandler commandManager = (CommandHandler)e.getServer().getCommandManager();
		commandManager.registerCommand(new APIKeyCommand());
		commandManager.registerCommand(new JSScriptingCommand());
		commandManager.registerCommand(new MinecraftScriptHelpCommand());
		commandManager.registerCommand(new KeyValueStoreCommand());
		
		JSScriptingManager.getInstance().loadAllScripts(scriptsDirectory);
		
		MinecraftScriptHTTPServer.getInstance().start();
		
		webServerStarted = true;
	}
	
	@ServerStarted
	public void serverStarted(FMLServerStartedEvent e) {
	}
	
	@ServerStopping
	public void serverStopping(FMLServerStoppingEvent e) {
		MinecraftScriptHTTPServer.getInstance().stop();
		webServerStarted = false;
	}
	
	public static boolean getClientSideEnabled() {
		return clientSideEnabled;
	}
	
	public static boolean getUnsafeEnabled() {
		return unsafeModeEnabled;
	}

	public static MinecraftScriptMod getInstance() {
		if (_singilton == null) {
			_singilton = new MinecraftScriptMod();
		}
		return _singilton;
	}
	
	public static Logger getLogger() {
		return mcLogger;
	}

	public static File getLogFileWriter() {
		String filename = "logFile_" + logDateFormat.format(new Date()) + ".log";
		return new File(loggingDirectory, filename);
	}
}
