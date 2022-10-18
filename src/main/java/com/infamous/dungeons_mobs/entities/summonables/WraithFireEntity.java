package com.infamous.dungeons_mobs.entities.summonables;

import java.util.List;

import com.infamous.dungeons_mobs.interfaces.ITrapsTarget;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class WraithFireEntity extends Entity implements IAnimatable, IAnimationTickable {

	public int lifeTime;
	
    AnimationFactory factory = new AnimationFactory(this);
    
    public Entity owner;
    
	public int textureChange = 0;
	
    public WraithFireEntity(EntityType<? extends WraithFireEntity> type, World world) {
        super(type, world);
    }
    
    @Override
    public void baseTick() {
    	super.baseTick();
    	 
    	textureChange ++;
    	
    	this.setYBodyRot(0);
    	
    	if (this.lifeTime == 1) {
    		this.playSound(ModSoundEvents.WRAITH_FIRE.get(), 1.25F, this.random.nextFloat() * 0.7F + 0.3F);
    	}
    	
        if (this.random.nextInt(24) == 0 && !this.isSilent()) {
            this.level.playLocalSound(this.getX() + 0.5D, this.getY() + 0.5D, this.getZ() + 0.5D, SoundEvents.GENERIC_BURN, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
         }
    	
    	if (!this.level.isClientSide) {
    		
    		if (this.isInWaterOrBubble()) {
    			this.remove();
    			this.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);
    		}
    		
    		if (this.isInRain()) {
    			if (this.random.nextInt(40) == 0) {
    				this.remove();
    				this.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);
    			}
    		}
    		
	    	this.lifeTime ++;
	    	
	    	if (this.lifeTime >= 82) {
	    		this.remove();
	    	}
	    	
	    	if (this.isBurning()) {
		    	List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), null);
				if (!list.isEmpty()) {
					for (Entity entity : list) {
						if (entity instanceof LivingEntity && this.canHarmEntity(((LivingEntity)entity))) {
					    	entity.hurt(DamageSource.IN_FIRE, 4.0F);
					    	entity.setSecondsOnFire(4);
						}
					}
				}
	    	}
    	}
    }
    
    public boolean isInRain() {
        BlockPos blockpos = this.blockPosition();
        return this.level.isRainingAt(blockpos) || this.level.isRainingAt(new BlockPos((double)blockpos.getX(), this.getBoundingBox().maxY, (double)blockpos.getZ()));
     }
    
    public boolean isBurning() {
    	return this.lifeTime >= 20 && this.lifeTime <= 70;
    }
    
    @Override
    public int tickTimer() {
    	return this.tickCount;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		event.getController().setAnimation(new AnimationBuilder().addAnimation("wraith_fire_burn", EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
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
    
	public boolean canHarmEntity(Entity target) {
		return this.owner != null && this.owner instanceof MobEntity ? ((MobEntity)this.owner).getTarget() == target : !target.fireImmune();
	}
}
