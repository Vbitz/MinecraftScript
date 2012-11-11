package com.vbitz.MinecraftScript.items;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.vbitz.MinecraftScript.ScriptingManager;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class JSStick extends Item {
	private static JSStick _singilton = null;
	
	public static final int itemID = 512;
	
	public JSStick() {
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
					ScriptingManager.runString(par1ItemStack.getTagCompound().getString("code"), par3EntityPlayer);
				} catch (EcmaError e) {
					par3EntityPlayer.sendChatToPlayer("Error: " + e.toString());
					ScriptingManager.exitContext();
				} catch (EvaluatorException e) {
					par3EntityPlayer.sendChatToPlayer("Error: " + e.toString());
					ScriptingManager.exitContext();
				}
			}
		}
		return par1ItemStack;
	}
	
	public static JSStick getSingilton() {
		if (_singilton == null) {
			_singilton = new JSStick();
		}
		return _singilton;
	}
}
