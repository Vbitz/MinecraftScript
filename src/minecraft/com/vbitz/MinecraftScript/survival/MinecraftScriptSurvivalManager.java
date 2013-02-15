package com.vbitz.MinecraftScript.survival;

import java.lang.reflect.Method;

import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;
import com.vbitz.MinecraftScript.titleEntitys.TileEntitySurvivalNode;

public class MinecraftScriptSurvivalManager {
	public static boolean canCall(Method method) {
		if (!JSScriptingManager.getInstance().getScriptRunner().isSurvival()) {
			return true;
		}
		ScriptRunner r = JSScriptingManager.getInstance().getScriptRunner();
		TileEntitySurvivalNode n = r.getSurvivalNode();
		if (n.getAmount() > getMethodCost(method)) {
			
		}
		return false;
	}

	private static int getMethodCost(Method method) {
		// TODO Auto-generated method stub
		return 0;
	}
}
