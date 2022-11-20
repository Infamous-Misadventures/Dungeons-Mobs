package com.infamous.dungeons_mobs.entities.summonables;

import com.infamous.dungeons_mobs.tags.CustomTags;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SimpleTrapEntity extends AbstractTrapEntity {

	private static final DataParameter<Integer> TRAP_TYPE = EntityDataManager.defineId(SimpleTrapEntity.class,
			DataSerializers.INT);	
	
	AnimationFactory factory = new AnimationFactory(this);
	
    public SimpleTrapEntity(EntityType<? extends SimpleTrapEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }
    
	@Override
	protected void defineSynchedData() {
		this.entityData.define(TRAP_TYPE, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
		this.setTrapType(p_70037_1_.getInt("TrapType"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
		p_213281_1_.putInt("TrapType", this.getTrapType());
	}
	
	public int getTrapType() {
		return MathHelper.clamp(this.entityData.get(TRAP_TYPE), 0, 1);
	}

	public void setTrapType(int attached) {
		this.entityData.set(TRAP_TYPE, attached);
	}

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }


    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
    	if (this.getTrapType() == 0) {
    		if (this.spawnAnimationTick > 0) {
        		event.getController().setAnimation(new AnimationBuilder().addAnimation("web_trap_spawn", true)); 
    		} else if (this.decayAnimationTick > 0) {
    			event.getController().setAnimation(new AnimationBuilder().addAnimation("vine_trap_decay", true)); 
    		} else {
    			event.getController().setAnimation(new AnimationBuilder().addAnimation("vine_trap_idle", true)); 
    		}   
    	} else if (this.getTrapType() == 1) {
    		if (this.spawnAnimationTick > 0) {
        		event.getController().setAnimation(new AnimationBuilder().addAnimation("vine_trap_spawn", true)); 
    		} else if (this.decayAnimationTick > 0) {
    			event.getController().setAnimation(new AnimationBuilder().addAnimation("vine_trap_decay", true)); 
    		} else {
    			event.getController().setAnimation(new AnimationBuilder().addAnimation("vine_trap_idle", true)); 
    		}    
    	} else {
 
    	}
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

	@Override
	public int getSpawnAnimationLength() {
		return this.getTrapType() == 1 ? 8 : 7;
	}

	@Override
	public int getDecayAnimationLength() {
		return 25;
	}
	
	@Override
	public boolean canTrapEntity(LivingEntity entity) {
		if (this.getTrapType() == 0) {
			return super.canTrapEntity(entity) && entity.getMobType() != CreatureAttribute.ARTHROPOD;
		} else if (this.getTrapType() == 1) {
			return super.canTrapEntity(entity) && !entity.getType().is(CustomTags.PLANT_MOBS);
		} else {
			return super.canTrapEntity(entity);
		}
	} 
}
