package com.vbitz.MinecraftScript;

import java.util.HashMap;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.Vec3;

public class MinecraftScriptPlayerAPI {
	private EntityPlayer _player;
	
	private static HashMap<String, Integer> _effects = new HashMap<String, Integer>();
	
	static {
		_effects.put("moveSpeed", 1);
		_effects.put("moveSlowdown", 2);
		_effects.put("digSpeed", 3);
		_effects.put("digSlowdown", 4);
		_effects.put("damageBoost", 5);
		_effects.put("heal", 6);
		_effects.put("harm", 7);
		_effects.put("jump", 8);
		_effects.put("confusion", 9);
		_effects.put("regeneration", 10);
		_effects.put("resistance", 11);
		_effects.put("fireResist", 12);
		_effects.put("waterBreathing", 13);
		_effects.put("invisibility", 14);
		_effects.put("blindness", 15);
		_effects.put("nightVision", 16);
		_effects.put("hunger", 17);
		_effects.put("weakness", 18);
		_effects.put("poison", 19);
	}
	
	public MinecraftScriptPlayerAPI(EntityPlayer ply) {
		_player = ply;
	}

	public int getHealth() {
		return _player.getHealth();
	}
	
	public void heal(int amount) {
		_player.heal(amount);
	}
	
	public int getHunger() {
		return _player.getFoodStats().getFoodLevel();
	}
	
	public void setHunger(int value) {
		_player.getFoodStats().setFoodLevel(value);
	}
	
	public void give(int id, int count) {
		_player.inventory.addItemStackToInventory(new ItemStack(Item.itemsList[id], count, 0));
	}
	
	public Vector3f getLoc() {
		return new Vector3f(getLocX(), getLocY(), getLocZ());
	}
	
	public double getLocX() {
		return _player.getPosition(0.0f).xCoord;
	}
	
	public double getLocY() {
		return _player.getPosition(0.0f).yCoord;
	}
	
	public double getLocZ() {
		return _player.getPosition(0.0f).zCoord;
	}
	
	public void tp(double x, double y, double z) {
		_player.setPositionAndUpdate(x, y, z);
	}
	
	public void tpv(Vector3f v) {
		_player.setPositionAndUpdate((double)v.getX(), (double)v.getY(), (double)v.getZ());
	}
	
	public void addEffect(String effectName, int level, int time) {
		if (_effects.containsKey(effectName)) {
			_player.addPotionEffect(new PotionEffect(_effects.get(effectName), time, level));
		}
	}
	
	public void cureAll() {
		_player.curePotionEffects(new ItemStack(Item.bucketMilk));
	}
	
	public void fly(boolean setFly) {
		_player.capabilities.allowFlying = setFly;
		_player.capabilities.isFlying = setFly;
		_player.sendPlayerAbilities();
	}
	
	public void setOnFire(boolean onFire) {
		if (onFire) {
			_player.setFire(999999 / 20);
		} else {
			_player.extinguish();
		}
	}
	
	public Vector3f getPlayerLook() {	// This code is based off the code from Item.class
        double compY = _player.prevPosY + (_player.posY - _player.prevPosY) * (double)1.0F + 1.62D - (double)_player.yOffset;
        Vec3 playerPos = _player.worldObj.func_82732_R().getVecFromPool(_player.posX, compY, _player.posZ);
        float var1 = MathHelper.cos(-_player.rotationYaw * 0.017453292F - (float)Math.PI);
        float var2 = MathHelper.sin(-_player.rotationYaw * 0.017453292F - (float)Math.PI);
        float var3 = -MathHelper.cos(-_player.rotationPitch * 0.017453292F);
        float var4 = MathHelper.sin(-_player.rotationPitch * 0.017453292F);
        Vec3 targetPos = playerPos.addVector((double)(var2 * var3) * 500.0D, (double)var4 * 500.0D, (double)(var1 * var3) * 500.0D);
        MovingObjectPosition objPos = _player.worldObj.rayTraceBlocks_do_do(playerPos, targetPos, false, true);
        if (objPos != null) {
        	return new Vector3f(objPos.blockX, objPos.blockY, objPos.blockZ);
        } else {
        	return new Vector3f(_player.posX, compY, _player.posZ);
        }
	}
	
	public MinecraftScriptItemStackAPI getCurrentItem() {
		return new MinecraftScriptItemStackAPI(_player.inventory.getCurrentItem());
	}
}
