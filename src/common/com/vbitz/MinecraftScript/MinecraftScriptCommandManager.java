package com.vbitz.MinecraftScript;

import java.util.HashMap;

import org.mozilla.javascript.Function;

public class MinecraftScriptCommandManager {
	private static HashMap<String, Function> _commands = new HashMap<String, Function>();
	
	public static void addCommand(String name, Function func) {
		_commands.put(name, func);
	}

	public static boolean containsCommand(String string) {
		return _commands.containsKey(string);
	}
	
	public static String runCommand(String cmdName, Object[] cmdArgs) {
		return ScriptingManager.runFunction(_commands.get(cmdName), cmdArgs).toString();
	}
	
	
}
