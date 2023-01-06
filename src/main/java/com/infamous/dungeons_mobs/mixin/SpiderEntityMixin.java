package com.infamous.dungeons_mobs.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.goals.RangedWebAttackGoal;
import com.infamous.dungeons_mobs.interfaces.ITrapsTarget;
import com.infamous.dungeons_mobs.interfaces.IWebShooter;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;

@Mixin(Spider.class)
public abstract class SpiderEntityMixin extends Monster implements IWebShooter {

	private static final EntityDataAccessor<Boolean> WEBSHOOTING = SynchedEntityData.defineId(Spider.class,
			EntityDataSerializers.BOOLEAN);
	private RangedWebAttackGoal<?> rangedWebAttackGoal;
	private LeapAtTargetGoal leapAtTargetGoal;
	private MeleeAttackGoal meleeAttackGoal;

	public int targetTrappedCounter = 0;

	protected SpiderEntityMixin(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
	}

	@Inject(at = @At("TAIL"), method = "registerGoals")
	private void registerGoals(CallbackInfo callbackInfo) {
		((GoalSelectorAccessor) this.goalSelector).getAvailableGoals().stream()
				.filter(pg -> pg.getPriority() == 4 && pg.getGoal() instanceof MeleeAttackGoal).findFirst()
				.ifPresent(pg -> {
					this.meleeAttackGoal = (MeleeAttackGoal) pg.getGoal();
					// DungeonsMobs.LOGGER.debug("Found and stored melee attack goal for Spider {}",
					// this);
				});
		((GoalSelectorAccessor) this.goalSelector).getAvailableGoals().stream()
				.filter(pg -> pg.getPriority() == 3 && pg.getGoal() instanceof LeapAtTargetGoal).findFirst()
				.ifPresent(pg -> {
					this.leapAtTargetGoal = (LeapAtTargetGoal) pg.getGoal();
					// DungeonsMobs.LOGGER.debug("Found and stored leap at target goal for Spider
					// {}", this);
				});

		this.rangedWebAttackGoal = new RangedWebAttackGoal<>(this, 1.0D, 60, 20.0F);
	}

	@Inject(at = @At("RETURN"), method = "defineSynchedData")
	private void registerData(CallbackInfo callbackInfo) {
		this.entityData.define(WEBSHOOTING, false);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (DungeonsMobsConfig.COMMON.ENABLE_RANGED_SPIDERS.get() && this.getType() != EntityType.CAVE_SPIDER) {
			this.reassessAttackGoals();
		}
	}

	/*
	 * We check for leapAtTargetGoal not being null on a case-by-case basis since we
	 * want compatibility with Spiders 2.0 which changes the LeapAtTargetGoal to a
	 * custom Goal that doesn't extend it
	 */
	private void reassessAttackGoals() {
		LivingEntity target = this.getTarget();
		if (this.meleeAttackGoal != null && this.rangedWebAttackGoal != null && target != null) {
			if (!this.isTargetTrapped()) {
				// DungeonsMobs.LOGGER.debug("Changing Spider {} to ranged AI!", this);
				this.goalSelector.removeGoal(this.meleeAttackGoal);
				if (this.leapAtTargetGoal != null) {
					this.goalSelector.removeGoal(this.leapAtTargetGoal);
				}
				this.goalSelector.addGoal(4, (Goal) this.rangedWebAttackGoal);
			} else {
				// DungeonsMobs.LOGGER.debug("Changing Spider {} to melee AI!", this);
				this.goalSelector.removeGoal((Goal) this.rangedWebAttackGoal);
				if (this.leapAtTargetGoal != null) {
					this.goalSelector.addGoal(3, this.leapAtTargetGoal);
				}
				this.goalSelector.addGoal(4, this.meleeAttackGoal);
			}
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.targetTrappedCounter > 0) {
			this.targetTrappedCounter--;
		}
	}

	@Override
	public void setTargetTrapped(boolean trapped, boolean notifyOthers) {
		TargetingConditions spiderTargeting = TargetingConditions.forCombat().range(10.0D).ignoreInvisibilityTesting();

		if (notifyOthers) {
			List<Spider> spiders = this.level.getNearbyEntities(Spider.class, spiderTargeting, this,
					this.getBoundingBox().inflate(10.0D));

			for (Spider spider : spiders) {
				if (spider instanceof ITrapsTarget && this.getTarget() != null && spider.getTarget() != null
						&& spider.getTarget() == this.getTarget()) {
					((ITrapsTarget) spider).setTargetTrapped(trapped, false);
				}
			}
		}

		if (trapped) {
			this.targetTrappedCounter = 20;
		} else {
			this.targetTrappedCounter = 0;
		}
	}

	@Override
	public boolean isTargetTrapped() {
		return this.targetTrappedCounter > 0;
	}

	@Override
	public void setWebShooting(boolean webShooting) {
		this.playSound(ModSoundEvents.SPIDER_PREPARE_SHOOT.get(), this.getSoundVolume(), this.getVoicePitch());
		this.entityData.set(WEBSHOOTING, webShooting);
	}

	@Override
	public boolean isWebShooting() {
		return this.entityData.get(WEBSHOOTING);
	}

}
