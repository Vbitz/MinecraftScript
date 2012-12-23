package com.vbitz.MinecraftScript.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.items.JSStick;

public class JSStickCommand extends CommandBase {

	private static String concat(String[] arr, String concatStr) {
		String ret = "";
		for (int i = 0; i < arr.length; i++) {
			if (i != arr.length - 1) {
				ret += arr[i] + concatStr;
			}
		}
		ret += arr[arr.length - 1];
		return ret;
	}
	
	@Override
	public String getCommandName() {
		return "jss";
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1iCommandSender) {
		return "/" + this.getCommandName() + " js-string";
	}
	
	@Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 0) {
			throw new WrongUsageException(this.getCommandUsage(sender));
		}
		if (!MinecraftScriptMod.getInstance().getClientSideEnabled()) {
			sender.sendChatToPlayer("Error: You need to enable MinecraftScript client functions for this command to work");
		}
		
		EntityPlayer ply = getCommandSenderAsPlayer(sender);
		if (ply.getCurrentEquippedItem() != null && ply.getCurrentEquippedItem().itemID == JSStick.itemID) {
			ItemStack stk = ply.getCurrentEquippedItem();
			stk.getTagCompound().setString("code", concat(args, " "));
		} else {
			ItemStack stk = new ItemStack(JSStick.getSingilton());
			NBTTagCompound comp = new NBTTagCompound();
			comp.setString("code", concat(args, " "));
			stk.setTagCompound(comp);
			ply.inventory.addItemStackToInventory(stk);
		}
	}

}
