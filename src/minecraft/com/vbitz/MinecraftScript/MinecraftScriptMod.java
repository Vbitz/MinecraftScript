package com.vbitz.MinecraftScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.logging.Logger;

import org.mozilla.javascript.*;

import com.vbitz.MinecraftScript.blocks.ScriptedBlock;
import com.vbitz.MinecraftScript.commands.APIKeyCommand;
import com.vbitz.MinecraftScript.commands.JSScriptingCommand;
import com.vbitz.MinecraftScript.commands.MinecraftScriptHelpCommand;
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
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.*;
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
		TickRegistry.registerTickHandler(MinecraftScriptHTTPServer.getInstance(), Side.SERVER);
		
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
		commandManager.registerCommand(new APIKeyCommand());
		commandManager.registerCommand(new JSScriptingCommand());
		commandManager.registerCommand(new MinecraftScriptHelpCommand());
		
		ScriptingManager.loadAllScripts(scriptsDirectory);
	}
	
	@ServerStarted
	public void serverStarted(FMLServerStartedEvent e) {
		MinecraftScriptHTTPServer.start();
		webServerStarted = true;
	}
	
	@ServerStopping
	public void serverStopping(FMLServerStoppingEvent e) {
		MinecraftScriptHTTPServer.stop();
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
