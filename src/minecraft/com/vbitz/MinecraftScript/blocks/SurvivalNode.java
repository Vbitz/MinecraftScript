package com.vbitz.MinecraftScript.blocks;

import com.vbitz.MinecraftScript.survival.SurvivalNodeManager;
import com.vbitz.MinecraftScript.titleEntitys.TileEntitySurvivalNode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SurvivalNode extends BlockContainer {

	public SurvivalNode(int itemID) {
		super(itemID, Material.iron);
		
		setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySurvivalNode();
	}
	
	@Override
	public int getBlockTextureFromSide(int par1) {
		return 22; // just looks like a iron block right now
	}

}
