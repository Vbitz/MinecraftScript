package com.vbitz.MinecraftScript.commands;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import com.vbitz.MinecraftScript.Vector3f;
import com.vbitz.MinecraftScript.web.MinecraftScriptHTTPServer;

public class MinecraftScriptHelpCommand extends CommandBase {

	private static ArrayList<HelpDoc> _helpDocs = new ArrayList<HelpDoc>();
	
	private static void addHelp(String api, String name, String args, String description) {
		MinecraftScriptHelpCommand c = new MinecraftScriptHelpCommand();
		_helpDocs.add(c.new HelpDoc(api, name, args, description));
	}
	
	public class HelpDoc {
		public String api;
		public String name;
		public String args;
		public String description;
		
		public HelpDoc(String api, String name, String args, String description) {
			this.api = api;
			this.name = name;
			this.args = args;
			this.description = description;
		}
	}
	
	static {
		addHelp("topics", "globals", "", "General API functions. These can be called before Minecraft has started");
		addHelp("topics", "playerapi1", "", "Methods working on the set player");
		addHelp("topics", "playerapi2", "", "More methods working on the set player");
		addHelp("topics", "playerapi3", "", "Even more methods working on the set player");
		addHelp("topics", "worldapi1", "", "Methods for modifying the world");
		addHelp("topics", "worldapi2", "", "More Methods for modifying the world");
		addHelp("topics", "itemstackapi", "", "Methods for working with a stack of items");
		addHelp("topics", "nbtapi", "", "Methods for working with nbt tags");
		addHelp("topics", "effectnames1", "", "A list of effect names");
		addHelp("topics", "effectnames2", "", "A list of the effect names not covered in the first list");
		addHelp("topics", "enchantsArmor", "", "A list of Armor Enchantments (The Descriptions are taken from MCP)");
		
		addHelp("globals", "me", "", "Returns the current player");
		addHelp("globals", "world", "", "Returns the current world");
		addHelp("globals", "vector", "float x, float y, float z", "Returns the vector x, y, z");
		addHelp("globals", "block", "int id", "Returns a Scripted Block with ID");
		addHelp("globals", "log", "Object obj", "Prints obj to Console");
		addHelp("globals", "chat", "string chat", "Sends chat to the current player as a chat message");
		addHelp("globals", "itemId", "string itemName", "Returns the ID of the item");
		addHelp("globals", "player", "string nickname", "Returns a PlayerAPI for nickname");
		addHelp("globals", "registerCommand", "string name, function(args) cmd", "Running /c name args will run cmd with args");
		addHelp("globals", "registerWebpoint", "string name, function() cmd", "Visiting the server address on port" + MinecraftScriptHTTPServer.getPort() + " with a web brower will trigger this function");
		addHelp("globals", "difficulty", "string difficulty", "Sets the Difficulty to difficulty, difficulty can be peaceful, easy, normal or hard");
		
		addHelp("playerapi1", "getHealth", "", "Returns the health of the player");
		addHelp("playerapi1", "heal", "int amount", "Heals the player by amount");
		addHelp("playerapi1", "give", "int itemID, int count", "Gives count of Item ID id to the player");
		addHelp("playerapi1", "pos", "", "Returns the player's location as a vector");
		addHelp("playerapi1", "tp", "vector3f v", "Teleports the player to v");
		addHelp("playerapi1", "addEffect", "string effectName, int level, int time", "Gives the player effectName at level for time ticks");
		addHelp("playerapi1", "cureAll", "", "Cures the player of all effects");
		addHelp("playerapi2", "fly", "boolean fly", "Set's the ability for the player to fly and causes them to start flying");
		addHelp("playerapi2", "setOnFire", "boolean onFire", "Set's the player alight or extinguishes them");
		addHelp("playerapi2", "getLook", "", "Returns a vector of the player's current target, it will return the player's locaiton if the target is invalid");
		addHelp("playerapi2", "getHunger", "", "Returns the player's current food level");
		addHelp("playerapi2", "setHunger", "int value", "Set's the player's current hunger level");
		addHelp("playerapi2", "getItem", "", "Returns a itemapi for the current item the player is holding");
		addHelp("playerapi2", "getLookDirection", "", "Returns the direction the player is looking in");
		addHelp("playerapi2", "gamemode", "string or int", "Set's the current gamemode the player is on");
		addHelp("playerapi3", "command", "string command", "Run's command on the server");
		
		addHelp("worldapi1", "explode", "int amo, int x, int y, int z", "Explodes the point at x, y, z with a force of amo");
		addHelp("worldapi1", "getBlock", "vector3f pos", "Returns the Block ID at pos");
		addHelp("worldapi1", "dropBlock", "vector3f pos", "Drops the block at pos on the ground");
		addHelp("worldapi1", "setBlock", "int blockType, vector3f pos", "Set's the point at v to blockType");
		addHelp("worldapi1", "setCube", "int blockType, vector3f v1, vector3f v2", "Set's a area to blockType (max 2000 blocks modifyed");
		addHelp("worldapi1", "time", "long value, or nothing", "Set's the time in all worlds to value or 0");
		addHelp("worldapi1", "downfall", "boolean downfall, int time", "Sets the rain to be on or off, and the time it has to be off if turned off");
		addHelp("worldapi1", "killDrops", "vector3f pos, or nothing", "Destorys all droped items withen 100 blocks of the player or pos");
		addHelp("worldapi1", "growTree", "vector3f pos", "Grows a tree at pos and returns if a tree grew");
		addHelp("worldapi1", "growBigTree", "vector3f pos, double heightLimit, double scaleWidth, double leafDensity", "The parameters are capped but you can grow a big tree at pos");
		addHelp("worldapi2", "spawn", "vector3f pos, string monstorName", "Spawns monstorName at pos");
		
		addHelp("itemstackapi", "getCount", "", "Get's the number of items in a stack");
		addHelp("itemstackapi", "addCount", "int count", "Adds count to the stack");
		addHelp("itemstackapi", "setCount", "int count", "Set's the stack size to count");
		addHelp("itemstackapi", "getDamage", "", "Returns the damage\\metadata on the stack");
		addHelp("itemstackapi", "setDamage", "int dmg", "Set's the damage\\metadata on the stack");
		addHelp("itemstackapi", "enchant", "string enchantName, int level", "Add's a enchantment to the item");
		addHelp("itemstackapi", "tag", "", "Returns the NBT tag or a new tag for the item");
		
		addHelp("vector3f", "add", "float x, float y, float z", "Returns a new Vector with x, y, z added");
		addHelp("vector3f", "expand", "int side, float amount", "Moves the vector by amount in side");
		
		addHelp("nbtapi", "set", "string key, object obj", "Set's key to obj");
		addHelp("nbtapi", "getString", "string key", "Returns the value at key");
		addHelp("nbtapi", "getInt", "string key", "Returns the value at key");
		addHelp("nbtapi", "getFloat", "string key", "Returns the value at key");
		addHelp("nbtapi", "getDouble", "string key", "Returns the value at key");
		addHelp("nbtapi", "getCompound", "string key", "Returns the sub compound at key");
		addHelp("nbtapi", "tags", "", "Returns all tags");
		
		addHelp("effectnames1", "moveSpeed", "", "Increase the player's move speed");
		addHelp("effectnames1", "moveSlowdown", "", "Decreases the player's move speed");
		addHelp("effectnames1", "digSpeed", "", "Increase the player's mining speed");
		addHelp("effectnames1", "digSlowdown", "", "Decreases the player's mining speed");
		addHelp("effectnames1", "damageBoost", "", "Boosts the amount of damage the player does");
		addHelp("effectnames1", "heal", "", "Instantly heals the player");
		addHelp("effectnames1", "harm", "", "Instantly harms the player");
		addHelp("effectnames1", "jump", "", "Boosts the player's jumping height");
		addHelp("effectnames1", "confusion", "", "Nausea?");
		addHelp("effectnames1", "regeneration", "", "Regenerates the player's health over time");
		addHelp("effectnames2", "resistance", "", "Decreases the amount of damage done to the player");
		addHelp("effectnames2", "fireResist", "", "Decreases the amount of damage done by fire to the player");
		addHelp("effectnames2", "waterBreathing", "", "Allows the player to stay underwater without oxygen draining");
		addHelp("effectnames2", "invisibility", "", "Makes the player invisible");
		addHelp("effectnames2", "blindness", "", "Darkens the player's vision and shortens there draw distance");
		addHelp("effectnames2", "nightVision", "", "All blocks are visually at a light level of 16");
		addHelp("effectnames2", "hunger", "", "The player's hunger bar will drain much faster");
		addHelp("effectnames2", "weakness", "", "Decreases the amount of damage done by the player");
		addHelp("effectnames2", "poison", "", "The player will take damage over time, this will not kill them");
		
		addHelp("enchantsArmor", "protection", "Armor Enchant", "Converts environmental damage to armour damage");
		addHelp("enchantsArmor", "fireProtection", "Armor Enchant", "Protection against fire");
		addHelp("enchantsArmor", "featherFalling", "Boots Enchant", "Less fall damage");
		addHelp("enchantsArmor", "blastProtection", "Armor Enchant", "Protection against explosions");
		addHelp("enchantsArmor", "projectileProtection", "Armor Enchant", "Protection against projectile entities (e.g. arrows)");
		addHelp("enchantsArmor", "respiration", "Helm Enchant", "Decreases the rate of air loss underwater; increases time between damage while suffocating");
		addHelp("enchantsArmor", "aquaAffinity", "Helm Enchant", "Increases underwater mining rate");
		
	}
	
	@Override
	public String getCommandName() {
		return "mcshelp";
	}
	
	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + this.getCommandName() + " [api] - Leave api empty to list all apis";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		if (var2.length == 0) {
			var1.sendChatToPlayer("\u00A7cMinecraftScript Help Topics");
			for (HelpDoc doc : _helpDocs) {
				if (doc.api.equals("topics")) {
					var1.sendChatToPlayer("\u00A7f" + doc.name + " : \u00A77" + doc.description);
				}
			}
		} else {
			var1.sendChatToPlayer("\u00A7cMinecraftScript Help Topics for " + var2[0]);
			for (HelpDoc doc : _helpDocs) {
				if (doc.api.equals(var2[0])) {
					var1.sendChatToPlayer(" - \u00A7e" + doc.api + "\u00A7f : "  + doc.name + " (" + doc.args + ") : \u00A77" + doc.description);
				}
			}
		}
	}

}
