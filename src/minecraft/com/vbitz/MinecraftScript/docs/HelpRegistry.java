package com.vbitz.MinecraftScript.docs;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.vbitz.MinecraftScript.MinecraftScriptClassShutter;
import com.vbitz.MinecraftScript.scripting.ScriptingGlobals;

public class HelpRegistry {
	private static HashMap<String, String> helpStuff = new HashMap<String, String>();

	public static void load(String pkgName) {
		try {
			Class cls = Class.forName(pkgName);
			for (Method method : cls.getDeclaredMethods()) {
				JSDoc doc = (JSDoc) method.getAnnotation(JSDoc.class);
				if (doc != null) {
					helpStuff.put(doc.jsName(), doc.doc());
				}
			}
		} catch (ClassNotFoundException e) { } // who cares, it's a non fatal condition
	}
	
	public static void load() {
		ArrayList<String> classesToSearch = new ArrayList<String>();
		load(ScriptingGlobals.class.getName());
		for (String str : MinecraftScriptClassShutter._allowedClasses) {
			load(str);
		}
	}
	
	public static String getHelp(String jsSearch) {
		String ret = "";
		for (String key : helpStuff.keySet()) {
			if (key.contains(jsSearch)) {
				ret += key + " : " + helpStuff.get(key) + "\n";
			}
		}
		return ret;
	}
}
