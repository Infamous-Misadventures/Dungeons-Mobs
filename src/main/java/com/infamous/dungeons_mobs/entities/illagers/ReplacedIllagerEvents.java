package com.infamous.dungeons_mobs.entities.illagers;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_libraries.utils.GoalUtils;
import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedProps;
import com.infamous.dungeons_mobs.capabilities.animatedprops.AnimatedPropsHelper;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.ReplacedModdedAttackGoal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MODID)
public class ReplacedIllagerEvents {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Vindicator vindicatorEntity) {
			GoalUtils.removeGoal(vindicatorEntity.goalSelector, MeleeAttackGoal.class);
			vindicatorEntity.goalSelector.addGoal(4, new ReplacedModdedAttackGoal<>(vindicatorEntity, null, 20));
			vindicatorEntity.goalSelector.addGoal(5, new ApproachTargetGoal(vindicatorEntity, 0, 1.0D, true));
			AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability(vindicatorEntity);
			cap.setAttackAnimationLength(7);
			cap.setAttackAnimationActionPoint(6);
		}
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {

		LivingEntity livingEntity = event.getEntity();
		if (livingEntity instanceof Vindicator) {
			tickDownAnimTimers((Mob) livingEntity);
		}
	}

	public static void tickDownAnimTimers(Mob mobEntity) {
		AnimatedProps cap = AnimatedPropsHelper.getAnimatedPropsCapability(mobEntity);
		if (cap.getAttackAnimationTick() > 0) {
			cap.setAttackAnimationTick(cap.getAttackAnimationTick() - 1);
//            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> mobEntity), new AnimatedPropsMessage(mobEntity.getId(), cap));
		}
	}

}
