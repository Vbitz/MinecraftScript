package com.vbitz.MinecraftScript;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;

import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;

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
		setBlock(blockType, loc, true);
	}
	
	public void setBlock(int blockType, Vector3f loc, boolean update) {
		if (update) {
			this._world.setBlockWithNotify((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), blockType);
		} else {
			this._world.setBlock((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), blockType);
		}
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
	
	public void replaceCube(int srcType, int targetType, Vector3f v1, Vector3f v2) throws ScriptErrorException {
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
					if (_world.getBlockId(x, y, z) == srcType) {
						this._world.setBlockWithNotify(x, y, z, targetType);
						blocks++;
						if (blocks > 4000) {
							return;
						}
					}
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

	public void setBiome(int biome, Vector3f loc) { // not quite working yet, the client needs to reload to see the change
		byte[] bytes = new byte[16 * 16];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) biome;
		}
		Chunk chk = _world.getChunkFromBlockCoords((int) loc.getX(), (int) loc.getZ());
		chk.setBiomeArray(bytes);
		chk.setChunkModified();
	}
	
	public String getBiome(Vector3f pos) {
		return _world.getBiomeGenForCoords((int) pos.getX(), (int) pos.getZ()).biomeName;
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
	
	public void spawn(Vector3f pos, String name) throws ScriptErrorException {
		EntityLiving ent = null;
		if (name.equals("creeper")) {
			ent = new EntityCreeper(_world);
		} else if (name.equals("zombie")) {
			ent = new EntityZombie(_world);
		} else if (name.equals("spider")) {
			ent = new EntitySpider(_world);
		} else if (name.equals("skeleton")) {
			ent = new EntitySkeleton(_world);
		} else if (name.equals("pig")) {
			ent = new EntityPig(_world);
		} else if (name.equals("cow")) {
			ent = new EntityCow(_world);
		} else if (name.equals("chicken")) {
			ent = new EntityChicken(_world);
		} else if (name.equals("pigzombie")) {
			ent = new EntityPigZombie(_world);
		} else if (name.equals("enderman")) {
			ent = new EntityEnderman(_world);
		} else if (name.equals("enderdragon")) {
			ent = new EntityDragon(_world);
		} else if (name.equals("bat")) {
			ent = new EntityBat(_world);
		} else if (name.equals("villager")) {
			ent = new EntityVillager(_world);
		} else {
			throw new ScriptErrorException("Ent not found");
		}
		ent.initCreature();
		ent.setPosition(pos.getX(), pos.getY(), pos.getZ());
		_world.spawnEntityInWorld(ent);
	}
	
	public boolean grow(Vector3f pos) {
		ItemStack t = new ItemStack(Item.dyePowder, 1);
		t.setItemDamage(15);
		return Item.dyePowder.onItemUse(t, _player, _world, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), 0, 0, 0, 0);
	}
	
	public void activate(Vector3f pos) {
		if (_world.blockExists((int) pos.getX(), (int) pos.getY(), (int) pos.getZ())) {
			Block blk = Block.blocksList[_world.getBlockId((int) pos.getX(), (int) pos.getY(), (int) pos.getZ())];
			blk.onBlockActivated(_world, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), _player,
					0, 0, 0, 0);
		}
	}
	
	public void dropItem(int id, int count, Vector3f pos) {
		EntityItem t = new EntityItem(_world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(id, count, 0));
		_world.spawnEntityInWorld(t);
	}
	
	public void spawnStructure(String type, Vector3f pos) throws ScriptErrorException {
		final Chunk chk = _world.getChunkFromBlockCoords((int) pos.getX(), (int) pos.getZ());
		BiomeGenBase biome = _world.getBiomeGenForCoords((int) pos.getX(), (int) pos.getZ());
		if (type.toLowerCase() == "village") {
			if (MapGenVillage.villageSpawnBiomes instanceof AbstractList) {
				MapGenVillage.villageSpawnBiomes = new ArrayList(MapGenVillage.villageSpawnBiomes);
			}
			boolean added = false;
			if (!MapGenVillage.villageSpawnBiomes.contains(biome)) {
				MapGenVillage.villageSpawnBiomes.add(biome);
				added = true;
			}
			MapGenVillage vilGen = new MapGenVillage() {
				@Override
				protected boolean canSpawnStructureAtCoords(int par1, int par2) {
					return par1 == chk.xPosition && par2 == chk.zPosition;
				}
			};
			vilGen.generate(_world.getChunkProvider(), _world, chk.xPosition, chk.zPosition, new byte[] {});
	    	for (int var11 = chk.xPosition - 8; var11 <= chk.xPosition + 8; ++var11)
	    	{
	    		for (int var12 = chk.zPosition - 8; var12 <= chk.zPosition + 8; ++var12)
	    		{
	    			vilGen.generateStructuresInChunk(_world, _world.rand, var11, var12);
	    		}
	    	}
	    	if (added) {
	    		MapGenVillage.villageSpawnBiomes.remove(biome);
			}
		} else if (type.toLowerCase() == "stronghold") {
			if (MapGenStronghold.allowedBiomes instanceof AbstractList) {
				MapGenStronghold.allowedBiomes = new ArrayList(MapGenStronghold.allowedBiomes);
			}
			boolean added = false;
			if (!MapGenStronghold.allowedBiomes.contains(biome)) {
				MapGenStronghold.allowedBiomes.add(biome);
				added = true;
			}
			MapGenStronghold strGen = new MapGenStronghold() {
				@Override
				protected boolean canSpawnStructureAtCoords(int par1, int par2) {
					return par1 == chk.xPosition && par2 == chk.zPosition;
				}
				
				@Override
				protected List getCoordList() {
					return new ArrayList();
				}
			};
			strGen.generate(_world.getChunkProvider(), _world, chk.xPosition, chk.zPosition, new byte[] {});
	    	for (int var11 = chk.xPosition - 8; var11 <= chk.xPosition + 8; ++var11)
	    	{
	    		for (int var12 = chk.zPosition - 8; var12 <= chk.zPosition + 8; ++var12)
	    		{
	    			strGen.generateStructuresInChunk(_world, _world.rand, var11, var12);
	    		}
	    	}
	    	if (added) {
	    		MapGenStronghold.allowedBiomes.remove(biome);
			}
		} else {
			// no mineshafts sorry, it has the bad tendancy to crash the client
			throw new ScriptErrorException("type needs to be village, stronghold or mineshaft");
		}
	}

}
