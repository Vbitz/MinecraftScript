package com.vbitz.MinecraftScript.commands;

import java.util.Random;

import cpw.mods.fml.common.network.Player;

import net.minecraft.block.material.MapColor;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.MapData;

public class TestMapData extends CommandBase {

	@Override
	public String getCommandName() {
		return "testmapdata";
	}
	
	@Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public void processCommand(ICommandSender cmdSender, String[] args) {
		if (cmdSender.getCommandSenderName() == "Rcon") {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
		}
		
		EntityPlayerMP var3 = func_82359_c(cmdSender, cmdSender.getCommandSenderName());
		if (var3.getCurrentEquippedItem() == null) {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
		}
		if (var3.getCurrentEquippedItem().itemID != 358) {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
		}
		 
		ItemStack it = var3.getCurrentEquippedItem();
		it.setItemDamage(var3.worldObj.getUniqueDataId("map"));
		
		MapData dta = Item.map.getMapData(it, var3.worldObj);
		
		Random r = new Random();
		dta.xCenter = r.nextInt(120000);
		dta.zCenter = r.nextInt(120000);
		for (int i = 0; i < dta.colors.length; i++) {
			dta.colors[i] = (byte)(MapColor.mapColorArray[r.nextInt(14)].colorIndex * 4 + 2);
		}
	}

}
