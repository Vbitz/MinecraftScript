package com.vbitz.MinecraftScript.docs;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.vbitz.MinecraftScript.MinecraftScriptClassShutter;
import com.vbitz.MinecraftScript.scripting.ScriptingGlobals;

public class HelpRegistry {
	public static HashMap<String, String> helpStuff = new HashMap<String, String>();

	public static void load(String pkgName) {
		try {
			Class cls = Class.forName(pkgName);
			for (Method method : cls.getDeclaredMethods()) {
				JSDoc doc = (JSDoc) method.getAnnotation(JSDoc.class);
				if (doc != null) {
					helpStuff.put(doc.jsName().replace(" ", ":"), doc.doc());
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
	
	public static String getHelp(String jsName) {
		if (helpStuff.containsKey(jsName)) {
			return helpStuff.get(jsName);
		} else {
			return null;
		}
	}
}
