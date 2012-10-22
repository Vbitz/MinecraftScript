package com.vbitz.MinecraftScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
	
	private File scriptsDirectory = null;
	private ScriptEngine mcJavascriptEngine = null;
	
	private Logger mcLogger = Logger.getLogger("MinecraftScriptMod");
	
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
		
		loadScriptEngine();
		loadStartupScripts();
		
	}
	
	private void loadScriptEngine() {
		ScriptEngineManager manager = new ScriptEngineManager();
		mcJavascriptEngine = manager.getEngineByName("JavaScript");
		this.mcLogger.info("Loaded Script Engine");
	}

	private void loadStartupScripts() {
		Logger.getLogger("MinecraftScriptMod").info("Loading Startup Scripts");
		for (File f : this.scriptsDirectory.listFiles()) {
			try {
				this.mcLogger.severe("Loading " + f.toString());
				FileReader fr = new FileReader(f);
				this.mcJavascriptEngine.eval(fr);
			} catch (FileNotFoundException e) {
				this.mcLogger.severe("Could not find " + f.getName());
			} catch (ScriptException e) {
				this.mcLogger.severe("Error in " + f.getName() + " : " + e.toString());
			}
		}
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent e) {
		
	}
}
