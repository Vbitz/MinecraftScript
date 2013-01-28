package com.vbitz.MinecraftScript.blocks;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;

import com.vbitz.MinecraftScript.MinecraftScriptWorldAPI;
import com.vbitz.MinecraftScript.Vector3f;
import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerPlayer;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

public class ScriptedBlock extends Block {
	
	public IFunction rightClickFunction = null;
	public IFunction updateFunction = null;
	
	public ScriptRunner lastUpdater = null;
	
	public HashMap<Integer, Integer> blockTextures = new HashMap<Integer, Integer>();
	
	public int blockId = 0;
	
	public ScriptedBlock(int blockId) {
		super(blockId, Material.ground);
		this.blockId = blockId;
	}
	
	@Override
	public int getBlockTextureFromSide(int par1) {
		if (blockTextures.containsKey(par1)) {
			return blockTextures.get(par1);
		} else {
			return 0;
		}
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int worldX, int worldY,
			int worldZ, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		if (rightClickFunction != null) {
			try {
				JSScriptingManager.getInstance().runFunction(new ScriptRunnerPlayer(par5EntityPlayer), rightClickFunction, new Vector3f(worldX, worldY, worldZ));
			} catch (InternalScriptingException e) {
				lastUpdater.sendChat("Error: " + e.getMessage());
			}
		}
		return true;
	}
	
	@Override
	public void onNeighborBlockChange(World par1World, int worldX, int worldY,
			int worldZ, int neiBlock) {
		if (updateFunction != null) {
			try {
				JSScriptingManager.getInstance().runFunction(lastUpdater, updateFunction, new Vector3f(worldX, worldY, worldZ));
			} catch (InternalScriptingException e) {
				lastUpdater.sendChat("Error: " + e.getMessage());
			}
		}
		super.onNeighborBlockChange(par1World, worldX, worldY, worldZ, neiBlock);
	}

}
