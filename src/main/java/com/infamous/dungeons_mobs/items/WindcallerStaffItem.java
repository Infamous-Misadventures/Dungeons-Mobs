package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.projectiles.WindcallerBlastProjectileEntity;
import com.infamous.dungeons_mobs.entities.summonables.WindcallerTornadoEntity;
import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PositionUtils;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class WindcallerStaffItem extends AbstractStaffItem implements IHasInventorySprite {
    public WindcallerStaffItem(Properties properties) {
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

    private void shoot(PlayerEntity mob, ItemStack itemStack, Hand hand, double targetX, double targetY, double targetZ) {
        double d1 = targetX - mob.getX();
        double d2 = targetY - mob.getY(0.5D);
        double d3 = targetZ - mob.getZ();
        WindcallerBlastProjectileEntity smallfireballentity = new WindcallerBlastProjectileEntity(mob.level, mob, d1, 0, d3);
        smallfireballentity.setPos(smallfireballentity.getX(), mob.getY(0.25D), smallfireballentity.getZ());
        mob.level.addFreshEntity(smallfireballentity);
        WindcallerTornadoEntity tornado = ModEntityTypes.TORNADO.get().create(mob.level);
        tornado.moveTo(mob.blockPosition(), 0, 0);
        tornado.playSound(ModSoundEvents.WINDCALLER_BLAST_WIND.get(), 1.5F, 1.0F);
        tornado.setBlast(true);
        mob.lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(targetX, targetY, targetZ));
        tornado.yRot = -mob.yHeadRot - 90;
        ((ServerWorld)mob.level).addFreshEntityWithPassengers(tornado);
        mob.getCooldowns().addCooldown(itemStack.getItem(), 400);
        itemStack.hurtAndBreak(1, mob, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }


}
