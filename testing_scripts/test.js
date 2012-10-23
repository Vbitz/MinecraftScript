var grassId = api.getItemId("grassBlock");

var blk = api.getScriptedBlock(1);
blk.setBlockBrightness(0.8);
blk.setBlockCreativeTab("blocks");
blk.setBlockTexture("tntSide");
blk.setRightClickFunction(function (world, x, y, z) {
	api.log(grassId);
	for (var x1 = x - 4; x1 < x + 4; x1++) {
		for (var x2 = z - 4; x2 < z + 4; x2++) {
			world.setBlock(grassId, x1, y - 1, x2);
		}
	}
});