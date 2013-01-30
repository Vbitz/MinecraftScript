package com.vbitz.MinecraftScript;

import java.util.ArrayList;
import java.util.HashMap;

import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

public class MinecraftScriptHookManager {
	private HashMap<String, ArrayList<IFunction>> _hooks = new HashMap<String, ArrayList<IFunction>>();
	
	public void addHook(String name, IFunction func) {
		if (!_hooks.containsKey(name)) {
			_hooks.put(name, new ArrayList<IFunction>());
		}
		_hooks.get(name).add(func);
	}
	
	public void callHook(ScriptRunner runner, String name, Object... args) {
		if (!_hooks.containsKey(name)) {
			return;
		}
		for (IFunction func : _hooks.get(name)) {
			try {
				JSScriptingManager.getInstance().runFunction(runner, func);
			} catch (InternalScriptingException e) {
				runner.sendChat("Error in hook: " + e.getMessage());
			}
		}
	}
}
