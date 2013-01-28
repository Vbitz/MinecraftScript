package com.vbitz.MinecraftScript.scripting;

import java.io.File;
import java.util.HashMap;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.NativeArray;

import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.MinecraftScriptUnsafeAPI;
import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;

public abstract class ScriptingManager {
	
	protected HashMap<String, IFunction> _calls = new HashMap<String, IFunction>();
	
	public abstract String getName();
	
	public abstract void onServerLoad();
	public abstract void onServerUnload();
	
	public abstract void initScope();
	public abstract void loadAllScripts(File scriptsDir);
	
	public abstract void reload();
	
	public abstract Object runFunction(ScriptRunner runner, IFunction func, Object... args) throws InternalScriptingException;
	public abstract Object runString(String str, ScriptRunner runner) throws InternalScriptingException;
	public abstract void runFile(String str, ScriptRunner runner) throws InternalScriptingException;
	
	public abstract void addGlobal(String name, Object obj);
	public abstract void addGlobal(String name, Class cls, String method, Class<?>... args) throws SecurityException, NoSuchMethodException;
	
	public abstract String getTidyOutput(Object obj);
	
	public abstract ScriptRunner getScriptRunner();
	
	public abstract IFunction getFunction(Object obj);
	
	private void addSGlobal(String jsName, String methodName, Class<?>... args) throws Exception {
		addGlobal(jsName, ScriptingGlobals.class, methodName, args);
	}
	
	public void loadScope(boolean firstLoad) {
		this.initScope();
		try {
			addSGlobal("me", "getScriptRunnerJS");
			addSGlobal("world", "getWorldJS");
			addSGlobal("vector", "newVectorJS", double.class, double.class, double.class);
			addSGlobal("v", "newVectorJS", double.class, double.class, double.class); // it's a nice shorthand
			addSGlobal("block", "getBlockJS", int.class);
			addSGlobal("item", "getItemJS", int.class);
			addSGlobal("log", "logJS", Object.class);
			addSGlobal("chat", "sendChatJS", String.class);
			addSGlobal("itemId", "getItemIdJS", String.class);
			addSGlobal("player", "playerJS", String.class);
			addSGlobal("registerCommand", "registerCommandJS", String.class, Object.class);
			addSGlobal("difficulty", "setDifficultyJS", String.class);
			addSGlobal("addSmeltingRecipe", "addSmeltingRecipeJS", int.class, int.class, int.class);
			addSGlobal("runExt", "runExtJS", String.class, NativeArray.class); // fix me to not rely on javascript
			addSGlobal("hasExt", "hasExtJS", String.class);
			addSGlobal("registerTick", "registerTickJS", String.class, Object.class);
			addSGlobal("deregisterTick", "deregisterTickJS", String.class);
			addSGlobal("col", "collectionJS", Object.class);
			addSGlobal("genFunc", "genFuncJS", Object.class);
			addSGlobal("reload", "reloadScopeJS");
			addSGlobal("require", "requireJS", String.class);
			addSGlobal("src", "getSrcJS", Object.class);
			if (MinecraftScriptMod.getUnsafeEnabled()) {
				if (firstLoad) {
					MinecraftScriptMod.getLogger().warning("UNSAFE MODE ENABLED");
				}
				addGlobal("$", new MinecraftScriptUnsafeAPI());
			}
		} catch (Exception e) {
			if (firstLoad) {
				e.printStackTrace();
				MinecraftScriptMod.getLogger().severe("Could not load globals");
			}
		}
	}
	
	public abstract void addExtCall(String jsName, Class<?> classObj, String methodName, Class<?>... methodArgs) throws SecurityException, NoSuchMethodException;
	
	public boolean hasExt(String jsName) {
		return _calls.containsKey(jsName);
	}
	
	public abstract Object extCall(String jsName, Object[] args);
}
