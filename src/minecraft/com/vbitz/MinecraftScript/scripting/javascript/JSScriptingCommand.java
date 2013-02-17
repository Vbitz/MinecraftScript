package com.vbitz.MinecraftScript.scripting.javascript;

import java.io.IOException;
import java.util.ArrayList;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.vbitz.MinecraftScript.MinecraftScriptCommandManager;
import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.commands.ScriptingCommand;
import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.items.JSStick;
import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerPlayer;

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
	public void runString(ScriptRunner cmdSender, String code) {
			String out = "";
			try {
				out = JSScriptingManager.getInstance().getTidyOutput(
						JSScriptingManager.getInstance().runString(code, cmdSender));
			} catch (InternalScriptingException e) {
				cmdSender.sendChat("Error: " + e.getMessage());
			}
			if (!out.equals("")) {
				cmdSender.sendChat(out);
			}
	}

	@Override
	public void runFile(ScriptRunner cmdSender, String filename) {
		try {
			JSScriptingManager.getInstance().runFile(filename, cmdSender);
		} catch (InternalScriptingException e) {
			cmdSender.sendChat("Error: "+ e.getMessage());
		}
	}
	
	private String getBookUsage(ScriptRunner cmdSender) {
		return "/" + this.getName() + " dobook - with a signed book in your hand";
	}

	@Override
	public void runBook(ScriptRunner cmdSender) {
		if (cmdSender.getPlayer() == null) {
			throw new WrongUsageException(getBookUsage(cmdSender));
		}
		
		EntityPlayer var3 = (EntityPlayer) cmdSender.getPlayer();
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
			JSScriptingManager.getInstance().runString(script, cmdSender);
		} catch (InternalScriptingException e) {
			cmdSender.sendChat("Error: " + e.getMessage());
		}
	}

	@Override
	public void createStick(ScriptRunner cmdSender, String code) {
		if (!MinecraftScriptMod.getInstance().getClientSideEnabled()) {
			cmdSender.sendChat("Error: You need to enable MinecraftScript client functions for this command to work");
			return;
		}
		
		EntityPlayer ply = cmdSender.getPlayer();
		if (ply == null) {
			cmdSender.sendChat("You need to be a player to use this command");
			// it would be useful for a command block to be able to use this
			return;
		}
		ItemStack stk = new ItemStack(JSStick.getSingilton());
		NBTTagCompound comp = new NBTTagCompound();
		comp.setString("code", code);
		stk.setTagCompound(comp);
		ply.inventory.addItemStackToInventory(stk);
	}
	
	@Override
	public void runCommand(ScriptRunner cmdSender, String commandName,
			String[] args) {
		if (MinecraftScriptCommandManager.containsCommand(commandName)) {
			try {
				MinecraftScriptCommandManager.runCommand(commandName, args, cmdSender);
			} catch (InternalScriptingException e) {
				cmdSender.sendChat(e.getMessage());
			}
		} else {
			cmdSender.sendChat("Error: Command not found, add with registerCommand(name, function)");
		}
	}

	@Override
	public String getName() {
		return "js";
	}
	
	public static void registerCommand(String name) {
		instance.commandNames.add(name);
	}

	@Override
	public boolean canRunCommand(ScriptRunner r) {
		return r.isOP() || r.isSurvival();
	}
	
}
