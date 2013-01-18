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
import com.vbitz.MinecraftScript.ScriptingManager;

public class ScriptedBlock extends Block {
	
	public Function rightClickFunction = null;
	public Function updateFunction = null;
	
	public EntityPlayer lastUpdater = null;
	
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
			ScriptingManager.enterContext();
			try {
				ScriptingManager.runFunction(rightClickFunction, new MinecraftScriptWorldAPI(par1World, par5EntityPlayer), worldX, worldY, worldZ);
			} catch (EcmaError e) {
				par5EntityPlayer.sendChatToPlayer("Error: " + e.getMessage());
			} catch (EvaluatorException e) {
				par5EntityPlayer.sendChatToPlayer("Error: " + e.getMessage());
			} catch (Error e) {
				par5EntityPlayer.sendChatToPlayer("Error: " + e.getMessage());
			}
			ScriptingManager.exitContext();
		}
		return true;
	}
	
	@Override
	public void onNeighborBlockChange(World par1World, int worldX, int worldY,
			int worldZ, int neiBlock) {
		if (updateFunction != null) {
			ScriptingManager.enterContext();
			try {
				ScriptingManager.runFunction(updateFunction, new MinecraftScriptWorldAPI(par1World, lastUpdater), worldX, worldY, worldZ);
			} catch (EcmaError e) {
				lastUpdater.sendChatToPlayer("Error: " + e.getMessage());
			} catch (EvaluatorException e) {
				lastUpdater.sendChatToPlayer("Error: " + e.getMessage());
			} catch (Error e) {
				lastUpdater.sendChatToPlayer("Error: " + e.getMessage());
			}
			ScriptingManager.exitContext();
		}
		super.onNeighborBlockChange(par1World, worldX, worldY, worldZ, neiBlock);
	}

}
