package com.vbitz.MinecraftScript.scripting;

import java.io.IOException;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;

import com.vbitz.MinecraftScript.MCSCollection;
import com.vbitz.MinecraftScript.MinecraftItemStore;
import com.vbitz.MinecraftScript.MinecraftScriptCommandManager;
import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.MinecraftScriptPlayerAPI;
import com.vbitz.MinecraftScript.MinecraftScriptScriptedBlockAPI;
import com.vbitz.MinecraftScript.MinecraftScriptScriptedItemAPI;
import com.vbitz.MinecraftScript.MinecraftScriptWorldAPI;
import com.vbitz.MinecraftScript.MinecraftScriptWorldGen;
import com.vbitz.MinecraftScript.MinecraftScriptedTickManager;
import com.vbitz.MinecraftScript.Vector3f;
import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;
import com.vbitz.MinecraftScript.scripting.javascript.JSFunction;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;
import com.vbitz.MinecraftScript.web.MinecraftScriptHTTPServer;

import cpw.mods.fml.common.registry.GameRegistry;

public class ScriptingGlobals {
	
	private static HashMap<String, Integer> Difficultys = new HashMap<String, Integer>();
	
	static {
		Difficultys.put("peaceful", 0);
		Difficultys.put("easy", 1);
		Difficultys.put("normal", 2);
		Difficultys.put("hard", 3);
	}
	
	public static Vector3f newVectorJS(double x, double y, double z) {
		return new Vector3f(x, y, z);
	}
	
	public static MinecraftScriptPlayerAPI getScriptRunnerJS() {
		return JSScriptingManager.getInstance().getScriptRunner().getPlayerAPI();
	}
	
	public static MinecraftScriptWorldAPI getWorldJS() {
		return JSScriptingManager.getInstance().getScriptRunner().getWorldAPI();
	}
	
	public static MinecraftScriptScriptedBlockAPI getBlockJS(int i) throws ScriptErrorException {
		if (!MinecraftScriptMod.getInstance().getClientSideEnabled()) {
			throw new ScriptErrorException("Client side needs to be enabled for ScriptedBlocks to work");
		}
		if (i > 128) {
			return null;
		} else {
			return new MinecraftScriptScriptedBlockAPI(i);
		}
	}
	
	public static MinecraftScriptScriptedItemAPI getItemJS(int i) throws ScriptErrorException {
		if (!MinecraftScriptMod.getInstance().getClientSideEnabled()) {
			throw new ScriptErrorException("Client side needs to be enabled for ScriptedItems to work");
		}
		if (i > 128) {
			return null;
		} else {
			return new MinecraftScriptScriptedItemAPI(i);
		}
	}
	
	public static void logJS(Object obj) {
		MinecraftScriptMod.getLogger().info(JSScriptingManager.getInstance().getTidyOutput(obj));
	}
	
	public static void sendChatJS(String chat) {
		if (JSScriptingManager.getInstance().getScriptRunner() == null) {
			MinecraftScriptMod.getLogger().info("chat(\"" + chat + "\")");
		} else {
			JSScriptingManager.getInstance().getScriptRunner().sendChat(chat);
		}
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
	
	public static void registerCommandJS(String name, Object command) {
		MinecraftScriptCommandManager.addCommand(name, JSScriptingManager.getInstance().getFunction(command));
	}
	
	public static void setDifficultyJS(String diff) throws ScriptErrorException {
		if (Difficultys.containsKey(diff)) {
			MinecraftServer.getServer().setDifficultyForAllWorlds(Difficultys.get(diff));
		} else {
			throw new ScriptErrorException("Difficulty not found");
		}
	}
	
	public static void addSmeltingRecipeJS(int input, int output, int xp) {
		GameRegistry.addSmelting(input, new ItemStack(output, 1, 0), xp);
	}
	
	public static void runExtJS(String name, NativeArray args) throws ScriptErrorException {
		if (JSScriptingManager.getInstance().hasExt(name)) {
			JSScriptingManager.getInstance().extCall(name, args.toArray());
		} else {
			throw new ScriptErrorException("Extention does not exist");
		}
	}
	
	public static boolean hasExtJS(String name) {
		return JSScriptingManager.getInstance().hasExt(name);
	}
	
	public static boolean registerTickJS(String id, Object obj) {
		if (JSScriptingManager.getInstance().getScriptRunner() == null) {
			return false;
		} else {
			return MinecraftScriptedTickManager.getInstance().registerOnTick(id, JSScriptingManager.getInstance().getScriptRunner(),
					JSScriptingManager.getInstance().getFunction(obj));
		}
	}
	
	public static void deregisterTickJS(String id) {
		MinecraftScriptedTickManager.getInstance().deregisterTick(id);
	}
	
	public static MCSCollection collectionJS(Object vec) throws ScriptErrorException {
		if (vec instanceof Vector3f) {
			return new MCSCollection((Vector3f) vec);
		} else {
			throw new ScriptErrorException("vec should be a vector");
		}
	}
	
	public static void genFuncJS(Object obj) {
		MinecraftScriptWorldGen.setFunc(JSScriptingManager.getInstance().getFunction(obj), JSScriptingManager.getInstance().getScriptRunner());
	}
	
	public static void reloadScopeJS() {
		JSScriptingManager.getInstance().reload();
	}
	
	public static void requireJS(String path) throws ScriptErrorException {
		// will at some point in the near future act more like node.js require, right now it just loads the file
		// into the current context
		try {
			JSScriptingManager.getInstance().runFile(path, JSScriptingManager.getInstance().getScriptRunner());
		} catch (InternalScriptingException e) {
			throw new ScriptErrorException(e.getMessage());
		} // all other errors are passed up to the calling source
	}
	
	public static String getSrcJS(Object func) {
		return JSScriptingManager.getInstance().getFunctionSrc(JSScriptingManager.getInstance().getFunction(func));
	}
}
