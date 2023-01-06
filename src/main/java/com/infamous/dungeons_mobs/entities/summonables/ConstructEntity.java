package com.infamous.dungeons_mobs.entities.summonables;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("EntityConstructor")
public abstract class ConstructEntity extends PathfinderMob {
	public static final EntityDataAccessor<Integer> LIFE_TICKS = SynchedEntityData.defineId(ConstructEntity.class,
			EntityDataSerializers.INT);
	public Direction directionToFace = null;
	private LivingEntity caster;
	private UUID casterUuid;

	protected ConstructEntity(EntityType<? extends ConstructEntity> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
	}

	protected ConstructEntity(EntityType<? extends ConstructEntity> entityTypeIn, Level worldIn, double x, double y,
			double z, LivingEntity casterIn, int lifeTicksIn) {
		this(entityTypeIn, worldIn);
		this.setLifeTicks(lifeTicksIn);
		this.setCaster(casterIn);
		this.setPos(x, y, z);
	}

	public static boolean canVehicleCollide(Entity p_242378_0_, Entity p_242378_1_) {
		return (p_242378_1_.canBeCollidedWith() || p_242378_1_.isPushable())
				&& !p_242378_0_.isPassengerOfSameVehicle(p_242378_1_);
	}

	@Nullable
	public LivingEntity getCaster() {
		if (this.caster == null && this.casterUuid != null && this.level instanceof ServerLevel) {
			Entity entity = ((ServerLevel) this.level).getEntity(this.casterUuid);
			if (entity instanceof LivingEntity) {
				this.caster = (LivingEntity) entity;
			}
		}

		return this.caster;
	}

	public void setCaster(@Nullable LivingEntity caster) {
		this.caster = caster;
		this.casterUuid = caster == null ? null : caster.getUUID();
	}

	@Override
	public void push(double p_70024_1_, double p_70024_3_, double p_70024_5_) {

	}

	@Override
	public void move(MoverType p_213315_1_, Vec3 p_213315_2_) {

	}

	public boolean canCollideWith(Entity p_241849_1_) {
		return canVehicleCollide(this, p_241849_1_);
	}

	public boolean canBeCollidedWith() {
		return true;
	}

	public boolean isPushable() {
		return false;
	}

	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		if (p_70097_1_ == DamageSource.OUT_OF_WORLD) {
			return super.hurt(p_70097_1_, p_70097_2_);
		} else {
			return false;
		}
	}

	public void faceDirection(Direction directionToFace) {

		float rotationAmount = 0;

		if (directionToFace == Direction.NORTH) {
			rotationAmount = 90.0F;
		}

		if (directionToFace == Direction.SOUTH) {
			rotationAmount = -90.0F;
		}

		if (directionToFace == Direction.EAST) {
			rotationAmount = 0.0F;
		}

		if (directionToFace == Direction.WEST) {
			rotationAmount = 180.0F;
		}

		this.setYRot(rotationAmount);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */

	/**
	 * Returns true if it's possible to attack this entity with an item.
	 */
	public boolean isAttackable() {
		return false;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.setLifeTicks(compound.getInt("LifeTicks"));
		if (compound.hasUUID("Owner")) {
			this.casterUuid = compound.getUUID("Owner");
		}

	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("LifeTicks", this.getLifeTicks());
		if (this.casterUuid != null) {
			compound.putUUID("Owner", this.casterUuid);
		}
	}

	public int getLifeTicks() {
		return this.entityData.get(LIFE_TICKS);
	}

	public void setLifeTicks(int p_189794_1_) {
		this.entityData.set(LIFE_TICKS, p_189794_1_);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(LIFE_TICKS, 0);
	}

	public void handleExistence() {
		this.updateInWaterStateAndDoFluidPushing(); // handles being in water
	}

	public void handleExpiration() {
		this.remove(RemovalReason.DISCARDED);
	}

	/**
	 * Called to update the entity's position/logic.
	 */

	@Override
	public void baseTick() {
		// super.tick();

		// this.faceDirection(this.directionToFace);

		if (this.getLifeTicks() > 100) {
			List<Entity> v = this.level.getEntities(this, this.getBoundingBox());
			for (Entity entity : v) {
				if (entity != this && entity instanceof ConstructEntity) {
					this.remove(RemovalReason.DISCARDED);
					break;
				}
			}
		}

		this.setLifeTicks(this.getLifeTicks() - 1);
		if (!this.level.isClientSide() && this.getLifeTicks() <= 0) {
			this.handleExpiration();
		} else {
			this.handleExistence();
		}
	}

	public void spawnAreaDamage() {
		AreaDamageEntity areaDamage = AreaDamageEntity.spawnAreaDamage(this.level, this.position(), this, 2.5F,
				DamageSource.mobAttack(this), 0.0F, 1.25F, 0.25F, 0.25F, 5, false, false, 0.75, 0.25, false, 0, 1);
		this.level.addFreshEntity(areaDamage);
	}
}
