package com.vbitz.MinecraftScript.commands;

import java.util.ArrayList;
import java.util.List;

import com.vbitz.MinecraftScript.docs.KeyValueStore;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class KeyValueStoreCommand extends CommandBase {

	private static String concat(String[] arr, int start, String concatStr) {
		String ret = "";
		for (int i = start; i < arr.length; i++) {
			if (i != arr.length - 1) {
				ret += arr[i] + concatStr;
			}
		}
		ret += arr[arr.length - 1];
		return ret;
	}
	
	@Override
	public String getCommandName() {
		return "kvs";
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1iCommandSender) {
		return "/kvs set/get itemName itemValue";
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender par1iCommandSender,
			String[] par2ArrayOfStr) {
		if (par2ArrayOfStr.length == 2 && par2ArrayOfStr[0].equals("get")) {
			return KeyValueStore.getAll();
		} else {
			return new ArrayList<String>();
		}
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		if (var2.length == 0) {
			throw new WrongUsageException(getCommandUsage(var1));
		}
		
		if (var2[0].equals("get")) {
			if (var2.length == 2 && KeyValueStore.getItem(var2[1]) != null) {
				var1.sendChatToPlayer(KeyValueStore.getItem(var2[1]));
			}
		} else if (var2[0].equals("set")) {
			if (var2.length > 2) {
				KeyValueStore.setItem(var2[1], concat(var2, 2, " "));
				var1.sendChatToPlayer(var2[1] + " defined");
			}
		} else {
			throw new WrongUsageException(getCommandUsage(var1));
		}
	}

}
