package com.vbitz.MinecraftScript.commands;

import java.io.IOException;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.vbitz.MinecraftScript.ScriptingManager;

public class JSDofileCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "jsdofile";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + this.getCommandName() + " filename";
	}
	
	@Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public void processCommand(ICommandSender cmdSender, String[] args) {
		if (args.length == 0) {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
		}
		
		try {
			ScriptingManager.doFile(args[0]);
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

}