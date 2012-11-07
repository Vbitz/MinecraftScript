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
	
	public void time(long value) {
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i)
        {
            MinecraftServer.getServer().worldServers[i].setWorldTime(value);
        }
	}
}
