package com.infamous.dungeons_mobs.entities.illagers;

import java.util.EnumSet;
import java.util.UUID;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MageCloneEntity extends SpellcastingIllagerEntity implements IAnimatable {

	public static final DataParameter<Integer> LIFT_TICKS = EntityDataManager.defineId(MageCloneEntity.class, DataSerializers.INT);
	public static final DataParameter<Boolean> ANGRY = EntityDataManager.defineId(MageCloneEntity.class, DataSerializers.BOOLEAN);
	
	public int spellInterval = 0;
	
    private LivingEntity caster;
    private UUID casterUuid;
    private int lifeTicks;
	
	AnimationFactory factory = new AnimationFactory(this);
	
    public MageCloneEntity(World world){
        super(ModEntityTypes.MAGE.get(), world);
    }

    public MageCloneEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }
    
    public MageCloneEntity(World worldIn, LivingEntity caster, int lifeTicks) {
        this(ModEntityTypes.MAGE_CLONE.get(), worldIn);
        this.setCaster(caster);
        this.lifeTicks = lifeTicks;
    }
    
    public void setTarget(@Nullable LivingEntity p_70624_1_) {
        if (p_70624_1_ == null) {
           this.entityData.set(ANGRY, false);
        } else {
           this.entityData.set(ANGRY, true);
        }

        super.setTarget(p_70624_1_); //Forge: Moved down to allow event handlers to write data manager values.
     }
    
    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
    	if (!this.isInvulnerable()) {
    		this.remove();
    	}
    	return super.hurt(p_70097_1_, p_70097_2_);
    }
    

    public void setCaster(@Nullable LivingEntity caster) {
        this.caster = caster;
        this.casterUuid = caster == null ? null : caster.getUUID();
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity)entity;
            }
        }

        return this.caster;
    }

    public int getLifeTicks() {
        return this.lifeTicks;
    }

    public void setLifeTicks(int lifeTicksIn){
        this.lifeTicks = lifeTicksIn;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        compound.putInt("LifeTicks", this.getLifeTicks());
        if (compound.hasUUID("Owner")) {
            this.casterUuid = compound.getUUID("Owner");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        this.setLifeTicks(compound.getInt("LifeTicks"));
        if (this.casterUuid != null) {
            compound.putUUID("Owner", this.casterUuid);
        }
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new MageCloneEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 5.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(4, new MageCloneEntity.LiftMobGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }
    
    @Override
    public void onAddedToWorld() {
        if(this.level.isClientSide){
            this.spawnPoofCloud();
        }
        super.onAddedToWorld();
    }

    @Override
    public void onRemovedFromWorld() {
    	this.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
        if(this.level.isClientSide){
           this.spawnPoofCloud();
        }
        super.onRemovedFromWorld();
    }

    private void spawnPoofCloud(){
        for(int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.WITCH, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }
    }
    
    /**
     * Gets the bounding box of this Entity, adjusted to take auxiliary entities into account (e.g. the tile contained by
     * a minecart, such as a command block).
     */
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(3.0D, 0.0D, 3.0D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void aiStep() {
        super.aiStep();
        if(!this.level.isClientSide){
            this.lifeTicks--;
            if(this.lifeTicks <= 0 || this.getCaster() == null || !this.getCaster().isAlive()){
                this.remove();
            }
        }
    }
    
    public void baseTick() {
    	super.baseTick();
    	
    	if (this.getTarget() != null) {
            this.getLookControl().setLookAt(this.getTarget(), (float)this.getMaxHeadYRot(), (float)this.getMaxHeadXRot());
        	}
    	
    	if (this.spellInterval > 0) {
    		this.spellInterval --;
    	}
    	
		if (this.getLiftTicks() > 0) {
			this.setLiftTicks(this.getLiftTicks() - 1);
		}
    }
    
    
    
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 10, this::predicate));
	}
   
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.getLiftTicks() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_lift", true));
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_walk", true));
			} else {
				if (this.isAngry()) {
				    event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_idle_attacking", true));					
				} else {
				    event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_idle", true));
				}
			}
		return PlayState.CONTINUE;
	}
	
	@Override
	public AnimationFactory getFactory() {
		return factory;
	}
	
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LIFT_TICKS, 0);
        this.entityData.define(ANGRY, false);
    }
	
	   public int getLiftTicks() {
		      return this.entityData.get(LIFT_TICKS);
		   }

		   public void setLiftTicks(int p_189794_1_) {	   
		      this.entityData.set(LIFT_TICKS, p_189794_1_);
		   }
		   
		   public boolean isAngry() {
			      return this.entityData.get(ANGRY);
			   }

			   public void setAngry(boolean p_189794_1_) {	   
			      this.entityData.set(ANGRY, p_189794_1_);
			   }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MAX_HEALTH, 24.0D);
    }



    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    public void applyRaidBuffs(int p_213660_1_, boolean p_213660_2_) {

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }

    class CastingSpellGoal extends SpellcastingIllagerEntity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (MageCloneEntity.this.getTarget() != null) {
                MageCloneEntity.this.getLookControl().setLookAt(MageCloneEntity.this.getTarget(), (float)MageCloneEntity.this.getMaxHeadYRot(), (float)MageCloneEntity.this.getMaxHeadXRot());
            }

        }
    }
    
    class LiftMobGoal extends Goal {
        private final EntityPredicate cloneTargeting = (new EntityPredicate()).range(20.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();
        
	      public LiftMobGoal() {
	         this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
	      }

	      public boolean canUse() {
	         return MageCloneEntity.this.getTarget() != null && MageCloneEntity.this.getLiftTicks() == 0 && MageCloneEntity.this.spellInterval == 0 && MageCloneEntity.this.random.nextInt(20) == 0;
	      }
	      
	      public boolean canContinueToUse() {
	    	  	return MageCloneEntity.this.hurtTime <= 0 && MageCloneEntity.this.getTarget() != null && MageCloneEntity.this.getLiftTicks() > 0;
	      }
	    
	    public void start() {
	    super.start();
	    MageCloneEntity.this.spellInterval = 100;
	    MageCloneEntity.this.setLiftTicks(40);
	    MageCloneEntity.this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, MageCloneEntity.this.getSoundVolume(), MageCloneEntity.this.getVoicePitch());
	    }
	      
	    public void tick() {
	    	LivingEntity target = MageCloneEntity.this.getTarget();
	    	MageCloneEntity mob = MageCloneEntity.this;
	    	
	    	for (MageCloneEntity nearbyClone : MageCloneEntity.this.level.getNearbyEntities(MageCloneEntity.class, this.cloneTargeting, MageCloneEntity.this, MageCloneEntity.this.getBoundingBox().inflate(20.0D))) {
	    		if (nearbyClone != mob) {
	    		nearbyClone.spellInterval = 40 + MageCloneEntity.this.random.nextInt(60);
	    		}
	    	}
	    	
	    	mob.getNavigation().stop();
	    	
            if (mob.getLiftTicks() > 10) {
            	if (target.getY() < mob.getY() + 3) {
            		target.hurtMarked = true;
            		target.setDeltaMovement(0, 0.5, 0);
            	} else {
            		target.fallDistance = 5;
            		target.hurtMarked = true;
            		target.setDeltaMovement(0, 0.1, 0);
            	}
            } else {
            	if (!target.isOnGround()) {
            	target.hurtMarked = true;
            	target.setDeltaMovement(0, -0.75, 0);
            	}
        		//if (target.getY() == mob.getEyeY()) {
        		//	target.hurt(DamageSource.FALL, 5.0F);
        		//}
            }
	    }
	    
	    public void stop() {
	    super.stop();
	    MageCloneEntity.this.setLiftTicks(0);
	    }
	   }
    

    /*class LiftMobGoal extends SpellcastingIllagerEntity.UseSpellGoal {
        private LiftMobGoal() {
        }

        protected int getCastingTime() {
            return 50;
        }

        protected int getCastingInterval() {
            return 200;
        }
        
        @Override
        public void start() {
        	super.start();
        	MageEntity.this.setLiftTicks(40);
        }
        
        
        
        @Override
        public void tick() {
        	super.tick();
            LivingEntity attackTarget = MageEntity.this.getTarget();
            MageEntity mage = MageEntity.this;
            System.out.print("\r\n" + "ticking LiftMobGoal");
            if (mage.getLiftTicks() > 20) {
            	if (attackTarget.getY() < mage.getY() + 3) {
            		attackTarget.hurtMarked = true;
            		attackTarget.setDeltaMovement(0, 0.5, 0);
            	} else {
            		attackTarget.hurtMarked = true;
            		attackTarget.fallDistance = 10;
            		attackTarget.setDeltaMovement(0, 0.1, 0);
            	}
            } else {
        		attackTarget.hurtMarked = true;
        		attackTarget.setDeltaMovement(0, -1.0, 0);
        		//if (attackTarget.getY() == mage.getEyeY()) {
        		//	attackTarget.hurt(DamageSource.FALL, 5.0F);
        		//}
            }
        }

        protected void performSpellCasting() {
            LivingEntity attackTarget = MageEntity.this.getTarget();
            //summonIceCloud(attackTarget);
            //summonIceBlocks(attackTarget);
        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected SpellcastingIllagerEntity.SpellType getSpell() {
            return SpellcastingIllagerEntity.SpellType.SUMMON_VEX;
        }
    }*/

    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }

}
