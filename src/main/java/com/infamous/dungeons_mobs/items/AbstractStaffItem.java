package com.infamous.dungeons_mobs.items;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractStaffItem extends Item {

    private static final double RAYTRACE_DISTANCE = 256;

    public AbstractStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult blockRTR = (BlockHitResult) player.pick(RAYTRACE_DISTANCE, 1.0f, false);
        Vec3 eyeVector = player.getEyePosition(1.0f);
        Vec3 lookVector = player.getViewVector(1.0F);
        Vec3 rayTraceVector = eyeVector.add(lookVector.x * RAYTRACE_DISTANCE, lookVector.y * RAYTRACE_DISTANCE, lookVector.z * RAYTRACE_DISTANCE);
        AABB rayTraceBoundingBox = player.getBoundingBox().expandTowards(lookVector.scale(RAYTRACE_DISTANCE)).inflate(1.0D, 1.0D, 1.0D);
        EntityHitResult entityRTR = ProjectileUtil.getEntityHitResult(world, player, eyeVector, rayTraceVector, rayTraceBoundingBox, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable());
        world.playSound(player, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0f, 1.0f);
        if (!world.isClientSide()) {
            if (entityRTR != null) {
                Entity target = entityRTR.getEntity();
                this.activateStaff(player, target, itemStack, hand);
            } else {
                BlockPos pos = blockRTR.getBlockPos();
                this.activateStaff(player, pos, itemStack, hand);
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    protected abstract void activateStaff(Player playerIn, Entity target, ItemStack itemStack, InteractionHand hand);

    protected abstract void activateStaff(Player playerIn, BlockPos targetPos, ItemStack itemStack, InteractionHand hand);

    public Rarity getRarity(ItemStack itemStack){
        return Rarity.RARE;
    }
}
