package com.infamous.dungeons_mobs.mobenchants;

import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanHealPredicate;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.RADIANCE;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
				LivingEntity source = event.getSource().isProjectile() ? event.getEntity() : (LivingEntity) attacker;
				applyToNearbyEntities(source, 1.5F, getCanHealPredicate(source), (LivingEntity nearbyEntity) -> {
					nearbyEntity.heal(level);
					PROXY.spawnParticles(nearbyEntity, ParticleTypes.HEART);
				});
			});
	}
}