MinecraftScript
===============

__Right now this mod is for advanced users only.__

This is my attempt of making a mod which enables JavaScript in Minecraft.

Security Warning
----------------
Due to the nature of this mod access to Java classes is possible so therefore any scripts made using this mod should be treated like a mod themselves.
This basically just means you should be careful with what commands and scripts you run with this mod installed.

To-do
-----
- Add In-Game help system
- Document Everything
- Add Scripted Item support
- Add rest of block identifiers
- Add item identifiers
- Finish Block and Item Texture ID's
- Add rest of creative tabs
- Hide blocks and items not in use from Not Enough Items
- Add more Scripted Block callbacks
- Add Map Graphics API

Thanks
------
__Any suggestions that are implemented and committed to this repository will be met with a mention in the commit and a agreed user handle will be listed here__


### Compiling and packaging MinecraftScript
1. Ensure that `Apache Ant` (found [here](http://ant.apache.org/)) is installed correctly on your system.
 * Linux users will need the latest version of astyle installed as well.
1. Create a base directory for the build
1. Clone the Buildcraft repository into `basedir/MinecraftScript/`
1. Copy the minecraft bin dir/Resources and minecraft_server.jar into `basedir/jars/`
1. Navigate to basedir/MinecraftScript in a shell and run `ant` (this will take 2-5 minutes)

Your directory structure should look like this:
***

    basedir
    \- jars
     |- minecraft_server.jar
     \- bin
      |- minecraft.jar
      |- ...
    \- MinecraftScript
     |- src
     |- testing_scripts
     |- ...
***