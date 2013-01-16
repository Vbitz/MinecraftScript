package com.vbitz.MinecraftScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.logging.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;

import com.vbitz.MinecraftScript.blocks.ScriptedBlock;
import com.vbitz.MinecraftScript.commands.JSCommand;
import com.vbitz.MinecraftScript.commands.JSCommandCommand;
import com.vbitz.MinecraftScript.commands.JSDobookCommand;
import com.vbitz.MinecraftScript.commands.JSDofileCommand;
import com.vbitz.MinecraftScript.commands.JSStickCommand;
import com.vbitz.MinecraftScript.commands.MinecraftScriptHelpCommand;
import com.vbitz.MinecraftScript.commands.TestMapData;
import com.vbitz.MinecraftScript.extend.BlockFunctions;
import com.vbitz.MinecraftScript.extend.IInternalExtendApi;
import com.vbitz.MinecraftScript.items.JSStick;
import com.vbitz.MinecraftScript.items.ScriptedItem;
import com.vbitz.MinecraftScript.web.MinecraftScriptHTTPServer;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
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

@Mod(modid="MinecraftScript", name="MinecraftScript", version="0.0.0")
@NetworkMod(clientSideRequired=false, serverSideRequired=true) // may change in the future
public class MinecraftScriptMod {
	@Instance("MinecraftScriptMod")
	public static MinecraftScriptMod instance;
	
	@SidedProxy(clientSide="com.vbitz.MinecraftScript.ClientProxy", serverSide="com.vbitz.MinecraftScript.CommonProxy")
	public static CommonProxy proxy;

	private static MinecraftScriptMod _singilton = null;
	
	private static File scriptsDirectory = null;
	
	private static Logger mcLogger = Logger.getLogger("MinecraftScriptMod");
	
	private static ScriptedBlock blocks[];
	private static ScriptedItem items[];
	
	private boolean webServerStarted = false;
	
	private static boolean clientSideEnabled = true;
	private static boolean unsafeModeEnabled = false;
	
	private static IInternalExtendApi[] _internalApis = new IInternalExtendApi[] {
		new BlockFunctions()
	};
	
	@PreInit
	public void preInit(FMLPreInitializationEvent e) {
		scriptsDirectory = new File(e.getModConfigurationDirectory(), "scripts");
		if (!scriptsDirectory.exists()) {
			if (!scriptsDirectory.mkdir()) {
				Logger.getLogger("MinecraftScriptMod").severe("Can't create scripts directory");
			}
		}
		
		Configuration config = new Configuration(e.getSuggestedConfigurationFile());
		
		config.load();
		
		clientSideEnabled = config.get(Configuration.CATEGORY_GENERAL, "clientSideEnabled", true).getBoolean(true);
		unsafeModeEnabled = config.get(Configuration.CATEGORY_GENERAL, "unsafeEnabled", false).getBoolean(false);
		
		config.save();
	}
	
	@Init
	public void load(FMLInitializationEvent e) {
		proxy.registerRenderers();
		
		this.mcLogger.setParent(FMLLog.getLogger());
		
		createScriptedObjects();
		
		ScriptingManager.loadScriptEngine();
		
		JSStick.getSingilton(); // just to get it to register
		
		TickRegistry.registerTickHandler(MinecraftScriptedTickManager.getInstance(), Side.SERVER);
		
		EntityRegistry.registerModEntity(ScriptedThrowable.class, "scriptedThrowable",
				1234, this, 50, 1, true); // need to change the id maybe
		
		GameRegistry.registerWorldGenerator(new MinecraftScriptWorldGen());
	}
	
	private void createScriptedObjects() {
		if (!getClientSideEnabled()) {
			return;
		}
		blocks = new ScriptedBlock[128]; // this will get bigger in the future
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new ScriptedBlock(512 + i);
			GameRegistry.registerBlock(blocks[i],"Scripted Block " + i);
			LanguageRegistry.addName(blocks[i], "Scripted Block " + i);
		}
		items = new ScriptedItem[128]; // this will get bigger in the future
		for (int i = 0; i < items.length; i++) {
			items[i] = new ScriptedItem(8192 + i); // allow people to configure this value
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
		commandManager.registerCommand(new JSCommand());
		commandManager.registerCommand(new JSDofileCommand());
		commandManager.registerCommand(new JSDobookCommand());
		commandManager.registerCommand(new JSCommandCommand());
		commandManager.registerCommand(new JSStickCommand());
		commandManager.registerCommand(new MinecraftScriptHelpCommand());
		commandManager.registerCommand(new TestMapData());
		
		ScriptingManager.loadAllScripts(scriptsDirectory);
	}
	
	@ServerStarted
	public void serverStarted(FMLServerStartedEvent e) {
		//MinecraftScriptHTTPServer.start(); // until this is done it's disabled
			// otherwise no new stuff for a good while
		webServerStarted = true;
	}
	
	@ServerStopping
	public void serverStopping(FMLServerStoppingEvent e) {
		//MinecraftScriptHTTPServer.stop();
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
}
