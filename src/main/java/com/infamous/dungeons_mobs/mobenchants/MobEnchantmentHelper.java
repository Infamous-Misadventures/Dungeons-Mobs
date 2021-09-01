package com.infamous.dungeons_mobs.mobenchants;

import net.minecraft.entity.LivingEntity;

import static com.infamous.dungeons_mobs.capabilities.enchantable.EnchantableHelper.getEnchantableCapability;

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

}
