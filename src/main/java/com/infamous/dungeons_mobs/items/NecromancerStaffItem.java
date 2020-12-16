package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.projectiles.WraithFireballEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class NecromancerStaffItem extends AbstractStaffItem{
    public NecromancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, LivingEntity target, ItemStack itemStack, Hand hand) {
        double squareDistanceToTarget = playerIn.getDistanceSq(target);
        double xDifference = target.getPosX() - playerIn.getPosX();
        double yDifference = target.getPosYHeight(0.5D) - playerIn.getPosYHeight(0.5D);
        double zDifference = target.getPosZ() - playerIn.getPosZ();
        float f = MathHelper.sqrt(MathHelper.sqrt(squareDistanceToTarget)) * 0.5F;

        WraithFireballEntity wraithFireballEntity = new WraithFireballEntity(playerIn.world, playerIn, xDifference, yDifference, zDifference);
        wraithFireballEntity.setPosition(wraithFireballEntity.getPosX(), playerIn.getPosYHeight(0.5D) + 0.5D, wraithFireballEntity.getPosZ());
        playerIn.world.addEntity(wraithFireballEntity);

        playerIn.getCooldownTracker().setCooldown(itemStack.getItem(), 20);
        itemStack.damageItem(1, playerIn, playerEntity -> playerEntity.sendBreakAnimation(hand));
    }
}
