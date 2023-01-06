package com.infamous.dungeons_mobs.entities.golem;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.PLAY_ONCE;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SquallGolemEntity extends Raider implements IAnimatable {
	private int attackTimer;
	private int attackID;
	public int cd;
	public static final byte STOMP_ATTACK = 1;
	public static final byte GOLEM_ACTIVATE = 2;
	public static final byte GOLEM_DEACTIVATE = 3;
	private int timeWithoutTarget;
	private static final EntityDataAccessor<Boolean> ACTIVATE = SynchedEntityData.defineId(SquallGolemEntity.class,
			EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> MELEEATTACKING = SynchedEntityData
			.defineId(SquallGolemEntity.class, EntityDataSerializers.BOOLEAN);

	public SquallGolemEntity(Level world) {
		super(ModEntityTypes.SQUALL_GOLEM.get(), world);
	}

	public SquallGolemEntity(EntityType<? extends Raider> type, Level world) {
		super(type, world);
		this.xpReward = 20;
	}

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 90.0D) // >= Golem Health
				.add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.ATTACK_DAMAGE, 15.0D) // 1x Golem Attack
				.add(Attributes.ATTACK_KNOCKBACK, 1.25D); // 1x Ravager knockback
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	AnimationFactory factory = GeckoLibUtil.createFactory(this);

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 10, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		Vec3 velocity = this.getDeltaMovement();
		float groundSpeed = Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
		if (this.attackID == GOLEM_ACTIVATE) {
			event.getController().setAnimationSpeed(1.0D);
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.activate", PLAY_ONCE));
			return PlayState.CONTINUE;

		} else if (this.attackID == GOLEM_DEACTIVATE) {
			event.getController().setAnimationSpeed(1.0D);
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.deactivate", PLAY_ONCE));
			return PlayState.CONTINUE;

		} else if (!this.getActivate()) {
			event.getController().setAnimationSpeed(1.0D);
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.deactivated", LOOP));
			return PlayState.CONTINUE;

		} else if (this.isMeleeAttacking() && this.isAlive()) {
			event.getController().setAnimationSpeed(1.0D);
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.attack", PLAY_ONCE));
			return PlayState.CONTINUE;

		} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimationSpeed(groundSpeed * 25);
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.walk", LOOP));
			return PlayState.CONTINUE;

		} else {
			event.getController().setAnimationSpeed(1.0D);
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("animation.squall_golem.idle", LOOP));
			return PlayState.CONTINUE;
		}
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new SquallGolemEntity.AttackGoal());
		this.goalSelector.addGoal(0, new SquallGolemEntity.MeleeGoal());
		this.goalSelector.addGoal(1, new SquallGolemEntity.DoNothingGoal());
		this.goalSelector.addGoal(0, new SquallGolemEntity.Deactivate());
		this.goalSelector.addGoal(0, new SquallGolemEntity.Activate());
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6D));

		this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ACTIVATE, false);
		this.entityData.define(MELEEATTACKING, false);
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("activate", getActivate());
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		setActivate(compound.getBoolean("activate"));
	}

	public void setActivate(boolean isActivate) {
		this.entityData.set(ACTIVATE, isActivate);
	}

	public boolean getActivate() {
		return this.entityData.get(ACTIVATE);
	}

	public boolean isMeleeAttacking() {
		return this.entityData.get(MELEEATTACKING);
	}

	public void setMeleeAttacking(boolean attacking) {
		this.entityData.set(MELEEATTACKING, attacking);
	}

	public void aiStep() {
		super.aiStep();
		if (this.attackID != 0) {
			++this.attackTimer;
		}
		this.setDeltaMovement(SquallGolemEntity.this.getDeltaMovement().x,
				SquallGolemEntity.this.getDeltaMovement().y - 2.5, SquallGolemEntity.this.getDeltaMovement().z);
		this.handleLeafCollision();
		this.handleSteppingOnBlocks();
	}

	public void tick() {
		super.tick();

		if (this.attackID == STOMP_ATTACK) {
			if (this.attackTimer == 30) {
				Attackparticle(40, 0.5f, 2.6f, 0.5f);
				Attackparticle(40, 0.5f, 2.4f, -1f);
				// this.playSound(SoundEvents.GENERIC_EXPLODE, 1.5f, 1F +
				// this.getRandom().nextFloat() * 0.1F);
			}
		}
		LivingEntity target = this.getTarget();
		if (!level.isClientSide) {
			timeWithoutTarget++;
			if (target != null) {
				timeWithoutTarget = 0;
				if (!this.getActivate()) {
					this.setActivate(true);
					this.attackID = GOLEM_ACTIVATE;
				}
			}
			if (this.getCurrentRaid() != null && this.getCurrentRaid().isActive()) {
				timeWithoutTarget = 0;
				if (!this.getActivate()) {
					this.setActivate(true);
					this.attackID = GOLEM_ACTIVATE;
				}
			}

			if (timeWithoutTarget > 200 && this.getActivate() && target == null) {
				timeWithoutTarget = 0;
				this.setActivate(false);
				this.attackID = GOLEM_DEACTIVATE;

			}
		}

		if (this.cd > 0) {
			this.cd--;
		}

	}

	private void Attackparticle(int paticle, float circle, float vec, float math) {
		if (this.level.isClientSide) {
			for (int i1 = 0; i1 < paticle; i1++) {
				double DeltaMovementX = getRandom().nextGaussian() * 0.07D;
				double DeltaMovementY = getRandom().nextGaussian() * 0.07D;
				double DeltaMovementZ = getRandom().nextGaussian() * 0.07D;
				float angle = (0.01745329251F * this.yBodyRot) + i1;
				float f = Mth.cos(this.getYRot() * ((float) Math.PI / 180F));
				float f1 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F));
				double extraX = circle * Mth.sin((float) (Math.PI + angle));
				double extraY = 0.3F;
				double extraZ = circle * Mth.cos(angle);
				double theta = (yBodyRot) * (Math.PI / 180);
				theta += Math.PI / 2;
				double vecX = Math.cos(theta);
				double vecZ = Math.sin(theta);
				int hitX = Mth.floor(getX() + vec * vecX + extraX);
				int hitY = Mth.floor(getY());
				int hitZ = Mth.floor(getZ() + vec * vecZ + extraZ);
				BlockPos hit = new BlockPos(hitX, hitY, hitZ);
				BlockState block = level.getBlockState(hit.below());
				this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block),
						getX() + vec * vecX + extraX + f * math, this.getY() + extraY,
						getZ() + vec * vecZ + extraZ + f1 * math, DeltaMovementX, DeltaMovementY, DeltaMovementZ);

			}
		}
	}

	private void handleSteppingOnBlocks() {
		if (this.getDeltaMovement().horizontalDistanceSqr() > (double) 2.5000003E-7F && this.random.nextInt(5) == 0) {
			int i = Mth.floor(this.getX());
			int j = Mth.floor(this.getY() - (double) 0.2F);
			int k = Mth.floor(this.getZ());
			BlockPos pos = new BlockPos(i, j, k);
			BlockState blockstate = this.level.getBlockState(pos);
			if (!blockstate.isAir()) {
				this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos),
						this.getX() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
						this.getY() + 0.1D,
						this.getZ() + ((double) this.random.nextFloat() - 0.5D) * (double) this.getBbWidth(),
						4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D,
						((double) this.random.nextFloat() - 0.5D) * 4.0D);
			}
		}
	}

	private void handleLeafCollision() {
		if (this.isAlive()) {

			if (this.horizontalCollision
					&& net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
				boolean destroyedLeafBlock = false;
				AABB axisalignedbb = this.getBoundingBox().inflate(0.2D);

				for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(axisalignedbb.minX),
						Mth.floor(axisalignedbb.minY), Mth.floor(axisalignedbb.minZ), Mth.floor(axisalignedbb.maxX),
						Mth.floor(axisalignedbb.maxY), Mth.floor(axisalignedbb.maxZ))) {
					BlockState blockstate = this.level.getBlockState(blockpos);
					Block block = blockstate.getBlock();
					if (block instanceof LeavesBlock) {
						destroyedLeafBlock = this.level.destroyBlock(blockpos, true, this) || destroyedLeafBlock;
					}
				}

				if (!destroyedLeafBlock && this.onGround) {
					this.jumpFromGround();
				}
			}
		}
	}

	protected int decreaseAirSupply(int air) {
		return air;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		if (this.entityData.get(ACTIVATE) && this.noActionTime > 5) {
			return ModSoundEvents.SQUALL_GOLEM_IDLE.get();
		} else {
			return null;
		}
	}

	public boolean doHurtTarget(Entity entityIn) {
		if (!this.level.isClientSide && this.attackID == 0) {
			this.attackID = STOMP_ATTACK;
		}
		return true;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean hurt(DamageSource source, float amount) {
		boolean flag = false;
		if (!this.getActivate() && source != DamageSource.OUT_OF_WORLD) {
			this.playSound(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, 1.0F, 0.4F);
			if (source.getEntity() instanceof LivingEntity && source.getEntity().isInvulnerable()) {
				this.setTarget((LivingEntity) source.getEntity());
			}
			flag = false;
		} else {
			flag = super.hurt(source, amount);
		}
		return flag;
	}

	public boolean checkSpawnObstruction(LevelReader worldIn) {
		BlockPos golemPos = this.blockPosition();
		BlockPos posBeneathGolem = golemPos.below();
		BlockState blockstateBeneathGolem = worldIn.getBlockState(posBeneathGolem);
		if (!blockstateBeneathGolem.entityCanStandOn(worldIn, posBeneathGolem, this)) {
			return false;
		} else {
			for (int i = 1; i < 3; ++i) {
				BlockPos posAboveGolem = golemPos.above(i);
				BlockState blockstateAboveGolem = worldIn.getBlockState(posAboveGolem);
				if (!NaturalSpawner.isValidEmptySpawnBlock(worldIn, posAboveGolem, blockstateAboveGolem,
						blockstateAboveGolem.getFluidState(), ModEntityTypes.SQUALL_GOLEM.get())) {
					return false;
				}
			}

			return NaturalSpawner.isValidEmptySpawnBlock(worldIn, golemPos, worldIn.getBlockState(golemPos),
					Fluids.EMPTY.defaultFluidState(), ModEntityTypes.SQUALL_GOLEM.get())
					&& worldIn.isUnobstructed(this);
		}
	}

	/**
	 * Handler for {link WorldsetEntityState}
	 */
	private void setAttackID(int id) {
		this.attackID = id;
		this.attackTimer = 0;
		this.level.broadcastEntityEvent(this, (byte) -id);
	}

	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte id) {
		if (id <= 0) {
			this.attackID = Math.abs(id);
			this.attackTimer = 0;
		} else {
			super.handleEntityEvent(id);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, (double) (0.875F * this.getEyeHeight()), (double) (this.getBbWidth() * 0.4F));
	}

	public SoundSource getSoundSource() {
		return SoundSource.HOSTILE;
	}

	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(ModSoundEvents.SQUALL_GOLEM_WALK.get(), 1.12F, 1.0F);
	}

	@Nullable
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return ModSoundEvents.SQUALL_GOLEM_HURT.get();
	}

	@Override
	protected float getSoundVolume() {
		return 2.5F;
	}

	@Nullable
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.SQUALL_GOLEM_DEATH.get();
	}

	// NAVIGATION

	protected PathNavigation createNavigation(Level worldIn) {
		return new Navigator(this, worldIn);
	}

	static class Navigator extends GroundPathNavigation {
		public Navigator(Mob mobEntity, Level world) {
			super(mobEntity, world);
		}

		protected PathFinder createPathFinder(int p_179679_1_) {
			this.nodeEvaluator = new Processor();
			return new PathFinder(this.nodeEvaluator, p_179679_1_);
		}
	}

	static class Processor extends WalkNodeEvaluator {
		private Processor() {
		}

		protected BlockPathTypes evaluateBlockPathType(BlockGetter blockReader, boolean canBreakDoors,
				boolean canWalkThroughDoorways, BlockPos blockPos, BlockPathTypes pathNodeType) {
			return pathNodeType == BlockPathTypes.LEAVES ? BlockPathTypes.OPEN
					: super.evaluateBlockPathType(blockReader, canBreakDoors, canWalkThroughDoorways, blockPos,
							pathNodeType);
		}
	}

	// RAIDER METHODS
	@Override
	public void applyRaidBuffs(int p_213660_1_, boolean p_213660_2_) {

	}

	@Override
	public SoundEvent getCelebrateSound() {
		return ModSoundEvents.SQUALL_GOLEM_IDLE.get();
	}

	public boolean canBeLeader() {
		return false;
	}

	class AttackGoal extends MeleeAttackGoal {
		public AttackGoal() {
			super(SquallGolemEntity.this, 1.25D, false);
		}

		protected double getAttackReachSqr(LivingEntity p_179512_1_) {
			float f = SquallGolemEntity.this.getBbWidth() - 0.1F;
			return f * 1.8F * f * 1.8F + p_179512_1_.getBbWidth();
		}
	}

	class DoNothingGoal extends Goal {
		public DoNothingGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
		}

		public boolean canUse() {
			return !SquallGolemEntity.this.getActivate();
		}

		@Override
		public void tick() {
			SquallGolemEntity.this.setDeltaMovement(0, SquallGolemEntity.this.getDeltaMovement().y, 0);
		}
	}

	class Activate extends Goal {
		public Activate() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
		}

		public boolean canUse() {
			return attackID == GOLEM_ACTIVATE;
		}

		@Override
		public boolean canContinueToUse() {
			// animation tick
			return attackTimer < 60;
		}

		@Override
		public void start() {
			setAttackID(GOLEM_ACTIVATE);
			SquallGolemEntity.this.playSound(ModSoundEvents.SQUALL_GOLEM_OPEN.get(), 1.0F, 1F);
		}

		@Override
		public void tick() {
			SquallGolemEntity.this.setDeltaMovement(0, SquallGolemEntity.this.getDeltaMovement().y, 0);
		}

		@Override
		public void stop() {
			setAttackID(0);
		}
	}

	class Deactivate extends Goal {
		public Deactivate() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
		}

		public boolean canUse() {
			return attackID == GOLEM_DEACTIVATE;
		}

		@Override
		public boolean canContinueToUse() {
			// animation tick
			return attackTimer < 80;
		}

		@Override
		public void start() {
			setAttackID(GOLEM_DEACTIVATE);
			SquallGolemEntity.this.playSound(ModSoundEvents.SQUALL_GOLEM_OFF.get(), 1.0F, 1F);
		}

		@Override
		public void tick() {
			SquallGolemEntity.this.setDeltaMovement(0, SquallGolemEntity.this.getDeltaMovement().y, 0);
		}

		@Override
		public void stop() {
			setAttackID(0);
		}
	}

	class MeleeGoal extends Goal {
		public MeleeGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			return attackID == STOMP_ATTACK && (SquallGolemEntity.this.cd <= 0);
		}

		@Override
		public boolean canContinueToUse() {
			// animation tick
			return attackTimer < 34;
		}

		@Override
		public void start() {
			if (SquallGolemEntity.this.getTarget() != null)
				SquallGolemEntity.this.getLookControl().setLookAt(SquallGolemEntity.this.getTarget(), 30, 30);
			setMeleeAttacking(true);
			setAttackID(STOMP_ATTACK);
		}

		@Override
		public void tick() {
			SquallGolemEntity.this.setDeltaMovement(0, SquallGolemEntity.this.getDeltaMovement().y, 0);
			LivingEntity target = SquallGolemEntity.this.getTarget();
			if (SquallGolemEntity.this.attackTimer < 15 && target != null) {
				SquallGolemEntity.this.lookAt(target, 15.0F, 15.0F);
			} else {
				SquallGolemEntity.this.setYRot(SquallGolemEntity.this.yRotO);
			}
			if (SquallGolemEntity.this.attackTimer == 12) {
				SquallGolemEntity.this.playSound(ModSoundEvents.SQUALL_GOLEM_ATTACK.get(), 2.0F, 1F);
			}
			if (SquallGolemEntity.this.attackTimer == 30) {
				AreaAttack(5, 5, 5, 5, 60, 1.0F);
			}
		}

		private void AreaAttack(float range, float X, float Y, float Z, float arc, float damage) {
			for (LivingEntity entityHit : level.getEntitiesOfClass(LivingEntity.class,
					getBoundingBox().inflate(X, Y, Z))) {
				float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - SquallGolemEntity.this.getZ(),
						entityHit.getX() - SquallGolemEntity.this.getX()) * (180 / Math.PI) - 90) % 360);
				float entityAttackingAngle = SquallGolemEntity.this.yBodyRot % 360;
				if (entityHitAngle < 0) {
					entityHitAngle += 360;
				}
				if (entityAttackingAngle < 0) {
					entityAttackingAngle += 360;
				}
				float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
				float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - SquallGolemEntity.this.getZ())
						* (entityHit.getZ() - SquallGolemEntity.this.getZ())
						+ (entityHit.getX() - SquallGolemEntity.this.getX())
								* (entityHit.getX() - SquallGolemEntity.this.getX()));
				if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2)
						|| (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
					if (!isAlliedTo(entityHit) && !(entityHit == SquallGolemEntity.this)) {
						entityHit.hurt(DamageSource.mobAttack(SquallGolemEntity.this),
								(float) SquallGolemEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage);

						SquallGolemEntity v = SquallGolemEntity.this;
						float attackKnockback = (float) SquallGolemEntity.this
								.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
						double ratioX = (double) Mth.sin(v.getYRot() * ((float) Math.PI / 180F));
						double ratioZ = (double) (-Mth.cos(v.getYRot() * ((float) Math.PI / 180F)));
						double knockbackReduction = 0.35D;
						entityHit.hurt(DamageSource.mobAttack(v), damage);
						this.forceKnockback(entityHit, attackKnockback * 0.8F, ratioX, ratioZ, knockbackReduction);
						entityHit.setDeltaMovement(entityHit.getDeltaMovement().add(0, 0.3333333, 0));
					}
				}
			}
		}

		@Override
		public void stop() {
			setMeleeAttacking(false);
			setAttackID(0);
			SquallGolemEntity.this.cd = 25;
		}

		private void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ,
				double knockbackResistanceReduction) {
			LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
			if (event.isCanceled())
				return;
			strength = event.getStrength();
			ratioX = event.getRatioX();
			ratioZ = event.getRatioZ();
			strength = (float) ((double) strength * (1.0D
					- attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
			if (!(strength <= 0.0F)) {
				attackTarget.hasImpulse = true;
				Vec3 vector3d = attackTarget.getDeltaMovement();
				Vec3 vector3d1 = (new Vec3(ratioX, 0.0D, ratioZ)).normalize().scale((double) strength);
				attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x,
						attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double) strength) : vector3d.y,
						vector3d.z / 2.0D - vector3d1.z);
			}
		}

	}

}