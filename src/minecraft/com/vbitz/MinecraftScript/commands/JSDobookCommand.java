package com.vbitz.MinecraftScript.commands;

import java.io.IOException;
import java.util.List;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.vbitz.MinecraftScript.ScriptingManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;

public class JSDobookCommand extends CommandBase {

	@Override
	public int compareTo(Object o) {
		return this.getCommandName().compareTo(((ICommand)o).getCommandName());
	}

	@Override
	public String getCommandName() {
		return "jsdobook";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/" + this.getCommandName() + " - with a signed book in your hand";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}
	
	@Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public void processCommand(ICommandSender cmdSender, String[] args) {
		if (cmdSender.getCommandSenderName() == "Rcon") {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
		}
		
		EntityPlayerMP var3 = func_82359_c(cmdSender, cmdSender.getCommandSenderName());
		if (var3.getCurrentEquippedItem() == null) {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
		}
		if (var3.getCurrentEquippedItem().itemID != 387) {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
		}
		
		ItemStack stk = var3.getCurrentEquippedItem();
		
		if (!ItemWritableBook.validBookTagPages(stk.getTagCompound())) {
			throw new WrongUsageException(this.getCommandUsage(cmdSender));
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