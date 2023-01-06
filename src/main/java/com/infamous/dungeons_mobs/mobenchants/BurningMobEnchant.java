package com.infamous.dungeons_mobs.mobenchants;

import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.applyToNearbyEntities;
import static com.infamous.dungeons_libraries.utils.AreaOfEffectHelper.getCanApplyToEnemyPredicate;
import static com.infamous.dungeons_mobs.DungeonsMobs.PROXY;
import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.BURNING;

import com.infamous.dungeons_mobs.capabilities.properties.MobProps;
import com.infamous.dungeons_mobs.capabilities.properties.MobPropsHelper;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BurningMobEnchant extends MobEnchant {

	public BurningMobEnchant(Properties properties) {
		super(properties);
	}

	@SubscribeEvent
	public static void OnLivingUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = (LivingEntity) event.getEntity();

		executeIfPresentWithLevel(entity, BURNING.get(), (level) -> {
			MobProps comboCap = MobPropsHelper.getMobPropsCapability(entity);
			if (comboCap == null)
				return;
			int burnNearbyTimer = comboCap.getBurnNearbyTimer();
			if (burnNearbyTimer <= 0) {
				PROXY.spawnParticles(entity, ParticleTypes.FLAME);
				applyToNearbyEntities(entity, 1.5F, getCanApplyToEnemyPredicate(entity),
						(LivingEntity nearbyEntity) -> {
							nearbyEntity.hurt(DamageSource.ON_FIRE, 0.5F * level);
							PROXY.spawnParticles(nearbyEntity, ParticleTypes.FLAME);
						});
				comboCap.setBurnNearbyTimer(20);
			} else {
				comboCap.setBurnNearbyTimer(burnNearbyTimer - 1);
			}
		});
	}
}