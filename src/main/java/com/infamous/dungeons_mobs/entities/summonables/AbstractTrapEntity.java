package com.infamous.dungeons_mobs.entities.summonables;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.interfaces.ITrapsTarget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;

import java.util.List;

public abstract class AbstractTrapEntity extends Entity implements IAnimatable {
	
	public int spawnAnimationTick = this.getSpawnAnimationLength();
	public int decayAnimationTick;
	
	public int lifeTime;
	
	public Entity owner;
	
	public boolean isTrappingMob;
	
	public List<Entity> trappedEntities = Lists.newArrayList();
	public List<Vec3> trappedEntityPositions = Lists.newArrayList();
	
    public AbstractTrapEntity(EntityType<? extends AbstractTrapEntity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }
    
    @Override
    public void baseTick() {
    	super.baseTick();
    	this.tickDownAnimTimers();    	  	
    	this.increaseLifeTime();
    	this.performBasicTrapFunctions();
    }
    
    public void increaseLifeTime() {
    	this.lifeTime++;
    	
    	if (!this.level.isClientSide) {
    		if (this.lifeTime == this.timeToDecay()) {
    			this.decayAnimationTick = this.getDecayAnimationLength();
    			this.level.broadcastEntityEvent(this, (byte) 2);
    		}
    		
    		if (this.decayAnimationTick == 2) {
    			this.remove(RemovalReason.DISCARDED);
    		}
    	}
    }
    
    public void performBasicTrapFunctions() {
    	if (!this.level.isClientSide) {   
    		boolean isTrapping = false;
    		if (this.canTrap()) {
    	    	List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), Entity::isAlive);
    			if (!list.isEmpty()) {
    				for (Entity entity : list) {
    					if (entity instanceof LivingEntity && this.canTrapEntity(((LivingEntity)entity))) {
	    					if (!trappedEntities.contains(entity)) {
	    						trappedEntities.add(entity);
	    						trappedEntityPositions.add(entity.position());
	    					}
	    					
	    			    	if (this.owner != null && this.owner instanceof Mob && ((Mob)this.owner).getTarget() != null && ((Mob)this.owner).getTarget().isAlive() && ((Mob)this.owner).getTarget() == entity && this.owner instanceof ITrapsTarget) {
	    			    		((ITrapsTarget)this.owner).setTargetTrapped(true, true);
	    			    	}
	    			    	
	    			    	isTrapping = true;
    					}
    				}
    			}
    		}
    		
    		for (Entity entity : trappedEntities) {
					int entityIndex = trappedEntities.indexOf(entity);
					Vec3 entityTrappedPosition = trappedEntityPositions.get(entityIndex);
					entity.fallDistance = 0;
					if (this.distanceBetweenVector3ds(entity.position(), entityTrappedPosition) > 0.1) {
						entity.teleportTo(entityTrappedPosition.x, entityTrappedPosition.y, entityTrappedPosition.z);
					}
    		}
    	
    		this.isTrappingMob = isTrapping;
    	}
    }
    
    public float distanceBetweenVector3ds(Vec3 position, Vec3 positionToGetDistanceTo) {
        float f = (float)(position.x - positionToGetDistanceTo.x);
        float f1 = (float)(position.y - positionToGetDistanceTo.y);
        float f2 = (float)(position.z - positionToGetDistanceTo.z);
        return Mth.sqrt(f * f + f1 * f1 + f2 * f2);
     }
    
    public void tickDownAnimTimers() {
    	if (this.spawnAnimationTick > 0) {
    		this.spawnAnimationTick--;
    	}
    	
    	if (this.decayAnimationTick > 0) {
    		this.decayAnimationTick--;
    	}
    }
    
    public abstract int getSpawnAnimationLength();
    public abstract int getDecayAnimationLength();
    
    public int timeToDecay() {
    	return 80;
    }
    
    public boolean canTrap() {
    	return this.spawnAnimationTick <= 0;
    }
    
    public boolean canTrapEntity(LivingEntity entity) {
    	return !entity.isSpectator();
    }
    
    @Override
    public void handleEntityEvent(byte p_70103_1_) {
    	if (p_70103_1_ == 1) {
    		this.spawnAnimationTick = this.getSpawnAnimationLength();
    	} else if (p_70103_1_ == 2) {
    		this.decayAnimationTick = this.getDecayAnimationLength();
    	} else {
    		super.handleEntityEvent(p_70103_1_);
    	}
    }

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_70037_1_) {
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_213281_1_) {
		
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
    
}
