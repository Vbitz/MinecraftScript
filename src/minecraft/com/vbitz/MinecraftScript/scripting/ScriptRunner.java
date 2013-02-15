package com.vbitz.MinecraftScript.scripting;

import com.vbitz.MinecraftScript.MinecraftScriptPlayerAPI;
import com.vbitz.MinecraftScript.MinecraftScriptWorldAPI;
import com.vbitz.MinecraftScript.Vector3f;
import com.vbitz.MinecraftScript.survival.SurvivalNodeManager;
import com.vbitz.MinecraftScript.titleEntitys.TileEntitySurvivalNode;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;

public abstract class ScriptRunner {
	public abstract void sendChat(String msg);
	public abstract boolean isOP();
	public abstract EntityPlayer getPlayer();
	public abstract World getWorld();
	
	public boolean isSurvival() {
		EntityPlayer ply = getPlayer();
		if (ply == null) {
			return false;
		}
		if (ply instanceof EntityPlayerMP) {
			EntityPlayerMP mpPly = (EntityPlayerMP) ply;
			return mpPly.theItemInWorldManager.getGameType() == EnumGameType.SURVIVAL;
		}
		return false;
	}
	
	public Vector3f getPos() {
		if (getPlayer() != null) {
			EntityPlayer ply = getPlayer();
			return new Vector3f(ply.posX, ply.posY, ply.posZ);
		}
		return new Vector3f();
	}
	
	public void finalise() {
		
	}
	
	public MinecraftScriptPlayerAPI getPlayerAPI() {
		return new MinecraftScriptPlayerAPI(getPlayer());
	}
	
	public MinecraftScriptWorldAPI getWorldAPI() {
		return new MinecraftScriptWorldAPI(getWorld(), getPlayer());
	}
	
	public TileEntitySurvivalNode getSurvivalNode() {
		if (getWorld() == null) {
			return null;
		}
		return SurvivalNodeManager.getNearNode(getWorld(), this.getPos());
	}
}
