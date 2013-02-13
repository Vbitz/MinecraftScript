package com.vbitz.MinecraftScript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

public class MinecraftScriptWrapFactory extends WrapFactory {
	@Override
	public Scriptable wrapAsJavaObject(Context cx, Scriptable scope,
			Object javaObject, Class<?> staticType) {
		return new MinecraftScriptJavaObject(scope, javaObject, staticType);
	}
}
