package com.infamous.dungeons_mobs.entities.ender;

import java.util.EnumSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import software.bernie.geckolib3.core.IAnimatable;

public abstract class AbstractEnderlingEntity extends MonsterEntity implements IAnimatable {

	public static final DataParameter<Integer> ATTACKING = EntityDataManager.defineId(AbstractEnderlingEntity.class,
			DataSerializers.INT);

	public static final DataParameter<Integer> RUNNING = EntityDataManager.defineId(AbstractEnderlingEntity.class,
			DataSerializers.INT);

	private static final DataParameter<Boolean> DATA_STARED_AT = EntityDataManager
			.defineId(AbstractEnderlingEntity.class, DataSerializers.BOOLEAN);

	private final EntityPredicate followPredicate = (new EntityPredicate()).range(50.0D).allowUnseeable()
			.ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

	protected AbstractEnderlingEntity(EntityType<? extends AbstractEnderlingEntity> p_i48553_1_, World p_i48553_2_) {
		super(p_i48553_1_, p_i48553_2_);
		this.maxUpStep = 1.0F;
		this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.ATTACK_DAMAGE, 7.0D)
				.add(Attributes.FOLLOW_RANGE, 32.0D);
	}

	public void setTarget(@Nullable LivingEntity p_70624_1_) {
		if (p_70624_1_ == null) {
			this.entityData.set(DATA_STARED_AT, false);
		} else {

		}

		super.setTarget(p_70624_1_); // Forge: Moved down to allow event handlers to write data manager values.
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ATTACKING, 0);
		this.entityData.define(RUNNING, 0);
		this.entityData.define(DATA_STARED_AT, false);
	}

	public boolean hasBeenStaredAt() {
		return this.entityData.get(DATA_STARED_AT);
	}

	public void setBeingStaredAt() {
		this.entityData.set(DATA_STARED_AT, true);
	}

	private boolean isLookingAtMe(PlayerEntity p_70821_1_) {
		ItemStack itemstack = p_70821_1_.inventory.armor.get(3);
		if (itemstack.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
			return false;
		} else {
			Vector3d vector3d = p_70821_1_.getViewVector(1.0F).normalize();
			Vector3d vector3d1 = new Vector3d(this.getX() - p_70821_1_.getX(), this.getEyeY() - p_70821_1_.getEyeY(),
					this.getZ() - p_70821_1_.getZ());
			double d0 = vector3d1.length();
			vector3d1 = vector3d1.normalize();
			double d1 = vector3d.dot(vector3d1);
			return d1 > 1.0D - 0.025D / d0 ? p_70821_1_.canSee(this) : false;
		}
	}

	// public static boolean checkMobSpawnRules(EntityType<? extends MobEntity>
	// p_223315_0_, IWorld p_223315_1_, SpawnReason p_223315_2_, BlockPos
	// p_223315_3_, Random p_223315_4_) {
	// return super.checkMobSpawnRules(p_223315_0_, p_223315_1_, p_223315_2_,
	// p_223315_3_, p_223315_4_);
	// }

	@SuppressWarnings("unchecked")
	public boolean checkSpawnRules(IWorld p_213380_1_, SpawnReason p_213380_2_) {
		return (p_213380_2_ == SpawnReason.NATURAL
				&& this.level.getBiomeName(this.blockPosition()).get() == Biomes.THE_END) ? false
						: super.checkMobSpawnRules(((EntityType<? extends MobEntity>) this.getType()), p_213380_1_,
								p_213380_2_, this.blockPosition(), this.random);
	}

	public void baseTick() {
		super.baseTick();

		if (this.isAttacking() > 0) {
			this.setAttacking(this.isAttacking() - 1);
		}

		if (this.isRunning() > 0) {
			this.setRunning(this.isRunning() - 1);
		}
	}

	public int isAttacking() {
		return this.entityData.get(ATTACKING);
	}

	public void setAttacking(int p_189794_1_) {
		this.entityData.set(ATTACKING, p_189794_1_);
	}

	public int isRunning() {
		return this.entityData.get(RUNNING);
	}

	public void setRunning(int p_189794_1_) {
		this.entityData.set(RUNNING, p_189794_1_);
	}

	public void aiStep() {
		if (this.level.isClientSide) {
			for (int i = 0; i < 2; ++i) {
				this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D), this.getRandomY() - 0.25D,
						this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(),
						(this.random.nextDouble() - 0.5D) * 2.0D);
			}
		}

		this.jumping = false;

		super.aiStep();
	}

	public boolean isSensitiveToWater() {
		return true;
	}

	protected void customServerAiStep() {
		if (this.shouldTeleportInDay() && this.level.isDay() && this.random.nextInt(600) == 0) {
			float f = this.getBrightness();
			if (f > 0.5F && this.level.canSeeSky(this.blockPosition())
					&& this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
				this.setTarget((LivingEntity) null);
				this.teleport();
			}
		}

		super.customServerAiStep();
	}

	public boolean shouldTeleportInDay() {
		return false;
	}

	protected boolean teleport() {
		if (!this.level.isClientSide() && this.isAlive()) {
			double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
			double d1 = this.getY() + (double) (this.random.nextInt(64) - 32);
			double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
			return this.teleport(d0, d1, d2);
		} else {
			return false;
		}
	}

	private boolean teleportTowards(Entity p_70816_1_) {
		Vector3d vector3d = new Vector3d(this.getX() - p_70816_1_.getX(), this.getY(0.5D) - p_70816_1_.getEyeY(),
				this.getZ() - p_70816_1_.getZ());
		vector3d = vector3d.normalize();
		double d0 = 16.0D;
		double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.x * 16.0D;
		double d2 = this.getY() + (double) (this.random.nextInt(16) - 8) - vector3d.y * 16.0D;
		double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.z * 16.0D;
		return this.teleport(d1, d2, d3);
	}

	protected boolean teleport(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_70825_1_, p_70825_3_, p_70825_5_);

		while (blockpos$mutable.getY() > 0
				&& !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
			blockpos$mutable.move(Direction.DOWN);
		}

		BlockState blockstate = this.level.getBlockState(blockpos$mutable);
		boolean flag = blockstate.getMaterial().blocksMotion();
		boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
		if (flag && !flag1) {
			net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory
					.onEnderTeleport(this, p_70825_1_, p_70825_3_, p_70825_5_);
			if (event.isCanceled())
				return false;
			boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
			if (!this.isSilent()) {
				this.level.playSound((PlayerEntity) null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT,
						this.getSoundSource(), 1.0F, 1.0F);
				this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}
			return flag2;
		} else {
			return false;
		}
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENDERMAN_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.ENDERMAN_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENDERMAN_DEATH;
	}

	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		if (this.isInvulnerableTo(p_70097_1_)) {
			return false;
		} else {
			boolean flag = super.hurt(p_70097_1_, p_70097_2_);
			if (!this.level.isClientSide() && !(p_70097_1_.getEntity() instanceof LivingEntity)
					&& this.random.nextInt(2) == 0) {
				this.teleport();
			}

			return flag;
		}
	}

	class AttackGoal extends MeleeAttackGoal {

		public final EntityPredicate slimePredicate = (new EntityPredicate()).range(20.0D).allowUnseeable()
				.ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

		public AttackGoal(double speed) {
			super(AbstractEnderlingEntity.this, speed, true);
		}

		public boolean canContinueToUse() {
			return super.canContinueToUse();
		}

		protected double getAttackReachSqr(LivingEntity p_179512_1_) {
			return (double) (this.mob.getBbWidth() * 3.0F * this.mob.getBbWidth() * 3.0F + p_179512_1_.getBbWidth());
		}

		public void tick() {
			super.tick();

			AbstractEnderlingEntity.this.setRunning(10);
		}

		protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
			double d0 = this.getAttackReachSqr(p_190102_1_);
			if (p_190102_2_ <= d0 && this.isTimeToAttack()) {
				this.resetAttackCooldown();
				this.mob.doHurtTarget(p_190102_1_);
			} else if (p_190102_2_ <= d0 * 1.5D) {
				if (this.isTimeToAttack()) {
					this.resetAttackCooldown();
				}

				if (this.getTicksUntilNextAttack() <= 30) {
					AbstractEnderlingEntity.this.setAttacking(30);
				}
			} else {
				this.resetAttackCooldown();
			}
		}
	}

	public class EnderlingTargetGoal<T extends LivingEntity> extends TargetGoal {
		protected final Class<T> targetType;
		protected final int randomInterval;
		protected LivingEntity target;
		protected EntityPredicate targetConditions;

		public EnderlingTargetGoal(MobEntity p_i50313_1_, Class<T> p_i50313_2_, boolean p_i50313_3_) {
			this(p_i50313_1_, p_i50313_2_, p_i50313_3_, false);
		}

		public EnderlingTargetGoal(MobEntity p_i50314_1_, Class<T> p_i50314_2_, boolean p_i50314_3_,
				boolean p_i50314_4_) {
			this(p_i50314_1_, p_i50314_2_, 10, p_i50314_3_, p_i50314_4_, (Predicate<LivingEntity>) null);
		}

		public EnderlingTargetGoal(MobEntity p_i50315_1_, Class<T> p_i50315_2_, int p_i50315_3_, boolean p_i50315_4_,
				boolean p_i50315_5_, @Nullable Predicate<LivingEntity> p_i50315_6_) {
			super(p_i50315_1_, p_i50315_4_, p_i50315_5_);
			this.targetType = p_i50315_2_;
			this.randomInterval = p_i50315_3_;
			this.setFlags(EnumSet.of(Goal.Flag.TARGET));
			this.targetConditions = (new EntityPredicate()).range(this.getFollowDistance()).selector(p_i50315_6_);
		}

		public boolean canUse() {
			if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
				return false;
			} else {
				this.findTarget();
				return this.target != null;
			}
		}

		protected AxisAlignedBB getTargetSearchArea(double p_188511_1_) {
			return this.mob.getBoundingBox().inflate(p_188511_1_, 4.0D, p_188511_1_);
		}

		protected void findTarget() {
			if (this.targetType != PlayerEntity.class && this.targetType != ServerPlayerEntity.class) {
				this.target = this.mob.level.getNearestLoadedEntity(this.targetType, this.targetConditions, this.mob,
						this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(),
						this.getTargetSearchArea(this.getFollowDistance()));
			} else {
				this.target = this.mob.level.getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(),
						this.mob.getEyeY(), this.mob.getZ());
			}

		}

		public void start() {
			this.mob.setTarget(this.target);
			super.start();
		}

		public void setTarget(@Nullable LivingEntity p_234054_1_) {
			this.target = p_234054_1_;
		}
	}

	static class FindPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
		private final AbstractEnderlingEntity enderman;
		private PlayerEntity pendingTarget;
		private int aggroTime;
		private int teleportTime;
		private final EntityPredicate startAggroTargetConditions;
		private final EntityPredicate continueAggroTargetConditions = (new EntityPredicate()).allowUnseeable();

		public FindPlayerGoal(AbstractEnderlingEntity p_i241912_1_, @Nullable Predicate<LivingEntity> p_i241912_2_) {
			super(p_i241912_1_, PlayerEntity.class, 10, false, false, p_i241912_2_);
			this.enderman = p_i241912_1_;
			this.startAggroTargetConditions = (new EntityPredicate()).range(this.getFollowDistance())
					.selector((p_220790_1_) -> {
						return p_i241912_1_.isLookingAtMe((PlayerEntity) p_220790_1_);
					});
		}

		public boolean canUse() {
			this.pendingTarget = this.enderman.level.getNearestPlayer(this.startAggroTargetConditions, this.enderman);
			return this.pendingTarget != null;
		}

		public void start() {
			this.aggroTime = 5;
			this.teleportTime = 0;
			this.enderman.setBeingStaredAt();
		}

		public void stop() {
			this.pendingTarget = null;
			super.stop();
		}

		public boolean canContinueToUse() {
			if (this.pendingTarget != null) {
				if (!this.enderman.isLookingAtMe(this.pendingTarget)) {
					return false;
				} else {
					this.enderman.lookAt(this.pendingTarget, 10.0F, 10.0F);
					return true;
				}
			} else {
				return this.target != null && this.continueAggroTargetConditions.test(this.enderman, this.target) ? true
						: super.canContinueToUse();
			}
		}

		public void tick() {
			if (this.enderman.getTarget() == null) {
				super.setTarget((LivingEntity) null);
			}

			if (this.pendingTarget != null) {
				if (--this.aggroTime <= 0) {
					this.target = this.pendingTarget;
					this.pendingTarget = null;
					super.start();
				}
			} else {
				if (this.target != null && !this.enderman.isPassenger()) {
					if (this.enderman.isLookingAtMe((PlayerEntity) this.target)) {
						if (this.target.distanceToSqr(this.enderman) < 16.0D) {
							this.enderman.teleport();
						}

						this.teleportTime = 0;
					} else if (this.target.distanceToSqr(this.enderman) > 256.0D && this.teleportTime++ >= 30
							&& this.enderman.teleportTowards(this.target)) {
						this.teleportTime = 0;
					}
				}

				super.tick();
			}

		}
	}

}
