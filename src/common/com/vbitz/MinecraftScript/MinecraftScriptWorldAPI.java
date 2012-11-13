package com.vbitz.MinecraftScript;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

public class MinecraftScriptWorldAPI {
	private World _world;
	private EntityPlayer _player;
	
	public MinecraftScriptWorldAPI(World wld, EntityPlayer ply) {
		this._world = wld;
		this._player = ply;
	}
	
	public void explode(int amo, int x, int y, int z) {
		if (!this._world.isRemote) {
			this._world.createExplosion(this._player, x, y, z, amo, false);
		}
	}
	
	public void setBlock(int blockType, int x, int y, int z) {
		this._world.setBlockWithNotify(x, y, z, blockType);
	}
	
	public void setBlockV(int blockType, Vector3f loc) {
		this._world.setBlockWithNotify((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), blockType);
	}
	
	public void setCube(int blockType, int x1, int y1, int z1, int x2, int y2, int z2) { // TODO : Optimize
		int x1f, x2f, y1f, y2f, z1f, z2f;
		if (x2 < x1) {
			x1f = x2;
			x2f = x1;
		} else {
			x1f = x1;
			x2f = x2;
		}
		if (y2 < y1) {
			y1f = y2;
			y2f = y1;
		} else {
			y1f = y1;
			y2f = y2;
		}
		if (z2 < z1) {
			z1f = z2;
			z2f = z1;
		} else {
			z1f = z1;
			z2f = z2;
		}
		int blocks = 0;
		for (int x = x1f; x < x2f; x++) {
			for (int y = y1f; y < y2f; y++) {
				for (int z = z1f; z < z2f; z++) {
					blocks++;
				}
			}
		}
		if (blocks > 2000) {
			if (ScriptingManager.getScriptRunner() != null) {
				ScriptingManager.getScriptRunner().sendChatToPlayer("Error: Too many blocks modifyed");
			}
			return;
		}
		for (int x = x1f; x < x2f; x++) {
			for (int y = y1f; y < y2f; y++) {
				for (int z = z1f; z < z2f; z++) {
					this._world.setBlockWithNotify(x, y, z, blockType);
				}
			}
		}
	}
	
	public void setCubeV(int blockType, Vector3f v1, Vector3f v2) {
		setCube(blockType, (int)v1.getX(), (int)v1.getY(), (int)v1.getZ(), (int)v2.getX(), (int)v2.getY(), (int)v2.getZ());
	}
	
	public void time(long value) {
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i)
        {
            MinecraftServer.getServer().worldServers[i].setWorldTime(value);
        }
	}
        public void time(){
                for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i)
        {
            MinecraftServer.getServer().worldServers[i].setWorldTime(0);
        }
	}

	public void biome(int biome, int x1, int z1, int x2, int z2){
		//being worked on, setting an area to an other biome, should be cool :D
	}

}
