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
	
	@Override
	public String toString() {
		return "Vector3f X: " + _X + " Y: " + _Y + " Z: " + _Z;
	}
	
}
