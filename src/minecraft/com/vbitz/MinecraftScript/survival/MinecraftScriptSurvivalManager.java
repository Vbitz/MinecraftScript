package com.vbitz.MinecraftScript.survival;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;
import com.vbitz.MinecraftScript.titleEntitys.TileEntitySurvivalNode;

public class MinecraftScriptSurvivalManager {
	private static Object recuriveGetItem(Class<?> cls, String name) {
		for (Method item : cls.getDeclaredMethods()) {
			if (item.getName().equals(name)) {
				return item;
			}
		}
		for (Field item : cls.getDeclaredFields()) {
			if (item.getName().equals(name)) {
				return item;
			}
		}
		if (cls.getSuperclass() != null) {
			return recuriveGetItem(cls.getSuperclass(), name);
		} else {
			return null;
		}
	}
	
	public static boolean canCall(Object method) {
		if (!JSScriptingManager.getInstance().getScriptRunner().isSurvival()) {
			return true;
		}
		ScriptRunner r = JSScriptingManager.getInstance().getScriptRunner();
		TileEntitySurvivalNode n = r.getSurvivalNode();
		if (n == null) {
			return false;
		}
		return n.getAmount() >= getChildCost(method);
	}

	public static int getChildCost(Object method) {
		SurvivalControl c = null;
		if (method == null) {
			throw new Error("method is null");
		}
		if (method instanceof Method) {
			c = ((Method) method).getAnnotation(SurvivalControl.class);
		} else if (method instanceof Field) {
			c = ((Field) method).getAnnotation(SurvivalControl.class);
		} else {
			throw new Error("You need to pass in a Method or a Field : " + method.toString());
		}
		if (c == null) {
			return Integer.MAX_VALUE;
		}
		return c.cost();
	}

	public static void functionSuccuss(TileEntitySurvivalNode node) {
		// deduct cost from nearest node
		ScriptRunner r = JSScriptingManager.getInstance().getScriptRunner();
		if (!r.isSurvival()) {
			return;
		}
		if (node == null) {
			node = r.getSurvivalNode();
			if (node == null) {
				throw new Error("Could not find survival node");
			}
		}
		Throwable t = new Throwable();
		StackTraceElement[] ele = t.getStackTrace();
		Class eleClass = null;
		try {
			eleClass = Class.forName(ele[1].getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Object obj = recuriveGetItem(eleClass, ele[1].getMethodName());
		if (obj == null) {
			throw new Error("object not found : " + ele[1].getMethodName());
		}
		int cost = getChildCost(obj);
		node.addAmount(-cost);
	}
}
