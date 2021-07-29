package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.summonables.TornadoEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class WindcallerStaffItem extends AbstractStaffItem{
    public WindcallerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, LivingEntity target, ItemStack itemStack, Hand hand) {
        World world = playerIn.getCommandSenderWorld();
        TornadoEntity tornadoEntity = new TornadoEntity(world, playerIn, target);
        tornadoEntity.addEffect(new EffectInstance(Effects.LEVITATION, 100,1));
        tornadoEntity.setDuration(100);
        world.addFreshEntity(tornadoEntity);
        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 400);
        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }
}
