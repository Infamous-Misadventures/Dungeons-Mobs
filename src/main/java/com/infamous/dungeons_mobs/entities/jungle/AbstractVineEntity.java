package com.infamous.dungeons_mobs.entities.jungle;

import java.util.Random;
import java.util.function.Predicate;

import com.infamous.dungeons_mobs.tags.CustomTags;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;

public abstract class AbstractVineEntity extends MobEntity implements IMob, IAnimatable {

    public static final DataParameter<Integer> LENGTH = EntityDataManager.defineId(AbstractVineEntity.class, DataSerializers.INT);
    
    public static final DataParameter<Boolean> VANISHES = EntityDataManager.defineId(AbstractVineEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> STAY_TIME = EntityDataManager.defineId(AbstractVineEntity.class, DataSerializers.INT);
    
    public static final DataParameter<Boolean> ALWAYS_OUT = EntityDataManager.defineId(AbstractVineEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SHOULD_RETRACT = EntityDataManager.defineId(AbstractVineEntity.class, DataSerializers.BOOLEAN);
    
    public static final DataParameter<Float> DETECTION_DISTANCE = EntityDataManager.defineId(AbstractVineEntity.class, DataSerializers.FLOAT);
    
    public static final DataParameter<Boolean> OUT = EntityDataManager.defineId(AbstractVineEntity.class, DataSerializers.BOOLEAN);
    
    public int lifeTime;
    
    public int burstAnimationTick;
    public int retractAnimationTick;
    
	   private final Predicate<Entity> SHOULD_BURST_FOR = (entity) -> {
		      return this.shouldBurstFor(entity);
	   };
	   
	public VinePartEntity[] subEntities = new VinePartEntity[27];
    
	protected AbstractVineEntity(EntityType<? extends AbstractVineEntity> entityType, World level) {
		super(entityType, level);
        int adjustedLength = 27;

        for (int i = 0; i < adjustedLength; i++) {
            VinePartEntity newPart = new VinePartEntity(this, 26 - i);
            this.subEntities[i] = newPart;
        }
	}
	
    protected void updateParts()
    {
        for (VinePartEntity part : subEntities) {
            part.refreshDimensions();
            movePart(part, 0, part.getYOffsetForSegment(), 0);
        }
    }

    protected void movePart(VinePartEntity part, double dX, double dY, double dZ)
    {
    	Vector3d lastPos = new Vector3d(part.getX(), part.getY(), part.getZ());

        part.setPos(this.getX() + dX, this.getY() + dY, this.getZ() + dZ);

        part.xo = lastPos.x;
        part.yo = lastPos.y;
        part.zo = lastPos.z;
        part.xOld = lastPos.x;
        part.yOld = lastPos.y;
        part.zOld = lastPos.z;
    }

    @Override
    public void aiStep()
    {
        super.aiStep();
        updateParts();
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts() {    
        return subEntities;
    }
    
    protected int decreaseAirSupply(int p_70682_1_) {
        return p_70682_1_;
     }
    
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
     }

    
	   public boolean isAlliedTo(Entity p_184191_1_) {
		      if (super.isAlliedTo(p_184191_1_)) {
		         return true;
		      } else if (p_184191_1_ instanceof LivingEntity && ((LivingEntity)p_184191_1_).getType().is(CustomTags.PLANT_MOBS)) {
		         return this.getTeam() == null && p_184191_1_.getTeam() == null;
		      } else {
		         return false;
		      }
		   }
	
	   protected void tickDeath() {
		      ++this.deathTime;
		      if (this.deathTime == this.getRetractAnimationLength()) {
		    	  this.remove();
		      }
		}

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LENGTH, 0);
        this.entityData.define(VANISHES, false);
        this.entityData.define(STAY_TIME, 0);
        this.entityData.define(ALWAYS_OUT, false);
        this.entityData.define(SHOULD_RETRACT, false);
        this.entityData.define(DETECTION_DISTANCE, 0.0F);
        this.entityData.define(OUT, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setLengthInSegments(compound.getInt("Length"));
        this.setVanishes(compound.getBoolean("Vanishes"));
        this.setStayTime(compound.getInt("StayTime"));
        this.setAlwaysOut(compound.getBoolean("AlwaysOut"));
        this.setShouldRetract(compound.getBoolean("ShouldRetract"));
        this.setDetectionDistance(compound.getFloat("DetectionDistance"));
        this.setOut(compound.getBoolean("Out"));
    }


    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Length", this.getLengthInSegments());
        compound.putBoolean("Vanishes", this.getVanishes());
        compound.putInt("StayTime", this.getStayTime());
        compound.putBoolean("AlwaysOut", this.getAlwaysOut());
        compound.putBoolean("ShouldRetract", this.getShouldRetract());
        compound.putFloat("DetectionDistance", this.getDetectionDistance());
        compound.putBoolean("Out", this.isOut());
    }
    
    public int getLengthInSegments() {
        return MathHelper.clamp(this.entityData.get(LENGTH), 1, 26);
    }
    
    public float getLengthInPixels() {
    	return this.getLengthInSegments() * 22;
    }
    
    public float getLengthInBlocks() {
    	return this.getLengthInPixels() / 16;
    }

    public void setLengthInSegments(int setTo){
        this.entityData.set(LENGTH, setTo);
    }
    
    public void setLengthInPixels(int setTo){
        this.setLengthInPixels(setTo / 22);
    }
    
    public void setLengthInBlocks(float setTo){
        this.setLengthInPixels(Math.round(setTo * 16));
    }
    
    public boolean getVanishes() {
        return this.entityData.get(VANISHES);
    }

    public void setVanishes(boolean setTo){
        this.entityData.set(VANISHES, setTo);
    }
    
    public int getStayTime() {
        return this.entityData.get(STAY_TIME);
    }

    public void setStayTime(int setTo){
        this.entityData.set(STAY_TIME, setTo);
    }
    
    public boolean getAlwaysOut() {
        return this.entityData.get(ALWAYS_OUT);
    }

    public void setAlwaysOut(boolean setTo){
        this.entityData.set(ALWAYS_OUT, setTo);
    }
    
    public boolean getShouldRetract() {
        return this.entityData.get(SHOULD_RETRACT);
    }

    public void setShouldRetract(boolean setTo){
        this.entityData.set(SHOULD_RETRACT, setTo);
    }
    
    public float getDetectionDistance() {
        return this.entityData.get(DETECTION_DISTANCE);
    }

    public void setDetectionDistance(float setTo){
        this.entityData.set(DETECTION_DISTANCE, setTo);
    }
    
    public boolean getOut() {
        return this.entityData.get(OUT);
    }

    public void setOut(boolean setTo){
        this.entityData.set(OUT, setTo);
    }
    
    public abstract int getBurstAnimationLength();
    public abstract int getRetractAnimationLength();
    
    public float getExtraHitboxY() {
    	return 0;
    }
    
	@Override
	public void playAmbientSound() {
	      SoundEvent soundeventVocal = this.getAmbientSound();
	      SoundEvent soundeventFoley = this.getAmbientSoundFoley();
	      this.playSound(soundeventVocal, soundeventFoley, this.getSoundVolume(), this.getVoicePitch(), this.getSoundVolume(), this.getVoicePitch());
	}
	
	@Override
	protected void playHurtSound(DamageSource p_184581_1_) {
		  this.ambientSoundTime = -this.getAmbientSoundInterval();
		  SoundEvent soundeventVocal = this.getHurtSound(p_184581_1_);
	      SoundEvent soundeventFoley = this.getHurtSoundFoley(p_184581_1_);
	      this.playSound(soundeventVocal, soundeventFoley, this.getSoundVolume(), this.getVoicePitch(), this.getSoundVolume(), this.getVoicePitch());
	}
	
	public void playBurstSound() {
	      SoundEvent soundeventVocal = this.getBurstSound();
	      SoundEvent soundeventFoley = this.getBurstSoundFoley();
	      this.playSound(soundeventVocal, soundeventFoley, this.getSoundVolume(), this.getVoicePitch(), this.getSoundVolume(), this.getVoicePitch());
	}
	
	public void playRetractSound() {
	      SoundEvent soundeventVocal = this.getRetractSound();
	      SoundEvent soundeventFoley = this.getRetractSoundFoley();
	      this.playSound(soundeventVocal, soundeventFoley, this.getSoundVolume(), this.getVoicePitch(), this.getSoundVolume(), this.getVoicePitch());
	}
	
	public void playSound(SoundEvent vocalSound, SoundEvent foleySound, float vocalVolume, float vocalPitch, float foleyVolume, float foleyPitch) {
		if (!this.isSilent()) {
			if (vocalSound != null) {
	         this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), vocalSound, this.getSoundSource(), vocalVolume, vocalPitch);
			}
			if (foleySound != null) {
	         this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), foleySound, this.getSoundSource(), foleyVolume, foleyPitch);
			}
	    }
	}
	
	protected abstract SoundEvent getAmbientSoundFoley();
	
	protected abstract SoundEvent getHurtSoundFoley(DamageSource p_184601_1_);
	
    public abstract SoundEvent getBurstSound();
    
    public abstract SoundEvent getRetractSound();
    
    public abstract SoundEvent getBurstSoundFoley();
    
    public abstract SoundEvent getRetractSoundFoley();
	
    public boolean isOut() {
        return this.getAlwaysOut() || this.getOut();
    }
    
    public boolean canBurst() {
    	return !this.isOut() && this.retractAnimationTick <= 0 && this.burstAnimationTick <= 0;
    }
    
    public boolean canRetract() {
    	return this.isOut() && !this.getAlwaysOut() && this.retractAnimationTick <= 0 && this.burstAnimationTick <= 0;
    }
    
    public boolean shouldBurstFor(Entity entity) {
    	return entity instanceof LivingEntity && !entity.getType().is(CustomTags.PLANT_MOBS) && entity.isAlive() && !entity.isSpectator() && !(entity instanceof PlayerEntity && ((PlayerEntity)entity).isCreative());
    }
   
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    
    @Override
    public boolean isPickable() {
    	return false;
    }
    
    @Override
    public boolean shouldShowName() {
    	return this.isOut();
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

     public boolean isPushable() {
        return false;
     }

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
    	if (p_70097_1_ == DamageSource.OUT_OF_WORLD || this.isOut()) {
    		return super.hurt(p_70097_1_, p_70097_2_);
    	} else {
    		return false;
    	}
    }

    @Override
    public void knockback(float strength, double ratioX, double ratioZ) {

    }
    
    @Override
    public void push(Entity p_70108_1_) {

    }
    
    @Override
    public void push(double p_70024_1_, double p_70024_3_, double p_70024_5_) {

    }
    
    @Override
    protected void pushEntities() {

    }
    
    @Override
    public void handleEntityEvent(byte p_70103_1_) {
    	if (p_70103_1_ == 4) {
    		this.burstAnimationTick = this.getBurstAnimationLength();
    	} else if (p_70103_1_ == 11) {
    		this.retractAnimationTick = this.getRetractAnimationLength();
    	} else {
    		super.handleEntityEvent(p_70103_1_);
    	}
    }
    
    @Override
    public EntitySize getDimensions(Pose p_213305_1_) {
    	return !this.isOut() ? EntitySize.scalable(1.0F, 0.1F) : EntitySize.scalable(0.1F, ((float)this.getLengthInBlocks()) + this.getExtraHitboxY());
    }
    
	public void refreshDimensions() {
		double d0 = this.getX();
		double d1 = this.getY();
		double d2 = this.getZ();
		super.refreshDimensions();
		this.setPos(d0, d1, d2);
	}
	
	public abstract void spawnAreaDamage();
	
	public abstract void setDefaultFeatures();
	
	public abstract boolean isKelp();
	public abstract boolean shouldDieInWrongHabitat();	
	public abstract int wrongHabitatDieChance();
	
	public boolean isInWrongHabitat() {
		return ((this.isKelp() && !this.isInWaterOrBubble()) || (!this.isKelp() && this.isInWaterOrBubble()));
	}
	
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_,
			SpawnReason p_213386_3_, ILivingEntityData p_213386_4_, CompoundNBT p_213386_5_) {
		this.setDefaultFeatures();
		return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		this.refreshDimensions();
		
		this.tickDownAnimTimers();
		
    	int nearbyEntities = this.level.getEntities(this, this.getBoundingBox().inflate(this.getDetectionDistance()), SHOULD_BURST_FOR).size();
    	
		if (!this.level.isClientSide) {
			
			if (this.isInWrongHabitat() && this.random.nextInt(this.wrongHabitatDieChance()) == 0 && this.shouldDieInWrongHabitat() && this.isOut()) {
				this.kill();
			}
			
			if (this.burstAnimationTick == 1) {
				this.setOut(true);
			} else if (this.retractAnimationTick == 1) {
				this.setOut(false);
			}
			
			if (nearbyEntities > 0) {
				if (this.canBurst()) {
					this.spawnAreaDamage();
					this.playBurstSound();
					this.burstAnimationTick = this.getBurstAnimationLength();
					this.level.broadcastEntityEvent(this, (byte) 4);
				}
			} else {
				if (this.canRetract() && this.getShouldRetract()) {
					this.spawnAreaDamage();
					this.playRetractSound();
					this.retractAnimationTick = this.getRetractAnimationLength();
					this.level.broadcastEntityEvent(this, (byte) 11);
				}
			}
		}
	}
	
	public void tickDownAnimTimers() {
		if (this.burstAnimationTick > 0) {
			this.burstAnimationTick--;
		}

		if (this.retractAnimationTick > 0) {
			this.retractAnimationTick--;
		}
	}

    @Override
    public void setId(int p_145769_1_) {
        super.setId(p_145769_1_);
        for(int i = 0; i < this.subEntities.length; ++i) // Forge: Fix MC-158205: Set part ids to successors of parent mob id
            this.subEntities[i].setId(p_145769_1_ + i + 1);
    }
    
}
