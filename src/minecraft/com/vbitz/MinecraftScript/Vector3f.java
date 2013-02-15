package com.vbitz.MinecraftScript;

public class Vector3f {

	private float _X, _Y, _Z;
	
	public Vector3f() {
		this(0f, 0f, 0f);
	}
	
	public Vector3f(double d, double e, double f) {
		this((float)d, (float)e, (float)f);
	}
	
	public Vector3f(float x, float y, float z) {
		_X = x;
		_Y = y;
		_Z = z;
	}
	
	public float getX() {
		return _X;
	}
	
	public float getY() {
		return _Y;
	}
	
	public float getZ() {
		return _Z;
	}
	
	public Vector3f add(float x, float y, float z) {
		return new Vector3f(_X + x, _Y + y, _Z + z);
	}
	
	public Vector3f expand(int look, float amount) {
		switch (look) {
		case 0:
			return this.add(amount, 0, 0);
		case 1:
			return this.add(0, 0, amount);
		case 2:
			return this.add(-amount, 0, 0);
		case 3:
			return this.add(0, 0, -amount);
		case 4:
			return this.add(0, amount, 0);
		case 5:
			return this.add(0, -amount, 0);
		default:
			return this;
		}
	}
	
	public float distince(Vector3f vec) {
		return distince(vec._X, vec._Y, vec._Z);
	}
	
	public float distince(float x, float y, float z) {
		return (float) Math.sqrt(Math.pow(x - _X, 2) + Math.pow(y - _Y, 2) + Math.pow(z - _Z, 2));
	}
	
	@Override
	public String toString() {
		return "Vector3f X: " + _X + " Y: " + _Y + " Z: " + _Z;
	}
	
}
