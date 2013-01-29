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

Future - Roughly in order of when I will work on them
=====================================================
- Have Script Editor auto extract assets
- Look into Jenkins reading from MinecraftScriptMod to get version detail
- Reenable SCP to vbitz.com/minecraftscript
- Write Permissions System
- Improve require to make it node.js compatable
- Improve In-Game Help
- Hook In-Game Help into the Key-Value Store (turning it into a bit of a wiki)
- Add Script Nodes
- Add Survival Mode Scripting with Script Nodes
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