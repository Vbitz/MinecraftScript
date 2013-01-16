package com.vbitz.MinecraftScript;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.mozilla.javascript.NativeJavaPackage;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/*
 * Tread with caution, this is a massively powerful API
 */
public class MinecraftScriptUnsafeAPI {
	public static void dir(Object obj, boolean getPublics, String search) {
		EntityPlayer ply = ScriptingManager.getScriptRunner();
		Class realClass = null;
		if (obj instanceof Class) {
			ply.sendChatToPlayer("Class Name: " + ((Class) obj).getName());
			realClass = (Class) obj;
		} else {
			ply.sendChatToPlayer("Class Name: " + obj.getClass().getName());
			realClass = obj.getClass();
		}
		if (!getPublics) {
			return;
		}
		ply.sendChatToPlayer("Methods: ");
		for (Method method : realClass.getDeclaredMethods()) {
			if (method.getName().contains(search)) {
				String args = "";
				for (Class params : method.getParameterTypes()) {
					args += params.getName() + ", ";
				}
				ply.sendChatToPlayer("  " + (method.isAccessible() ? "private " : "public ") + method.getReturnType().getSimpleName() + " " + method.getName() + "( " + args + " );");
			}
		}
	}
	
	public static void fmlMods() {
		EntityPlayer ply = ScriptingManager.getScriptRunner();
		ply.sendChatToPlayer("Mods: ");
		for (ModContainer mod : Loader.instance().getActiveModList()) {
			if (mod.getMod() != null) {
				ply.sendChatToPlayer("  " + mod.getModId() + " : " + mod.getMod().getClass().getName());
			}
		}
	}
	
	public static Object fmlMod(String modID) {
		for (ModContainer mod : Loader.instance().getActiveModList()) {
			if (!mod.getModId().equals(modID)) {
				continue;
			}
			if (mod.getMod() != null) {
				return mod.getMod();
			} else {
				return null;
			}
		}
		return null;
	}
	
	public void access(Object obj, String itemName) {
		EntityPlayer ply = ScriptingManager.getScriptRunner();
		if (obj == null) {
			return;
		}
		for (Method method : obj.getClass().getDeclaredMethods()) {
			if (method.getName().equals(obj)) {
				if (!method.isAccessible()) {
					ply.sendChatToPlayer("Making " + method.getName() + " public");
				}
				method.setAccessible(true);
			}
		}
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (field.getName().equals(obj)) {
				if (!field.isAccessible()) {
					ply.sendChatToPlayer("Making " + field.getName() + " public");
				}
				field.setAccessible(true);
			}
		}
	}
	
	public static Object getTileEntity(Vector3f pos) {
		return ScriptingManager.getScriptRunner().worldObj.getBlockTileEntity((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
	}
	
	public static Object getBlock(Vector3f pos) {
		return Block.blocksList[ScriptingManager.getScriptRunner().worldObj.getBlockId((int) pos.getX(), (int) pos.getY(), (int) pos.getZ())];
	}
	
	public static EntityPlayer getMe() {
		return ScriptingManager.getScriptRunner();
	}
	
	public static World getWorld() {
		return ScriptingManager.getScriptRunner().worldObj;
	}
}
