var stoneId = itemId("cobblestone");
var glassId = itemId("glass");
var airId = itemId("air");

var blk = block(1);
blk.setLight(0.8);
blk.setBlockCreativeTab("blocks");
blk.setTexture("tntSide");
blk.onRightClick(function (world, x, y, z) {
	var x3 = 0;
	var x2 = 0;
	var x1 = 0;

	for (x1 = x - 5; x1 < x + 4; x1++) {
		for (x3 = y - 2; x3 < y + 3; x3++) {
			for (x2 = z - 5; x2 < z + 4; x2++) {
				world.setBlock(stoneId, vector(x1, x3, x2));
			}
		}
	}

	// fill with air
	for (x1 = x - 4; x1 < x + 3; x1++) {
		for (x3 = y - 1; x3 < y + 2; x3++) {
			for (x2 = z - 4; x2 < z + 3; x2++) {
				world.setBlock(airId, vector(x1, x3, x2));
			}
		}
	}

	// make roof
	for (x1 = x - 4; x1 < x + 3; x1++) {
		for (x2 = z - 4; x2 < z + 3; x2++) {
			if (x2 != z && x1 != x) {
				world.setBlock(glassId, vector(x1, y + 2, x2));
			}
		}
	}
});