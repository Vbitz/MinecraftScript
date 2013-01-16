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
		_allowedClasses.add("com.vbitz.MinecraftScript.MinecraftScriptScriptedItemAPI");
		_allowedClasses.add("com.vbitz.MinecraftScript.MinecraftScriptNBTagAPI");
		_allowedClasses.add("com.vbitz.MinecraftScript.Vector3f");
		_allowedClasses.add("com.vbitz.MinecraftScript.MCSCollection");
		_allowedClasses.add("java.lang.String");
		_allowedClasses.add("java.lang.Object");
	}

	@Override
	public boolean visibleToScripts(String className) {
		if (MinecraftScriptMod.getInstance().getUnsafeEnabled()) {
			return true;
		} else {
			return _allowedClasses.contains(className);
		}
	}}
