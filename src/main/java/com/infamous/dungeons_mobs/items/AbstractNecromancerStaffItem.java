package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PositionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public abstract class AbstractNecromancerStaffItem extends AbstractStaffItem{

    public AbstractNecromancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, Entity target, ItemStack itemStack, Hand hand) {
        shoot(playerIn, itemStack, hand, target.getX(), target.getY(0.6D), target.getZ());
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, BlockPos targetPos, ItemStack itemStack, Hand hand) {
        shoot(playerIn, itemStack, hand, targetPos.getX(), targetPos.getY(), targetPos.getZ());
    }

    private void shoot(PlayerEntity playerIn, ItemStack itemStack, Hand hand, double targetX, double targetY, double targetZ) {
        Vector3d pos = PositionUtils.getOffsetPos(playerIn, 0.3, 1.5, 0.5, playerIn.yBodyRot);
        double d1 = targetX - pos.x;
        double d2 = targetY - pos.y;
        double d3 = targetZ - pos.z;
        ProjectileEntity projectile = createOrb(playerIn, d1, d2, d3);
        projectile.moveTo(pos.x, pos.y, pos.z);
        playerIn.level.addFreshEntity(projectile);
        playerIn.playSound(ModSoundEvents.NECROMANCER_SHOOT.get(), 1.0F, 1.0F);
        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 20);
        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }

    protected abstract ProjectileEntity createOrb(PlayerEntity playerIn, double xDifference, double yDifference, double zDifference);
}
