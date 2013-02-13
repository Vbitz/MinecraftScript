Globals
=======
Stored in com.vbitz.MinecraftScript.scripting.ScriptingGlobals
Most methods in the file have slightly different names with JS appended to the end

Vector3f 								v(double x, double y, double z)
	Returns a new Vector with (x, y, z)

MinecraftScriptPlayerAPI				me()
	Returns the current player

MinecraftScriptWorldAPI					world()
	Returns the current world

MinecraftScriptScriptedBlockAPI			block(int id)
	Returns a Scripted Block with ID

MinecraftScriptScriptedItemAPI			item(int id)
	Returns a Scripted Item with ID

void									log(Object obj)
	Prints obj to Console

String									logFile(String data)
	Dumps str to a file, size limited to 8MB. the name of the file is returned

void									logAll()
	Dumps the rest of the chat in this script to a logfile
	The filename will be printed to chat once this file is writen

void									chat(String chat)
	Sends chat to the current player as a chat message

int 									itemId(String name)						
	Returns the ID of the item

MinecraftScriptPlayerAPI				player(String nickname)					
	Returns a MinecraftScriptPlayerAPI for nickname

void									registerCommand(String name, Function command)
	Running /js c cmd args will run cmd with args

void 									difficulty(String diff)
	Sets the Difficulty to difficulty, difficulty can be peaceful, easy, normal or hard

void									addSmeltingRecipe(int(item id) input, int(item id) output, int xp)
	Adds a Smelting Recipe

boolean									registerTick(String id, Function func)
	Runs a function on the next server tick
	If it won't run on the next tick this function will return false

void									deregisterTick(String id)
	Stops id from running on the next server tick

void									genFunc(Function func)
	This function will run each time a new chunk generates

void									reload()
	Reloads the scripting scope

void									require(String path)
	Loads script path

String									src(Function func)
	Decompiles func, this will only work for javascript functions

String									help(String term)
	Returns JSDoc help for term

void									on(string eventName, function func)
	On eventName being called func will be called

MinecraftScriptPlayerAPI
========================
Stored in com.vbitz.MinecraftScript.MinecraftScriptPlayerAPI
Methods named get[Name] and set[Name] should be accessed like variables
Such as me().health += 10;

int										getHealth()

void									setHealth(int health)

void									kill()

int 									getHunger()

void 									setHunger(int value)

void									give(int id, int count)

Vector3f								getPos()

void									setPos(Vector3f v)

void									tp(Vector3f v)

void									addEffect(String effectName, int level, int time)

void									cureAll()

void									fly(boolean setFly)

void									setFire(boolean onFire)

boolean									getFire()

Vector3f								getLook()

MinecraftScriptItemStackAPI				getItem()

float									getLookDirection()

void									gamemode(String gamemode)

void									gamemode(int gamemode)

void									runCommand(String command)

void									addExp(int amo)

void									addExpLevels(int amo)

String									getName()

MinecraftScriptWorldAPI					getWorld()

void									shootArrow(float damage, int knockback)

void									shootArrow(float damage, int knockback, Object func)

MinecraftScriptWorldAPI
=======================
Stored in com.vbitz.MinecraftScript.MinecraftScriptWorldAPI
Methods named get[Name] and set[Name] should be accessed like variables
Such as world().time += 10;

void 									explode(int amo, Vector3f loc)

int										getBlock(Vector3f loc)

void 									dropBlock(Vector3f loc)

void 									setBlock(int blockType, Vector3f loc)

void 									setBlock(int blockType, int metadata, Vector3f loc)

void 									setBlock(int blockType, Vector3f loc, boolean update)

void 
	setBlock(int blockType, int metadata, Vector3f loc, boolean update)

boolean
	setCube(int blockType, Vector3f v1, Vector3f v2, int limit)

boolean 
	setCube(int blockType, Vector3f v1, Vector3f v2, int limit, boolean hollow)

boolean 
	replaceCube(int srcType, int targetType, Vector3f v1, Vector3f v2, int limit)

boolean
	replaceCube(int srcType, int targetType, Vector3f v1, Vector3f v2, int limit, boolean hollow)

void									setTime(long value)

long									getTime()

void									setBiome(int biome, Vector3f loc)

String									getBiome(Vector3f pos)

void									killDrops(Vector3f pos)

void									killDrops(Vector3f pos, boolean killExp)

void									killAll()

void									killAll(Vector3f pos)

boolean									getRain()

void 									setRain(boolean rain)

boolean									spawnTree(Vector3f pos)

boolean									spawnBigTree(Vector3f pos)

boolean 
	spawnBigTree(Vector3f pos, double heightLimit, double scaleWidth, double leafDensity)

void									spawn(Vector3f pos, String name)

void									spawnTnt(Vector3f pos, int fuse, int size)

boolean									grow(Vector3f pos)

void									activate(Vector3f pos)

void									dropItem(int id, int count, Vector3f pos)

void									spawnStructure(String type, Vector3f pos)

void
	spawnCaves(Vector3f pos, int chunkWidth, boolean addRavines, boolean newGen)

void 									spawnVein(Vector3f pos, int blockId, int amount)

void 									addItemToChest(Vector3f pos, int itemId, int itemCount)

void
	spawnFirework(Vector3f pos, int color, int type, int flightTime, int explodeCount)

void 									spawnFirecode(Vector3f pos, int flightTime, Function func)

double 									getTickRate()

MinecraftScriptUnsafeAPI
========================
Stored in com.vbitz.MinecraftScript.MinecraftScriptUnsafeAPI
Avalible in game as $ if Unsafe Mode is enabled

void									dir(Object obj)

void									dir(Object obj, String search)

void									dir(Object obj, String search, boolean onlySelf)

String									dirStr(Object obj)

void									fmlMods()

Object									fmlMod(String modID)

Object 									getTileEntity(Vector3f pos)

Object									getBlock(Vector3f pos)

ItemStack 								getItemStack(EntityPlayer ply, int pos)

EntityPlayer 							getMe()

World 									getWorld()

ItemStack 								getItem(int id, int metadata, int count)

Vector3f
========
Stored in com.vbitz.MinecraftScript.Vector3f

float									getX()

float									getY()

float									getZ()

Vector3f								add(float x, float y, float z)

Vector3f								expand(int look, float amount)

string									toString()

MinecraftScriptScriptedBlockAPI
===============================
Stored in com.vbitz.MinecraftScript.MinecraftScriptScriptedBlockAPI

void									setHardness(float hard)

boolean									setBlockCreativeTab(String tab)

void									setLight(float bright)

boolean									setTexture(String tex)

boolean									setTexture(String tex, int side)

void									onRightClick(Function func)

void									onBlockUpdate(Function func)

void									give(int count)

void									give(MinecraftScriptPlayerAPI ply, int count)

MinecraftScriptScriptedItemAPI
==============================
Stored in com.vbitz.MinecraftScript.MinecraftScriptScriptedItemAPI

void									give(int count)

void									give(MinecraftScriptPlayerAPI ply, int count)

void									set3D()

void									onRightClick(Function func)

void									rightClickConsumes(boolean set)

void									rightClickThrows(boolean set)

MinecraftScriptItemStackAPI
===========================
Stored in com.vbitz.MinecraftScript.MinecraftScriptItemStackAPI

int 									getCount()

void 									addCount(int count)

void 									setCount(int count)

int 									getDamage()

void 									setDamage(int dmg)

void									enchant(String enchantName, int enchantLevel)

MinecraftScriptNBTagAPI					tag()

MinecraftScriptNBTagAPI
=======================
Stored in com.vbitz.MinecraftScript.MinecraftScriptNBTagAPI

String 									getString(String key)

void 									set(String key, String value)

int 									getInt(String key)

void									set(String key, int value)

float									getFloat(String key)

void									set(String key, float value)

double									getDouble(String key)

void									set(String key, double value)

MinecraftScriptNBTagAPI					getCompound(String key)

NativeArray								tags()
