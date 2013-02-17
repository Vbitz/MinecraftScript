package com.vbitz.MinecraftScript;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

import com.vbitz.MinecraftScript.survival.MinecraftScriptSurvivalManager;

public class MinecraftScriptJavaObject extends NativeJavaObject {

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
	
	public MinecraftScriptJavaObject(Scriptable scope, Object javaObject,
			Class<?> staticType) {
		super(scope, javaObject, staticType);
	}
	
	@Override
	public Object get(String name, Scriptable thisObject) {
		if (name.equals("getClass") && !MinecraftScriptMod.getUnsafeEnabled()) {
			return NOT_FOUND;
		}
		
		Class<?> objClass = unwrap().getClass();
		Object reflectItem = recuriveGetItem(objClass, name);
		
		if (reflectItem == null) {
			reflectItem = getRealObject(objClass, name);
		}
		
		if (reflectItem == null) {
			return NOT_FOUND;
		}
		
		if (!MinecraftScriptSurvivalManager.canCall(reflectItem)) {
			return false;
		}
		
		if (!MinecraftScriptMod.getUnsafeEnabled()) {
			return super.get(name, thisObject);
		}
		
		if (reflectItem instanceof Method) {
			if (!((Method) reflectItem).isAccessible()) {
				((Method) reflectItem).setAccessible(true);
			}
		} else if (reflectItem instanceof Field) {
			if (!((Field) reflectItem).isAccessible()) {
				((Field) reflectItem).setAccessible(true);
			}
		}
		
		return super.get(name, thisObject);
	}

	private Object getRealObject(Class<?> objClass, String name) {
		if (name.startsWith("get") || name.startsWith("set")) {
			return null; // already tried
		}
		name = new String(new char[] {name.charAt(0)}, 0, 1).toUpperCase() + name.substring(1);
		Object ret = recuriveGetItem(objClass, "get" + name);
		if (ret != null) {
			System.out.println("Resolved " + name + " to get" + name);
			return ret;
		}
		ret = recuriveGetItem(objClass, "set" + name);
		if (ret != null) {
			System.out.println("Resolved " + name + " to set" + name);
			return ret;
		}
		return null; // I've tried my best
	}

}
