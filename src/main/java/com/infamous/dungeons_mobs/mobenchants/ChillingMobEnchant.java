package com.infamous.dungeons_mobs.mobenchants;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.capabilities.properties.MobProps;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanApplyToEnemyPredicate;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.CHILLING;

public class ChillingMobEnchant extends MobEnchant {

    public ChillingMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void OnLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        executeIfPresentWithLevel(entity, CHILLING.get(), (level) -> {
            MobProps comboCap = MobPropsHelper.getMobPropsCapability(entity);
            if (comboCap == null) return;
            int freezeNearbyTimer = comboCap.getFreezeNearbyTimer();
            if (freezeNearbyTimer <= 0) {
                PROXY.spawnParticles(entity, ParticleTypes.ITEM_SNOWBALL);
                applyToNearbyEntities(entity, 1.5F,
                        getCanApplyToEnemyPredicate(entity), (LivingEntity nearbyEntity) -> {
                            freezeEnemy(1, nearbyEntity, level);
                            PROXY.spawnParticles(nearbyEntity, ParticleTypes.ITEM_SNOWBALL);
                        }
                );
                comboCap.setFreezeNearbyTimer(40);
            } else {
                comboCap.setFreezeNearbyTimer(freezeNearbyTimer - 1);
            }
        });
    }

    private static void freezeEnemy(int amplifier, LivingEntity nearbyEntity, int durationInSeconds) {
        MobEffectInstance slowness = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, durationInSeconds * 20, amplifier);
        MobEffectInstance fatigue = new MobEffectInstance(MobEffects.DIG_SLOWDOWN, durationInSeconds * 20, Math.max(0, amplifier * 2 - 1));
        nearbyEntity.addEffect(slowness);
        nearbyEntity.addEffect(fatigue);
        PROXY.spawnParticles(nearbyEntity, ParticleTypes.ITEM_SNOWBALL);
    }
}