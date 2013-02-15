package com.vbitz.MinecraftScript.scripting;

import com.vbitz.MinecraftScript.Vector3f;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;

public class ScriptRunnerCommandBlock extends ScriptRunner {

	private World worldObj;
	
	private Vector3f pos;
	
	public ScriptRunnerCommandBlock(ICommandSender sender) {
		if (sender instanceof TileEntityCommandBlock) {
			TileEntityCommandBlock c = ((TileEntityCommandBlock) sender);
			worldObj = c.getWorldObj();
			pos = new Vector3f(c.xCoord, c.yCoord, c.zCoord);
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
	
	@Override
	public Vector3f getPos() {
		return pos;
	}

}
