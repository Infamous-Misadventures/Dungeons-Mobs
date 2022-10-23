package com.infamous.dungeons_mobs.entities.summonables;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SummonSpotEntity extends Entity implements IAnimatable {

	private static final DataParameter<Integer> SUMMON_TYPE = EntityDataManager.defineId(SummonSpotEntity.class,
			DataSerializers.INT);
	
	public int lifeTime = 0;
	
	public Entity summonedEntity = null;
	
	AnimationFactory factory = new AnimationFactory(this);
	
	public int mobSpawnRotation;
	
    public SummonSpotEntity(World worldIn) {
        super(ModEntityTypes.SUMMON_SPOT.get(), worldIn);
    }
    
	public SummonSpotEntity(EntityType<?> p_i48580_1_, World p_i48580_2_) {
		super(p_i48580_1_, p_i48580_2_);
	}
	
	@Override
	public void baseTick() {
		super.baseTick();
		
		this.lifeTime ++;
		
		if (!this.level.isClientSide && this.lifeTime == this.getSummonTime() && this.summonedEntity != null) {
			summonedEntity.moveTo(this.blockPosition(), 0.0F, 0.0F);
			summonedEntity.setYBodyRot(this.random.nextInt(360));
			((ServerWorld)this.level).addFreshEntityWithPassengers(summonedEntity);
		}
		
		if (!this.level.isClientSide && this.lifeTime >= this.getDespawnTime()) {
			this.remove();
		}
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(SUMMON_TYPE, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
		this.setSummonType(p_70037_1_.getInt("SummonType"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
		p_213281_1_.putInt("SummonType", this.getSummonType());
	}
	
	public int getSummonType() {
		return MathHelper.clamp(this.entityData.get(SUMMON_TYPE), 0, 3);
	}

	public void setSummonType(int attached) {
		this.entityData.set(SUMMON_TYPE, attached);
	}
	
	public int getDespawnTime() {
		if (this.getSummonType() == 0) {
			return 18;
		} else if (this.getSummonType() == 1) {
			return 18;
		} else if (this.getSummonType() == 2) {
			return 18;
		} else if (this.getSummonType() == 3) {
			return 18;
		} else {
			return 2;
		}
	}
	
	public int getSummonTime() {
		if (this.getSummonType() == 0) {
			return 10;
		} else if (this.getSummonType() == 1) {
			return 10;
		} else if (this.getSummonType() == 2) {
			return 10;
		} else if (this.getSummonType() == 3) {
			return 10;
		} else {
			return 1;
		}
	}
	
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
    }


    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
    	if (this.getSummonType() == 0) {
    		event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_summon_spot_summon", true));    
    	} else if (this.getSummonType() == 1) {
    		event.getController().setAnimation(new AnimationBuilder().addAnimation("wildfire_summon_spot_summon", true));    
    	} else if (this.getSummonType() == 2) {
    		event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_summon_spot_summon", true));    
    	} else if (this.getSummonType() == 3) {
    		event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_summon_spot_summon", true));   
    	} else {
 
    	}
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
