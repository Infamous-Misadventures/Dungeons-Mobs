package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.projectiles.AbstractOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.LaserOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

import net.minecraft.item.Item.Properties;

public class NecromancerStaffItem extends AbstractStaffItem{
    public NecromancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, LivingEntity target, ItemStack itemStack, Hand hand) {
        double squareDistanceToTarget = playerIn.distanceToSqr(target);
        double xDifference = target.getX() - playerIn.getX();
        double yDifference = target.getY(0.5D) - playerIn.getY(0.5D);
        double zDifference = target.getZ() - playerIn.getZ();
        float f = MathHelper.sqrt(MathHelper.sqrt(squareDistanceToTarget)) * 0.5F;

        AbstractOrbEntity orb = createOrb(playerIn, xDifference, yDifference, zDifference);
        orb.setPos(orb.getX(), playerIn.getY(0.5D) + 0.5D, orb.getZ());
        playerIn.level.addFreshEntity(orb);

        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 20);
        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }

    protected AbstractOrbEntity createOrb(PlayerEntity playerIn, double xDifference, double yDifference, double zDifference) {
        return new LaserOrbEntity(playerIn.level, playerIn, xDifference, yDifference, zDifference);
    }
}
