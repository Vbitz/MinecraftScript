// commented out right now because this causes alot of lag
genFunc(function(pos, world) {
	world.replaceCube(0, 1, pos.add(4, 4, 4), pos.add(12, Math.floor(Math.random() * 20) + 10, 12), 4000, true);
});