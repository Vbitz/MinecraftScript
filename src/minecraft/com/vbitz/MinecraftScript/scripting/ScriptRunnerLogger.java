package com.vbitz.MinecraftScript.scripting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.vbitz.MinecraftScript.MinecraftScriptMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ScriptRunnerLogger extends ScriptRunner {

	private ScriptRunner previousScriptRunner;
	
	private String data;
	
	public ScriptRunnerLogger(ScriptRunner previous) {
		previousScriptRunner = previous;
	}
	
	@Override
	public void sendChat(String msg) {
		data += msg + "\n";
	}

	@Override
	public boolean isOP() {
		return previousScriptRunner.isOP();
	}

	@Override
	public EntityPlayer getPlayer() {
		return previousScriptRunner.getPlayer();
	}

	@Override
	public World getWorld() {
		return previousScriptRunner.getWorld();
	}
	
	@Override
	public void finalise() {
		File file = MinecraftScriptMod.getLogFileWriter();
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(data);
			writer.close();
		} catch (IOException e) { }
		previousScriptRunner.sendChat("Log Saved as: " + file.getName());
	}

}
