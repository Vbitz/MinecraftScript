package com.vbitz.MinecraftScript.scripting;

import com.vbitz.MinecraftScript.MinecraftScriptMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ScriptRunnerEvent extends ScriptRunner {

	private World _world;
	
	public ScriptRunnerEvent(World world) {
		_world = world;
	}
	
	@Override
	public void sendChat(String msg) {
		MinecraftScriptMod.getLogger().fine("Log from Event: " + msg);
	}

	@Override
	public boolean isOP() {
		return true;
	}

	@Override
	public EntityPlayer getPlayer() { return null; }

	@Override
	public World getWorld() {
		return _world;
	}

}
