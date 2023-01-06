package com.infamous.dungeons_mobs.entities.jungle;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

import java.util.EnumSet;
import java.util.UUID;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.entities.summonables.AreaDamageEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.tags.EntityTags;
import com.infamous.dungeons_mobs.utils.PositionUtils;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class LeapleafEntity extends Monster implements IAnimatable {

	private static final UUID SPEED_MODIFIER_CHARGING_UUID = UUID.fromString("b380d5fd-85cb-4ac3-9450-d9092a09e0c9");
	private static final AttributeModifier SPEED_MODIFIER_CHARGING = new AttributeModifier(SPEED_MODIFIER_CHARGING_UUID,
			"Charging speed increase", 0.1D, AttributeModifier.Operation.ADDITION);

	private static final EntityDataAccessor<Integer> TIMES_LEAPT = SynchedEntityData.defineId(LeapleafEntity.class,
			EntityDataSerializers.INT);

	private static final EntityDataAccessor<Boolean> CAN_LEAP = SynchedEntityData.defineId(LeapleafEntity.class,
			EntityDataSerializers.BOOLEAN);

	private static final EntityDataAccessor<Boolean> LEAPING = SynchedEntityData.defineId(LeapleafEntity.class,
			EntityDataSerializers.BOOLEAN);

	public int strafeTick;
	public int strafeLength = 40;

	public int restTick;
	public int restLength = 100;

	public int attackAnimationTick;
	public int attackAnimationLength = 30;
	public int attackAnimationActionPoint = 15;

	public int prepareLeapAnimationTick;
	public int prepareLeapAnimationLength = 30;

	public int leapAnimationTick;
	public int leapAnimationLength = 9;

	public int smashAnimationTick;
	public int smashAnimationLength = 25;
	public int smashAnimationActionPoint = 18;

	public int leapCooldown;

	AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public LeapleafEntity(Level world) {
		super(ModEntityTypes.LEAPLEAF.get(), world);
	}

	public LeapleafEntity(EntityType<? extends LeapleafEntity> type, Level world) {
		super(type, world);
		this.xpReward = 20;
		this.maxUpStep = 1.0F;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new LeapleafEntity.RemainStationaryGoal());
		this.goalSelector.addGoal(1, new LeapleafEntity.StrafeGoal(this));
		this.goalSelector.addGoal(2, new LeapleafEntity.LeapGoal(this));
		this.goalSelector.addGoal(3, new LeapleafEntity.PrepareLeapGoal(this));
		this.goalSelector.addGoal(4, new LeapleafEntity.BasicAttackGoal(this));
		this.goalSelector.addGoal(5, new ApproachTargetGoal(this, 0, 1.2D, true));
		this.goalSelector.addGoal(6, new LookAtTargetGoal(this));
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.275D)
				.add(Attributes.FOLLOW_RANGE, 25.0D).add(Attributes.MAX_HEALTH, 75.0D).add(Attributes.ARMOR, 15D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	public boolean shouldBeStationary() {
		return this.restTick > 0;
	}

	@Override
	public void playAmbientSound() {
		SoundEvent soundeventVocal = this.getAmbientSound();
		SoundEvent soundeventFoley = this.getAmbientSoundFoley();
		this.playSound(soundeventVocal, soundeventFoley, this.getSoundVolume(), this.getVoicePitch(),
				this.getSoundVolume(), this.getVoicePitch());
	}

	@Override
	protected void playHurtSound(DamageSource p_184581_1_) {
		this.ambientSoundTime = -this.getAmbientSoundInterval();
		SoundEvent soundeventVocal = this.getHurtSound(p_184581_1_);
		SoundEvent soundeventFoley = this.getHurtSoundFoley(p_184581_1_);
		this.playSound(soundeventVocal, soundeventFoley, this.getSoundVolume(), this.getVoicePitch(),
				this.getSoundVolume(), this.getVoicePitch());
	}

	@Override
	protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
		this.playSound(this.getStepSound(), 0.5F, 1.0F);
		this.playSound(this.getStepSoundFoley(), 0.5F, 1.0F);
	}

	public void playSound(SoundEvent vocalSound, SoundEvent foleySound, float vocalVolume, float vocalPitch,
			float foleyVolume, float foleyPitch) {
		if (!this.isSilent()) {
			if (vocalSound != null) {
				this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), vocalSound,
						this.getSoundSource(), vocalVolume, vocalPitch);
			}
			if (foleySound != null) {
				this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), foleySound,
						this.getSoundSource(), foleyVolume, foleyPitch);
			}
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return ModSoundEvents.LEAPLEAF_IDLE_VOCAL.get();
	}

	protected SoundEvent getAmbientSoundFoley() {
		return ModSoundEvents.LEAPLEAF_IDLE_FOLEY.get();
	}

	@Override
	public int getAmbientSoundInterval() {
		return 250;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return ModSoundEvents.LEAPLEAF_HURT_VOCAL.get();
	}

	protected SoundEvent getHurtSoundFoley(DamageSource p_184601_1_) {
		return ModSoundEvents.LEAPLEAF_HURT_FOLEY.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.LEAPLEAF_DEATH.get();
	}

	protected SoundEvent getStepSound() {
		return ModSoundEvents.LEAPLEAF_STEP_VOCAL.get();
	}

	protected SoundEvent getStepSoundFoley() {
		return ModSoundEvents.LEAPLEAF_STEP_FOLEY.get();
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(TIMES_LEAPT, 0);
		this.entityData.define(CAN_LEAP, false);
		this.entityData.define(LEAPING, false);
	}

	public int getTimesLeapt() {
		return this.entityData.get(TIMES_LEAPT);
	}

	public void setTimesLeapt(int attached) {
		this.entityData.set(TIMES_LEAPT, attached);
	}

	public boolean canLeap() {
		return this.entityData.get(CAN_LEAP);
	}

	public void setCanLeap(boolean attached) {
		this.entityData.set(CAN_LEAP, attached);
	}

	public boolean isLeaping() {
		return this.entityData.get(LEAPING);
	}

	public void setLeaping(boolean attached) {
		this.entityData.set(LEAPING, attached);
	}

	public void handleEntityEvent(byte p_28844_) {
		if (p_28844_ == 4) {
			this.attackAnimationTick = attackAnimationLength;
		} else if (p_28844_ == 8) {
			this.prepareLeapAnimationTick = prepareLeapAnimationLength;
		} else if (p_28844_ == 9) {
			this.leapAnimationTick = leapAnimationLength;
		} else if (p_28844_ == 11) {
			this.smashAnimationTick = smashAnimationLength;
		} else if (p_28844_ == 7) {
			this.restTick = restLength;
		} else {
			super.handleEntityEvent(p_28844_);
		}
	}

	public void baseTick() {
		super.baseTick();
		this.tickDownAnimTimers();

		AttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);

		if (!this.level.isClientSide && this.canLeap()
				&& (this.getTarget() == null || this.getTarget().isDeadOrDying())) {
			this.restTick = this.restLength;
			this.level.broadcastEntityEvent(this, (byte) 7);
			this.setCanLeap(false);
			this.setTimesLeapt(0);
		}

		if (this.canLeap()) {
			if (!modifiableattributeinstance.hasModifier(SPEED_MODIFIER_CHARGING)) {
				modifiableattributeinstance.addTransientModifier(SPEED_MODIFIER_CHARGING);
			}
		} else {
			modifiableattributeinstance.removeModifier(SPEED_MODIFIER_CHARGING);
		}

		if (this.getDeltaMovement().horizontalDistanceSqr() > (double) 2.5000003E-7F && this.random.nextInt(3) == 0) {
			int i = Mth.floor(this.getX());
			int j = Mth.floor(this.getY() - (double) 0.2F);
			int k = Mth.floor(this.getZ());
			BlockPos pos = new BlockPos(i, j, k);
			BlockState blockstate = this.level.getBlockState(pos);
			if (!blockstate.isAir()) {
				this.level.addParticle(ModParticleTypes.DUST.get(),
						this.getX() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
						this.getY() + 0.1D,
						this.getZ() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
						this.random.nextFloat() * 0.5, this.random.nextGaussian() * 1, this.random.nextGaussian() * 1);
			}
		}

		if (this.restTick > 0) {
			this.restTick--;
		}

		if (this.strafeTick > 0) {
			this.strafeTick--;
		}

		if (this.leapCooldown > 0) {
			this.leapCooldown--;
		}
	}

	public void tickDownAnimTimers() {
		if (this.attackAnimationTick > 0) {
			this.attackAnimationTick--;
		}

		if (this.prepareLeapAnimationTick > 0) {
			this.prepareLeapAnimationTick--;
		}

		if (this.leapAnimationTick > 0) {
			this.leapAnimationTick--;
		}

		if (this.smashAnimationTick > 0) {
			this.smashAnimationTick--;
		}
	}

	@Override
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource p_147189_) {
		return false;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.smashAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("leapleaf_smash", LOOP));
		} else if (this.leapAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("leapleaf_leap", LOOP));
		} else if (this.prepareLeapAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("leapleaf_prepare_leap", LOOP));
		} else if (this.attackAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("leapleaf_attack", LOOP));
		} else if (this.restTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("leapleaf_rest", LOOP));
		} else if (this.isLeaping()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("leapleaf_leaping", LOOP));
		} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("leapleaf_walk", LOOP));
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("leapleaf_idle", LOOP));
		}
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public boolean isAlliedTo(Entity p_184191_1_) {
		if (super.isAlliedTo(p_184191_1_)) {
			return true;
		} else if (p_184191_1_ instanceof LivingEntity
				&& ((LivingEntity) p_184191_1_).getType().is(EntityTags.PLANT_MOBS)) {
			return this.getTeam() == null && p_184191_1_.getTeam() == null;
		} else {
			return false;
		}
	}

	class BasicAttackGoal extends Goal {

		public LeapleafEntity mob;
		@Nullable
		public LivingEntity target;

		public BasicAttackGoal(LeapleafEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
			this.mob = mob;
			this.target = mob.getTarget();
		}

		@Override
		public boolean isInterruptable() {
			return mob.shouldBeStationary();
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public boolean canUse() {
			target = mob.getTarget();

			return target != null && mob.distanceTo(target) <= 3.25 && animationsUseable()
					&& mob.hasLineOfSight(target);
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.playSound(ModSoundEvents.LEAPLEAF_ATTACK_VOCAL.get(), ModSoundEvents.LEAPLEAF_ATTACK_FOLEY.get(), 1.25F,
					1.0F, 1.25F, 1.0F);
			mob.attackAnimationTick = mob.attackAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 4);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			if (mob.attackAnimationTick == mob.attackAnimationActionPoint) {
				Vec3 areaDamagePos = PositionUtils.getOffsetPos(mob, -1, 0, 1.5, mob.yBodyRot);
				AreaDamageEntity areaDamage = AreaDamageEntity.spawnAreaDamage(mob.level, areaDamagePos, mob, 15F,
						DamageSource.mobAttack(mob), 0.0F, 4.5F, 1.0F, 0.5F, 0, false, false, 1.0D, 0.2D, false, 0, 1);
				mob.level.addFreshEntity(areaDamage);
			}
		}

		public boolean animationsUseable() {
			return mob.attackAnimationTick <= 0;
		}

	}

	class PrepareLeapGoal extends Goal {

		public LeapleafEntity mob;
		@Nullable
		public LivingEntity target;

		public PrepareLeapGoal(LeapleafEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
			this.mob = mob;
			this.target = mob.getTarget();
		}

		@Override
		public boolean isInterruptable() {
			return mob.shouldBeStationary();
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public boolean canUse() {
			target = mob.getTarget();

			return target != null && mob.distanceTo(target) <= 15 && !mob.canLeap() && mob.random.nextInt(30) == 0
					&& animationsUseable() && mob.hasLineOfSight(target) && mob.leapCooldown <= 0;
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.playSound(ModSoundEvents.LEAPLEAF_PREPARE_LEAP_VOCAL.get(),
					ModSoundEvents.LEAPLEAF_PREPARE_LEAP_FOLEY.get(), 1.25F, 1.0F, 1.25F, 1.0F);
			mob.prepareLeapAnimationTick = mob.prepareLeapAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 8);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			if (mob.prepareLeapAnimationTick == 1) {
				mob.setCanLeap(true);
			}
		}

		public boolean animationsUseable() {
			return mob.prepareLeapAnimationTick <= 0;
		}

	}

	class LeapGoal extends Goal {

		public LeapleafEntity mob;
		@Nullable
		public LivingEntity target;

		public int leapTime;

		public LeapGoal(LeapleafEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
			this.mob = mob;
			this.target = mob.getTarget();
		}

		@Override
		public boolean isInterruptable() {
			return mob.shouldBeStationary();
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public boolean canUse() {
			target = mob.getTarget();

			return target != null && mob.distanceTo(target) <= 10 && mob.canLeap() && mob.random.nextInt(5) == 0
					&& animationsUseable() && mob.hasLineOfSight(target);
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && (!animationsUseable() || mob.isLeaping() || mob.smashAnimationTick > 0);
		}

		@Override
		public void start() {
			mob.playSound(ModSoundEvents.LEAPLEAF_LEAP_VOCAL.get(), ModSoundEvents.LEAPLEAF_LEAP_FOLEY.get(), 1.5F,
					1.0F, 1.5F, 1.0F);
			mob.leapAnimationTick = mob.leapAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 9);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			if (target != null && mob.leapAnimationTick == 1) {
				double d0 = target.getX() - mob.getX();
				double d1 = target.getY() - mob.getY();
				double d2 = target.getZ() - mob.getZ();
				mob.setDeltaMovement(d0 * 0.15D, 0.75 + Mth.clamp(d1 * 0.05D, 0, 10), d2 * 0.15D);
				mob.setLeaping(true);
				mob.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
				this.leapTime = 0;
			}

			if (mob.isLeaping()) {
				this.leapTime++;
			}

			if (mob.isLeaping() && mob.isOnGround() && this.leapTime > 10) {
				mob.playSound(ModSoundEvents.LEAPLEAF_LAND.get(), 2.0F, 1.0F);
				mob.setLeaping(false);
				mob.smashAnimationTick = mob.smashAnimationLength;
				mob.level.broadcastEntityEvent(mob, (byte) 11);
			}

			if (mob.smashAnimationTick == mob.smashAnimationActionPoint) {
				Vec3 areaDamagePos = PositionUtils.getOffsetPos(mob, -1.5, 0, 2, mob.yBodyRot);
				AreaDamageEntity areaDamage = AreaDamageEntity.spawnAreaDamage(mob.level, areaDamagePos, mob, 25F,
						DamageSource.mobAttack(mob), 0.0F, 6.0F, 1.0F, 0.75F, 10, false, false, 2.0D, 0.4D, true, 120,
						1);

				Vec3 areaDamagePos2 = PositionUtils.getOffsetPos(mob, 1.5, 0, 2, mob.yBodyRot);
				AreaDamageEntity areaDamage2 = AreaDamageEntity.spawnAreaDamage(mob.level, areaDamagePos2, mob, 25F,
						DamageSource.mobAttack(mob), 0.0F, 6.0F, 1.0F, 0.75F, 10, false, false, 2.0D, 0.4D, true, 120,
						1);

				areaDamage.connectedAreaDamages.add(areaDamage2);
				areaDamage2.connectedAreaDamages.add(areaDamage);

				mob.level.addFreshEntity(areaDamage);
				mob.level.addFreshEntity(areaDamage2);
			}
		}

		@Override
		public void stop() {
			super.stop();
			this.leapTime = 0;
			mob.setCanLeap(false);
			mob.setLeaping(false);
			mob.setTimesLeapt(mob.getTimesLeapt() + 1);
			mob.leapCooldown = 60;

			int leapTimesByDifficulty = mob.level.getCurrentDifficultyAt(mob.blockPosition()).getDifficulty().getId();

			if (mob.getTimesLeapt() >= leapTimesByDifficulty) {
				mob.restTick = mob.restLength;
				mob.level.broadcastEntityEvent(mob, (byte) 7);
				mob.setTimesLeapt(0);
				mob.playSound(ModSoundEvents.LEAPLEAF_REST_VOCAL.get(), ModSoundEvents.LEAPLEAF_REST_FOLEY.get(), 1.0F,
						mob.getVoicePitch(), 1.0F, mob.getVoicePitch());
			} else {
				mob.strafeTick = mob.strafeLength;
			}
		}

		public boolean animationsUseable() {
			return mob.leapAnimationTick <= 0;
		}

	}

	class StrafeGoal extends Goal {

		public LeapleafEntity mob;
		@Nullable
		public LivingEntity target;

		private boolean strafingLeft;

		public StrafeGoal(LeapleafEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
			this.mob = mob;
			this.target = mob.getTarget();
		}

		@Override
		public boolean isInterruptable() {
			return mob.shouldBeStationary();
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public boolean canUse() {
			target = mob.getTarget();

			return target != null && mob.strafeTick > 0;
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && mob.strafeTick > 0;
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			if (target != null) {
				mob.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());

				if (mob.random.nextInt(30) == 0) {
					this.strafingLeft = !this.strafingLeft;
				}

				if (mob.distanceTo(target) < 4) {
					mob.setDeltaMovement(
							mob.getDeltaMovement().add(PositionUtils.getOffsetMotion(mob, 0, 0, -0.1, mob.yBodyRot)));
				}

				if (this.strafingLeft) {
					mob.setDeltaMovement(
							mob.getDeltaMovement().add(PositionUtils.getOffsetMotion(mob, 0.05, 0, 0, mob.yBodyRot)));
				} else {
					mob.setDeltaMovement(
							mob.getDeltaMovement().add(PositionUtils.getOffsetMotion(mob, -0.05, 0, 0, mob.yBodyRot)));
				}
			}
		}

	}

	class RemainStationaryGoal extends Goal {

		public RemainStationaryGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET, Goal.Flag.JUMP));
		}

		@Override
		public boolean canUse() {
			return LeapleafEntity.this.shouldBeStationary();
		}
	}

}
