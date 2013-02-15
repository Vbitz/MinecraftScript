package com.vbitz.MinecraftScript.titleEntitys;

import com.vbitz.MinecraftScript.Vector3f;
import com.vbitz.MinecraftScript.survival.SurvivalNodeManager;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySurvivalNode extends TileEntity {
	private int energyCount = 0; // will be stored in external blocks later
	private String name = "untilted";
	
	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		// load
		energyCount = par1nbtTagCompound.getInteger("energyCount");
		name = par1nbtTagCompound.getString("name");
		if (name.equals("")) {
			name = "untilted";
		}
		super.readFromNBT(par1nbtTagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		// save
		par1nbtTagCompound.setInteger("energyCount", energyCount);
		par1nbtTagCompound.setString("name", name);
		super.writeToNBT(par1nbtTagCompound);
	}

	public int getAmount() {
		return energyCount;
	}
	
	public void addAmount(int energyCount) {
		this.energyCount += energyCount;
	}
	
	public void setAmount(int energyCount) {
		this.energyCount = energyCount;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Vector3f getPos() {
		return new Vector3f(xCoord, yCoord, zCoord);
	}
}
