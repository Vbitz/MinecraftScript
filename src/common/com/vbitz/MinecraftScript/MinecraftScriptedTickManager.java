package com.vbitz.MinecraftScript;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Queue;
import java.util.logging.Logger;

import net.minecraft.src.EntityPlayer;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class MinecraftScriptedTickManager implements ITickHandler {

	private static int _tickRate = 1;
	
	private class TickFunction {
		public String ID;
		public Function Func;
		public EntityPlayer Ply;
		
		public TickFunction(String id, EntityPlayer entityPlayer, Function func) {
			ID = id;
			Func = func;
			Ply = entityPlayer;
		}
	}
	
	private static MinecraftScriptedTickManager _instance;
	
	private ArrayDeque<TickFunction> _functions = new ArrayDeque<TickFunction>();
	
	private Logger _log = Logger.getLogger("MinecraftScriptedTickManager");
	
	static {
		_instance = new MinecraftScriptedTickManager();
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) { }

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		ArrayList<MinecraftScriptedTickManager.TickFunction> func =
				new ArrayList<MinecraftScriptedTickManager.TickFunction>();
		for (int i = 0; i < _tickRate; i++) {
			if (_functions.size() > 0) {
				func.add(_functions.pop());
			}
		}
		for (TickFunction tickFunction : func) {
			ScriptingManager.enterContext();
			ScriptingManager.setScriptRunner(tickFunction.Ply);
			try {
				ScriptingManager.runFunction(tickFunction.Func);
			} catch (EcmaError e) {
				_log.severe("Error: " + e.getMessage());
			} catch (EvaluatorException e) {
				_log.severe("Error: " + e.getMessage());
			}
			ScriptingManager.setScriptRunner(null);
			ScriptingManager.exitContext();
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() { return null; }

	public boolean registerOnTick(String id, EntityPlayer entityPlayer, Function func) {
		_functions.add(new TickFunction(id, entityPlayer, func));
		return _functions.size() <= _tickRate;
	}
	
	public void deregisterTick(String id) {
		for (TickFunction func : _functions) {
			if (func.ID.equals(id)) {
				_functions.remove(func);
			}
		}
	}
	
	public static MinecraftScriptedTickManager getInstance() {
		return _instance;
	}

}
