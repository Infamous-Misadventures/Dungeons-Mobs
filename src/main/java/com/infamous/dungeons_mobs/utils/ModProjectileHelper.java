package com.infamous.dungeons_mobs.utils;

import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ModProjectileHelper {

    private static final double RAYTRACE_DISTANCE = 16.0D;

    public static InteractionHand getHandWith(LivingEntity livingEntity, Predicate<Item> itemPredicate){

        return itemPredicate.test(livingEntity.getMainHandItem().getItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public static ItemStack createRocket(int explosions, DyeColor... dyeColor) {
       ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
       ItemStack star = new ItemStack(Items.FIREWORK_STAR);
       CompoundTag starExplosionNBT = star.getOrCreateTagElement("Explosion");
       starExplosionNBT.putInt("Type", FireworkRocketItem.Shape.BURST.getId());
       CompoundTag rocketFireworksNBT = rocket.getOrCreateTagElement("Fireworks");
       ListTag rocketExplosionsNBT = new ListTag();
       CompoundTag actualStarExplosionNBT = star.getTagElement("Explosion");
       if (actualStarExplosionNBT != null) {
          // making firework pink
          List<Integer> colorList = Lists.newArrayList();
          for (int i = 0; i < dyeColor.length; i++) {
        	  int pinkFireworkColor = dyeColor[i].getFireworkColor();
        	  colorList.add(pinkFireworkColor);
          }
          actualStarExplosionNBT.putIntArray("Colors", colorList);
          actualStarExplosionNBT.putIntArray("FadeColors", colorList);
          // adding actualStarExplosionNBT to rocketExplosionsNBT
          for (int i = 0; i < explosions; i++) { 
        	  rocketExplosionsNBT.add(actualStarExplosionNBT);
          }
       }
       if (!rocketExplosionsNBT.isEmpty()) {
    	rocketFireworksNBT.put("Explosions", rocketExplosionsNBT);
       }
       return rocket;
    }

    public static HitResult getLaserRayTrace(LivingEntity shooter){
        Level world = shooter.level;
        BlockHitResult blockRTR = (BlockHitResult) shooter.pick(RAYTRACE_DISTANCE, 1.0F, false);
        Vec3 startVec = shooter.getEyePosition(1.0F);
        Vec3 lookVec = shooter.getViewVector(1.0F);
        Vec3 endVec = startVec.add(lookVec.x * RAYTRACE_DISTANCE, lookVec.y * RAYTRACE_DISTANCE, lookVec.z * RAYTRACE_DISTANCE);
        if (blockRTR.getType() != HitResult.Type.MISS)
            endVec = blockRTR.getLocation();

        AABB targetAreaBoundingBox = shooter.getBoundingBox().expandTowards(lookVec.scale(RAYTRACE_DISTANCE)).inflate(1.0D);
        EntityHitResult entityRTR = ProjectileUtil.getEntityHitResult(world, shooter, startVec, endVec, targetAreaBoundingBox, entity -> !entity.isSpectator() && entity.isPickable());

        if (entityRTR != null) {
            return entityRTR;
        } else{
            return blockRTR;
        }
    }
}
