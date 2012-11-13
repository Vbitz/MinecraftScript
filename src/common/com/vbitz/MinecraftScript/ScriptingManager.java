package com.vbitz.MinecraftScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import net.minecraft.src.EntityPlayer;

import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

public class ScriptingManager {
	private static Context mcJavascriptContext = null;
	private static ScriptableObject mcJavascriptScope = null;
	
	private static File _scriptsDirectory = null;
	
	private static EntityPlayer _scriptRunner = null;
	
	public static Object runFunction(Function func, Object... args) {
		return func.call(mcJavascriptContext, mcJavascriptScope, mcJavascriptScope, args);
	}
	
	public static void enterContext() {
		if (Context.getCurrentContext() != null) {
			Context.exit();
		}
		ContextFactory.getGlobal().enterContext(mcJavascriptContext);
	}
	
	public static void exitContext() {
		Context.exit();
	}
	
	public static void loadScriptEngine() {
		mcJavascriptContext = Context.enter();
		mcJavascriptContext.setClassShutter(new MinecraftScriptClassShutter());
		mcJavascriptScope = mcJavascriptContext.initStandardObjects();
		mcJavascriptScope.put("api", mcJavascriptScope, new MinecraftScriptAPI());
		try {
			mcJavascriptScope.put("me", mcJavascriptScope, new FunctionObject("me", new ScriptingManager().getClass().getMethod("getScriptRunnerJS", new Class<?>[] {}), mcJavascriptScope));
			mcJavascriptScope.put("world", mcJavascriptScope, new FunctionObject("world", new ScriptingManager().getClass().getMethod("getWorldJS", new Class<?>[] {}), mcJavascriptScope));
		} catch (SecurityException e) {
			MinecraftScriptMod.getLogger().severe("Could not load me()");
		} catch (NoSuchMethodException e) {
			MinecraftScriptMod.getLogger().severe("Could not load me()");
		}
		MinecraftScriptMod.getLogger().info("Loaded Script Engine");
		Context.exit();
	}

	public static void loadAllScripts(File scriptsDirectory) {
		_scriptsDirectory = scriptsDirectory;
		
		enterContext();
		
		mcJavascriptScope.put("isPreload", mcJavascriptScope, true);
		Logger.getLogger("MinecraftScriptMod").info("Loading Startup Scripts");
		for (File f : scriptsDirectory.listFiles()) {
			try {
				MinecraftScriptMod.getLogger().info("Loading " + f.toString());
				FileReader fr = new FileReader(f);
				mcJavascriptContext.evaluateReader(mcJavascriptScope, fr, f.getName(), 0, null);
			} catch (FileNotFoundException e) {
				MinecraftScriptMod.getLogger().severe("Could not find " + f.getName());
			} catch (IOException e) {
				MinecraftScriptMod.getLogger().severe(e.toString());
			}
		}
		mcJavascriptScope.put("isPreload", mcJavascriptScope, false);
		
		exitContext();
	}

	public static Object runString(String string, EntityPlayer ply) {
		Object ret = null;
		enterContext();
		_scriptRunner = ply;
		ret = mcJavascriptContext.compileString(string, "command", 0, null).exec(mcJavascriptContext, mcJavascriptScope);
		_scriptRunner = null;
		exitContext();
		return ret;
	}
	
	public static void doFile(String filename) throws IOException {
		enterContext();
		File fullPath = new File(_scriptsDirectory, filename);
		FileReader fr = new FileReader(fullPath);
		mcJavascriptContext.evaluateReader(mcJavascriptScope, fr, fullPath.getName(), 0, null);
		exitContext();
	}
	
	public static MinecraftScriptPlayerAPI getScriptRunnerJS() {
		return new MinecraftScriptPlayerAPI(getScriptRunner());
	}
	
	public static MinecraftScriptWorldAPI getWorldJS() {
		if (getScriptRunner() == null) {
			return null;
		} else {
			return new MinecraftScriptWorldAPI(getScriptRunner().worldObj, getScriptRunner());
		}
	}
	
	public static EntityPlayer getScriptRunner() {
		return _scriptRunner;
	}
	
	public static void setScriptRunner(EntityPlayer ply) {
		_scriptRunner = ply;
	}
	
	public static String getTidyOutput(Object obj) {
		if (obj instanceof Undefined) {
			return "";
		} else if (obj instanceof NativeJavaObject) {
			NativeJavaObject natObj = (NativeJavaObject)obj;
			return "JavaObject [" + natObj.getClassName() + "]";
		} else if (obj instanceof String) {
			return (String)obj;
		}
		return obj.toString();
	}
}
