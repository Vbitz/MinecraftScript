package com.vbitz.MinecraftScript.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.vbitz.MinecraftScript.MinecraftScriptMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class MinecraftScriptAPIKey {
	private static class APIKey {
		public String key;
		public String ownerName;
		
		public APIKey(String k, String ownerName) {
			key = k;
			this.ownerName = ownerName;
		}
	}
	
	private static final char[] chars = "abcdef09123456789".toCharArray();
	private static Random rand = new Random();
	
	private static HashMap<String,APIKey> apiKeys = new HashMap<String, APIKey>();
	
	public static void loadAll(File filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine(); // ignore the first line
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				String[] tokens = line.split(",");
				apiKeys.put(tokens[1], new APIKey(tokens[1], tokens[0]));
				MinecraftScriptMod.getLogger().info("Loading Static API Key for: " + tokens[0]);
			}
			reader.close();
		} catch (IOException e) {
			MinecraftScriptMod.getLogger().warning("API Keys failed to load");
		}
	}
	
	public static boolean validateKey(String key) {
		return apiKeys.containsKey(key);
	}
	
	public static EntityPlayer getPlayer(String key) {
		System.out.println(apiKeys.get(key).ownerName);
		return MinecraftServer.getServer().getConfigurationManager()
				.getPlayerForUsername(apiKeys.get(key).ownerName);
	}
	
	private static String genKey() {
		String ret = "";
		for (int i = 0; i < 8; i++) {
			ret += chars[rand.nextInt(chars.length)];
		}
		return ret;
	}
	
	public static String getApiKey(EntityPlayer ply) {
		String key = genKey();
		apiKeys.put(key, new APIKey(key, ply.getEntityName()));
		return key;
	}
}
