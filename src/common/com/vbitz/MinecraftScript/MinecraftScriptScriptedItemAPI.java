package com.vbitz.MinecraftScript;

import org.mozilla.javascript.Function;

import net.minecraft.src.ItemStack;

import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;
import com.vbitz.MinecraftScript.items.ScriptedItem;

public class MinecraftScriptScriptedItemAPI {
	
	private ScriptedItem itemId = null;
	
	public MinecraftScriptScriptedItemAPI(int id) {
		itemId = MinecraftScriptMod.getInstance().getScriptedItem(id);
	}
	
	public void give(int count) throws ScriptErrorException {
		if (ScriptingManager.getScriptRunner() == null) {
			throw new ScriptErrorException("A player must run this function");
		}
		ScriptingManager.getScriptRunner().inventory.addItemStackToInventory(new ItemStack(itemId, count));
	}
	
	public void give(MinecraftScriptPlayerAPI ply, int count) {
		ply.give(itemId.itemId, count);
	}
	
	public void set3D() {
		itemId.setFull3D();
	}
	
	public void onRightClick(Function func) {
		itemId.rightClickFunction = func;
	}
	
	public void rightClickConsumes(boolean set) {
		itemId.rightClickConsumes = set;
	}
	
	public void rightClickThrows(boolean set) {
		itemId.rightClickThrows = set;
	}
}
