package com.vbitz.MinecraftScript;

import net.minecraft.client.Minecraft;
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
}
