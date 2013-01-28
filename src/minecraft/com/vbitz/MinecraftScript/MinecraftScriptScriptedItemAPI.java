package com.vbitz.MinecraftScript;

import net.minecraft.item.ItemStack;

import org.mozilla.javascript.Function;

import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;
import com.vbitz.MinecraftScript.items.ScriptedItem;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

public class MinecraftScriptScriptedItemAPI {
	
	private ScriptedItem itemId = null;
	
	public MinecraftScriptScriptedItemAPI(int id) {
		itemId = MinecraftScriptMod.getInstance().getScriptedItem(id);
	}
	
	public void give(int count) throws ScriptErrorException {
		if (JSScriptingManager.getInstance().getScriptRunner() == null) {
			throw new ScriptErrorException("A player must run this function");
		}
		JSScriptingManager.getInstance().getScriptRunner().getPlayer().inventory.addItemStackToInventory(new ItemStack(itemId, count));
	}
	
	public void give(MinecraftScriptPlayerAPI ply, int count) {
		ply.give(itemId.itemId, count);
	}
	
	public void set3D() {
		itemId.setFull3D();
	}
	
	public void onRightClick(Object func) {
		itemId.rightClickFunction = JSScriptingManager.getInstance().getFunction(func);
	}
	
	public void rightClickConsumes(boolean set) {
		itemId.rightClickConsumes = set;
	}
	
	public void rightClickThrows(boolean set) {
		itemId.rightClickThrows = set;
	}
}
