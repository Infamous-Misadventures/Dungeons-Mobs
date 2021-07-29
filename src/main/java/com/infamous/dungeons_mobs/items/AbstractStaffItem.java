package com.infamous.dungeons_mobs.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public abstract class AbstractStaffItem extends Item {

    private static final double RAYTRACE_DISTANCE = 16;

    public AbstractStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        RayTraceResult result = player.pick(RAYTRACE_DISTANCE, 1.0f, false);
        Vector3d eyeVector = player.getEyePosition(1.0f);
        Vector3d lookVector = player.getViewVector(1.0F);
        Vector3d rayTraceVector = eyeVector.add(lookVector.x * RAYTRACE_DISTANCE, lookVector.y * RAYTRACE_DISTANCE, lookVector.z * RAYTRACE_DISTANCE);
        AxisAlignedBB rayTraceBoundingBox = player.getBoundingBox().expandTowards(lookVector.scale(RAYTRACE_DISTANCE)).inflate(1.0D, 1.0D, 1.0D);
        EntityRayTraceResult entityRayTraceResult = ProjectileHelper.getEntityHitResult(world, player, eyeVector, rayTraceVector, rayTraceBoundingBox, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable());
        if (entityRayTraceResult != null
                && entityRayTraceResult.getEntity() instanceof LivingEntity
                && result.getLocation().distanceToSqr(eyeVector) > entityRayTraceResult.getLocation().distanceToSqr(eyeVector)) {
            world.playSound(player, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.0f, 1.0f);
            if (!world.isClientSide()) {
                LivingEntity livingEntity = (LivingEntity)entityRayTraceResult.getEntity();
                this.activateStaff(player, livingEntity, itemStack, hand);
            }
            return ActionResult.success(player.getItemInHand(hand));
        }
        return ActionResult.pass(player.getItemInHand(hand));
    }

    protected abstract void activateStaff(PlayerEntity playerIn, LivingEntity target, ItemStack itemStack, Hand hand);

    public Rarity getRarity(ItemStack itemStack){
        return Rarity.RARE;
    }
}
