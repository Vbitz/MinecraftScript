package com.vbitz.MinecraftScript.items;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerPlayer;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class JSStick extends Item {
	public static JSStick instance = null;
	
	public JSStick(int itemID) {
		super(itemID);
		setIconCoord(5, 3);
		setFull3D();
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		if (!par2World.isRemote){
			if (par1ItemStack.getTagCompound().hasKey("code")) {
				try {
					JSScriptingManager.getInstance().runString(par1ItemStack.getTagCompound().getString("code"),
							new ScriptRunnerPlayer(par3EntityPlayer));
				} catch (InternalScriptingException e) {
					par3EntityPlayer.sendChatToPlayer("Error: " + par3EntityPlayer);
				}
			}
		}
		return par1ItemStack;
	}
	
	public static JSStick getSingilton() {
		return instance;
	}
}
