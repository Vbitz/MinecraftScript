package com.vbitz.MinecraftScript.commands;

import java.io.IOException;
import java.util.ArrayList;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.vbitz.MinecraftScript.MinecraftScriptCommandManager;
import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.ScriptingManager;
import com.vbitz.MinecraftScript.items.JSStick;

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
		try {
			String out = ScriptingManager.getTidyOutput(
					ScriptingManager.runString(code, MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(cmdSender.getCommandSenderName())));
			if (!out.equals("")) {
				cmdSender.sendChatToPlayer(out);
			}
		} catch (EcmaError e) {
			cmdSender.sendChatToPlayer("Error: " + e.getMessage());
			ScriptingManager.exitContext();
		} catch (EvaluatorException e) {
			cmdSender.sendChatToPlayer("Error: " + e.getMessage());
			ScriptingManager.exitContext();
		} catch (Error e) {
			cmdSender.sendChatToPlayer("Error: " + e.getMessage());
			ScriptingManager.exitContext();
		}
	}

	@Override
	public void runFile(ICommandSender cmdSender, String filename) {
		try {
			ScriptingManager.doFile(filename);
		} catch (EcmaError e) {
			cmdSender.sendChatToPlayer("Error: " + e.toString());
			ScriptingManager.exitContext();
		} catch (EvaluatorException e) {
			cmdSender.sendChatToPlayer("Error: " + e.toString());
			ScriptingManager.exitContext();
		} catch (IOException e) {
			cmdSender.sendChatToPlayer("Error: " + e.toString());
			ScriptingManager.exitContext();
		} catch (Error e) {
			cmdSender.sendChatToPlayer("Error: " + e.getMessage());
			ScriptingManager.exitContext();
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
		
		try {
			ScriptingManager.runString(script, MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(cmdSender.getCommandSenderName()));
		} catch (EcmaError e) {
			cmdSender.sendChatToPlayer("Error: " + e.toString());
			ScriptingManager.exitContext();
		} catch (EvaluatorException e) {
			cmdSender.sendChatToPlayer("Error: " + e.toString());
			ScriptingManager.exitContext();
		} catch (Error e) {
			cmdSender.sendChatToPlayer("Error: " + e.getMessage());
			ScriptingManager.exitContext();
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
		ScriptingManager.setScriptRunner(getCommandSenderAsPlayer(cmdSender));
		if (MinecraftScriptCommandManager.containsCommand(commandName)) {
			try {
				ScriptingManager.enterContext();
					String out = ScriptingManager.getTidyOutput(MinecraftScriptCommandManager.runCommand(commandName, args));
					if (!out.equals("")) {cmdSender.sendChatToPlayer(out); }
			} catch (EcmaError e) {
				cmdSender.sendChatToPlayer("Error: " + e.getMessage());
				ScriptingManager.exitContext();
			} catch (EvaluatorException e) {
				cmdSender.sendChatToPlayer("Error: " + e.getMessage());
				ScriptingManager.exitContext();
			} catch (Error e) {
				cmdSender.sendChatToPlayer("Error: " + e.getMessage());
				ScriptingManager.exitContext();
			}
		} else {
			cmdSender.sendChatToPlayer("Error: Command not found, add with registerCommand(name, function)");
		}
		ScriptingManager.setScriptRunner(null);
	}

	@Override
	public String getName() {
		return "js";
	}
	
	public static void registerCommand(String name) {
		instance.commandNames.add(name);
	}
	
}
