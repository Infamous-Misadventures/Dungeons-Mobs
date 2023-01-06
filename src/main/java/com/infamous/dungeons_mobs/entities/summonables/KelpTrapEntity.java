package com.infamous.dungeons_mobs.entities.summonables;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

import java.util.List;

import com.infamous.dungeons_mobs.tags.EntityTags;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class KelpTrapEntity extends AbstractTrapEntity {

	private static final EntityDataAccessor<Boolean> PULLING = SynchedEntityData.defineId(KelpTrapEntity.class,
			EntityDataSerializers.BOOLEAN);

	AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public int ensnareAnimationTick;
	public int ensnareAnimationLength = 5;

	public int trappedMobTime;

	public int bubbleAudioInterval;

	public KelpTrapEntity(EntityType<? extends KelpTrapEntity> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.ensnareAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("kelp_trap_ensnare", LOOP));
		} else if (this.spawnAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("kelp_trap_spawn", LOOP));
		} else if (this.decayAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("vine_trap_decay", LOOP));
		} else {
			if (this.isPulling()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("kelp_trap_idle_pulling", LOOP));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("vine_trap_idle", LOOP));
			}
		}
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public int getSpawnAnimationLength() {
		return 10;
	}

	@Override
	public int getDecayAnimationLength() {
		return 25;
	}

	@Override
	public boolean canTrapEntity(LivingEntity entity) {
		return super.canTrapEntity(entity) && !entity.getType().is(EntityTags.PLANT_MOBS);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(PULLING, true);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_70037_1_) {
		this.setPulling(p_70037_1_.getBoolean("Pulling"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_213281_1_) {
		p_213281_1_.putBoolean("Pulling", this.isPulling());
	}

	public boolean isPulling() {
		return this.entityData.get(PULLING);
	}

	public void setPulling(boolean attached) {
		this.entityData.set(PULLING, attached);
	}

	@Override
	public void baseTick() {
		if (!this.level.isClientSide && this.lifeTime == 0) {
			this.setPulling(true);
		}

		if (this.bubbleAudioInterval > 0) {
			this.bubbleAudioInterval--;
		}

		if (!this.level.isClientSide && this.isPulling()) {
			List<Entity> list = this.level.getEntities(this,
					this.getBoundingBox().inflate(0, this.waterBlocksAbove(), 0), Entity::isAlive);
			for (int i = 0; i < this.waterBlocksAbove(); i++) {
				((ServerLevel) this.level).sendParticles(ParticleTypes.CURRENT_DOWN, this.getRandomX(0.25),
						this.getY() + i + 0.8D, this.getRandomZ(0.25) - 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
			}
			if (!list.isEmpty()) {
				for (Entity entity : list) {
					if (entity.getY() > this.getY()
							&& ((entity instanceof LivingEntity && this.canTrapEntity(((LivingEntity) entity)))
									|| !(entity instanceof LivingEntity))) {
						if (this.bubbleAudioInterval <= 0) {
							this.playSound(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE, 1.25F, 1.0F);
							this.bubbleAudioInterval = 40;
						}
						entity.push(0, -0.1, 0);
						entity.hurtMarked = true;
					}
				}
			}
		}

		super.baseTick();
	}

	@Override
	public void tickDownAnimTimers() {
		super.tickDownAnimTimers();
		if (this.ensnareAnimationTick > 0) {
			this.ensnareAnimationTick--;
		}
	}

	public double waterBlocksAbove() {
		double waterBlocksAbove = 0;
		for (int i = 0; i < 256; i++) {
			waterBlocksAbove = i;
			if (!this.level.getFluidState(this.blockPosition().above(i)).is(FluidTags.WATER)) {
				return waterBlocksAbove;
			}
		}
		return waterBlocksAbove;
	}

	@Override
	public void increaseLifeTime() {
		if (!this.level.isClientSide) {
			if (this.isTrappingMob) {
				this.trappedMobTime++;

				if (this.isPulling()) {
					if (this.ensnareAnimationTick <= 0) {
						this.setPulling(false);
						this.ensnareAnimationTick = this.ensnareAnimationLength;
						this.level.broadcastEntityEvent(this, (byte) 3);
					}
				}
			} else {
				this.lifeTime++;
			}

			if (this.trappedMobTime == this.timeToDecay()) {
				this.decayAnimationTick = this.getDecayAnimationLength();
				this.level.broadcastEntityEvent(this, (byte) 2);
			}

			if (this.isPulling()) {
				if (this.lifeTime == 200) {
					this.ensnareAnimationTick = this.ensnareAnimationLength;
					this.level.broadcastEntityEvent(this, (byte) 3);
				}

				if (this.lifeTime == 200 + this.ensnareAnimationLength) {
					this.decayAnimationTick = this.getDecayAnimationLength();
					this.level.broadcastEntityEvent(this, (byte) 2);
				}
			} else {
				if (this.trappedMobTime == 100) {
					this.decayAnimationTick = this.getDecayAnimationLength();
					this.level.broadcastEntityEvent(this, (byte) 2);
				}
			}

			if (this.decayAnimationTick == 2) {
				this.remove(RemovalReason.DISCARDED);
			}
		}
	}

	public boolean canTrap() {
		return true;
	}

	@Override
	public void handleEntityEvent(byte p_70103_1_) {
		if (p_70103_1_ == 3) {
			this.ensnareAnimationTick = this.ensnareAnimationLength;
		} else {
			super.handleEntityEvent(p_70103_1_);
		}
	}
}