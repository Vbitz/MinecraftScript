package com.vbitz.MinecraftScript;

public class MinecraftScriptAPI {

	public ScriptedBlock getScriptedBlock(int id) {
		return MinecraftScriptMod.getInstance().getScriptedBlock(id);
	}
	
	public void log(Object obj) {
		MinecraftScriptMod.getLogger().info(obj.toString());
	}
}
