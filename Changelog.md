Current Changelog
=================

Version 1.0.0
- Bumped the Version to 1.0.0
- ScriptRunners now implamented
- Overhalled Scripting to use JSScriptingManager

Version 1.0.1
- Fixed /js dofile throwing a error when you don't specify a filename

Version 1.1.0
- Added JSDoc tag, it will in the near future be readable in game but for now it will be used to document each function
- Added JSDoc tags for ScriptingGlobals
- underscore.js works in the enviroment

Version 1.2.0
- Add Command Block Script Runner
- Moved JSScriptingCommand to com.vbitz.MinecraftScript.scripting.javascript
- Refactored ScriptingCommand to use ScriptRunner
- Added Config Options for Blocks and Items
- Added key value store as /kvs get/set
- Reenable SCP to vbitz.com/minecraftscript

Version 1.2.1
- Added world().getTime
- Moved Block IDs to conflict less with FTB
- Refactored world
- Removed Override to world().explode
- Refactored player

Version 1.3.0
- Fixed item and block
- Colated Help Docs
- Moved all minecraftScript files to a seprate directory
- Added setScriptRunner to ScriptingManager
- Added logFile(content) which outputs string to a file in config/minecraftScript/logs

Version 1.4.0
- Hook In-Game Help into the Key-Value Store (turning it into a bit of a wiki)
- Improved tab completion on /wiki
- Fixed JSScriptingManager not setting runners correctly in all cases
- Added logAll() which dumps the rest of the chat in the current command or script to a file
- Added Hook System

Version 1.5.0
- Hooked into some MinecraftForge Event to Test
- Added on(string hookName, function hookFunc)
- Fixed JSFunction
- Fix Resource loading with script editor
- RunScriptEndpoint now uses ScriptRunnerWeb

Version 1.5.1
- Added chatMessage hook, it even gives you a player object

Version 1.5.2
- Added player.world()
- Changed world().downfall() to use a get and set, you can now just use world().rain as a boolean

Future - Roughly in order of when I will work on them
=====================================================
- API Keys should be loaded from a file
- Write Permissions System
- Hook in most forge events
- Write NBT Block Duplicate Function
- Improve require to make it node.js compatable
- Improve In-Game Help
- Add Script Nodes - The block will be cheapish (maybe some gold nuggets surounding a diamond on some wood but the script running tool might require a nether star or something else really expensive)
- Add Survival Mode Scripting with Script Nodes
- Add Beacon Function to Script Node
- Add Force Load Chunks with (callback - not needed?)
- Add Container API
- Add Crafting Function
- Add Redstone API
- Add Scripts for just one world
- Hide Blocks and Items from NEI
- Document rest of enchants
- Add rest of creative tabs
- Finish Texture ID's
- Start thinking about scripted custom renderers ( Maybe )
- Write Tutorial
- Add Map Graphics API
- Write GUI API ( Not sure if I want to run code on the client )
- Improve World Gen
- Add Achivements API