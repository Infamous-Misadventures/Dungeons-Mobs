package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.capabilities.cloneable.CloneableHelper;
import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MageEntity extends SpellcastingIllagerEntity implements IAnimatable {

	public static final DataParameter<Integer> LIFT_TICKS = EntityDataManager.defineId(MageEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> DUPLICATE_TICKS = EntityDataManager.defineId(MageEntity.class, DataSerializers.INT);
	public static final DataParameter<Boolean> ANGRY = EntityDataManager.defineId(MageEntity.class, DataSerializers.BOOLEAN);
	
	public int liftInterval = 0;
	public int duplicateInterval = 0;
	
	AnimationFactory factory = new AnimationFactory(this);
	
    public MageEntity(World world){
        super(ModEntityTypes.MAGE.get(), world);
    }

    public MageEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new MageEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new MageEntity.DuplicateGoal());
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 5.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(4, new MageEntity.LiftMobGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }
    
    public void setTarget(@Nullable LivingEntity p_70624_1_) {
        if (p_70624_1_ == null) {
           this.entityData.set(ANGRY, false);
        } else {
           this.entityData.set(ANGRY, true);
        }

        super.setTarget(p_70624_1_); //Forge: Moved down to allow event handlers to write data manager values.
     }
    
    public void baseTick() {
    	super.baseTick();
    	
    	if (this.getTarget() != null) {
        this.getLookControl().setLookAt(this.getTarget(), (float)this.getMaxHeadYRot(), (float)this.getMaxHeadXRot());
    	}
    	
    	//this.setAngry(this.getTarget() != null);
    	//System.out.print("\r\n" + this.isAngry());
    	
    	if (this.liftInterval > 0) {
    		this.liftInterval --;
    	}
    	
    	if (this.duplicateInterval > 0) {
    		this.duplicateInterval --;
    	}
    	
    	//if (this.level.isClientSide) {
    	//	System.out.print("\r\n" + this.isAggressive());
    	//}
    	
		if (this.getLiftTicks() > 0) {
			this.setLiftTicks(this.getLiftTicks() - 1);
		}
		
		if (this.getDuplicateTicks() > 0) {
			this.setDuplicateTicks(this.getDuplicateTicks() - 1);
		}
    }
    
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 10, this::predicate));
	}
   
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.getDuplicateTicks() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_duplicate", true));
			} else if (this.getLiftTicks() > 0) {
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
        this.entityData.define(DUPLICATE_TICKS, 0);
        this.entityData.define(ANGRY, false);
    }
	
	   public int getLiftTicks() {
		      return this.entityData.get(LIFT_TICKS);
		   }

		   public void setLiftTicks(int p_189794_1_) {	   
		      this.entityData.set(LIFT_TICKS, p_189794_1_);
		   }
		   
		   public int getDuplicateTicks() {
			      return this.entityData.get(DUPLICATE_TICKS);
			   }

			   public void setDuplicateTicks(int p_189794_1_) {	   
			      this.entityData.set(DUPLICATE_TICKS, p_189794_1_);
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
            if (MageEntity.this.getTarget() != null) {
                MageEntity.this.getLookControl().setLookAt(MageEntity.this.getTarget(), (float)MageEntity.this.getMaxHeadYRot(), (float)MageEntity.this.getMaxHeadXRot());
            }

        }
    }
    
    /*class DuplicateSpellGoal extends SpellcastingIllagerEntity.UseSpellGoal {
        private DuplicateSpellGoal() {
        }

        public boolean canUse() {
           if (!super.canUse()) {
              return false;
           } else {
              return !MageEntity.this.hasEffect(Effects.INVISIBILITY);
           }
        }
        
        @Override
        public void start() {
        	super.start();
        	MageEntity.this.setDuplicateTicks(20);
        }

        protected int getCastingTime() {
           return 20;
        }

        protected int getCastingInterval() {
           return 1200;
        }

        protected void performSpellCasting() {
           BlockPos blockpos = MageEntity.this.blockPosition().offset(-5 + MageEntity.this.getRandom().nextInt(10), 1, -5 + MageEntity.this.getRandom().nextInt(10));
           MageEntity.this.moveTo(blockpos.getX(), blockpos.getY(), blockpos.getZ());
           this.summonIllusionerClones();
        }

        private void summonIllusionerClones(){
           int difficultyAsInt = MageEntity.this.level.getDifficulty().getId();
           int mobsToSummon = difficultyAsInt * 2 + 1; // 3 on easy, 5 on normal, 7 on hard
           for(int i = 0; i < mobsToSummon; ++i) {
              BlockPos blockpos = MageEntity.this.blockPosition().offset(-5 + MageEntity.this.getRandom().nextInt(10), 1, -5 + MageEntity.this.getRandom().nextInt(10));
              MageCloneEntity illusionerCloneEntity = new MageCloneEntity(MageEntity.this.level, MageEntity.this, 600);
              DifficultyInstance difficultyForLocation = MageEntity.this.level.getCurrentDifficultyAt(blockpos);
              illusionerCloneEntity.moveTo(blockpos, 0.0F, 0.0F);
              illusionerCloneEntity.finalizeSpawn((IServerWorld) illusionerCloneEntity.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
              MageEntity.this.level.addFreshEntity(illusionerCloneEntity);
              ICloneable cloneable = CloneableHelper.getCloneableCapability(MageEntity.this);
              if(cloneable != null){
                 cloneable.addClone(illusionerCloneEntity.getUUID());
              }
           }
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
           return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
        }

        protected SpellcastingIllagerEntity.SpellType getSpell() {
           return SpellcastingIllagerEntity.SpellType.DISAPPEAR;
        }
     }*/
    
    class LiftMobGoal extends Goal {
	      public LiftMobGoal() {
	         this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
	      }

	      public boolean canUse() {
	         return MageEntity.this.getTarget() != null && MageEntity.this.getLiftTicks() == 0 && MageEntity.this.liftInterval == 0 && MageEntity.this.random.nextInt(20) == 0;
	      }
	      
	      public boolean canContinueToUse() {
	    	  	return MageEntity.this.hurtTime <= 0 && MageEntity.this.getTarget() != null && MageEntity.this.getLiftTicks() > 0;
	      }
	    
	    public void start() {
	    super.start();
	    MageEntity.this.liftInterval = 100;
	    MageEntity.this.setLiftTicks(40);
	    MageEntity.this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, MageEntity.this.getSoundVolume(), MageEntity.this.getVoicePitch());
	    }
	      
	    public void tick() {
            MageEntity.this.getLookControl().setLookAt(MageEntity.this.getTarget(), (float)MageEntity.this.getMaxHeadYRot(), (float)MageEntity.this.getMaxHeadXRot());
	    	LivingEntity target = MageEntity.this.getTarget();
	    	MageEntity mob = MageEntity.this;
	    	
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
	    MageEntity.this.setLiftTicks(0);
	    }
	   }
    
    class DuplicateGoal extends Goal {
	      public DuplicateGoal() {
	         this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
	      }

	      public boolean canUse() {
	         return MageEntity.this.getTarget() != null && MageEntity.this.getDuplicateTicks() == 0 && MageEntity.this.duplicateInterval == 0;
	      }
	      
	      public boolean canContinueToUse() {
	    	  	return MageEntity.this.hurtTime <= 0 && MageEntity.this.getTarget() != null && MageEntity.this.getDuplicateTicks() > 0;
	      }
	      
	      @Override
	    public boolean isInterruptable() {
	    	return false;
	    }
	    
	    public void start() {
	    super.start();
	    MageEntity.this.duplicateInterval = 640;
	    MageEntity.this.setDuplicateTicks(20);
	    MageEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, MageEntity.this.getSoundVolume(), MageEntity.this.getVoicePitch());
	    }
	      
	    public void tick() {
            MageEntity.this.getLookControl().setLookAt(MageEntity.this.getTarget(), (float)MageEntity.this.getMaxHeadYRot(), (float)MageEntity.this.getMaxHeadXRot());
	    	LivingEntity target = MageEntity.this.getTarget();
	    	MageEntity mob = MageEntity.this;
	    	
	    	mob.getNavigation().stop();
	    	
          if (mob.getDuplicateTicks() == 1) {
              this.summonIllusionerClones();
      	      MageEntity.this.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, MageEntity.this.getSoundVolume(), MageEntity.this.getVoicePitch());
              BlockPos blockpos = MageEntity.this.blockPosition().offset(-5 + MageEntity.this.getRandom().nextInt(10), 0, -5 + MageEntity.this.getRandom().nextInt(10));
              MageEntity.this.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
           }
	    }

           private void summonIllusionerClones(){
              int difficultyAsInt = MageEntity.this.level.getDifficulty().getId();
              int mobsToSummon = difficultyAsInt * 2 + 1; // 3 on easy, 5 on normal, 7 on hard
              for(int i = 0; i < mobsToSummon; ++i) {
                 BlockPos blockpos = MageEntity.this.blockPosition().offset(-5 + MageEntity.this.getRandom().nextInt(10), 0, -5 + MageEntity.this.getRandom().nextInt(10));
                 MageCloneEntity illusionerCloneEntity = new MageCloneEntity(MageEntity.this.level, MageEntity.this, 600);
                 DifficultyInstance difficultyForLocation = MageEntity.this.level.getCurrentDifficultyAt(blockpos);
                 illusionerCloneEntity.moveTo(blockpos, 0.0F, 0.0F);
                 illusionerCloneEntity.finalizeSpawn((IServerWorld) illusionerCloneEntity.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                 MageEntity.this.level.addFreshEntity(illusionerCloneEntity);
                 ICloneable cloneable = CloneableHelper.getCloneableCapability(MageEntity.this);
                 if(cloneable != null){
                    cloneable.addClone(illusionerCloneEntity.getUUID());
                 }
              }
           }
	    
	    public void stop() {
	    super.stop();
	    MageEntity.this.setDuplicateTicks(0);
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
