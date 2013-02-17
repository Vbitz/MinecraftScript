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

Version 1.6.0
- Updated for Minecraft 1.4.7 - Just some MCP name updates
- Removed HTML Serve Directory

Version 1.6.1 (Stream 1)
- API Keys should be loaded from a file

Version 1.6.2 (Stream 2)
- Added world().tickRate

Version 1.6.3 (Stream 2)
- Add Scripts for just one world

Version 1.6.4 (Stream 2)
- Added arrowLoose Event
- Added player().shootArrow function

Version 1.7.0
- Added Documents.md
- Quite a few method name changes

Version 1.8.0
- Unified Tick Handlers
- Added killAll() which kills tnt and arrows and dropped items, basicly goodbye massive lag

Version 1.9.0
- Goodbye Access Checks if unsafeMode is enabled

Version 1.9.1
- Removed some debug code
- Fixed a major bug with loadAllScripts()

Version 1.9.2
- Derp

Version 2.0.0
- Yay Version 2
- Added SurvivalNode, this is the very start of coding for survival mode MinecarftScript
- Added a improvement to ScriptedItem
- You can now name nodes
- Vector3f now has a distince function

Version 2.1.0
- Survival mode is now live - woops
- You can now craft Survival Nodes, it's 4 iron and 4 stonebricks in a ring, they only have a range of 64 blocks though
- Added playNoteSound(Vector3f pos, String type, int pitch), I'm likaly going to write a midi parser in javascript that hooks into this at some point
- Moved most PlayerAPI function into SurvivalPlayerAPI, you can't get a normal Player API in survival mode
- Added nodeByName(String name) which allows you to search for a scriptnode by name
- You can now teleport to a distant node

Future - Roughly in order of when I will work on them
=====================================================
- Stop the API's from leaking World and Player Objects
- Add Block API in world working on a collection of blocks
- Expand ScriptedItem and ScriptedBlock
- Add Python/node.js based Javascript REPL
- Minecraft PI Edition API Support
- Add Ore Dict Intergration
- Write Permissions System
- Hook in most forge events
- Write NBT Block Duplicate Function
- Improve require to make it node.js compatable
- Push Documents.md into JSDoc
- Add Survival Mode Scripting with Script Nodes
- Add Beacon Function to Script Node
- Add Force Load Chunks with (callback - not needed?)
- Add Container API
- Add Crafting Function
- Add Redstone API
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