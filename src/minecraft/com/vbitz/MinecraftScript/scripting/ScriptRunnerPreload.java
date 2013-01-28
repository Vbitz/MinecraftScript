package com.vbitz.MinecraftScript.scripting;

import com.vbitz.MinecraftScript.MinecraftScriptMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ScriptRunnerPreload extends ScriptRunner {

	@Override
	public void sendChat(String msg) {
		MinecraftScriptMod.getLogger().fine(msg);
	}

	@Override
	public boolean isOP() {
		return true;
	}

	@Override
	public EntityPlayer getPlayer() {
		return null;
	}

	@Override
	public World getWorld() {
		return null; // may want to find a way of doing this later
	}

}
