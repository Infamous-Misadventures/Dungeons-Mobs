package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.projectiles.AbstractOrbEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public abstract class AbstractNecromancerStaffItem extends AbstractStaffItem{

    public AbstractNecromancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, Entity target, ItemStack itemStack, Hand hand) {
        double scale = 1.0D;
        Vector3d viewVector = playerIn.getViewVector(1.0F);
        double xAccel = target.getX() - (playerIn.getX() + viewVector.x * scale);
        double yAccel = target.getY(0.5D) - (0.5D + playerIn.getY(0.5D));
        double zAccel = target.getZ() - (playerIn.getZ() + viewVector.z * scale);
        float euclidDist = MathHelper.sqrt(xAccel * xAccel + yAccel * yAccel + zAccel * zAccel);

        AbstractOrbEntity orb = createOrb(playerIn, 0, 0, 0);
        orb.setPos(playerIn.getX() + viewVector.x * scale, playerIn.getY(0.5D) + 0.5D, orb.getZ() + viewVector.z * scale);
        orb.shoot(xAccel, yAccel, zAccel, euclidDist, 0.0F);
        playerIn.level.addFreshEntity(orb);

        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 20);
        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, BlockPos targetPos, ItemStack itemStack, Hand hand) {
        double scale = 1.0D;
        Vector3d viewVector = playerIn.getViewVector(1.0F);
        double xAccel = targetPos.getX() - (playerIn.getX() + viewVector.x * scale);
        double yAccel = targetPos.getY() + 0.5D - (0.5D + playerIn.getY(0.5D));
        double zAccel = targetPos.getZ() - (playerIn.getZ() + viewVector.z * scale);
        float euclidDist = MathHelper.sqrt(xAccel * xAccel + yAccel * yAccel + zAccel * zAccel);

        AbstractOrbEntity orb = createOrb(playerIn, 0, 0, 0);
        orb.setPos(playerIn.getX() + viewVector.x * scale, playerIn.getY(0.5D) + 0.5D, orb.getZ() + viewVector.z * scale);
        orb.shoot(xAccel, yAccel, zAccel, euclidDist, 0.0F);
        playerIn.level.addFreshEntity(orb);

        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 20);
        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }

    protected abstract AbstractOrbEntity createOrb(PlayerEntity playerIn, double xDifference, double yDifference, double zDifference);
}
