package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PositionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.item.Item.Properties;

public abstract class AbstractNecromancerStaffItem extends AbstractStaffItem{

    public AbstractNecromancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void activateStaff(Player playerIn, Entity target, ItemStack itemStack, InteractionHand hand) {
        shoot(playerIn, itemStack, hand, target.getX(), target.getY(0.6D), target.getZ());
    }

    @Override
    protected void activateStaff(Player playerIn, BlockPos targetPos, ItemStack itemStack, InteractionHand hand) {
        shoot(playerIn, itemStack, hand, targetPos.getX(), targetPos.getY(), targetPos.getZ());
    }

    private void shoot(Player playerIn, ItemStack itemStack, InteractionHand hand, double targetX, double targetY, double targetZ) {
        Vec3 pos = PositionUtils.getOffsetPos(playerIn, 0.3, 1.5, 0.5, playerIn.yBodyRot);
        double d1 = targetX - pos.x;
        double d2 = targetY - pos.y;
        double d3 = targetZ - pos.z;
        Projectile projectile = createOrb(playerIn, d1, d2, d3);
        projectile.moveTo(pos.x, pos.y, pos.z);
        playerIn.level.addFreshEntity(projectile);
        playerIn.playSound(ModSoundEvents.NECROMANCER_SHOOT.get(), 1.0F, 1.0F);
        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 20);
        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }

    protected abstract Projectile createOrb(Player playerIn, double xDifference, double yDifference, double zDifference);
}
