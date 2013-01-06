function setBlocks(t, v1, v2, id) {
	var vector1 = v1;
	var vector2 = v2;
	var result = world().setCube(t, vector1, vector2, 50);
	if (!result) {
		registerTick(id, function () {
			setBlocks(t, vector1, vector2, id);
		});
	}
}