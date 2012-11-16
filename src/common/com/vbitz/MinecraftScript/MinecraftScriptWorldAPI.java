package com.vbitz.MinecraftScript;

import java.util.List;
import java.util.Random;

import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenBigTree;
import net.minecraft.src.WorldGenTrees;

public class MinecraftScriptWorldAPI {
	private World _world;
	private EntityPlayer _player;
	
	public MinecraftScriptWorldAPI(World wld, EntityPlayer ply) {
		this._world = wld;
		this._player = ply;
	}
	
	public void explode(int amo, Vector3f loc) {
		if (!this._world.isRemote) {
			this._world.createExplosion(this._player, loc.getX(), loc.getY(), loc.getZ(), amo, true);
		}
	}
	
	public int getBlock(Vector3f loc) {
		return _world.getBlockId((int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
	}
	
	public void dropBlock(Vector3f loc) {
		if (_world.getBlockId((int) loc.getX(), (int) loc.getY(), (int) loc.getZ()) != 0) {
			EntityItem t = new EntityItem(_world, loc.getX(), loc.getY(), loc.getZ(), 
				new ItemStack(_world.getBlockId((int) loc.getX(), (int) loc.getY(), (int) loc.getZ()),
						1, _world.getBlockMetadata((int) loc.getX(), (int) loc.getY(), (int) loc.getZ())));
			_world.spawnEntityInWorld(t);
			_world.setBlockWithNotify((int) loc.getX(), (int) loc.getY(), (int) loc.getZ(), 0);
		}
	}
	
	public void setBlock(int blockType, Vector3f loc) {
		this._world.setBlockWithNotify((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), blockType);
	}
	
	public void setCube(int blockType, Vector3f v1, Vector3f v2) throws ScriptErrorException {
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
			throw new ScriptErrorException("Too many Blocks");
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
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
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
	
	public void killDrops(Vector3f pos) {
		List ents = _world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(
				pos.getX() - 100, pos.getY() - 100, pos.getZ() - 100, pos.getX() + 100, pos.getY() + 100, pos.getZ() + 100));
		for (Object object : ents) {
			if (object instanceof EntityItem) {
				((EntityItem) object).age = ((EntityItem) object).lifespan; // kill the little lagger
			}
		}
	}
	
	public void downfall(boolean rain, int time){
		if(rain)
		{
			_world.getWorldInfo().setRaining(true);
		}
		else
		{
			_world.getWorldInfo().setRaining(false);
			_world.getWorldInfo().setRainTime(time);
		}
	}
	
	public void downfall(boolean rain) {
		downfall(rain, _world.rand.nextInt(20000) + 1000);
	}
	
	public boolean growTree(Vector3f pos) {
		WorldGenTrees t = new WorldGenTrees(true);
		return t.generate(_world, _world.rand, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
	}
	
	public boolean growBigTree(Vector3f pos, double heightLimit, double scaleWidth, double leafDensity) throws ScriptErrorException {
		if (heightLimit < 4 && scaleWidth < 3 && leafDensity < 3) {
			WorldGenBigTree t = new WorldGenBigTree(true);
			t.setScale(heightLimit, scaleWidth, leafDensity);
			return t.generate(_world, _world.rand, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ());	
		} else {
			throw new ScriptErrorException("growBigTree is limited");
		}
	}

}
