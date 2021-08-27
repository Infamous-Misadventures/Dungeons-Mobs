package com.infamous.dungeons_mobs.utils;

import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class ModProjectileHelper {

    private static final double RAYTRACE_DISTANCE = 16.0D;

    public static Hand getHandWith(LivingEntity livingEntity, Predicate<Item> itemPredicate){

        return itemPredicate.test(livingEntity.getMainHandItem().getItem()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    public static ItemStack createRocket(DyeColor dyeColor) {
       ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
       ItemStack star = new ItemStack(Items.FIREWORK_STAR);
       CompoundNBT starExplosionNBT = star.getOrCreateTagElement("Explosion");
       starExplosionNBT.putInt("Type", FireworkRocketItem.Shape.BURST.getId());
       CompoundNBT rocketFireworksNBT = rocket.getOrCreateTagElement("Fireworks");
       ListNBT rocketExplosionsNBT = new ListNBT();
       CompoundNBT actualStarExplosionNBT = star.getTagElement("Explosion");
       if (actualStarExplosionNBT != null) {
          // making firework pink
          List<Integer> colorList = Lists.newArrayList();
          int pinkFireworkColor = dyeColor.getFireworkColor();
          colorList.add(pinkFireworkColor);
          actualStarExplosionNBT.putIntArray("Colors", colorList);
          actualStarExplosionNBT.putIntArray("FadeColors", colorList);
          // adding actualStarExplosionNBT to rocketExplosionsNBT
          rocketExplosionsNBT.add(actualStarExplosionNBT);
       }
       if (!rocketExplosionsNBT.isEmpty()) {
          rocketFireworksNBT.put("Explosions", rocketExplosionsNBT);
       }
       return rocket;
    }

    public static RayTraceResult getLaserRayTrace(LivingEntity shooter){
        World world = shooter.level;
        BlockRayTraceResult blockRTR = (BlockRayTraceResult) shooter.pick(RAYTRACE_DISTANCE, 1.0F, false);
        Vector3d startVec = shooter.getEyePosition(1.0F);
        Vector3d lookVec = shooter.getViewVector(1.0F);
        Vector3d endVec = startVec.add(lookVec.x * RAYTRACE_DISTANCE, lookVec.y * RAYTRACE_DISTANCE, lookVec.z * RAYTRACE_DISTANCE);
        if (blockRTR.getType() != RayTraceResult.Type.MISS)
            endVec = blockRTR.getLocation();

        AxisAlignedBB targetAreaBoundingBox = shooter.getBoundingBox().expandTowards(lookVec.scale(RAYTRACE_DISTANCE)).inflate(1.0D);
        EntityRayTraceResult entityRTR = ProjectileHelper.getEntityHitResult(world, shooter, startVec, endVec, targetAreaBoundingBox, entity -> !entity.isSpectator() && entity.isPickable());

        if (entityRTR != null) {
            return entityRTR;
        } else{
            return blockRTR;
        }
    }
}
