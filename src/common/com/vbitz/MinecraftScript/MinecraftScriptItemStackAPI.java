package com.vbitz.MinecraftScript;

import java.util.HashMap;

import net.minecraft.src.Enchantment;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public class MinecraftScriptItemStackAPI {
	private ItemStack _stack;
	
	private static HashMap<String, Integer> _enchants = new HashMap<String, Integer>();
	
	static {
		_enchants.put("protection", 0);
		_enchants.put("fireProtection", 1);
		_enchants.put("featherFalling", 2);
		_enchants.put("blastProtection", 3);
		_enchants.put("projectileProtection", 4);
		_enchants.put("respiration", 5);
		_enchants.put("aquaAffinity", 6);
		_enchants.put("sharpness", 16);
		_enchants.put("smite", 17);
		_enchants.put("baneOfArthropods", 18);
		_enchants.put("knockback", 19);
		_enchants.put("fireAspect", 20);
		_enchants.put("looting", 21);
		_enchants.put("efficiency", 32);
		_enchants.put("silkTouch", 33);
		_enchants.put("unbreaking", 34);
		_enchants.put("fortune", 35);
		_enchants.put("power", 48);
		_enchants.put("punch", 49);
		_enchants.put("flame", 50);
		_enchants.put("infinity", 51);
	}
	
	public MinecraftScriptItemStackAPI(ItemStack stack) {
		_stack = stack;
	}
	
	public int getCount() {
		return _stack.stackSize;
	}
	
	public void addCount(int count) {
		_stack.stackSize += count;
	}
	
	public void setCount(int count) {
		_stack.stackSize = count;
	}
	
	public int getDamage() {
		return _stack.getItemDamage();
	}
	
	public void setDamage(int dmg) {
		_stack.setItemDamage(dmg);
	}
	
	public void enchant(String enchantName, int enchantLevel) {
		if (_enchants.containsKey(enchantName)) {
			_stack.addEnchantment(Enchantment.enchantmentsList[_enchants.get(enchantName)], enchantLevel);
		}
	}
	
	public MinecraftScriptNBTagAPI tag() {
		if (_stack.getTagCompound() != null) {
			return new MinecraftScriptNBTagAPI(_stack.getTagCompound());
		} else {
			NBTTagCompound comp = new NBTTagCompound();
			_stack.setTagCompound(comp);
			return new MinecraftScriptNBTagAPI(comp);
		}
	}
}
