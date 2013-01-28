package com.vbitz.MinecraftScript.scripting.javascript;

import org.mozilla.javascript.Function;

import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.IFunction;

public class JSFunction implements IFunction {
	
	private Function _func;
	private String _name;
	
	public JSFunction(Function func) {
		_func = func;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object call(Object... args) throws InternalScriptingException {
		return null;
	}
	
	public Function getFunction() {
		return _func;
	}

}
