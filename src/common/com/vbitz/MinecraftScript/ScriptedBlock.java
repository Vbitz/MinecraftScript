package com.vbitz.MinecraftScript;

import java.util.HashMap;

import org.mozilla.javascript.Function;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class ScriptedBlock extends Block {

	private static HashMap<String, CreativeTabs> tabs = new HashMap<String, CreativeTabs>();
	public static HashMap<String, Integer> texs = new HashMap<String, Integer>();
	
	private static void addTextures(String[] strs) {
		for (int i = 0; i < strs.length; i++) {
			texs.put(strs[i], i);
		}
	}
	
	static {
		tabs.put("blocks", CreativeTabs.tabBlock);
		
		addTextures(new String[] {"grass", "stone", "dirt", "grassSide", "oakWood", "smoothSlabSide", "smoothSlabTop", "brick", "tntSide", "tntTop", "tntBottom", "web", "rose", "flower", "blue", "oakSapling", 
				"cobblestone", "bedrock", "sand", "gravel", "oakSide", "oakTop", "ironBlock", "goldBlock", "diamondBlock", "emaruldBlock", "empty1", "empty2", "redMushroom", "brownMushroom", "jungleSapling", "fireText1",
				"goldOre", "ironOre", "coalOre", "bookcase", "mossyCobble", "obsidiun", "biomeGrassSide", "longGrass", "unknown1", "empty3", "empty4", "craftingTop", "furnaceFront", "furnaceSide","dispencerFront", "fireText2",
				"sponge", "glass", "diamondOre", "redstoneOre", "leavesSide1", "leavesSide2", "stoneBrick", "deadShrub", "fern", "empty5", "empty6", "craftingSide", "craftingSide", "furnaceOnFront", "furnaceTop", "tagiaSapling",
				"wool", "monstorSpawner", "snow", "ice", "snowGrassSide", "cactusTop", "cactusSide", "cactusBottom", "clay", "reads", "musicBoxSide", "musicBoxTop"});
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
	
	public boolean setBlockTexture(String tex) {
		if (texs.containsKey(tex)) {
			this.blockIndexInTexture = texs.get(tex);
			return true;
		}
		return false;
	}
	
	public void setRightClickFunction(Function func) {
		MinecraftScriptMod.getLogger().info(func.toString());
	}

}
