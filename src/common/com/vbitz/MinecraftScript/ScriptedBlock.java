package com.vbitz.MinecraftScript;

import java.util.HashMap;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;

public class ScriptedBlock extends Block {

	private static HashMap<String, CreativeTabs> tabs = new HashMap<String, CreativeTabs>();
	
	static {
		tabs.put("blocks", CreativeTabs.tabBlock);
	}
	
	public ScriptedBlock(int blockId) {
		super(blockId, Material.ground);
	}
	
	public void setBlockHardness(float hard) {
		this.setHardness(hard);
	}
	
	public boolean setBlockCreativeTab(String tab) {
		if (tabs.containsKey(tab)) {
			this.setCreativeTab(tabs.get(tab));
			return true;
		}
		return false;
	}
	
	public void setBlockBrightness(float bright) {
		this.setLightValue(bright);
	}

}
