package com.infamous.dungeons_mobs.entities.summonables;

import java.util.List;

import com.infamous.dungeons_mobs.mod.ModDamageSources;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class TridentStormEntity extends Entity implements IAnimatable, IAnimationTickable {

	AnimationFactory factory = new AnimationFactory(this);
	
	public int lifeTime;
	public Entity owner;
	
    public TridentStormEntity(EntityType<? extends TridentStormEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
    }

	@Override
	public int tickTimer() {
		return this.tickCount;
	}

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
    	event.getController().setAnimation(new AnimationBuilder().addAnimation("trident_storm_strike", true));       
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    
    @Override
    public void baseTick() {
    	super.baseTick();
    	
    	this.refreshDimensions();
    	
			List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), null);
			if (!list.isEmpty() && !this.level.isClientSide) {
				for (Entity entity : list) {
					if(entity instanceof LivingEntity){
	                    LivingEntity livingEntity = (LivingEntity)entity;
						if (this.lifeTime >= 80 && this.lifeTime <= 90) {
							if (this.owner != null) {
								if (livingEntity != this.owner) {
									livingEntity.hurt(ModDamageSources.summonedTridentStorm(this, owner), 20); 
								}
							} else {
		                        livingEntity.hurt(ModDamageSources.tridentStorm(this), 20); 
							}
		                }
					}
					
				}
			}
    	
    	this.lifeTime ++;
    	
    	if (this.lifeTime == 80) {
    		this.playSound(ModSoundEvents.DROWNED_NECROMANCER_TRIDENT_STORM_HIT.get(), 3.0F, 1.0F);
    	}
    			
    	if (this.lifeTime >= 500 && !this.level.isClientSide) {
    		this.remove();
    	}
    }

	@Override
	protected void defineSynchedData() {

	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
		
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
