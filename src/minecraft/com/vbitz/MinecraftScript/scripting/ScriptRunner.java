package com.vbitz.MinecraftScript.scripting;

import com.vbitz.MinecraftScript.MinecraftScriptPlayerAPI;
import com.vbitz.MinecraftScript.MinecraftScriptWorldAPI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class ScriptRunner {
	public abstract void sendChat(String msg);
	public abstract boolean isOP();
	public abstract EntityPlayer getPlayer();
	public abstract World getWorld();
	
	public void finalise() {
		
	}
	
	public MinecraftScriptPlayerAPI getPlayerAPI() {
		return new MinecraftScriptPlayerAPI(getPlayer());
	}
	
	public MinecraftScriptWorldAPI getWorldAPI() {
		return new MinecraftScriptWorldAPI(getWorld(), getPlayer());
	}
}
