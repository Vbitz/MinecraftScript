package com.vbitz.MinecraftScript;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.management.relation.Relation;

import org.mozilla.javascript.NativeJavaPackage;

import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;

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
	private static void printMethods(EntityPlayer ply, Class<?> cls, String search, boolean recurse) {
		for (Method method : cls.getDeclaredMethods()) {
			if (method.getName().contains(search)) {
				String args = "";
				for (Class params : method.getParameterTypes()) {
					args += params.getName() + ", ";
				}
				// welcome to the future, this is a quantum access nuke
				method.setAccessible(true);
				ply.sendChatToPlayer("  " + cls.getSimpleName() + " : " + method.getReturnType().getSimpleName() + " " + method.getName() + "( " + args + " );");
			}
		}
		if (cls.getSuperclass() != null || !recurse) {
			printMethods(ply, cls.getSuperclass(), search, true);
		}
	}
	
	private static void printFields(EntityPlayer ply, Class<?> cls, String search, boolean recurse) {
		for (Field method : cls.getDeclaredFields()) {
			if (method.getName().contains(search)) {
				String args = "";
				method.setAccessible(true);
				ply.sendChatToPlayer("  " + cls.getSimpleName() + " : " + method.getType().getSimpleName() + " " + method.getName() + ";");
			}
		}
		if (cls.getSuperclass() != null || !recurse) {
			printFields(ply, cls.getSuperclass(), search, true);
		}
	}
	
	public static void dir(Object obj) {
		dir(obj, "", true);
	}
	
	public static void dir(Object obj, String search) {
		dir(obj, search, false);
	}
	
	public static void dir(Object obj, String search, boolean onlySelf) {
		EntityPlayer ply = ScriptingManager.getScriptRunner();
		Class realClass = null;
		if (obj instanceof Class) {
			ply.sendChatToPlayer("Class Name: " + ((Class) obj).getName());
			realClass = (Class) obj;
		} else {
			ply.sendChatToPlayer("Class Name: " + obj.getClass().getName());
			realClass = obj.getClass();
		}
		ply.sendChatToPlayer("Fields: ");
		printFields(ply, realClass, search, onlySelf);
		ply.sendChatToPlayer("Methods: ");
		printMethods(ply, realClass, search, onlySelf);
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
	
	public static void call(Object obj, String methodName, Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ScriptErrorException {
		for (Method method : obj.getClass().getDeclaredMethods()) {
			if (method.getName().equals(methodName)) {
				method.setAccessible(true);
				Class<?>[] params = method.getParameterTypes();
				Object[] realArgs = new Object[args.length];
				for (int i = 0; i < realArgs.length; i++) {
					if (args[i] instanceof Number) {
						if (args[i].getClass().getName().equals(params[i].getName())) {
							realArgs[i] = args[i];
						} else {
							if (params[i].getSimpleName() == "int") {
								realArgs[i] = ((Number) args[i]).intValue();
							} else if (params[i].getSimpleName() == "float") {
								realArgs[i] = ((Number) args[i]).floatValue();
							} else if (params[i].getSimpleName() == "double") {
								realArgs[i] = ((Number) args[i]).doubleValue();
							} else {
								MinecraftScriptMod.getLogger().severe(params[i] + " not implamented");
							}
						}
					} else {
						realArgs[i] = args[i];
					}
				}
				method.invoke(obj, realArgs);
				return;
			}
		}
		throw new ScriptErrorException("Method not Found");
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
