package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.utils.GeomancyHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

public class GeomancerStaffItem extends AbstractStaffItem{
    public GeomancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, Entity target, ItemStack itemStack, Hand hand) {
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

    @Override
    protected void activateStaff(PlayerEntity playerIn, BlockPos targetPos, ItemStack itemStack, Hand hand) {
        if(playerIn.getRandom().nextFloat() < 0.25F){
            GeomancyHelper.summonOffensiveConstruct(playerIn, targetPos, ModEntityTypes.GEOMANCER_BOMB.get());
        }
        else{
            int[] rowToRemove = Util.getRandom(GeomancyHelper.ROWS, playerIn.getRandom());
            GeomancyHelper.summonAreaDenialTrap(playerIn, targetPos, ModEntityTypes.GEOMANCER_WALL.get(), rowToRemove);
        }
        playerIn.getCooldowns().addCooldown(itemStack.getItem(), 400);
        itemStack.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(hand));
    }
}
