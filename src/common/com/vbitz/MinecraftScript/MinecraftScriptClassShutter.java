package com.vbitz.MinecraftScript;

import java.util.ArrayList;

import org.mozilla.javascript.ClassShutter;

public class MinecraftScriptClassShutter implements ClassShutter {

	private static ArrayList<String> _allowedClasses = new ArrayList<String>();
	
	static {
		_allowedClasses.add("com.vbitz.MinecraftScript.MinecraftScriptAPI");
		_allowedClasses.add("com.vbitz.MinecraftScript.MinecraftScriptPlayerAPI");
		_allowedClasses.add("com.vbitz.MinecraftScript.MinecraftScriptWorldAPI");
		_allowedClasses.add("com.vbitz.MinecraftScript.MinecraftScriptItemStackAPI");
		_allowedClasses.add("com.vbitz.MinecraftScript.MinecraftScriptScriptedBlockAPI");
		_allowedClasses.add("com.vbitz.MinecraftScript.Vector3f");
		_allowedClasses.add("java.lang.String");
	}

	@Override
	public boolean visibleToScripts(String className) {
		return _allowedClasses.contains(className);
	}}
