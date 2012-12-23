package com.vbitz.MinecraftScript;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;

public class ScriptedThrowable extends EntityThrowable {

	private Function onInpactFunction = null;
	private EntityLiving owner = null;
	
	public ScriptedThrowable(World par1World) {
		super(par1World);
	}
	
	public ScriptedThrowable(World par1World, EntityLiving par2EntityLiving) {
		super(par1World, par2EntityLiving);
		owner = par2EntityLiving;
	}
	
	public ScriptedThrowable(World par1World, double x, double y, double z) {
		super(par1World, x, y, z);
	}
	
	public ScriptedThrowable(World par1World, EntityLiving par2EntityLiving, Function func) {
		super(par1World, par2EntityLiving);
		onInpactFunction = func;
		owner = par2EntityLiving;
	}

	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if (onInpactFunction != null && !worldObj.isRemote) {
			ScriptingManager.enterContext();
			try {
				if (owner instanceof EntityPlayer) {
					ScriptingManager.runFunction(onInpactFunction,
							new MinecraftScriptWorldAPI(owner.worldObj, (EntityPlayer) owner),
							new MinecraftScriptPlayerAPI((EntityPlayer) owner),
							new Vector3f(var1.blockX, var1.blockY, var1.blockZ));
				}
			} catch (EcmaError e) {
				if (owner instanceof EntityPlayer) {
					((EntityPlayer) owner).sendChatToPlayer("Error: " + e.getMessage());
				}
			} catch (EvaluatorException e) {
				if (owner instanceof EntityPlayer) {
					((EntityPlayer) owner).sendChatToPlayer("Error: " + e.getMessage());
				}
			}
			ScriptingManager.exitContext();
		}
	}

}
