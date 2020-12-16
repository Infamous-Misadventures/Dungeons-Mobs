package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.utils.GeomancyHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;

public class GeomancerStaffItem extends AbstractStaffItem{
    public GeomancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void activateStaff(PlayerEntity playerIn, LivingEntity target, ItemStack itemStack, Hand hand) {
        if(playerIn.getRNG().nextFloat() < 0.25F){
            GeomancyHelper.summonOffensiveConstruct(playerIn, target, ModEntityTypes.GEOMANCER_BOMB.get());
        }
        else{
            int[] rowToRemove = Util.getRandomObject(GeomancyHelper.ROWS, playerIn.getRNG());
            GeomancyHelper.summonAreaDenialTrap(playerIn, target, ModEntityTypes.GEOMANCER_WALL.get(), rowToRemove);
        }
        playerIn.getCooldownTracker().setCooldown(itemStack.getItem(), 400);
        itemStack.damageItem(1, playerIn, playerEntity -> playerEntity.sendBreakAnimation(hand));
    }
}
