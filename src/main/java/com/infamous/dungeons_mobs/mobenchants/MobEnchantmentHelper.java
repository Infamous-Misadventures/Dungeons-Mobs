package com.infamous.dungeons_mobs.mobenchants;

import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.infamous.dungeons_mobs.capabilities.enchantable.EnchantableHelper.getEnchantableCapability;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.MOB_ENCHANTMENTS;

public class MobEnchantmentHelper {

    public static void executeIfPresent(LivingEntity entity, MobEnchantment mobEnchantment, Runnable runnable){
        if (entity != null) {
            getEnchantableCapability(entity).ifPresent(cap -> {
                if(cap.hasEnchantment(mobEnchantment)) {
                    runnable.run();
                }
            });
        }
    }

    public static MobEnchantment getRandomMobEnchantment(Random random){
        List<MobEnchantment> mobEnchantments = new ArrayList<>(MOB_ENCHANTMENTS.get().getValues());
        double totalWeight = 0.0;

        // Compute the total weight of all items together.
        for (MobEnchantment mobEnchantment : mobEnchantments) {
            totalWeight += mobEnchantment.getRarity().getWeight();
        }

        // Now choose a random item.
        int index = 0;
        for (double randomWeightPicked = random.nextFloat() * totalWeight; index < mobEnchantments.size() - 1; ++index) {
            randomWeightPicked -= mobEnchantments.get(index).getRarity().getWeight();
            if (randomWeightPicked <= 0.0) break;
        }

        return mobEnchantments.get(index);
    }

}
