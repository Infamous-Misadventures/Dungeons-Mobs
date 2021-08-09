package com.infamous.dungeons_mobs.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public interface ITrapsTarget {
    default boolean isTargetTrapped(LivingEntity target){
        Vector3d stuckSpeedMultiplier = ObfuscationReflectionHelper.getPrivateValue(Entity.class, target, "field_213328_B");
        if (stuckSpeedMultiplier != null && !stuckSpeedMultiplier.equals(Vector3d.ZERO)) {
            return stuckSpeedMultiplier.x <= 0.25D && stuckSpeedMultiplier.y <= 0.05D && stuckSpeedMultiplier.z <= 0.25D;
        }
        else return false;
    }
}
