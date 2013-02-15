package com.vbitz.MinecraftScript;

import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;
import com.vbitz.MinecraftScript.titleEntitys.TileEntitySurvivalNode;

public class MinecraftScriptNodeAPI {
	private TileEntitySurvivalNode _internal;
	
	public MinecraftScriptNodeAPI() {
		_internal = JSScriptingManager.getInstance().getScriptRunner().getSurvivalNode();
	}
	
	public boolean getValid() {
		return _internal != null;
	}
	
	public String getName() {
		return _internal.getName();
	}
	
	public void setName(String name) {
		_internal.setName(name);
	}
}
