package com.vbitz.MinecraftScript;

import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerPlayer;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumGameType;

public class MinecraftScriptPlayerAPI {
	private EntityPlayer _player;
	
	private static HashMap<String, Integer> _effects = new HashMap<String, Integer>();
	private static HashMap<String, Integer> _gamemodes = new HashMap<String, Integer>();
	
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
		
		_gamemodes.put("survial", 0);
		_gamemodes.put("creative", 1);
		_gamemodes.put("adventure", 2);
	}
	
	public MinecraftScriptPlayerAPI(EntityPlayer ply) {
		_player = ply;
	}

	public int getHealth() {
		return _player.getHealth();
	}
	
	public void addHealth(int amount) {
		_player.heal(amount);
	}
	
	public void kill() {
		_player.heal(-100);
	}
	
	public int getHunger() {
		return _player.getFoodStats().getFoodLevel();
	}
	
	public void setHunger(int value) { // fix these on a server
		_player.getFoodStats().setFoodLevel(value);
	}
	
	public void give(int id, int count) {
		_player.inventory.addItemStackToInventory(new ItemStack(Item.itemsList[id], count, 0));
	}
	
	public Vector3f pos() {
		return new Vector3f(_player.posX, _player.posY, _player.posZ);
	}
	
	public void tp(Vector3f v) {
		_player.setPositionAndUpdate((double)v.getX(), (double)v.getY(), (double)v.getZ());
	}
	
	public void addEffect(String effectName, int level, int time) throws ScriptErrorException {
		if (_effects.containsKey(effectName)) {
			_player.addPotionEffect(new PotionEffect(_effects.get(effectName), time, level));
		} else {
			throw new ScriptErrorException("Effect not found");
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
	
	public Vector3f getLook() {	// This code is based off the code from Item.class
        double compY = _player.prevPosY + (_player.posY - _player.prevPosY) * (double)1.0F + 1.62D - (double)_player.yOffset;
        Vec3 playerPos = _player.worldObj.getWorldVec3Pool().getVecFromPool(_player.posX, compY, _player.posZ);
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
	
	public MinecraftScriptItemStackAPI getItem() throws ScriptErrorException {
		if (_player.inventory.getCurrentItem() == null) {
			throw new ScriptErrorException("No Current Item");
		}
		return new MinecraftScriptItemStackAPI(_player.inventory.getCurrentItem());
	}
	
	public float getLookDirection() {
		if (_player.rotationPitch < 45) {
			return 4;
		} else if (_player.rotationPitch > 135) {
			return 5;
		}
		if (_player.rotationYaw < 45) {
			return 1;
		} else if (_player.rotationYaw < 135) {
			return 2;
		} else if (_player.rotationYaw < 225) {
			return 3;
		} else if (_player.rotationYaw < 315) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public void gamemode(String gamemode) {
		if (_gamemodes.containsKey(gamemode)) {
			gamemode(_gamemodes.get(gamemode));
		}
	}
	
	public void gamemode(int gamemode) {
		_player.setGameType(EnumGameType.getByID(gamemode));
	}
	
	public void runCommand(String command) {
		MinecraftServer.getServer().getCommandManager().executeCommand(_player, command);
	}
	
	public void addExp(int amo) {
		_player.addExperience(amo);
	}
	
	public void addExpLevels(int amo) {
		_player.addExperienceLevel(amo);
	}
	
	public String getName() {
		return _player.getEntityName();
	}
	
	public MinecraftScriptWorldAPI world() {
		return new MinecraftScriptWorldAPI(_player);
	}
	
	public void shootArrow(float damage, Object func) {
		final IFunction explodeFunc = JSScriptingManager.getInstance().getFunction(func);
		final EntityPlayer owner = _player;
		EntityArrow arrow = new EntityArrow(_player.worldObj, _player, 1.0f) {
			@Override
			public void onUpdate() {
				super.onUpdate();
				try {
					Field inGround = this.getClass().getSuperclass().getDeclaredField("inGround");
					inGround.setAccessible(true);
					boolean inGroundValue = inGround.getBoolean(this);
					if (inGroundValue && !worldObj.isRemote) {
						try {
							JSScriptingManager.getInstance().runFunction(new ScriptRunnerPlayer(owner), explodeFunc, 
									new Vector3f(this.posX, this.posY, this.posZ));
						} catch (InternalScriptingException e) {
							owner.sendChatToPlayer("Error in firecode: " + e.getMessage());
						}
						this.setDead();
					}
				} catch (Throwable t) { t.printStackTrace(); }
			}
		};
		try {
			Field damageObj = arrow.getClass().getSuperclass().getDeclaredField("damage");
			damageObj.setAccessible(true);
			damageObj.set(arrow, damage);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		_player.worldObj.spawnEntityInWorld(arrow);
	}
}
