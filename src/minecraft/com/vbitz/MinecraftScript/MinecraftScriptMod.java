package com.vbitz.MinecraftScript;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.command.CommandHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import com.vbitz.MinecraftScript.blocks.ScriptedBlock;
import com.vbitz.MinecraftScript.blocks.SurvivalNode;
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
import com.vbitz.MinecraftScript.survival.MinecraftScriptSurvivalManager;
import com.vbitz.MinecraftScript.survival.SurvivalNodeManager;
import com.vbitz.MinecraftScript.titleEntitys.TileEntitySurvivalNode;
import com.vbitz.MinecraftScript.web.MinecraftScriptAPIKey;
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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="MinecraftScript", name="MinecraftScript", version="2.1.0") // mental note, update this loads
@NetworkMod(clientSideRequired=false, serverSideRequired=true)
public class MinecraftScriptMod {
	@Instance("MinecraftScriptMod")
	public static MinecraftScriptMod instance;
	
	@SidedProxy(clientSide="com.vbitz.MinecraftScript.ClientProxy", serverSide="com.vbitz.MinecraftScript.CommonProxy")
	public static CommonProxy proxy;

	private static MinecraftScriptMod _singilton = null;
	
	private static File scriptsDirectory = null;
	public static File loggingDirectory = null;
	private static File apiKeyFilename = null;
	
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
	
	private int survivalNodeId = 1417;
	
	private static int webServerPort = 12543;
	
	private static IInternalExtendApi[] _internalApis = new IInternalExtendApi[] {
		new BlockFunctions()
	};
	
	@PreInit
	public void preInit(FMLPreInitializationEvent e) {
		File baseDirectory = new File(e.getModConfigurationDirectory(), "minecraftScript");
		apiKeyFilename = new File(baseDirectory, "apiKeys.cfg");
		scriptsDirectory = new File(baseDirectory, "scripts");
		loggingDirectory = new File(baseDirectory, "logs");
		KeyValueStore.load(new File(baseDirectory, "mcsKeyValueStore.dat"));
		
		if (!baseDirectory.exists()) {
			baseDirectory.mkdir();
		}
		if (!scriptsDirectory.exists()) {
			scriptsDirectory.mkdir();
		}
		if (!loggingDirectory.exists()) {
			loggingDirectory.mkdir();
		}
		
		if (!new File(baseDirectory, "apiKeys.cfg").exists()) {
			try {
				FileWriter w = new FileWriter(apiKeyFilename);
				w.write("MinecraftScript APIKey File");
				w.close();
			} catch (IOException e1) {
				this.getLogger().warning("Failed to create APIKey Config File");
			}
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
		
		survivalNodeId = config.get(Configuration.CATEGORY_BLOCK, "survivalNodeId", 1417).getInt(1417);
		
		webServerPort = config.get("network", "webServerPort", 12543).getInt(12543);
		
		config.save();
	}
	
	@Init
	public void load(FMLInitializationEvent e) {
		proxy.registerRenderers();
		
		this.mcLogger.setParent(FMLLog.getLogger());
		
		this.mcLogger.info("MinecraftScript Version " + "2.1.0" + " Loading");
		
		createScriptedObjects();
		
		JSScriptingManager.getInstance().onServerLoad();
		
		HelpRegistry.load();
		
		if (clientSideEnabled) {
			JSStick.instance = new JSStick(jsStickId);
			
			EntityRegistry.registerModEntity(ScriptedThrowable.class, "scriptedThrowable",
					1234, this, 50, 1, true); // need to change the id maybe
			
			GameRegistry.registerTileEntity(TileEntitySurvivalNode.class,
					"com.vbitz.MinecraftScript.survivalNodeTile");
		
			SurvivalNode n = new SurvivalNode(survivalNodeId);
			GameRegistry.registerBlock(n,
					"com.vbitz.MinecraftScript.survivalNodeBlock");
			LanguageRegistry.addName(n, "Survival Node");
			
			GameRegistry.addShapedRecipe(new ItemStack(n), new Object[] {
				".*.",
				"* *",
				".*.",
				'*', Item.ingotIron,
				'.', Block.stoneBrick
			}); // pretty dam easy recipe
		}
		
		new MinecraftScriptHTTPServer();
		
		TickRegistry.registerTickHandler(MinecraftScriptTickManager.getInstance(), Side.SERVER);
		
		GameRegistry.registerWorldGenerator(new MinecraftScriptWorldGen());
		
		MinecraftForge.EVENT_BUS.register(new MinecraftScriptHookManager());
		
		MinecraftScriptAPIKey.loadAll(apiKeyFilename);
	}
	
	private void createScriptedObjects() {
		if (!getClientSideEnabled()) {
			this.mcLogger.fine("Client Side Disabled");
			return;
		}
		blocks = new ScriptedBlock[scriptedBlockCount];
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new ScriptedBlock(scriptedBlockIdStart + i);
			GameRegistry.registerBlock(blocks[i],"Scripted Block " + i);
			LanguageRegistry.addName(blocks[i], "Scripted Block " + i);
		}
		items = new ScriptedItem[scriptedItemIdCount];
		for (int i = 0; i < items.length; i++) {
			items[i] = new ScriptedItem(scriptedItemIdStart + i);
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
		
		JSScriptingManager.getInstance().loadAllScripts(scriptsDirectory, false);
		
		MinecraftScriptHTTPServer.getInstance().start();
		
		if (DimensionManager.getCurrentSaveRootDirectory().exists()) {
			JSScriptingManager.getInstance().loadAllScripts(new File(
					DimensionManager.getCurrentSaveRootDirectory(), "scripts"), true);
		}
		
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
	
	public static int getWebServerPort() {
		return webServerPort;
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

	public int getSurvivalNodeId() {
		return survivalNodeId;
	}
}
