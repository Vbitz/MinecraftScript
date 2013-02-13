package com.vbitz.MinecraftScript.web;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.mozilla.javascript.Function;

import com.sun.net.httpserver.*;
import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.MinecraftScriptTickManager;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class MinecraftScriptHTTPServer {
	
	public static Map<String, String> getQueryMap(String query) {  
	    String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params) {  
	        String name = param.split("=")[0];  
	        String value = param.split("=")[1];  
	        map.put(name, value);  
	    }  
	    return map;  
	}
	
	private static MinecraftScriptHTTPServer _instance;
	
	private RunScriptEndpoint _runScript = null;
	
	private HttpServer _server;
	
	private boolean _firstStart = true;
	
	private int _portNum = 12543;
	
	public MinecraftScriptHTTPServer() {
		_instance = this;
	}

	public void start() {
		try {
			_server = HttpServer.create(new InetSocketAddress(_portNum), 0);
		} catch (IOException e) {
			MinecraftScriptMod.getLogger().severe("Could Not Start web Server");
			e.printStackTrace();
			return;
		}
		_server.start();
		
		if (!_firstStart)
			return;
		
		_runScript = new RunScriptEndpoint();
		
		_server.createContext("/static", new StaticFileEndpoint());
		_server.createContext("/runScript", _runScript);
		
		_firstStart = false;
	}
	
	public void stop() {
		if (_server != null) {
			_server.stop(0);
			_runScript = null;
		}
	}

	public int getPort() {
		return MinecraftScriptMod.getWebServerPort();
	}

	public synchronized static MinecraftScriptHTTPServer getInstance() {
		return _instance;
	}
	
	public synchronized void addRunnable(Runnable run) {
		MinecraftScriptTickManager.getInstance().addHTTPRunnable(run);
	}
}
