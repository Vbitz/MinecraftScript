package com.vbitz.MinecraftScript;

import java.util.ArrayList;

import org.mozilla.javascript.ClassShutter;

public class MinecraftScriptClassShutter implements ClassShutter {

	private static ArrayList<String> _allowedClasses = new ArrayList<String>();
	
	static {
		_allowedClasses.add("com.vbitz.MinecraftScript.MinecraftScriptAPI");
		_allowedClasses.add("com.vbitz.MinecraftScript.MinecraftScriptPlayerAPI");
		_allowedClasses.add("com.vbitz.MinecraftScript.MinecraftScriptWorldAPI");
		_allowedClasses.add("com.vbitz.MinecraftScript.ScriptedBlock");
	}

	@Override
	public boolean visibleToScripts(String className) {
		return _allowedClasses.contains(className);
	}}