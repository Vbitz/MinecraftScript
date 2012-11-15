package com.vbitz.MinecraftScript;

public class ScriptingGlobals {
	public static Vector3f newVectorJS(double x, double y, double z) {
		return new Vector3f(x, y, z);
	}
	
	public static MinecraftScriptPlayerAPI getScriptRunnerJS() {
		return new MinecraftScriptPlayerAPI(ScriptingManager.getScriptRunner());
	}
	
	public static MinecraftScriptWorldAPI getWorldJS() {
		if (ScriptingManager.getScriptRunner() == null) {
			return null;
		} else {
			return new MinecraftScriptWorldAPI(ScriptingManager.getScriptRunner().worldObj, ScriptingManager.getScriptRunner());
		}
	}
	
	public static MinecraftScriptScriptedBlockAPI getBlockJS(int i) {
		if (i > 128) {
			return null;
		} else {
			return new MinecraftScriptScriptedBlockAPI(i);
		}
	}
}
