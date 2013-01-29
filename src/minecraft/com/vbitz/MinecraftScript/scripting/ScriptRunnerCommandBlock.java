package com.vbitz.MinecraftScript.scripting;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;

public class ScriptRunnerCommandBlock extends ScriptRunner {

	private World worldObj;
	
	public ScriptRunnerCommandBlock(ICommandSender sender) {
		if (sender instanceof TileEntityCommandBlock) {
			worldObj = ((TileEntityCommandBlock) sender).getWorldObj();
		}
	}
	
	@Override
	public void sendChat(String msg) { }

	@Override
	public boolean isOP() {
		return true;
	}

	@Override
	public EntityPlayer getPlayer() {
		return null;
	}

	@Override
	public World getWorld() {
		return worldObj;
	}

}
