package com.infamous.dungeons_mobs.mobenchantments;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.*;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.RADIANCE;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class RadianceMobEnchant extends MobEnchant {


    public RadianceMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        Entity attacker;
        if (!event.getSource().isProjectile()) {
            attacker = event.getSource().getDirectEntity();
        } else {
            attacker = event.getSource().getEntity();
        }
        if (attacker instanceof LivingEntity)
            executeIfPresentWithLevel((LivingEntity) attacker, RADIANCE.get(), (level) -> {
                LivingEntity source = event.getSource().isProjectile() ? event.getEntityLiving() : (LivingEntity) attacker;
                applyToNearbyEntities(source, 1.5F,
                        getCanHealPredicate(source), (LivingEntity nearbyEntity) -> {
                            nearbyEntity.heal(level);
                            PROXY.spawnParticles(nearbyEntity, ParticleTypes.HEART);
                        }
                );
            });
    }
}