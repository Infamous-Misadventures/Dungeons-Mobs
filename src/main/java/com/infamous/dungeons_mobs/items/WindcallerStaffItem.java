package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class WindcallerStaffItem extends AbstractStaffItem implements IHasInventorySprite {
    public WindcallerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, Entity target, ItemStack itemStack, Hand hand) {
//        World world = playerIn.getCommandSenderWorld();
//        TornadoEntity tornadoEntity = new TornadoEntity(world, playerIn, target);
//        tornadoEntity.addEffect(new EffectInstance(Effects.LEVITATION, 100,1));
//        tornadoEntity.setDuration(100);
//        world.addFreshEntity(tornadoEntity);
//        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 400);
//        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, BlockPos targetPos, ItemStack itemStack, Hand hand) {
//        World world = playerIn.getCommandSenderWorld();
//        TornadoEntity tornadoEntity = new TornadoEntity(world, playerIn, targetPos.getX(), targetPos.getY(), targetPos.getZ());
//        tornadoEntity.addEffect(new EffectInstance(Effects.LEVITATION, 100,1));
//        tornadoEntity.setDuration(100);
//        world.addFreshEntity(tornadoEntity);
//        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 400);
//        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }
}
