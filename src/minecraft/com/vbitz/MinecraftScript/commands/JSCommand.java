package com.vbitz.MinecraftScript.commands;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.ScriptingManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class JSCommand extends CommandBase {

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
		return "js";
	}
	
	@Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + this.getCommandName() + " js-string";
	}

	@Override
	public void processCommand(ICommandSender cmdSender, String[] args) {
		if (args.length == 0) {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
		}
		
		try {
			String out = ScriptingManager.getTidyOutput(
					ScriptingManager.runString(concat(args, " "), MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(cmdSender.getCommandSenderName())));
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

}
