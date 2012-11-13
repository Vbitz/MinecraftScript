package com.vbitz.MinecraftScript;

import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class MinecraftScriptWorldAPI {
	private World _world;
	private EntityPlayer _player;
	
	public MinecraftScriptWorldAPI(World wld, EntityPlayer ply) {
		this._world = wld;
		this._player = ply;
	}
	
	public void explode(int amo, Vector3f loc) {
		//if (!this._world.isRemote) {
			this._world.createExplosion(this._player, loc.getX(), loc.getY(), loc.getZ(), amo, true);
		//}
	}
	
	public void setBlock(int blockType, Vector3f loc) {
		this._world.setBlockWithNotify((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), blockType);
	}
	
	public void setCube(int blockType, Vector3f v1, Vector3f v2) {
		int x1f, x2f, y1f, y2f, z1f, z2f;
		if (v2.getX() < v1.getX()) {
			x1f = (int) v2.getX();
			x2f = (int) v1.getX();
		} else {
			x1f = (int) v1.getX();
			x2f = (int) v2.getX();
		}
		if (v2.getY() < v1.getY()) {
			y1f = (int) v2.getY();
			y2f = (int) v1.getY();
		} else {
			y1f = (int) v1.getY();
			y2f = (int) v2.getY();
		}
		if (v2.getZ() < v1.getZ()) {
			z1f = (int) v2.getZ();
			z2f = (int) v1.getZ();
		} else {
			z1f = (int) v1.getZ();
			z2f = (int) v2.getZ();
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
	
	public void time(long value) {
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i)
        {
            MinecraftServer.getServer().worldServers[i].setWorldTime(value);
        }
	}
    
	public void time(){
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
            MinecraftServer.getServer().worldServers[i].setWorldTime(0);
        }
	}

	public void biome(int biome, Vector3f v1, Vector3f v2){
		//being worked on, setting an area to an other biome, should be cool :D
	}
	
	public void killDrops() {
		Vec3 playerLoc = _player.getPosition(1.0f);
		
		List ents = _world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(
				playerLoc.xCoord - 100, playerLoc.yCoord - 100, playerLoc.zCoord - 100, playerLoc.xCoord + 100, playerLoc.yCoord + 100, playerLoc.zCoord + 100));
		for (Object object : ents) {
			if (object instanceof EntityItem) {
				((EntityItem) object).age = ((EntityItem) object).lifespan; // kill the little lagger
			}
		}
	}
	
	/*public void weatherRain(boolean Rain, int time){
		if(Rain)
		{
			_world.getWorldInfo().setRaining(true);
		}
		else
		{
			_world.getWorldInfo().setRaining(false);
			_world.getWorldInfo().setRainTime(time);
		}
	}*/

}
