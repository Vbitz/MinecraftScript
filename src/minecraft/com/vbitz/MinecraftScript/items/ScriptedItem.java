package com.vbitz.MinecraftScript.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;

import com.vbitz.MinecraftScript.MinecraftScriptPlayerAPI;
import com.vbitz.MinecraftScript.MinecraftScriptWorldAPI;
import com.vbitz.MinecraftScript.ScriptedThrowable;
import com.vbitz.MinecraftScript.ScriptingManager;

public class ScriptedItem extends Item {

	public int itemId = 0;
	
	public boolean rightClickConsumes = true;
	
	public boolean rightClickThrows = false;
	
	public Function rightClickFunction = null;
	
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
				ScriptingManager.enterContext();
				try {
					ScriptingManager.runFunction(rightClickFunction,
							new MinecraftScriptWorldAPI(par2World, par3EntityPlayer),
							new MinecraftScriptPlayerAPI(par3EntityPlayer));
				} catch (EcmaError e) {
					par3EntityPlayer.sendChatToPlayer("Error: " + e.getMessage());
				} catch (EvaluatorException e) {
					par3EntityPlayer.sendChatToPlayer("Error: " + e.getMessage());
				}
				ScriptingManager.exitContext();
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
