package com.vbitz.MinecraftScript.commands;

import java.io.IOException;
import java.util.ArrayList;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.vbitz.MinecraftScript.MinecraftScriptCommandManager;
import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.items.JSStick;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerPlayer;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;

public class JSScriptingCommand extends ScriptingCommand {
	
	private static JSScriptingCommand instance;
	
	public JSScriptingCommand() {
		instance = this;
	}
	
	@Override
	public void runString(ICommandSender cmdSender, String code) {
			String out = "";
			try {
				out = JSScriptingManager.getInstance().getTidyOutput(
						JSScriptingManager.getInstance().runString(code, new ScriptRunnerPlayer(func_82359_c(cmdSender, cmdSender.getCommandSenderName()))));
			} catch (InternalScriptingException e) {
				cmdSender.sendChatToPlayer("Error: " + e.getMessage());
			}
			if (!out.equals("")) {
				cmdSender.sendChatToPlayer(out);
			}
	}

	@Override
	public void runFile(ICommandSender cmdSender, String filename) {
		try {
			JSScriptingManager.getInstance().runFile(filename, new ScriptRunnerPlayer(func_82359_c(cmdSender, cmdSender.getCommandSenderName())));
		} catch (InternalScriptingException e) {
			cmdSender.sendChatToPlayer("Error: "+ e.getMessage());
		}
	}
	
	private String getBookUsage(ICommandSender cmdSender) {
		return "/" + this.getName() + " dobook - with a signed book in your hand";
	}

	@Override
	public void runBook(ICommandSender cmdSender) {
		if (cmdSender.getCommandSenderName() == "Rcon") {
			throw new WrongUsageException(getBookUsage(cmdSender));
		}
		
		EntityPlayerMP var3 = func_82359_c(cmdSender, cmdSender.getCommandSenderName());
		if (var3.getCurrentEquippedItem() == null) {
			throw new WrongUsageException(getBookUsage(cmdSender));
		}
		if (var3.getCurrentEquippedItem().itemID != 387) {
			throw new WrongUsageException(getBookUsage(cmdSender));
		}
		
		ItemStack stk = var3.getCurrentEquippedItem();
		
		if (!ItemWritableBook.validBookTagPages(stk.getTagCompound())) {
			throw new WrongUsageException(getBookUsage(cmdSender));
		}
		
		String script = "";
		
        NBTTagList var1 = (NBTTagList)stk.getTagCompound().getTag("pages");

        for (int var2 = 0; var2 < var1.tagCount(); ++var2)
        {
            NBTTagString var4 = (NBTTagString)var1.tagAt(var2);

            if (var4.data != null)
            {
                script += var4.data + "\n";
            }
        }
		
        ScriptRunnerPlayer ply = new ScriptRunnerPlayer(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(cmdSender.getCommandSenderName()));
        
        try {
			JSScriptingManager.getInstance().runString(script, ply);
		} catch (InternalScriptingException e) {
			cmdSender.sendChatToPlayer("Error: " + e.getMessage());
		}
	}

	@Override
	public void createStick(ICommandSender cmdSender, String code) {
		if (!MinecraftScriptMod.getInstance().getClientSideEnabled()) {
			cmdSender.sendChatToPlayer("Error: You need to enable MinecraftScript client functions for this command to work");
			return;
		}
		
		EntityPlayer ply = getCommandSenderAsPlayer(cmdSender);
		if (ply.getCurrentEquippedItem() != null && ply.getCurrentEquippedItem().itemID == JSStick.itemID) {
			ItemStack stk = ply.getCurrentEquippedItem();
			stk.getTagCompound().setString("code", code);
		} else {
			ItemStack stk = new ItemStack(JSStick.getSingilton());
			NBTTagCompound comp = new NBTTagCompound();
			comp.setString("code", code);
			stk.setTagCompound(comp);
			ply.inventory.addItemStackToInventory(stk);
		}
	}
	
	@Override
	public void runCommand(ICommandSender cmdSender, String commandName,
			String[] args) {
		if (MinecraftScriptCommandManager.containsCommand(commandName)) {
			try {
				MinecraftScriptCommandManager.runCommand(commandName, args, new ScriptRunnerPlayer(getCommandSenderAsPlayer(cmdSender)));
			} catch (InternalScriptingException e) {
				cmdSender.sendChatToPlayer(e.getMessage());
			}
		} else {
			cmdSender.sendChatToPlayer("Error: Command not found, add with registerCommand(name, function)");
		}
	}

	@Override
	public String getName() {
		return "js";
	}
	
	public static void registerCommand(String name) {
		instance.commandNames.add(name);
	}
	
}
