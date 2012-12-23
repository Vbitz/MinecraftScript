package com.vbitz.MinecraftScript;

import java.util.HashMap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import org.mozilla.javascript.Function;

import com.vbitz.MinecraftScript.blocks.ScriptedBlock;
import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;

public class MinecraftScriptScriptedBlockAPI {

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
	
	private ScriptedBlock blockID = null;
	
	public MinecraftScriptScriptedBlockAPI(int i) {
		blockID = MinecraftScriptMod.getInstance().getScriptedBlock(i);
	}
	
	public void setHardness(float hard) {
		blockID.setHardness(hard);
	}
	
	public boolean setBlockCreativeTab(String tab) {
		if (tabs.containsKey(tab)) {
			blockID.setCreativeTab(tabs.get(tab));
			return true;
		}
		return false;
	}
	
	public void setLight(float bright) {
		blockID.setLightValue(bright);
	}
	
	public boolean setTexture(String tex) {
		setTexture(tex, 0);
		setTexture(tex, 1);
		setTexture(tex, 2);
		setTexture(tex, 3);
		setTexture(tex, 4);
		return setTexture(tex, 5);
	}
	
	public boolean setTexture(String tex, int side) {
		if (texs.containsKey(tex)) {
			blockID.blockTextures.put(side, texs.get(tex));
			return true;
		}
		return false;
	}
	
	public void onRightClick(Function func) {
		blockID.lastUpdater = ScriptingManager.getScriptRunner();
		blockID.rightClickFunction = func;
	}
	
	public void onBlockUpdate(Function func) {
		blockID.lastUpdater = ScriptingManager.getScriptRunner();
		blockID.updateFunction = func;
	}
	
	public void give(int count) throws ScriptErrorException {
		if (ScriptingManager.getScriptRunner() == null) {
			throw new ScriptErrorException("A player must run this function");
		}
		ScriptingManager.getScriptRunner().inventory.addItemStackToInventory(new ItemStack(blockID, count));
	}
	
	public void give(MinecraftScriptPlayerAPI ply, int count) {
		ply.give(blockID.blockId, count);
	}

}
