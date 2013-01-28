package com.vbitz.MinecraftScript.scripting;

import com.vbitz.MinecraftScript.web.MinecraftScriptAPIKey;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ScriptRunnerWeb extends ScriptRunnerPlayer {
	
	public ScriptRunnerWeb(String apiKey) {
		super(MinecraftScriptAPIKey.getPlayer(apiKey)); // really freaking simple
	}

}
