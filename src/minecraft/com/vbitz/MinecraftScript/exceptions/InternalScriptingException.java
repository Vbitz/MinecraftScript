package com.vbitz.MinecraftScript.exceptions;

public class InternalScriptingException extends Exception {
	private String _error;
	
	public InternalScriptingException(String error) {
		_error = error;
	}
	
	@Override
	public String getMessage() {
		return _error;
	}
}
