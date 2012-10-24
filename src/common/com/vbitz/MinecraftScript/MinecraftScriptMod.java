package com.vbitz.MinecraftScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;

import com.vbitz.MinecraftScript.commands.JSCommand;
import com.vbitz.MinecraftScript.commands.JSDofileCommand;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ICommand;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="MinecraftScript", name="MinecraftScript", version="0.0.0")
@NetworkMod(clientSideRequired=true, serverSideRequired=true) // may change in the future
public class MinecraftScriptMod {
	@Instance("MinecraftScriptMod")
	public static MinecraftScriptMod instance;
	
	@SidedProxy(clientSide="com.vbitz.MinecraftScript.ClientProxy", serverSide="com.vbitz.MinecraftScript.CommonProxy")
	public static CommonProxy proxy;

	private static MinecraftScriptMod _singilton = null;
	
	private static File scriptsDirectory = null;
	
	private static Logger mcLogger = Logger.getLogger("MinecraftScriptMod");
	
	private static ScriptedBlock blocks[];
	
	@PreInit
	public void preInit(FMLPreInitializationEvent e) {
		
	}
	
	@Init
	public void load(FMLInitializationEvent e) {
		proxy.registerRenderers();
		
		this.mcLogger.setParent(FMLLog.getLogger());
		
		scriptsDirectory = new File(Minecraft.getMinecraftDir(), "scripts");
		if (!scriptsDirectory.exists()) {
			if (!scriptsDirectory.mkdir()) {
				Logger.getLogger("MinecraftScriptMod").severe("Can't create scripts directory");
			}
		}
		
		createScriptedObjects();
		
		ScriptingManager.loadScriptEngine();
		ScriptingManager.loadAllScripts(scriptsDirectory);
	}
	
	private void createScriptedObjects() {
		blocks = new ScriptedBlock[128]; // this will get bigger in the future
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new ScriptedBlock(512 + i);
			GameRegistry.registerBlock(blocks[i]);
			LanguageRegistry.addName(blocks[i], "Scripted Block " + i);
		}
	}
	
	public ScriptedBlock getScriptedBlock(int id) {
		return blocks[id];
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent e) {
		
	}
	
	@ServerStarting
	public void serverStarting(FMLServerStartingEvent e) {
		CommandHandler commandManager = (CommandHandler)e.getServer().getCommandManager();
		commandManager.registerCommand(new JSCommand());
		commandManager.registerCommand(new JSDofileCommand());
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
