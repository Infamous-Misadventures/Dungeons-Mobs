package com.infamous.dungeons_mobs.mobenchants;

import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.RUSH;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RushMobEnchant extends MobEnchant {

	public RushMobEnchant(Properties properties) {
		super(properties);
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event) {
		LivingEntity defender = (LivingEntity) event.getEntity();
		executeIfPresentWithLevel(defender, RUSH.get(), (level) -> {
			defender.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10 + 20 * level, 3, false, false));
		});

	}
}