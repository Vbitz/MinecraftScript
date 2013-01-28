package com.vbitz.MinecraftScript.scripting;

import org.mozilla.javascript.Function;

import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;

public interface IFunction {
	public String getName();
	
	public Object call(Object... args) throws InternalScriptingException;
}
