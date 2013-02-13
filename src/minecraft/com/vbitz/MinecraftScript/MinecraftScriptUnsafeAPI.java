package com.vbitz.MinecraftScript;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.management.relation.Relation;

import org.mozilla.javascript.NativeJavaPackage;

import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;
import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/*
 * Tread with caution, this is a massively powerful API
 */
public class MinecraftScriptUnsafeAPI {
	private static String concat(String[] arr, int start, String concatStr) {
		String ret = "";
		for (int i = start; i < arr.length; i++) {
			if (i != arr.length - 1) {
				ret += arr[i] + concatStr;
			}
		}
		ret += arr[arr.length - 1];
		return ret;
	}
	
	private static Object getRealObject(Object obj, Class<?> target) {
		if (obj instanceof Number) {
			if (obj.getClass().getName().equals(target.getName())) {
				return obj;
			} else {
				if (target.getSimpleName() == "int") {
					return ((Number) obj).intValue();
				} else if (target.getSimpleName() == "float") {
					return ((Number) obj).floatValue();
				} else if (target.getSimpleName() == "double") {
					return ((Number) obj).doubleValue();
				} else {
					MinecraftScriptMod.getLogger().severe(target.getSimpleName() + " not implamented");
					return obj;
				}
			}
		} else {
			return obj;
		}
	}
	
	private static void printMethods(ScriptRunner ply, Class<?> cls, String search, boolean recurse) {
		for (Method method : cls.getDeclaredMethods()) {
			if (method.getName().contains(search)) {
				String args = "";
				for (Class params : method.getParameterTypes()) {
					args += params.getName() + ", ";
				}
				// welcome to the future, this is a quantum access nuke
				method.setAccessible(true);
				ply.sendChat("  " + cls.getSimpleName() + " : " + method.getReturnType().getSimpleName() + " " + method.getName() + "( " + args + " );");
			}
		}
		if (cls.getSuperclass() != null || !recurse) {
			printMethods(ply, cls.getSuperclass(), search, true);
		}
	}
	
	private static void printFields(ScriptRunner ply, Class<?> cls, String search, boolean recurse) {
		for (Field method : cls.getDeclaredFields()) {
			if (method.getName().contains(search)) {
				String args = "";
				method.setAccessible(true);
				ply.sendChat("  " + cls.getSimpleName() + " : " + method.getType().getSimpleName() + " " + method.getName() + ";");
			}
		}
		if (cls.getSuperclass() != null || !recurse) {
			printFields(ply, cls.getSuperclass(), search, true);
		}
	}
	
	private static Field getFieldRecurse(Class<?> objClass, String name) {
		for (Field field : objClass.getDeclaredFields()) {
			if (field.getName().equals(name)) {
				return field;
			}
		}
		if (objClass.getSuperclass() != null) {
			return getFieldRecurse(objClass.getSuperclass(), name);
		} else {
			return null;
		}
	}
	
	public static void dir(Object obj) {
		dir(obj, "", true);
	}
	
	public static void dir(Object obj, String search) {
		dir(obj, search, false);
	}
	
	public static void dir(Object obj, String search, boolean onlySelf) {
		ScriptRunner ply = JSScriptingManager.getInstance().getScriptRunner();
		Class realClass = null;
		if (obj instanceof Class) {
			ply.sendChat("Class Name: " + ((Class) obj).getName());
			realClass = (Class) obj;
		} else {
			ply.sendChat("Class Name: " + obj.getClass().getName());
			realClass = obj.getClass();
		}
		ply.sendChat("Fields: ");
		printFields(ply, realClass, search, onlySelf);
		ply.sendChat("Methods: ");
		printMethods(ply, realClass, search, onlySelf);
	}
	
	public static String dirStr(Object obj) {
		final ArrayList<String> str = new ArrayList<String>();
		ScriptRunner runner = JSScriptingManager.getInstance().getScriptRunner();
		ScriptRunner logRunner = new ScriptRunner() {
			@Override
			public void sendChat(String msg) {
				str.add(msg);
			}
			
			@Override
			public boolean isOP() { return false; }
			
			@Override
			public World getWorld() { return null; }
			
			@Override
			public EntityPlayer getPlayer() { return null;}
		};
		JSScriptingManager.getInstance().setScriptRunner(logRunner);
		dir(obj, "", false);
		JSScriptingManager.getInstance().setScriptRunner(runner);
		return concat(str.toArray(new String[]{}), 0, "\n");
	}
	
	public static void fmlMods() {
		ScriptRunner ply = JSScriptingManager.getInstance().getScriptRunner();
		ply.sendChat("Mods: ");
		for (ModContainer mod : Loader.instance().getActiveModList()) {
			if (mod.getMod() != null) {
				ply.sendChat("  " + mod.getModId() + " : " + mod.getMod().getClass().getName());
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
	
	/*
	
	public static void call(Object obj, String methodName, Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ScriptErrorException {
		for (Method method : obj.getClass().getDeclaredMethods()) {
			if (method.getName().equals(methodName)) {
				method.setAccessible(true);
				Class<?>[] params = method.getParameterTypes();
				Object[] realArgs = new Object[args.length];
				for (int i = 0; i < realArgs.length; i++) {
					realArgs[i] = getRealObject(args[i], params[i]);
				}
				method.invoke(obj, realArgs);
				return;
			}
		}
		throw new ScriptErrorException("Method not Found");
	}
	
	public static Object getField(Object obj, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = getFieldRecurse(obj.getClass(), name);
		if (f == null) {
			return null;
		}
		if (!f.isAccessible()) {
			f.setAccessible(true);
		}
		return f.get(obj);
	}
	
	public static void setField(Object obj, String name, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = getFieldRecurse(obj.getClass(), name);
		if (f == null) {
			return;
		}
		if (!f.isAccessible()) {
			f.setAccessible(true);
		}
		f.set(obj, getRealObject(value, f.getType()));
	}
	
	*/
	
	public static Object getTileEntity(Vector3f pos) {
		return JSScriptingManager.getInstance().getScriptRunner().getWorld().getBlockTileEntity((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
	}
	
	public static Object getBlock(Vector3f pos) {
		return Block.blocksList[JSScriptingManager.getInstance().getScriptRunner().getWorld().getBlockId((int) pos.getX(), (int) pos.getY(), (int) pos.getZ())];
	}
	
	public static ItemStack getItemStack(EntityPlayer ply, int pos) {
		return ply.inventory.getStackInSlot(pos);
	}
	
	public static EntityPlayer getMe() {
		return JSScriptingManager.getInstance().getScriptRunner().getPlayer();
	}
	
	public static World getWorld() {
		return JSScriptingManager.getInstance().getScriptRunner().getWorld();
	}
	
	public static ItemStack getItem(int id, int metadata, int count) {
		return new ItemStack(id, count, metadata);
	}
}
