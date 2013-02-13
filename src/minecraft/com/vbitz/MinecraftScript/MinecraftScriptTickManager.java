package com.vbitz.MinecraftScript;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.logging.Logger;

import javax.script.Invocable;

import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class MinecraftScriptTickManager implements ITickHandler {

	private static int _scriptedTickRate = 1;
	private static int _httpTickRate = 1;
	
	private class TickFunction {
		public String ID;
		public IFunction Func;
		public ScriptRunner Ply;
		
		public TickFunction(String id, ScriptRunner entityPlayer, IFunction func) {
			ID = id;
			Func = func;
			Ply = entityPlayer;
		}
	}
	
	private static MinecraftScriptTickManager _instance;
	
	private ArrayDeque<TickFunction> _scriptedToInvoke = new ArrayDeque<TickFunction>();
	
	private ArrayDeque<Runnable> _httpToInvoke = new ArrayDeque<Runnable>();
	
	private Logger _log = Logger.getLogger("MinecraftScriptTickManager");
	
	static {
		_instance = new MinecraftScriptTickManager();
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) { }

	@Override
	public synchronized void tickEnd(EnumSet<TickType> type, Object... tickData) {
		ArrayList<MinecraftScriptTickManager.TickFunction> func =
				new ArrayList<MinecraftScriptTickManager.TickFunction>();
		for (int i = 0; i < _scriptedTickRate; i++) {
			if (_scriptedToInvoke.size() > 0) {
				func.add(_scriptedToInvoke.pop());
			}
		}
		for (TickFunction tickFunction : func) {
			try {
				JSScriptingManager.getInstance().runFunction(tickFunction.Ply, tickFunction.Func);
			} catch (InternalScriptingException e) {
				tickFunction.Ply.sendChat("Error: " + e.getMessage());
			}
		}
		
		for (int i = 0; i < _httpTickRate; i++) {
			if (_httpToInvoke.size() < 1) {
				break;
			}
			Runnable r = _httpToInvoke.pop();
			if (r != null) {
				r.run();
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() { return null; }

	public boolean registerOnTick(String id, ScriptRunner entityPlayer, IFunction func) {
		_scriptedToInvoke.add(new TickFunction(id, entityPlayer, func));
		return _scriptedToInvoke.size() <= _scriptedTickRate;
	}
	
	public synchronized void addHTTPRunnable(Runnable r) {
		_httpToInvoke.push(r);
	}
	
	public void deregisterTick(String id) {
		for (TickFunction func : _scriptedToInvoke) {
			if (func.ID.equals(id)) {
				_scriptedToInvoke.remove(func);
			}
		}
	}
	
	public static MinecraftScriptTickManager getInstance() {
		return _instance;
	}
}
