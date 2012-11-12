package com.vbitz.MinecraftScript;

import org.mozilla.javascript.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.PlayerNotFoundException;
import net.minecraft.src.PlayerSelector;
import net.minecraftforge.common.MinecraftForge;

public class MinecraftScriptAPI {

	public ScriptedBlock getScriptedBlock(int id) {
		return MinecraftScriptMod.getInstance().getScriptedBlock(id);
	}
	
	public void log(Object obj) {
		MinecraftScriptMod.getLogger().info(obj.toString());
	}
	
	public void sendChat(String chat) {
		Minecraft.getMinecraft().thePlayer.sendChatToPlayer(chat);
	}
	
	public int getItemId(String name) {
		return MinecraftItemStore.getBlockByName(name);
	}
	
	public MinecraftScriptPlayerAPI getPlayer(String nick) {
        EntityPlayerMP ply = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(nick);
        
        if (ply == null) {
			return null;
		} else {
			return new MinecraftScriptPlayerAPI(ply);
		}
	}
	
	public MinecraftScriptWorldAPI getWorld() {
		EntityPlayer ply = ScriptingManager.getScriptRunner();
		if (ply == null) {
			return null;
		} else {
			return new MinecraftScriptWorldAPI(ply.worldObj, ply);
		}
	}
	
	public void registerCommand(String name, Function command) {
		MinecraftScriptCommandManager.addCommand(name, command);
	}
	
	public void registerWebpoint(String name, Function func) {
		JSHTTPServer.addFunction(name, func);
	}
}
