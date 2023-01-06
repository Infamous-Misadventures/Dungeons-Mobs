package com.infamous.dungeons_mobs.mobenchants;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

import static baguchan.enchantwithmob.utils.MobEnchantUtils.getMobEnchantLevelFromHandler;

public class NewMobEnchantUtils {

    public static void executeIfPresentWithLevel(LivingEntity entity, MobEnchant mobEnchantment, Consumer<Integer> consumer) {
        if (entity != null) {
            entity.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent((cap) -> {
                int level = getMobEnchantLevelFromHandler(cap.getMobEnchants(), mobEnchantment);
                if (level > 0) {
                    consumer.accept(level);
                }

            });
        }

    }
}
