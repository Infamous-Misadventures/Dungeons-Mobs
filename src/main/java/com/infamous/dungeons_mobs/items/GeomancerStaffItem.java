package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.utils.GeomancyHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;

import net.minecraft.item.Item.Properties;

public class GeomancerStaffItem extends AbstractStaffItem{
    public GeomancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, LivingEntity target, ItemStack itemStack, Hand hand) {
        if(playerIn.getRandom().nextFloat() < 0.25F){
            GeomancyHelper.summonOffensiveConstruct(playerIn, target, ModEntityTypes.GEOMANCER_BOMB.get());
        }
        else{
            int[] rowToRemove = Util.getRandom(GeomancyHelper.ROWS, playerIn.getRandom());
            GeomancyHelper.summonAreaDenialTrap(playerIn, target, ModEntityTypes.GEOMANCER_WALL.get(), rowToRemove);
        }
        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 400);
        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }
}
