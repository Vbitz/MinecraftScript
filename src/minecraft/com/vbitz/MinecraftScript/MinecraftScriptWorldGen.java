package com.vbitz.MinecraftScript;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;

import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerWorldGen;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

import cpw.mods.fml.common.IWorldGenerator;

public class MinecraftScriptWorldGen implements IWorldGenerator {

	private static IFunction _func = null;
	private static ScriptRunner _lastPlayer = null;
	
	public static void setFunc(IFunction func, ScriptRunner ply) {
		_func = func;
		_lastPlayer = ply;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (_func == null) {
			return;
		}
		
		try {
			JSScriptingManager.getInstance().runFunction(new ScriptRunnerWorldGen(world, _lastPlayer.getPlayer()), _func, new Vector3f(chunkX * 16, 0, chunkZ * 16));
		} catch (InternalScriptingException e) {
			_lastPlayer.sendChat("Error in WorldGen: " + e.getMessage());
		}
	}

}
