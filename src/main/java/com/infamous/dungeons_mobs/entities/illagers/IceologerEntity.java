package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class IceologerEntity extends SpellcastingIllagerEntity implements IAnimatable {

	public static final DataParameter<Integer> LIFT_TICKS = EntityDataManager.defineId(IceologerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> DUPLICATE_TICKS = EntityDataManager.defineId(IceologerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> ANGRY = EntityDataManager.defineId(IceologerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> POWERFUL_ATTACK = EntityDataManager.defineId(IceologerEntity.class, DataSerializers.INT);

	public boolean SpellAttacking;
	public int liftInterval = 0;
	public int duplicateInterval = 0;
	public int powerfulAttackInterval = 0;
	public int spellInterval = 0;
	public int timer = 0;

	AnimationFactory factory = new AnimationFactory(this);

    public IceologerEntity(World world){
        super(ModEntityTypes.DUNGEONS_EVOKER.get(), world);
    }

    public IceologerEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, AbstractVillagerEntity.class, 3.0F, 1.4D, 1.35D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PlayerEntity.class, 3.0F, 1.4D, 1.4D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, IronGolemEntity.class, 3.0F, 1.4D, 1.35D));
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new IceologerEntity.CastingSpellGoal());
		this.goalSelector.addGoal(5, new IceologerEntity.SummonFangsGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }
    
    public void baseTick() {
    	super.baseTick();
    	
    	if (this.getTarget() != null) {
        this.getLookControl().setLookAt(this.getTarget(), (float)this.getMaxHeadYRot(), (float)this.getMaxHeadXRot());
    	}

    	if (this.liftInterval > 0) {
    		this.liftInterval --;
    	}

		if (this.spellInterval > 0) {
			this.spellInterval --;
		}

		if (this.powerfulAttackInterval > 0) {
			this.powerfulAttackInterval --;
		}

    	if (this.duplicateInterval > 0) {
    		this.duplicateInterval --;
    	}

		if (this.timer > 0) {
			this.timer --;
		}
    	
		if (this.getLiftTicks() > 0) {
			this.setLiftTicks(this.getLiftTicks() - 1);
		}
		
		if (this.getDuplicateTicks() > 0) {
			this.setDuplicateTicks(this.getDuplicateTicks() - 1);
		}

		if (this.isAngry() > 0) {
			this.setAngry(this.isAngry() - 1);
		}

		if (this.getPowerfulAttack() > 0) {
			this.setPowerfulAttack(this.getPowerfulAttack() - 1);
		}
    }
    
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
	}


	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.getDuplicateTicks() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_summon_spell", false));
			} else if (this.getLiftTicks() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_summon_spell", false));
			}else if (this.isAngry() > 0 || this.getPowerfulAttack() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_attack_spell", false));
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_walk", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_idel", true));
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
        this.entityData.define(ANGRY, 0);
		this.entityData.define(POWERFUL_ATTACK, 0);
    }

	public int getPowerfulAttack() {
		return this.entityData.get(POWERFUL_ATTACK);
	}

	public void setPowerfulAttack(int p_189794_1_) {
		this.entityData.set(POWERFUL_ATTACK, p_189794_1_);
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
			   
			   public int isAngry() {
				      return this.entityData.get(ANGRY);
				   }

				   public void setAngry(int p_189794_1_) {
				      this.entityData.set(ANGRY, p_189794_1_);
				   }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return EvokerEntity.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED,0.31D);
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
        return SoundEvents.EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.EVOKER_HURT;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (IceologerEntity.this.getTarget() != null) {
                IceologerEntity.this.getLookControl().setLookAt(IceologerEntity.this.getTarget(), (float) IceologerEntity.this.getMaxHeadYRot(), (float) IceologerEntity.this.getMaxHeadXRot());
            }

        }
    }

	class SummonFangsGoal extends Goal {


		@Override
		public boolean isInterruptable() {
			return false;
		}

	      public SummonFangsGoal() {
	         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
	      }

	      public boolean canUse() {
	         return IceologerEntity.this.getTarget() != null && IceologerEntity.this.getLiftTicks() == 0 && IceologerEntity.this.liftInterval == 0 && IceologerEntity.this.random.nextInt(20) == 0;
	      }
	      
	      public boolean canContinueToUse() {
			  return IceologerEntity.this.getTarget() != null && IceologerEntity.this.getLiftTicks() > 0;
	      }


	    
	    public void start() {
	    super.start();
		IceologerEntity.this.SpellAttacking = false;
	    IceologerEntity.this.liftInterval = 300;
	    IceologerEntity.this.setLiftTicks(48);
	    IceologerEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, IceologerEntity.this.getSoundVolume(), IceologerEntity.this.getVoicePitch());
	    }
	      
	    public void tick() {
			IceologerEntity.this.getLookControl().setLookAt(IceologerEntity.this.getTarget(), (float) IceologerEntity.this.getMaxHeadYRot(), (float) IceologerEntity.this.getMaxHeadXRot());
			IceologerEntity mob = IceologerEntity.this;

			if (mob.getLiftTicks()==3){
				IceCloudEntity v = new IceCloudEntity(mob.level,mob.getX(),mob.getY(),mob.getZ(),mob,232,mob.getTarget());
				v.moveTo(mob.getX(),mob.getY(),mob.getZ());
				mob.level.addFreshEntity(v);
			}

	    }
	    
	    public void stop() {
	    super.stop();
	    IceologerEntity.this.setLiftTicks(0);
	    }
	   }




    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }

}
