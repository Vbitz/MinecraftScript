package com.vbitz.MinecraftScript;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

import com.vbitz.MinecraftScript.exceptions.ScriptErrorException;

import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
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
	
	public void explode(int amo, Vector3f loc) throws ScriptErrorException {
		if (amo > 200) {
			throw new ScriptErrorException("Bad Idea");
		}
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
	
	public void genCaves(Vector3f pos, int chunkWidth, boolean addRavines, boolean newGen) {
		final Random rand_new = new Random(_world.rand.nextLong());
		MapGenCaves caves = new MapGenCaves();
		MapGenRavine ravines = new MapGenRavine();
		if (newGen) {
			caves = new MapGenCaves() {
				@Override
				public void generate(IChunkProvider par1iChunkProvider,
						World par2World, int par3, int par4,
						byte[] par5ArrayOfByte) {
					this.rand.setSeed(rand_new.nextLong());
					super.generate(par1iChunkProvider, par2World, par3, par4, par5ArrayOfByte);
				}
			};
			ravines = new MapGenRavine() {
				@Override
				public void generate(IChunkProvider par1iChunkProvider,
						World par2World, int par3, int par4,
						byte[] par5ArrayOfByte) {
					this.rand.setSeed(rand_new.nextLong());
					super.generate(par1iChunkProvider, par2World, par3, par4, par5ArrayOfByte);
				}
			};
		}
		Chunk baseChunk = _world.getChunkFromBlockCoords((int) pos.getX(), (int) pos.getZ());
		for (int x = baseChunk.xPosition - chunkWidth; x < baseChunk.xPosition + chunkWidth; x++) { 
			for (int z = baseChunk.zPosition - chunkWidth; z < baseChunk.zPosition + chunkWidth; z++) { 
				byte[] bteArray = new byte[32768]; // this might cause problems
				Arrays.fill(bteArray, (byte) 1);
				caves.generate(_world.getChunkProvider(), _world, x, z, bteArray);
				if (addRavines) {
					ravines.generate(_world.getChunkProvider(), _world, x, z, bteArray);
				}
				
				Chunk chk = _world.getChunkFromChunkCoords(x, z);
				
				// for compatability's sake this is based off built in minecraft code after being modifyed
		        int var5 = bteArray.length / 256;

		        for (int var6 = 0; var6 < 16; ++var6)
		        {
		            for (int var7 = 0; var7 < 16; ++var7)
		            {
		                for (int var8 = 0; var8 < var5; ++var8)
		                {
		                    /* FORGE: The following change, a cast from unsigned byte to int,
		                     * fixes a vanilla bug when generating new chunks that contain a block ID > 127 */
		                    int var9 = bteArray[var6 << 11 | var7 << 7 | var8] & 0xFF;
		                    if (var9 != (byte) 1) {
		                    	_world.setBlockWithNotify(var6 + (x*16), var8, var7 + (z*16), 0); // no lava sorry, covering super flat in lava is not nice
		                    }
		                }
		            }
		        }
			}
		}
	}
	
	public void genVeins(Vector3f pos, int blockId, int amount) {
		WorldGenMinable m = new WorldGenMinable(blockId, amount);
		m.generate(_world, _world.rand, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
	}
	
	public void addItemToChest(Vector3f pos, int itemId, int itemCount) throws ScriptErrorException {
		if (_world.getBlockTileEntity((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()) != null &&
				_world.getBlockTileEntity((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()) instanceof TileEntityChest) {
			TileEntityChest chest = (TileEntityChest) _world.getBlockTileEntity((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
			for (int i = 0; i < chest.getSizeInventory(); i++) {
				if (chest.getStackInSlot(i) == null) {
					chest.setInventorySlotContents(i, new ItemStack(itemId, itemCount, 0)); // combining stacks will come later, too many edge cases right now
					break;
				}
			}
		} else {
			throw new ScriptErrorException("Block is not a chest");
		}
	}
	
	// happy new years
	public void firework(Vector3f pos, int color, int type, int flightTime, int explodeCount) {
		Random rand = new Random(_world.rand.nextLong());
		ItemStack stk = new ItemStack(Item.field_92052_bU); // mcp really needs to update this
		NBTTagCompound baseComp = new NBTTagCompound();
		baseComp.setCompoundTag("Fireworks", new NBTTagCompound("Fireworks"));
		baseComp.getCompoundTag("Fireworks").setByte("Flight", (byte) flightTime);
		NBTTagList lst = baseComp.getCompoundTag("Fireworks").getTagList("Explosions");
		for (int i = 0; i < explodeCount; i++) {
			NBTTagCompound explosion = new NBTTagCompound();
			explosion.setByte("Type", (byte) type);
			explosion.setBoolean("Trail", true);
			explosion.setBoolean("Flicker", rand.nextBoolean());
			explosion.setIntArray("Colors", new int[] { color + (rand.nextInt(400) - 200) });
			explosion.setIntArray("FadeColors", new int[] { color + (rand.nextInt(400) - 200)  });
			lst.appendTag(explosion);
		}
		baseComp.getCompoundTag("Fireworks").setTag("Explosions", lst);
		stk.setTagCompound(baseComp);
		EntityFireworkRocket fireWork = new EntityFireworkRocket(_world, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), stk);
		_world.spawnEntityInWorld(fireWork);
	}
	
	public void firecode(Vector3f pos, int flightTime, Function func) {
		final Function explodeFunc = func;
		final EntityPlayer owner = _player;
		ItemStack stk = new ItemStack(Item.field_92052_bU);
		NBTTagCompound baseComp = new NBTTagCompound();
		baseComp.setCompoundTag("Fireworks", new NBTTagCompound("Fireworks"));
		baseComp.getCompoundTag("Fireworks").setByte("Flight", (byte) flightTime);
		stk.setTagCompound(baseComp);
		EntityFireworkRocket fireWork = new EntityFireworkRocket(_world, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), stk) {
			@Override
			public void onUpdate() {
				super.onUpdate();
				if (this.isDead) {
					if (explodeFunc != null && !worldObj.isRemote) {
						ScriptingManager.enterContext();
						try {
							if (owner instanceof EntityPlayer) {
								ScriptingManager.runFunction(explodeFunc,
										new MinecraftScriptWorldAPI(owner.worldObj, (EntityPlayer) owner),
										new Vector3f(this.posX, this.posY, this.posZ));
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
		};
		_world.spawnEntityInWorld(fireWork);
	}

}
