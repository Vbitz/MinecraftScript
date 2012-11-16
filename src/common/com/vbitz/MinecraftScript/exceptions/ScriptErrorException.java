package com.vbitz.MinecraftScript.exceptions;

public class ScriptErrorException extends Exception {
	private String _error;
	
	public ScriptErrorException(String error) {
		_error = error;
	}
	
	@Override
	public String getMessage() {
		return _error;
	}
}
