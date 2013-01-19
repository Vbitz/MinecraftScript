package com.vbitz.MinecraftScript.commands;

import com.vbitz.MinecraftScript.web.MinecraftScriptAPIKey;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class APIKeyCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "api";
	}
	
	@Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + this.getCommandName();
	}

	@Override
	public void processCommand(ICommandSender cmdSender, String[] args) {		
		cmdSender.sendChatToPlayer("Your API Key is: " + MinecraftScriptAPIKey.getApiKey(func_82359_c(cmdSender, cmdSender.getCommandSenderName())));
	}

}
