package com.vbitz.MinecraftScript.commands;

import java.util.ArrayList;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.vbitz.MinecraftScript.MinecraftScriptCommandManager;
import com.vbitz.MinecraftScript.ScriptingManager;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.WrongUsageException;

public class JSCommandCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "c";
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1iCommandSender) {
		return "/" + this.getCommandName() + " js-command command-args";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		if (var2.length == 0) {
			throw new WrongUsageException(this.getCommandUsage(var1));
		}
		
		ScriptingManager.setScriptRunner(getCommandSenderAsPlayer(var1));
		if (MinecraftScriptCommandManager.containsCommand(var2[0])) {
			try {
				ScriptingManager.enterContext();
				if (var2.length > 1) {
					ArrayList<String> str = new ArrayList<String>();
					for (int i = 1; i < var2.length; i++) {
						str.add(var2[i]);
					}
					String out = ScriptingManager.getTidyOutput(MinecraftScriptCommandManager.runCommand(var2[0], str.toArray()));
					if (!out.equals("")) {var1.sendChatToPlayer(out); }
				} else {
					String out = ScriptingManager.getTidyOutput(MinecraftScriptCommandManager.runCommand(var2[0], new Object[] { }));
					if (!out.equals("")) {var1.sendChatToPlayer(out); }
				}
			} catch (EcmaError e) {
				var1.sendChatToPlayer("Error: " + e.getMessage());
				ScriptingManager.exitContext();
			} catch (EvaluatorException e) {
				var1.sendChatToPlayer("Error: " + e.getMessage());
				ScriptingManager.exitContext();
			}
		} else {
			var1.sendChatToPlayer("Error: Command not found, add with api.registerCommand(name, function)");
		}
		ScriptingManager.setScriptRunner(null);
	}

}
