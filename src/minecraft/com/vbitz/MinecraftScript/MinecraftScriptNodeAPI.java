package com.vbitz.MinecraftScript;

import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;
import com.vbitz.MinecraftScript.survival.MinecraftScriptSurvivalManager;
import com.vbitz.MinecraftScript.survival.SurvivalControl;
import com.vbitz.MinecraftScript.survival.SurvivalNodeManager;
import com.vbitz.MinecraftScript.titleEntitys.TileEntitySurvivalNode;

public class MinecraftScriptNodeAPI {
	private TileEntitySurvivalNode _internal;
	
	public MinecraftScriptNodeAPI() {
		_internal = JSScriptingManager.getInstance().getScriptRunner().getSurvivalNode();
	}
	
	public MinecraftScriptNodeAPI(String name) throws ScriptErrorException {
		_internal = JSScriptingManager.getInstance().getScriptRunner().getSurvivalNode(name);
	}

	@SurvivalControl(cost = 0)
	public boolean getValid() {
		return _internal != null;
	}
	
	@SurvivalControl(cost = 0)
	public String getName() {
		return _internal.getName();
	}
	
	@SurvivalControl(cost = 100)
	public void setName(String name) throws ScriptErrorException {
		if (SurvivalNodeManager.getNodeByName(_internal.worldObj, name) != null) {
			throw new ScriptErrorException("A loaded node with the same name already exists");
		}
		_internal.setName(name);
		MinecraftScriptSurvivalManager.functionSuccuss(_internal);
	}
	
	@SurvivalControl(cost = 0)
	public int getAmount() {
		return _internal.getAmount();
	}
	
	// this is just a testing method, it will be replaced with a admin command or something later
	@SurvivalControl(cost = 0)
	public void addAmount(int count) {
		_internal.addAmount(count);
	}
	
	@SurvivalControl(cost = 0)
	public Vector3f getPos() {
		return new Vector3f(_internal.xCoord, _internal.yCoord, _internal.zCoord);
	}
}
