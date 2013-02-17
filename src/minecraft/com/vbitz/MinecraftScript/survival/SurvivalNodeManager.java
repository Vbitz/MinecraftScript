package com.vbitz.MinecraftScript.survival;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.Vector3f;
import com.vbitz.MinecraftScript.titleEntitys.TileEntitySurvivalNode;

public class SurvivalNodeManager {

	public static final float distinceLimit = 64;
	
	public static TileEntitySurvivalNode getNearNode(World world, Vector3f pos) {
		float closestDistince = distinceLimit;
		TileEntitySurvivalNode ret = null;
		for (Object entity : world.loadedTileEntityList) {
			if (!(entity instanceof TileEntitySurvivalNode)) {
				continue;
			}
			TileEntitySurvivalNode survivalNode = (TileEntitySurvivalNode) entity;
			float dist = pos.distince(survivalNode.getPos());
			if (dist < closestDistince) {
				ret = survivalNode;
				closestDistince = dist;
			}
		}
		return ret;
	}
	
	public static TileEntitySurvivalNode getNodeByName(World world, String name) {
		for (Object entity : world.loadedTileEntityList) {
			if (!(entity instanceof TileEntitySurvivalNode)) {
				continue;
			}
			TileEntitySurvivalNode survivalNode = (TileEntitySurvivalNode) entity;
			if (survivalNode.getName().equals(name)) {
				return survivalNode;
			}
		}
		return null;
	}

	public static float getDistinceToClosestNode(World world, Vector3f pos) {
		float closestDistince = distinceLimit;
		for (Object entity : world.loadedTileEntityList) {
			if (!(entity instanceof TileEntitySurvivalNode)) {
				continue;
			}
			TileEntitySurvivalNode survivalNode = (TileEntitySurvivalNode) entity;
			float dist = pos.distince(survivalNode.getPos());
			if (dist < closestDistince) {
				closestDistince = dist;
			}
		}
		return closestDistince;
	}
	
}
