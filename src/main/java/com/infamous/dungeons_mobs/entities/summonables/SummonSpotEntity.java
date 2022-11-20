package com.infamous.dungeons_mobs.entities.summonables;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class SummonSpotEntity extends Entity implements IAnimatable {

	private static final EntityDataAccessor<Integer> SUMMON_TYPE = SynchedEntityData.defineId(SummonSpotEntity.class,
			EntityDataSerializers.INT);
	
	public int lifeTime = 0;
	
	public Entity summonedEntity = null;
	
	AnimationFactory factory = GeckoLibUtil.createFactory(this);
	
	public int mobSpawnRotation;
	
    public SummonSpotEntity(Level worldIn) {
        super(ModEntityTypes.SUMMON_SPOT.get(), worldIn);
    }
    
	public SummonSpotEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
		super(p_i48580_1_, p_i48580_2_);
	}
	
	@Override
	public void baseTick() {
		super.baseTick();
		
		this.lifeTime ++;
		
		if (!this.level.isClientSide && this.lifeTime == this.getSummonTime() && this.summonedEntity != null) {
			summonedEntity.moveTo(this.blockPosition(), 0.0F, 0.0F);
			summonedEntity.setYBodyRot(this.random.nextInt(360));
			((ServerLevel)this.level).addFreshEntityWithPassengers(summonedEntity);
		}
		
		if (!this.level.isClientSide && this.lifeTime >= this.getDespawnTime()) {
			this.remove(RemovalReason.DISCARDED);
		}
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(SUMMON_TYPE, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_70037_1_) {
		this.setSummonType(p_70037_1_.getInt("SummonType"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_213281_1_) {
		p_213281_1_.putInt("SummonType", this.getSummonType());
	}
	
	public int getSummonType() {
		return Mth.clamp(this.entityData.get(SUMMON_TYPE), 0, 3);
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
    		event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_summon_spot_summon", LOOP));    
    	} else if (this.getSummonType() == 1) {
    		event.getController().setAnimation(new AnimationBuilder().addAnimation("wildfire_summon_spot_summon", LOOP));    
    	} else if (this.getSummonType() == 2) {
    		event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_summon_spot_summon", LOOP));    
    	} else if (this.getSummonType() == 3) {
    		event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_summon_spot_summon", LOOP));   
    	} else {
 
    	}
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
