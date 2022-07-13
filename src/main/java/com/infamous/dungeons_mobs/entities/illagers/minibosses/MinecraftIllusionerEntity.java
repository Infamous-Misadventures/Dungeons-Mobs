package com.infamous.dungeons_mobs.entities.illagers.minibosses;

import com.infamous.dungeons_mobs.capabilities.cloneable.CloneableHelper;
import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
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
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

public class MinecraftIllusionerEntity extends SpellcastingIllagerEntity implements IAnimatable,IRangedAttackMob {

	public static final DataParameter<Integer> LIFT_TICKS = EntityDataManager.defineId(MinecraftIllusionerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> DUPLICATE_TICKS = EntityDataManager.defineId(MinecraftIllusionerEntity.class, DataSerializers.INT);
	public static final DataParameter<Boolean> ANGRY = EntityDataManager.defineId(MinecraftIllusionerEntity.class, DataSerializers.BOOLEAN);

	public int liftInterval = 0;
	public int duplicateInterval = 0;

	AnimationFactory factory = new AnimationFactory(this);

    public MinecraftIllusionerEntity(World world){
        super(ModEntityTypes.DUNGEONS_ILLUSIONER.get(), world);
    }

    public MinecraftIllusionerEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
    }

	@Override
	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		if (!this.isInvulnerable()) {
			this.duplicateInterval = 0;
		}
		if (!(p_70097_1_.getDirectEntity() == this) && !(p_70097_1_.getDirectEntity() instanceof IllusionerCloneCloneEntity)) {
			this.duplicateInterval = (this.duplicateInterval - 10 - this.getRandom().nextInt(20));
			return super.hurt(p_70097_1_, p_70097_2_);
		}else
			return false;
	}

	@Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
		this.targetSelector.addGoal(2, new FindTargetGoal(this, 10F));
        this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(5, new MinecraftIllusionerEntity.RkGoal(this,1));
        this.goalSelector.addGoal(1, new MinecraftIllusionerEntity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new MinecraftIllusionerEntity.DuplicateGoal());
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 5.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(4, new MinecraftIllusionerEntity.LiftMobGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }
	class RkGoal extends MeleeAttackGoal {
		private int maxAttackTimer = 15;
		private final double moveSpeed;
		private int delayCounter;
		private int attackTimer;

		public MinecraftIllusionerEntity v = MinecraftIllusionerEntity.this;

		public RkGoal(CreatureEntity creatureEntity, double moveSpeed) {
			super(creatureEntity, moveSpeed, true);
			this.moveSpeed = moveSpeed;
		}

		@Override
		public boolean canUse() {
			return v.getTarget() != null && v.getTarget().isAlive();
		}

		@Override
		public void start() {
			v.setAggressive(true);
			this.delayCounter = 0;
		}

		@Override
		public void tick() {
			LivingEntity livingentity = v.getTarget();
			if (livingentity == null) {
				v.setAggressive(false);
				return;
			}
			v.setAggressive(true);
			if (--this.delayCounter <= 0) {
				this.delayCounter = 8 + v.getRandom().nextInt(5);
				{
					if (v.distanceToSqr(livingentity) >= 80) {
						v.getNavigation().moveTo(livingentity,
								(double) this.moveSpeed
						);
					}
				}
				if (v.distanceToSqr(livingentity) >= 60 && v.distanceToSqr(livingentity) <= 70) {
					v.getNavigation().stop();
				}
			}
		}

		@Override
		public void stop() {
			v.getNavigation().stop();
			if (v.getTarget() == null) {
				v.setAggressive(false);
			}
		}

		public MinecraftIllusionerEntity.RkGoal setMaxAttackTick(int max) {
			this.maxAttackTimer = max;
			return this;
		}
	}

	public void baseTick() {
    	super.baseTick();
    	
    	if (this.getTarget() != null) {
			//this.getNavigation().moveTo(this.getTarget(), this.getAttributeValue(Attributes.MOVEMENT_SPEED));
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
		data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
	}
   
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.isAngry()&&this.getDuplicateTicks() >0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.teleport_in_short", true));
		} else if (this.isAngry()&&this.getLiftTicks() >0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.shoot2_clone", true));
		} else if (this.getDuplicateTicks() > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.teleport_out", true));
		} else if (this.getLiftTicks() > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.bow_action_clone", true));
		} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_walk", true));
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.illusioner_mcd.v2.idle", true));
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
        return MonsterEntity.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.MAX_HEALTH, 80.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
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
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ILLUSIONER_HURT;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
		return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float p_82196_2_) {
		ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
		AbstractArrowEntity abstractarrowentity = ProjectileHelper.getMobArrow(this, itemstack, p_82196_2_);
		if (this.getMainHandItem().getItem() instanceof BowItem)
			abstractarrowentity = ((BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
		double xDifference = target.getX() - this.getX();
		double yDifference = target.getY(0.3333333333333333D) - abstractarrowentity.getY();
		double zDifference = target.getZ() - this.getZ();
		double horizontalDistance = (double) MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference);
		abstractarrowentity.shoot(xDifference, yDifference + horizontalDistance * (double)0.2F, zDifference, 1.6F, (float)(18 - this.level.getDifficulty().getId() * 5));
		this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.level.addFreshEntity(abstractarrowentity);
	}

	class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (MinecraftIllusionerEntity.this.getTarget() != null) {
                MinecraftIllusionerEntity.this.getLookControl().setLookAt(MinecraftIllusionerEntity.this.getTarget(), (float) MinecraftIllusionerEntity.this.getMaxHeadYRot(), (float) MinecraftIllusionerEntity.this.getMaxHeadXRot());
            }

        }
    }
    
    class LiftMobGoal extends Goal {

		private boolean d;

		@Override
		public boolean isInterruptable() {
			return false;
		}

		public LiftMobGoal() {
	         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
	      }

	      public boolean canUse() {
	         return MinecraftIllusionerEntity.this.getTarget() != null && MinecraftIllusionerEntity.this.getLiftTicks() == 0 && MinecraftIllusionerEntity.this.liftInterval <= 0 && MinecraftIllusionerEntity.this.random.nextInt(20) == 0;
	      }
	      
	      public boolean canContinueToUse() {
	    	  	return MinecraftIllusionerEntity.this.getTarget() != null && MinecraftIllusionerEntity.this.getLiftTicks() > 0;
	      }
	    
	    public void start() {
	    super.start();
	    MinecraftIllusionerEntity.this.liftInterval = MinecraftIllusionerEntity.this.getRandom().nextInt(35) + 21;
			this.d =true;
			MinecraftIllusionerEntity.this.setAngry(false);
	    MinecraftIllusionerEntity.this.setLiftTicks(21 + MinecraftIllusionerEntity.this.getRandom().nextInt(3));
	    }
	      
	    public void tick() {
            MinecraftIllusionerEntity.this.getLookControl().setLookAt(MinecraftIllusionerEntity.this.getTarget(), (float) MinecraftIllusionerEntity.this.getMaxHeadYRot(), (float) MinecraftIllusionerEntity.this.getMaxHeadXRot());
	    	LivingEntity target = MinecraftIllusionerEntity.this.getTarget();
	    	MinecraftIllusionerEntity mob = MinecraftIllusionerEntity.this;
	    	
	    	mob.getNavigation().stop();

			if (mob.getLiftTicks() == 1 && this.d) {
				MinecraftIllusionerEntity.this.setLiftTicks(14);
				MinecraftIllusionerEntity.this.setAngry(true);
				this.d =false;
				mob.performRangedAttack(target, 0);
			}
	    }
	    
	    public void stop() {
	    super.stop();
			MinecraftIllusionerEntity.this.setAngry(false);
	    MinecraftIllusionerEntity.this.setLiftTicks(0);
	    }
	}
    
    class DuplicateGoal extends Goal {

		private boolean e;
		private int o;
	      public DuplicateGoal() {
	         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
	      }

	      public boolean canUse() {
	         return MinecraftIllusionerEntity.this.getTarget() != null && MinecraftIllusionerEntity.this.getDuplicateTicks() == 0  && MinecraftIllusionerEntity.this.getLiftTicks() == 0 && MinecraftIllusionerEntity.this.duplicateInterval <= 0;
	      }
	      
	      public boolean canContinueToUse() {
	    	  	return MinecraftIllusionerEntity.this.getTarget() != null && MinecraftIllusionerEntity.this.getDuplicateTicks() > 0;
	      }
	      
	      @Override
	    public boolean isInterruptable() {
	    	return false;
	    }
	    
	    public void start() {
			  super.start();
			  this.e = false;
			  this.o = 0;

			  MinecraftIllusionerEntity.this.setInvulnerable(true);
			MinecraftIllusionerEntity.this.setAngry(false);

			  MinecraftIllusionerEntity.this.duplicateInterval = 60000;


			  MinecraftIllusionerEntity.this.setDuplicateTicks(20);

			  MinecraftIllusionerEntity.this.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, MinecraftIllusionerEntity.this.getSoundVolume(), MinecraftIllusionerEntity.this.getVoicePitch());
		  }
	      
	    public void tick() {
            MinecraftIllusionerEntity.this.getLookControl().setLookAt(MinecraftIllusionerEntity.this.getTarget(), (float) MinecraftIllusionerEntity.this.getMaxHeadYRot(), (float) MinecraftIllusionerEntity.this.getMaxHeadXRot());
	    	LivingEntity target = MinecraftIllusionerEntity.this.getTarget();

	    	MinecraftIllusionerEntity mob = MinecraftIllusionerEntity.this;
	    	
	    	mob.getNavigation().stop();
			if (mob.getDuplicateTicks() == 1 && !this.e) {
			  this.summonIllusionerClones();
			  this.e = true;
			  mob.setInvulnerable(false);
			  MinecraftIllusionerEntity.this.setDuplicateTicks(33);
			  MinecraftIllusionerEntity.this.setAngry(true);
      	      MinecraftIllusionerEntity.this.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, MinecraftIllusionerEntity.this.getSoundVolume(), MinecraftIllusionerEntity.this.getVoicePitch());

			  BlockPos blockpos = mob.getTarget().blockPosition().offset(-7.5 + mob.getRandom().nextInt(15) * (target.getBbWidth() / 2 +1), 0, -7.5 + MinecraftIllusionerEntity.this.getRandom().nextInt(15) * (target.getBbWidth() / 2 +1));
			  this.o = 0;
			  do {
				  if (mob.level.isEmptyBlock(blockpos) && !mob.level.isEmptyBlock(blockpos.offset(0,-1,0))) {
					  mob.moveTo(blockpos,0,0);
					  break;
				  }else if (!mob.level.isEmptyBlock(blockpos)) {
					  this.o++;
					  blockpos = blockpos.offset(0, 1, 0);
				  }else {
					  this.o++;
					  blockpos = blockpos.offset(0, -1, 0);
				  }
			  }while (this.o < 20);
           }
	    }

           private void summonIllusionerClones(){
			  LivingEntity target = MinecraftIllusionerEntity.this.getTarget();

			   MinecraftIllusionerEntity mob = MinecraftIllusionerEntity.this;
              int difficultyAsInt = MinecraftIllusionerEntity.this.level.getDifficulty().getId();
              int mobsToSummon = difficultyAsInt * 3 + 6; // 9 on easy, 12 on normal, 15 on hard
              for(int i = 0; i < mobsToSummon; ++i) {
				  BlockPos blockpos = mob.getTarget().blockPosition().offset(-7.5 + mob.getRandom().nextInt(15) * (target.getBbWidth() / 2 +1), 0, -7.5 + MinecraftIllusionerEntity.this.getRandom().nextInt(15) * (target.getBbWidth() / 2 +1));
				 IllusionerCloneCloneEntity illusionerCloneEntity = new IllusionerCloneCloneEntity(MinecraftIllusionerEntity.this.level, MinecraftIllusionerEntity.this, 60000, MinecraftIllusionerEntity.this.getTarget());
                 DifficultyInstance difficultyForLocation = MinecraftIllusionerEntity.this.level.getCurrentDifficultyAt(blockpos);
				 int ou = 0;
				 do {
					 if (illusionerCloneEntity.level.isEmptyBlock(blockpos) && !illusionerCloneEntity.level.isEmptyBlock(blockpos.offset(0,-1,0))) {
						 illusionerCloneEntity.moveTo(blockpos, 0.0F, 0.0F);
						 illusionerCloneEntity.finalizeSpawn((IServerWorld) illusionerCloneEntity.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
						 illusionerCloneEntity.setHealth(MinecraftIllusionerEntity.this.getHealth());
						 illusionerCloneEntity.setTarget(MinecraftIllusionerEntity.this.getTarget());
						 illusionerCloneEntity.setDuplicateTicks(33);
						 illusionerCloneEntity.setAngry(true);
						 illusionerCloneEntity.liftInterval = MinecraftIllusionerEntity.this.getRandom().nextInt(130);
						 illusionerCloneEntity.setItemSlot(EquipmentSlotType.MAINHAND, MinecraftIllusionerEntity.this.getItemBySlot(EquipmentSlotType.MAINHAND));
						 illusionerCloneEntity.setItemSlot(EquipmentSlotType.OFFHAND, MinecraftIllusionerEntity.this.getItemBySlot(EquipmentSlotType.OFFHAND));
						 illusionerCloneEntity.setItemSlot(EquipmentSlotType.HEAD, MinecraftIllusionerEntity.this.getItemBySlot(EquipmentSlotType.HEAD));
						 illusionerCloneEntity.setItemSlot(EquipmentSlotType.CHEST, MinecraftIllusionerEntity.this.getItemBySlot(EquipmentSlotType.CHEST));
						 illusionerCloneEntity.setItemSlot(EquipmentSlotType.LEGS, MinecraftIllusionerEntity.this.getItemBySlot(EquipmentSlotType.LEGS));
						 illusionerCloneEntity.setItemSlot(EquipmentSlotType.FEET, MinecraftIllusionerEntity.this.getItemBySlot(EquipmentSlotType.FEET));
						 MinecraftIllusionerEntity.this.level.addFreshEntity(illusionerCloneEntity);
						 ICloneable cloneable = CloneableHelper.getCloneableCapability(MinecraftIllusionerEntity.this);
						 if(cloneable != null){
							 cloneable.addClone(illusionerCloneEntity.getUUID());
						 }
						 MinecraftIllusionerEntity.this.moveTo(blockpos,0,0);
						 break;
					 }else {
						 if (!illusionerCloneEntity.level.isEmptyBlock(blockpos)) {
							 ou++;
							 blockpos = blockpos.offset(0, 1, 0);
						 }else {
							 ou++;
							 blockpos = blockpos.offset(0, -1, 0);
						 }
					 }
				 }while (ou < 20);
              }
           }
	    
	    public void stop() {
	    super.stop();
			MinecraftIllusionerEntity.this.setInvisible(false);
			MinecraftIllusionerEntity.this.setInvulnerable(false);
			MinecraftIllusionerEntity.this.setDuplicateTicks(0);
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
