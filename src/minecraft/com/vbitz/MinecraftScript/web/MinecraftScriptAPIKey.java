package com.vbitz.MinecraftScript.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;

public class MinecraftScriptAPIKey {
	private static class APIKey {
		public String key;
		public EntityPlayer owner;
		
		public APIKey(String k, EntityPlayer o) {
			key = k;
			owner = o;
		}
	}
	
	private static final char[] chars = "abcdef09123456789".toCharArray();
	private static Random rand = new Random();
	
	private static HashMap<String,APIKey> apiKeys = new HashMap<String, APIKey>();
	
	public static boolean validateKey(String key) {
		return apiKeys.containsKey(key);
	}
	
	public static EntityPlayer getPlayer(String key) {
		return apiKeys.get(key).owner;
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
		apiKeys.put(key, new APIKey(key, ply));
		return key;
	}
}
