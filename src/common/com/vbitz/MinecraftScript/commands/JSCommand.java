package com.vbitz.MinecraftScript.commands;

import java.util.List;

import org.mozilla.javascript.EcmaError;

import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.ScriptingManager;

import net.minecraft.src.ICommand;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.WrongUsageException;

public class JSCommand implements ICommand {

	@Override
	public int compareTo(Object o) {
		return this.getCommandName().compareTo(((ICommand)o).getCommandName());
	}

	@Override
	public String getCommandName() {
		return "js";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + this.getCommandName() + " js-string";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender cmdSender, String[] args) {
		MinecraftScriptMod.getLogger().info(String.valueOf(args.length));
		if (args.length == 0) {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
		}
		
		try {
			cmdSender.sendChatToPlayer(ScriptingManager.runString(args[0].concat(" ")).toString());
		} catch (EcmaError e) {
			cmdSender.sendChatToPlayer("Error: " + e.toString());
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender var1) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
		// TODO Auto-generated method stub
		return null;
	}

}
