package com.vbitz.MinecraftScript.scripting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ScriptRunnerCommandBlock extends ScriptRunner {

	@Override
	public void sendChat(String msg) {
		
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
		return null;
	}

}
