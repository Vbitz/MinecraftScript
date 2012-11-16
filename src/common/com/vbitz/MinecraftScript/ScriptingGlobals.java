package com.vbitz.MinecraftScript;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;

import org.mozilla.javascript.Function;

import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;

public class ScriptingGlobals {
	public static Vector3f newVectorJS(double x, double y, double z) {
		return new Vector3f(x, y, z);
	}
	
	public static MinecraftScriptPlayerAPI getScriptRunnerJS() {
		return new MinecraftScriptPlayerAPI(ScriptingManager.getScriptRunner());
	}
	
	public static MinecraftScriptWorldAPI getWorldJS() {
		if (ScriptingManager.getScriptRunner() == null) {
			return null;
		} else {
			return new MinecraftScriptWorldAPI(ScriptingManager.getScriptRunner().worldObj, ScriptingManager.getScriptRunner());
		}
	}
	
	public static MinecraftScriptScriptedBlockAPI getBlockJS(int i) {
		if (i > 128) {
			return null;
		} else {
			return new MinecraftScriptScriptedBlockAPI(i);
		}
	}
	
	public static void logJS(Object obj) {
		MinecraftScriptMod.getLogger().info(obj.toString());
	}
	
	public static void sendChatJS(String chat) {
		MinecraftServer.getServer().sendChatToPlayer(chat);
	}
	
	public static int getItemIdJS(String name) {
		return MinecraftItemStore.getBlockByName(name);
	}
	
	public static MinecraftScriptPlayerAPI playerJS(String nick) {
        EntityPlayerMP ply = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(nick);
        
        if (ply == null) {
			return null;
		} else {
			return new MinecraftScriptPlayerAPI(ply);
		}
	}
	
	public static void registerCommandJS(String name, Function command) {
		MinecraftScriptCommandManager.addCommand(name, command);
	}
	
	public static void registerWebpointJS(String name, Function func) {
		JSHTTPServer.addFunction(name, func);
	}
	
	public static void commandJS(String command) throws ScriptErrorException {
		if (getScriptRunnerJS() == null) {
			throw new ScriptErrorException("No Script Runner");
		}
		MinecraftServer.getServer().getCommandManager().executeCommand(ScriptingManager.getScriptRunner(), command);
	}
}
