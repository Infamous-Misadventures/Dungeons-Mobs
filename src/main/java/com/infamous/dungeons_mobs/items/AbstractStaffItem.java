package com.infamous.dungeons_mobs.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public abstract class AbstractStaffItem extends Item {

    private static final double RAYTRACE_DISTANCE = 256;

    public AbstractStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockRayTraceResult blockRTR = (BlockRayTraceResult) player.pick(RAYTRACE_DISTANCE, 1.0f, false);
        Vector3d eyeVector = player.getEyePosition(1.0f);
        Vector3d lookVector = player.getViewVector(1.0F);
        Vector3d rayTraceVector = eyeVector.add(lookVector.x * RAYTRACE_DISTANCE, lookVector.y * RAYTRACE_DISTANCE, lookVector.z * RAYTRACE_DISTANCE);
        AxisAlignedBB rayTraceBoundingBox = player.getBoundingBox().expandTowards(lookVector.scale(RAYTRACE_DISTANCE)).inflate(1.0D, 1.0D, 1.0D);
        EntityRayTraceResult entityRTR = ProjectileHelper.getEntityHitResult(world, player, eyeVector, rayTraceVector, rayTraceBoundingBox, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable());
        world.playSound(player, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.0f, 1.0f);
        if (!world.isClientSide()) {
            if (entityRTR != null) {
                Entity target = entityRTR.getEntity();
                this.activateStaff(player, target, itemStack, hand);
            } else {
                BlockPos pos = blockRTR.getBlockPos();
                this.activateStaff(player, pos, itemStack, hand);
            }
        }
        return ActionResult.success(player.getItemInHand(hand));
    }

    protected abstract void activateStaff(PlayerEntity playerIn, Entity target, ItemStack itemStack, Hand hand);

    protected abstract void activateStaff(PlayerEntity playerIn, BlockPos targetPos, ItemStack itemStack, Hand hand);

    public Rarity getRarity(ItemStack itemStack){
        return Rarity.RARE;
    }
}
