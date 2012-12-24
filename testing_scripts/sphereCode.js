function makeSphere(base,x1,y1,z1,r,blk) {
	for (var x = -x1; x < x1; x++) {
		for (var y = -y1; y < y1; y++) {
			for (var z = -z1; z < z1; z++) {
				if (x*x+y*y+z*z<r*r) {
					world().setBlock(blk, base.add(x,y,z));
				}
			}
		}
	}
}