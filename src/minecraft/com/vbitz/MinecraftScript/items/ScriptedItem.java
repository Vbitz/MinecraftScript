package com.vbitz.MinecraftScript.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.vbitz.MinecraftScript.MinecraftScriptPlayerAPI;
import com.vbitz.MinecraftScript.MinecraftScriptWorldAPI;
import com.vbitz.MinecraftScript.ScriptedThrowable;
import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerPlayer;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

public class ScriptedItem extends Item {

	public int itemId = 0;
	
	public boolean rightClickConsumes = true;
	
	public boolean rightClickThrows = false;
	
	public IFunction rightClickFunction = null;
	
	public ScriptedItem(int itemId) {
		super(itemId);
		this.itemId = itemId;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		if (rightClickFunction != null) {
			if (rightClickThrows) {
				ScriptedThrowable thr = new ScriptedThrowable(par2World, par3EntityPlayer, rightClickFunction);
				par2World.spawnEntityInWorld(thr);
			} else {
				try {
					JSScriptingManager.getInstance().runFunction(new ScriptRunnerPlayer(par3EntityPlayer), rightClickFunction);
				} catch (InternalScriptingException e) {
					par3EntityPlayer.sendChatToPlayer("Error: " + e.getMessage());
				}
			}
		}
		if (rightClickConsumes) {
			par1ItemStack.stackSize--;
			return par1ItemStack;
		} else {
			return par1ItemStack;
		}
	}
	
}
