package com.vbitz.MinecraftScript;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.mozilla.javascript.net.sf.retrotranslator.runtime.java.lang._Integer;

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

import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerPlayer;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;
import com.vbitz.MinecraftScript.survival.MinecraftScriptSurvivalManager;
import com.vbitz.MinecraftScript.survival.SurvivalControl;
import com.vbitz.MinecraftScript.survival.SurvivalNodeManager;

public class MinecraftScriptSurvivalPlayerAPI {
	protected EntityPlayer _player;
	
	public MinecraftScriptSurvivalPlayerAPI(EntityPlayer ply) {
		_player = ply;
	}

	@SurvivalControl(cost = 0)
	public int getHealth() {
		return _player.getHealth();
	}
	

	@SurvivalControl(cost = 0)
	public int getHunger() {
		return _player.getFoodStats().getFoodLevel();
	}
	
	@SurvivalControl(cost = 0)
	public Vector3f getPos() {
		return new Vector3f(_player.posX, _player.posY, _player.posZ);
	}
	
	// free for the target node usefully
	@SurvivalControl(cost = 500)
	public void teleport(Vector3f dest) throws ScriptErrorException {
		if (SurvivalNodeManager.getDistinceToClosestNode(_player.worldObj, dest) < 64) {
			_player.setPositionAndUpdate(dest.getX(), dest.getY(), dest.getZ());
			// make sure were not going to suffocate the player
			MinecraftScriptSurvivalManager.functionSuccuss(null);
		} else {
			throw new ScriptErrorException("No node in range at destination");
		}
	}
	
	@SurvivalControl(cost = 0)
	public boolean getFire() {
		return _player.isBurning();
	}
	
	@SurvivalControl(cost = 0)
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
	
	@SurvivalControl(cost = 0)
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
	
	@SurvivalControl(cost = 0)
	public String getName() {
		return _player.getEntityName();
	}
}
