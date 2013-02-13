package com.vbitz.MinecraftScript;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

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
		
		if (!MinecraftScriptMod.getUnsafeEnabled()) {
			return super.get(name, thisObject);
		}
		
		Object reflectItem = recuriveGetItem(unwrap().getClass(), name);
		
		if (reflectItem == null) {
			return NOT_FOUND;
		}
		
		if (reflectItem instanceof Method) {
			if (!((Method) reflectItem).isAccessible()) {
				System.out.println("private call");
				((Method) reflectItem).setAccessible(true);
			}
		} else if (reflectItem instanceof Field) {
			if (!((Field) reflectItem).isAccessible()) {
				System.out.println("private call");
				((Field) reflectItem).setAccessible(true);
			}
		}
		
		return super.get(name, thisObject);
	}

}
