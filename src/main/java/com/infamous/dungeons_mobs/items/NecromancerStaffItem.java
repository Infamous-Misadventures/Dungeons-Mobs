package com.infamous.dungeons_mobs.items;

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

        WraithFireballEntity wraithFireballEntity = new WraithFireballEntity(playerIn.level, playerIn, xDifference, yDifference, zDifference);
        wraithFireballEntity.setPos(wraithFireballEntity.getX(), playerIn.getY(0.5D) + 0.5D, wraithFireballEntity.getZ());
        playerIn.level.addFreshEntity(wraithFireballEntity);

        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 20);
        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }
}
