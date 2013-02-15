package com.vbitz.MinecraftScript.commands;

import java.util.ArrayList;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerCommandBlock;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerPlayer;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityCommandBlock;

public abstract class ScriptingCommand extends CommandBase {
	
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
	
	private static String[] slice(String[] arr, int start, int length) {
		String[] ret = new String[length - start];
		int x = 0;
		for (int i = start; i < length; i++) {
			ret[x++] = arr[i];
		}
		return ret;
	}
	
	protected ArrayList<String> commandNames = new ArrayList<String>();
	
	public abstract boolean canRunCommand(ScriptRunner s);
	public abstract String getName();
	public abstract void runString(ScriptRunner cmdSender, String code);
	public abstract void runFile(ScriptRunner cmdSender, String code);
	public abstract void runBook(ScriptRunner cmdSender);
	public abstract void runCommand(ScriptRunner cmdSender, String commandName, String[] args);
	public abstract void createStick(ScriptRunner cmdSender, String code);

	@Override
	public String getCommandName() {
		return getName();
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + this.getCommandName() + "";
	}

	@Override
	public void processCommand(ICommandSender cmdSender, String[] args) {
		if (args.length == 0) {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
		}
		
		ScriptRunner runner = null;
		if (cmdSender instanceof EntityPlayer) {
			runner = new ScriptRunnerPlayer((EntityPlayer) cmdSender);
		} else if (cmdSender instanceof TileEntityCommandBlock) {
			runner = new ScriptRunnerCommandBlock(cmdSender);
		}
		
		if (!canRunCommand(runner)) {
			return;
		}
		
		if (args[0].equals("dofile")) {
			if (args.length > 1) {
				runFile(runner, args[1]);
			} else {
				throw new WrongUsageException("/" + this.getCommandName() + " dofile filename");
			}
		} else if (args[0].equals("dobook")) {
			runBook(runner);
		} else if (args[0].equals("stick")) {
			createStick(runner, concat(args, 1, " "));
		} else if (commandNames.contains(args[0])) {
			runCommand(runner, args[0], slice(args, 1, args.length));
		} else {
			runString(runner, concat(args, 0, " "));
		}
	}
	
}
