package com.vbitz.MinecraftScript;

import java.util.HashMap;

import org.mozilla.javascript.Function;

import com.vbitz.MinecraftScript.commands.JSScriptingCommand;
import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

public class MinecraftScriptCommandManager {
	private static HashMap<String, IFunction> _commands = new HashMap<String, IFunction>();
	
	public static void addCommand(String name, IFunction func) {
		_commands.put(name, func);
		JSScriptingCommand.registerCommand(name);
	}

	public static boolean containsCommand(String string) {
		return _commands.containsKey(string);
	}
	
	public static Object runCommand(String cmdName, Object[] cmdArgs, ScriptRunner runner) throws InternalScriptingException {
		return JSScriptingManager.getInstance().runFunction(runner, _commands.get(cmdName), cmdArgs);
	}
	
	
}
