package com.infamous.dungeons_mobs.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class PositionUtils {

	public static Vector3d getOffsetPos(Entity entity, double offsetX, double offsetY, double offsetZ) {
        Vector3d vector3d = (new Vector3d(offsetZ, offsetY, offsetX).yRot(-entity.yRot * ((float)Math.PI / 180F) - ((float)Math.PI / 2F)));
        return entity.position().add(vector3d.x, vector3d.y, vector3d.z);
	}
	
	public static BlockPos getOffsetBlockPos(Entity entity, double offsetX, double offsetY, double offsetZ) {
        Vector3d vector3d = (new Vector3d(offsetZ, offsetY, offsetX).yRot(-entity.yRot * ((float)Math.PI / 180F) - ((float)Math.PI / 2F)));
        return entity.blockPosition().offset(vector3d.x, vector3d.y, vector3d.z);
	}
	
	public static Vector3d getOffsetMotion(Entity entity, double offsetX, double offsetY, double offsetZ) {
        Vector3d vector3d = (new Vector3d(offsetZ, offsetY, offsetX).yRot(-entity.yRot * ((float)Math.PI / 180F) - ((float)Math.PI / 2F)));
        return vector3d;
	}
	
}
