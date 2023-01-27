package com.infamous.dungeons_mobs.entities;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;
import static com.infamous.dungeons_mobs.mod.ModEffects.ENSNARED;
import static net.minecraft.world.entity.EntityType.HUSK;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MODID)
public class EntityEvents {

	@SubscribeEvent
	public static void changeAttributes(EntityJoinLevelEvent event) {
		// Tougher Husks
		if (event.getEntity().getType().equals(HUSK) && event.getEntity() instanceof LivingEntity livingEntity && DungeonsMobsConfig.COMMON.ENABLE_STRONGER_HUSKS.get()) {
			AttributeInstance attribute = livingEntity.getAttribute(Attributes.ARMOR);
			if (attribute != null) {
				attribute.setBaseValue(10.0D);
			}
			attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
			if (attribute != null) {
				attribute.setBaseValue(0.17D);
			}
			attribute = livingEntity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
			if (attribute != null) {
				attribute.setBaseValue(0.6D);
			}
		}
	}

	@SubscribeEvent
	public static void preventKnockback(LivingKnockBackEvent event) {
		LivingEntity owner = event.getEntity();

		if (owner.hasEffect(ENSNARED.get())) event.setCanceled(true);
	}

	@SubscribeEvent
	public static void preventExtraMovement(MovementInputUpdateEvent event) {
		Player owner = event.getEntity();

		if (owner.hasEffect(ENSNARED.get())) {
			event.getInput().getMoveVector().scale(0);
			if (event.getInput().jumping) event.getInput().jumping = false;
		}
	}
}
