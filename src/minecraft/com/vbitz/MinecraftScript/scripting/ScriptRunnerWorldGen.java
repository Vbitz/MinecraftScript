package com.vbitz.MinecraftScript.scripting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ScriptRunnerWorldGen extends ScriptRunner {

	private World _world;
	private EntityPlayer _ply;
	
	public ScriptRunnerWorldGen(World world, EntityPlayer player) {
		_world = world;
		_ply = player;
	}

	@Override
	public void sendChat(String msg) {
		_ply.sendChatToPlayer(msg);
	}

	@Override
	public boolean isOP() {
		return true;
	}

	@Override
	public EntityPlayer getPlayer() {
		return _ply;
	}

	@Override
	public World getWorld() {
		return _world;
	}

}
