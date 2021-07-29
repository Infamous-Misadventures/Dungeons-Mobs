package com.infamous.dungeons_mobs.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;

import java.util.function.Predicate;

public class ModProjectileHelper {

    public static Hand getHandWith(LivingEntity livingEntity, Predicate<Item> itemPredicate){

        return itemPredicate.test(livingEntity.getMainHandItem().getItem()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }
}
