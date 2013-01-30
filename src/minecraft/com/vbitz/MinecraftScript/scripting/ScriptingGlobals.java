package com.vbitz.MinecraftScript.scripting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import com.vbitz.MinecraftScript.MinecraftScriptHookManager;
import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.MinecraftScriptPlayerAPI;
import com.vbitz.MinecraftScript.MinecraftScriptScriptedBlockAPI;
import com.vbitz.MinecraftScript.MinecraftScriptScriptedItemAPI;
import com.vbitz.MinecraftScript.MinecraftScriptWorldAPI;
import com.vbitz.MinecraftScript.MinecraftScriptWorldGen;
import com.vbitz.MinecraftScript.MinecraftScriptedTickManager;
import com.vbitz.MinecraftScript.Vector3f;
import com.vbitz.MinecraftScript.docs.HelpRegistry;
import com.vbitz.MinecraftScript.docs.JSDoc;
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
	
	@JSDoc(jsName = "v(double x,y,z)", doc = "Returns a new Vector with (x, y, z)")
	public static Vector3f newVectorJS(double x, double y, double z) {
		return new Vector3f(x, y, z);
	}
	
	@JSDoc(jsName = "me()", doc = "Returns the current player")
	public static MinecraftScriptPlayerAPI getScriptRunnerJS() {
		return JSScriptingManager.getInstance().getScriptRunner().getPlayerAPI();
	}
	
	@JSDoc(jsName = "world()", doc = "Returns the current world")
	public static MinecraftScriptWorldAPI getWorldJS() {
		return JSScriptingManager.getInstance().getScriptRunner().getWorldAPI();
	}
	
	@JSDoc(jsName = "block(int id)", doc = "Returns a Scripted Block with ID")
	public static MinecraftScriptScriptedBlockAPI getBlockJS(int i) throws ScriptErrorException {
		if (!MinecraftScriptMod.getInstance().getClientSideEnabled()) {
			throw new ScriptErrorException("Client side needs to be enabled for ScriptedBlocks to work");
		}
		return new MinecraftScriptScriptedBlockAPI(i);
	}
	
	@JSDoc(jsName = "item(int id)", doc = "Returns a Scripted Item with ID")
	public static MinecraftScriptScriptedItemAPI getItemJS(int i) throws ScriptErrorException {
		if (!MinecraftScriptMod.getInstance().getClientSideEnabled()) {
			throw new ScriptErrorException("Client side needs to be enabled for ScriptedItems to work");
		}
		return new MinecraftScriptScriptedItemAPI(i);
	}
	
	@JSDoc(jsName = "log(object obj)", doc = "Prints obj to Console")
	public static void logJS(Object obj) {
		MinecraftScriptMod.getLogger().info(JSScriptingManager.getInstance().getTidyOutput(obj));
	}
	
	@JSDoc(jsName = "logFile(string data)", doc = "Dumps str to a file, size limited to 8MB")
	public static String logFileJS(String str) throws ScriptErrorException {
		if (str.length() > 8 * 1024 * 1024) {
			throw new ScriptErrorException("File Size limited to 8MB");
		}
		File file = MinecraftScriptMod.getLogFileWriter();
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			return "Error";
		}
		return file.getName();
	}
	
	@JSDoc(jsName = "logAll()", doc = "Dumps the rest of the chat in this script to a logfile")
	public static void logAllJS() {
		JSScriptingManager.getInstance().setScriptRunner(new ScriptRunnerLogger(JSScriptingManager.getInstance().getScriptRunner()));
	}
	
	@JSDoc(jsName = "chat(string chat)", doc = "Sends chat to the current player as a chat message")
	public static void sendChatJS(String chat) {
		if (JSScriptingManager.getInstance().getScriptRunner() == null) {
			MinecraftScriptMod.getLogger().info("chat(\"" + chat + "\")");
		} else {
			JSScriptingManager.getInstance().getScriptRunner().sendChat(chat);
		}
	}
	
	@JSDoc(jsName = "itemId(string name)", doc = "Returns the ID of the item")
	public static int getItemIdJS(String name) {
		return MinecraftItemStore.getBlockByName(name);
	}
	
	@JSDoc(jsName = "player(string name)", doc = "Returns a PlayerAPI for nickname")
	public static MinecraftScriptPlayerAPI playerJS(String nick) {
        EntityPlayerMP ply = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(nick);
        
        if (ply == null) {
			return null;
		} else {
			return new MinecraftScriptPlayerAPI(ply);
		}
	}
	
	@JSDoc(jsName = "registerCommand(string name,function command)", doc = "Running /c name args will run cmd with args")
	public static void registerCommandJS(String name, Object command) {
		MinecraftScriptCommandManager.addCommand(name, JSScriptingManager.getInstance().getFunction(command));
	}
	
	@JSDoc(jsName = "difficulty(string diff)", doc = "Sets the Difficulty to difficulty, difficulty can be peaceful, easy, normal or hard")
	public static void setDifficultyJS(String diff) throws ScriptErrorException {
		if (Difficultys.containsKey(diff)) {
			MinecraftServer.getServer().setDifficultyForAllWorlds(Difficultys.get(diff));
		} else {
			throw new ScriptErrorException("Difficulty not found");
		}
	}
	
	@JSDoc(jsName = "addSmeltingRecipe(int input,int output,int xp)", doc = "Adds a Smelting Recipe")
	public static void addSmeltingRecipeJS(int input, int output, int xp) {
		GameRegistry.addSmelting(input, new ItemStack(output, 1, 0), xp);
	}
	
	@JSDoc(jsName = "runExt(string name,array args)", doc = "Runs a extention with args")
	public static void runExtJS(String name, NativeArray args) throws ScriptErrorException {
		if (JSScriptingManager.getInstance().hasExt(name)) {
			JSScriptingManager.getInstance().extCall(name, args.toArray());
		} else {
			throw new ScriptErrorException("Extention does not exist");
		}
	}
	
	@JSDoc(jsName = "hasExt(string name)", doc = "Returns if a extention is avalible")
	public static boolean hasExtJS(String name) {
		return JSScriptingManager.getInstance().hasExt(name);
	}
	
	@JSDoc(jsName = "registerTick(string id,function func)" , doc = "Runs a function on the next server tick")
	public static boolean registerTickJS(String id, Object obj) {
		if (JSScriptingManager.getInstance().getScriptRunner() == null) {
			return false;
		} else {
			return MinecraftScriptedTickManager.getInstance().registerOnTick(id, JSScriptingManager.getInstance().getScriptRunner(),
					JSScriptingManager.getInstance().getFunction(obj));
		}
	}
	
	@JSDoc(jsName = "deregisterTick(string id)", doc = "Stops id from running on the next server tick")
	public static void deregisterTickJS(String id) {
		MinecraftScriptedTickManager.getInstance().deregisterTick(id);
	}
	
	@JSDoc(jsName = "col(vector3f vec)", doc = "Creates a new collection from a vector")
	public static MCSCollection collectionJS(Object vec) throws ScriptErrorException {
		if (vec instanceof Vector3f) {
			return new MCSCollection((Vector3f) vec);
		} else {
			throw new ScriptErrorException("vec should be a vector");
		}
	}
	
	@JSDoc(jsName = "genFunc(function func)", doc = "This function will run each world tick")
	public static void genFuncJS(Object obj) {
		MinecraftScriptWorldGen.setFunc(JSScriptingManager.getInstance().getFunction(obj), JSScriptingManager.getInstance().getScriptRunner());
	}
	
	@JSDoc(jsName = "reload()", doc = "Reloads the scripting scope")
	public static void reloadScopeJS() {
		JSScriptingManager.getInstance().reload();
	}
	
	@JSDoc(jsName = "require(string path)", doc = "Loads script path")
	public static void requireJS(String path) throws ScriptErrorException {
		// will at some point in the near future act more like node.js require, right now it just loads the file
		// into the current context
		try {
			JSScriptingManager.getInstance().runFile(path, JSScriptingManager.getInstance().getScriptRunner());
		} catch (InternalScriptingException e) {
			throw new ScriptErrorException(e.getMessage());
		} // all other errors are passed up to the calling source
	}
	
	@JSDoc(jsName = "src(function func)", doc = "Decompiles func")
	public static String getSrcJS(Object func) {
		return JSScriptingManager.getInstance().getFunctionSrc(JSScriptingManager.getInstance().getFunction(func));
	}
	
	@JSDoc(jsName = "help(string term)", doc = "What your using right now")
	public static String helpJS(String term) {
		return HelpRegistry.getHelp(term);
	}
	
	@JSDoc(jsName = "on(string eventName,function func)", doc = "On eventName being called func will be called")
	public static void onEventJS(String eventName, Object func) {
		MinecraftScriptHookManager.addHook(eventName, JSScriptingManager.getInstance().getFunction(func));
	}
}
