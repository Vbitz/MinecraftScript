package com.vbitz.MinecraftScript;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class MinecraftScriptPlayerAPI {
	private EntityPlayer _player;
	
	public MinecraftScriptPlayerAPI(EntityPlayer ply) {
		_player = ply;
	}

	public int getHealth() {
		return _player.getHealth();
	}
	
	public void addHealth(int amount) {
		_player.heal(amount);
	}
	
	public void giveItem(int id, int count) {
		_player.inventory.addItemStackToInventory(new ItemStack(Item.itemsList[id], count, 0));
	}
}
