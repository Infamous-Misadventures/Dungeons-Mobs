package com.infamous.dungeons_mobs.mobenchants;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ShulkerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.infamous.dungeons_mobs.capabilities.enchantable.EnchantableHelper.getEnchantableCapability;
import static com.infamous.dungeons_mobs.mobenchants.MobEnchantment.Type.*;
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

    public static MobEnchantment getRandomMobEnchantment(Random random, List<MobEnchantment> mobEnchantments){
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

    public static MobEnchantment getRandomMobEnchantment(Entity entity, Random random) {
        List<MobEnchantment> mobEnchantments = MOB_ENCHANTMENTS.get().getValues()
                .stream()
                .filter(MobEnchantmentHelper::isDisabled)
                .filter(mobEnchantment -> MobEnchantmentHelper.hasType(mobEnchantment, getPossibleTypes(entity)))
                .collect(Collectors.toList());
        return getRandomMobEnchantment(random, mobEnchantments);
    }

    private static ArrayList<MobEnchantment.Type> getPossibleTypes(Entity entity) {
        if(entity instanceof IRangedAttackMob || entity instanceof ShulkerEntity){
            return Lists.newArrayList(ANY, RANGED);
        }else {
            return Lists.newArrayList(ANY);
        }
    }

    private static boolean isDisabled(MobEnchantment mobEnchantment) {
        return !DungeonsMobsConfig.COMMON.DISABLED_SPAWN_ENCHANTMENTS.get().contains(mobEnchantment.getRegistryName().toString());
    }

    private static boolean hasType(MobEnchantment mobEnchantment, ArrayList<MobEnchantment.Type> possibleTypes) {
        return possibleTypes.contains(mobEnchantment.getType());
    }

    public static MobEnchantment getRandomMobEnchantment(Random random) {
        List<MobEnchantment> mobEnchantments = MOB_ENCHANTMENTS.get().getValues()
                .stream()
                .filter(MobEnchantmentHelper::isDisabled)
                .collect(Collectors.toList());
        return getRandomMobEnchantment(random, mobEnchantments);
    }

}
