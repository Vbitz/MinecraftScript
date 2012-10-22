var blk = api.getScriptedBlock(1);
blk.setBlockBrightness(0.8);
blk.setBlockCreativeTab("blocks");
blk.setBlockTexture("tntTop");
blk.setRightClickFunction(function (x, y, z) {
	java.lang.System.out.println(x + " : " + y + " : " + z);
});