package com.vbitz.MinecraftScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;

public class ScriptingManager {
	private static Context mcJavascriptContext = null;
	private static ScriptableObject mcJavascriptScope = null;
	
	private static File _scriptsDirectory = null;
	
	public static Object runFunction(Function func, Object... args) {
		return func.call(mcJavascriptContext, mcJavascriptScope, mcJavascriptScope, args);
	}
	
	public static void enterContext() {
		ContextFactory.getGlobal().enterContext(mcJavascriptContext);
	}
	
	public static void exitContext() {
		Context.exit();
	}
	
	public static void loadScriptEngine() {
		mcJavascriptContext = Context.enter();
		mcJavascriptScope = mcJavascriptContext.initStandardObjects();
		mcJavascriptScope.put("api", mcJavascriptScope, new MinecraftScriptAPI());
		MinecraftScriptMod.getLogger().info("Loaded Script Engine");
		Context.exit();
	}

	public static void loadAllScripts(File scriptsDirectory) {
		_scriptsDirectory = scriptsDirectory;
		
		enterContext();
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
		exitContext();
	}

	public static Object runString(String string) {
		Object ret = null;
		enterContext();
		ret = mcJavascriptContext.compileString(string, "command", 0, null).exec(mcJavascriptContext, mcJavascriptScope);
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
}
