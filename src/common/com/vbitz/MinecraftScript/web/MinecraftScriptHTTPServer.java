package com.vbitz.MinecraftScript.web;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.mozilla.javascript.Function;

import com.sun.net.httpserver.*;
import com.vbitz.MinecraftScript.MinecraftScriptMod;

public class MinecraftScriptHTTPServer {
	private static HTTPStaticFileHandler _staticFile = new HTTPStaticFileHandler();
	private static HTTPWebpointHandler _webpoint = new HTTPWebpointHandler();
	
	private static HttpServer _server;
	
	private static boolean _firstStart = true;
	
	private static int _portNum = 12543;
	
	public static void start() {
		try {
			_server = HttpServer.create(new InetSocketAddress(_portNum), 0);
		} catch (IOException e) {
			MinecraftScriptMod.getLogger().severe("Could Not Start web Server");
			return;
		}
		_server.start();
		
		if (!_firstStart)
			return;
		
		_server.createContext("/static", _staticFile);
		_server.createContext("/webpoint", _webpoint);
		
		_firstStart = false;
	}
	
	public static void stop() {
		_server.stop(0);
	}

	public static int getPort() {
		return _portNum;
	}

	public static void addFunction(String name, Function func) {
		_webpoint.addFunction(name, func);
	}
}
