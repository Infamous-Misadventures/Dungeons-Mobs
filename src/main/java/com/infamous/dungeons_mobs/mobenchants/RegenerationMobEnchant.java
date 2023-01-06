package com.infamous.dungeons_mobs.mobenchants;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.REGENERATION;

public class RegenerationMobEnchant extends MobEnchant {

    public RegenerationMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        executeIfPresentWithLevel(entity, REGENERATION.get(), (level) -> {
            if (entity.getHealth() < entity.getMaxHealth() && entity.tickCount % getTickCountForLevel(level) == 0) {
                entity.heal(1.0F);
                PROXY.spawnParticles(entity, ParticleTypes.HEART);
            }
        });
    }

    private static int getTickCountForLevel(Integer level) {
        return 62 - level * 12;
    }
}