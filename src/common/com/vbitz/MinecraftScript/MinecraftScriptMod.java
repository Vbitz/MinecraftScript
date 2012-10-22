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

	private static MinecraftScriptMod _singilton = null;
	
	private File scriptsDirectory = null;
	private Context mcJavascriptContext = null;
	private ScriptableObject mcJavascriptScope = null;
	
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
		
		loadScriptEngine();
		loadStartupScripts();
	}
	
	private void createScriptedObjects() {
		blocks = new ScriptedBlock[128]; // this will get bigger in the future
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new ScriptedBlock(512 + i);
			GameRegistry.registerBlock(blocks[i]);
			LanguageRegistry.addName(blocks[i], "Scripted Block " + i);
		}
	}

	private void loadScriptEngine() {
		mcJavascriptContext = Context.enter();
		mcJavascriptScope = this.mcJavascriptContext.initStandardObjects();
		mcJavascriptScope.put("api", mcJavascriptScope, new MinecraftScriptAPI());
		this.mcLogger.info("Loaded Script Engine");
		Context.exit();
	}

	private void loadStartupScripts() {
		ContextFactory.getGlobal().enterContext(mcJavascriptContext);
		Logger.getLogger("MinecraftScriptMod").info("Loading Startup Scripts");
		for (File f : this.scriptsDirectory.listFiles()) {
			try {
				this.mcLogger.info("Loading " + f.toString());
				FileReader fr = new FileReader(f);
				mcJavascriptContext.evaluateReader(mcJavascriptScope, fr, f.getName(), 0, null);
			} catch (FileNotFoundException e) {
				this.mcLogger.severe("Could not find " + f.getName());
			} catch (IOException e) {
				this.mcLogger.severe(e.toString());
			}
		}
		Context.exit();
	}
	
	public ScriptedBlock getScriptedBlock(int id) {
		return blocks[id];
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent e) {
		
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
