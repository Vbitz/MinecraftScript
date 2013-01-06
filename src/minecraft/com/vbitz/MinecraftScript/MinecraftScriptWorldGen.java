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

import cpw.mods.fml.common.IWorldGenerator;

public class MinecraftScriptWorldGen implements IWorldGenerator {

	private static Function _func = null;
	private static EntityPlayer _lastPlayer = null;
	
	public static void setFunc(Function func, EntityPlayer ply) {
		_func = func;
		_lastPlayer = ply;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (_func == null) {
			return;
		}
		ScriptingManager.enterContext();
		ScriptingManager.setScriptRunner(_lastPlayer);
		try {
			ScriptingManager.runFunction(_func, new Vector3f(chunkX * 16, 0, chunkZ * 16), new MinecraftScriptWorldAPI(world, _lastPlayer));
		} catch (EcmaError e) {
			_lastPlayer.sendChatToPlayer("Error: " + e.getMessage());
		} catch (EvaluatorException e) {
			_lastPlayer.sendChatToPlayer("Error: " + e.getMessage());
		}
		ScriptingManager.setScriptRunner(null);
		ScriptingManager.exitContext();
	}

}
