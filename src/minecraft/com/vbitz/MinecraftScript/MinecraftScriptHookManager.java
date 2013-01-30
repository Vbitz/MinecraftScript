package com.vbitz.MinecraftScript;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerEvent;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

public class MinecraftScriptHookManager {
	private static HashMap<String, ArrayList<IFunction>> _hooks = new HashMap<String, ArrayList<IFunction>>();
	
	public static void addHook(String name, IFunction func) {
		if (func == null) {
			return;
		}
		if (!_hooks.containsKey(name)) {
			_hooks.put(name, new ArrayList<IFunction>());
		}
		_hooks.get(name).add(func);
	}
	
	public static void callHook(ScriptRunner runner, String name, Object... args) {
		if (!_hooks.containsKey(name)) {
			return;
		}
		for (IFunction func : _hooks.get(name)) {
			try {
				JSScriptingManager.getInstance().runFunction(runner, func, args);
			} catch (InternalScriptingException e) {
				runner.sendChat("Error in hook: " + e.getMessage());
			}
		}
	}
	
	@ForgeSubscribe
	public void livingDeath(LivingDeathEvent e) {
		if (e.entityLiving.worldObj.isRemote) {
			return;
		}
		callHook(new ScriptRunnerEvent(e.entityLiving.worldObj), "livingDeath", 
				new Vector3f(e.entityLiving.posX, e.entityLiving.posY, e.entityLiving.posZ));
	}
	
	@ForgeSubscribe
	public void livingDrops(LivingDropsEvent e) {
		if (e.entityLiving.worldObj.isRemote) {
			return;
		}
		// this function should also pass a list of itemId's droped, and allow that to be added to
		callHook(new ScriptRunnerEvent(e.entityLiving.worldObj), "livingDrops", 
				new Vector3f(e.entityLiving.posX, e.entityLiving.posY, e.entityLiving.posZ));
	}
	
	@ForgeSubscribe
	public void livingFall(LivingFallEvent e) {
		if (e.entityLiving.worldObj.isRemote) {
			return;
		}
		callHook(new ScriptRunnerEvent(e.entityLiving.worldObj), "livingFall", e.distance, 
				new Vector3f(e.entityLiving.posX, e.entityLiving.posY, e.entityLiving.posZ));
	}
	
	@ForgeSubscribe
	public void livingHurt(LivingHurtEvent e) {
		if (e.entityLiving.worldObj.isRemote) {
			return;
		}
		callHook(new ScriptRunnerEvent(e.entityLiving.worldObj), "livingHurt", e.ammount, 
				new Vector3f(e.entityLiving.posX, e.entityLiving.posY, e.entityLiving.posZ));
	}
}
