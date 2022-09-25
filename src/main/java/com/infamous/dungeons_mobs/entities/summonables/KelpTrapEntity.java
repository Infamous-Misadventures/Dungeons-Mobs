package com.infamous.dungeons_mobs.entities.summonables;

import java.util.List;

import com.infamous.dungeons_mobs.interfaces.ITrapsTarget;
import com.infamous.dungeons_mobs.tags.CustomTags;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class KelpTrapEntity extends AbstractTrapEntity {
		
	private static final DataParameter<Boolean> PULLING = EntityDataManager.defineId(KelpTrapEntity.class,
			DataSerializers.BOOLEAN);
	
	AnimationFactory factory = new AnimationFactory(this);
	
	public int ensnareAnimationTick;
	public int ensnareAnimationLength = 5;
	
	public int trappedMobTime;
	
	public int bubbleAudioInterval;
	
    public KelpTrapEntity(EntityType<? extends KelpTrapEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }


    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
    		if (this.ensnareAnimationTick > 0) {
    			event.getController().setAnimation(new AnimationBuilder().addAnimation("kelp_trap_ensnare", true)); 
    		} else if (this.spawnAnimationTick > 0) {
        		event.getController().setAnimation(new AnimationBuilder().addAnimation("kelp_trap_spawn", true)); 
    		} else if (this.decayAnimationTick > 0) {
    			event.getController().setAnimation(new AnimationBuilder().addAnimation("vine_trap_decay", true)); 
    		} else {
    			if (this.isPulling()) {
    				event.getController().setAnimation(new AnimationBuilder().addAnimation("kelp_trap_idle_pulling", true)); 
    			} else {
    				event.getController().setAnimation(new AnimationBuilder().addAnimation("vine_trap_idle", true)); 
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
		return super.canTrapEntity(entity) && !entity.getType().is(CustomTags.PLANT_MOBS);
	} 
	
	@Override
	protected void defineSynchedData() {
		this.entityData.define(PULLING, true);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
		this.setPulling(p_70037_1_.getBoolean("Pulling"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
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
			this.bubbleAudioInterval --;
		}
		
		if (!this.level.isClientSide && this.isPulling()) {
			List<Entity> list = this.level.getEntities(this,
					this.getBoundingBox().inflate(0, this.waterBlocksAbove(), 0), null);
			for (int i = 0; i < this.waterBlocksAbove(); i++) {
				((ServerWorld)this.level).sendParticles(ParticleTypes.CURRENT_DOWN, this.getRandomX(0.25), this.getY() + i + 0.8D, this.getRandomZ(0.25) - 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
			}
			if (!list.isEmpty()) {
				for (Entity entity : list) {
					if (entity.getY() > this.getY() && ((entity instanceof LivingEntity && this.canTrapEntity(((LivingEntity)entity))) || !(entity instanceof LivingEntity))) {
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
			this.ensnareAnimationTick --;
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
				this.trappedMobTime ++;
				
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
    			this.remove();
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