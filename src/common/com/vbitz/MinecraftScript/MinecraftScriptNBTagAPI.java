package com.vbitz.MinecraftScript;

import java.util.ArrayList;

import org.mozilla.javascript.NativeArray;

import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;

public class MinecraftScriptNBTagAPI {
	private NBTTagCompound _comp = null;
	
	public MinecraftScriptNBTagAPI(NBTTagCompound comp) {
		_comp = comp;
	}
	
	public String getString(String key) {
		return _comp.getString(key);
	}
	
	public void set(String key, String value) {
		_comp.setString(key, value);
	}
	
	public int getInt(String key) {
		return _comp.getInteger(key);
	}
	
	public void set(String key, int value) {
		_comp.setInteger(key, value);
	}

	public float getFloat(String key) {
		return _comp.getFloat(key);
	}
	
	public void set(String key, float value) {
		_comp.setFloat(key, value);
	}
	
	public double getDouble(String key) {
		return _comp.getDouble(key);
	}
	
	public void set(String key, double value) {
		_comp.setDouble(key, value);
	}
	
	public MinecraftScriptNBTagAPI getCompound(String key) {
		return new MinecraftScriptNBTagAPI(_comp.getCompoundTag(key));
	}
	
	public NativeArray tags() {
		ArrayList<String> ret = new ArrayList<String>();
		for (Object tag : _comp.getTags()) {
			if (tag instanceof NBTBase) {
				ret.add(((NBTBase) tag).getName());
			}
		}
		return new NativeArray(ret.toArray(new String[] {}));
	}
}
