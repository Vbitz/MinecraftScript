nmc = net.minecraft.src;
fmlCommon = Packages.cpw.mods.fml.common;

var i = new nmc.Block(3000, nmc.Material.grass);
i.setCreativeTab(nmc.CreativeTabs.tabBlock);
fmlCommon.registry.GameRegistry.registerBlock(i);
fmlCommon.registry.LanguageRegistry.addName(i, "Hello World");