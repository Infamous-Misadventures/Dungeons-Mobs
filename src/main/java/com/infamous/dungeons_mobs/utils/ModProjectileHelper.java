package com.infamous.dungeons_mobs.utils;

import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;

import java.util.List;
import java.util.function.Predicate;

public class ModProjectileHelper {

    public static Hand getHandWith(LivingEntity livingEntity, Predicate<Item> itemPredicate){

        return itemPredicate.test(livingEntity.getMainHandItem().getItem()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    public static ItemStack createRocket(DyeColor dyeColor) {
       ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
       ItemStack star = new ItemStack(Items.FIREWORK_STAR);
       CompoundNBT starExplosionNBT = star.getOrCreateTagElement("Explosion");
       starExplosionNBT.putInt("Type", FireworkRocketItem.Shape.BURST.getId());
       CompoundNBT rocketFireworksNBT = rocket.getOrCreateTagElement("Fireworks");
       ListNBT rocketExplosionsNBT = new ListNBT();
       CompoundNBT actualStarExplosionNBT = star.getTagElement("Explosion");
       if (actualStarExplosionNBT != null) {
          // making firework pink
          List<Integer> colorList = Lists.newArrayList();
          int pinkFireworkColor = dyeColor.getFireworkColor();
          colorList.add(pinkFireworkColor);
          actualStarExplosionNBT.putIntArray("Colors", colorList);
          actualStarExplosionNBT.putIntArray("FadeColors", colorList);
          // adding actualStarExplosionNBT to rocketExplosionsNBT
          rocketExplosionsNBT.add(actualStarExplosionNBT);
       }
       if (!rocketExplosionsNBT.isEmpty()) {
          rocketFireworksNBT.put("Explosions", rocketExplosionsNBT);
       }
       return rocket;
    }
}
