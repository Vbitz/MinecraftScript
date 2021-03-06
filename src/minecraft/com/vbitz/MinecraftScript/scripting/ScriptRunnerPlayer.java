package com.vbitz.MinecraftScript.scripting;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
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
		if (_ply instanceof EntityPlayer) {
			if (MinecraftServer.getServer().isSinglePlayer()) {
				return true;
			} else {
				return MinecraftServer.getServer()
						.getConfigurationManager().getOps().contains(((EntityPlayer) _ply)
								.username.toLowerCase());
			}
		} else {
			return true;
		}
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
