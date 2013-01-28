package com.vbitz.MinecraftScript;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;

import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerPlayer;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

public class ScriptedThrowable extends EntityThrowable {

	private IFunction onInpactFunction = null;
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
	
	public ScriptedThrowable(World par1World, EntityLiving par2EntityLiving, IFunction func) {
		super(par1World, par2EntityLiving);
		onInpactFunction = func;
		owner = par2EntityLiving;
	}

	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if (onInpactFunction != null && !worldObj.isRemote) {
			try {
				JSScriptingManager.getInstance().runFunction(new ScriptRunnerPlayer(owner), onInpactFunction, new Vector3f(var1.blockX, var1.blockY, var1.blockZ));
			} catch (InternalScriptingException e) {
				if (owner instanceof EntityPlayer) {
					((EntityPlayer) owner).sendChatToPlayer(e.getMessage());
				}
			}
		}
	}

}
