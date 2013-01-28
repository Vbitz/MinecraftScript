package com.vbitz.MinecraftScript.scripting;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ScriptRunnerPlayer extends ScriptRunner {

	private EntityLiving _ply;
	
	public ScriptRunnerPlayer(EntityLiving player) {
		_ply = player;
	}

	@Override
	public void sendChat(String msg) {
		if (_ply instanceof EntityPlayer) {
			((EntityPlayer) _ply).sendChatToPlayer(msg);
		}
	}

	@Override
	public boolean isOP() {
		return false;
	}

	@Override
	public EntityPlayer getPlayer() {
		if (_ply instanceof EntityPlayer) {
			return (EntityPlayer) _ply;
		} else {
			return null;
		}
	}

	@Override
	public World getWorld() {
		return _ply.worldObj;
	}

}
