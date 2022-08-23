package com.infamous.dungeons_mobs.entities.ender;

import java.util.EnumSet;
import java.util.function.Predicate;

import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import com.infamous.dungeons_mobs.entities.projectiles.SnarelingGlobEntity;
import com.infamous.dungeons_mobs.mod.ModEffects;

public class SnarelingEntity extends AbstractEnderlingEntity implements IAnimatable {
	
	public static final DataParameter<Integer> SHOOT_TIME = EntityDataManager.defineId(SnarelingEntity.class, DataSerializers.INT);
	
	AnimationFactory factory = new AnimationFactory(this);

	public SnarelingEntity(EntityType<? extends SnarelingEntity> p_i50210_1_, World p_i50210_2_) {
		super(p_i50210_1_, p_i50210_2_);
	}
	
	   protected void registerGoals() {
		      this.goalSelector.addGoal(0, new SwimGoal(this));
		      this.goalSelector.addGoal(0, new SnarelingEntity.AvoidEntityGoal<>(this, 3, 1.0D, 1.0D));
		      this.goalSelector.addGoal(2, new SnarelingEntity.AttackGoal());
		      this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
		      this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		      this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		      this.targetSelector.addGoal(1, new AbstractEnderlingEntity.FindPlayerGoal(this, null));
		      this.targetSelector.addGoal(2, new HurtByTargetGoal(this, AbstractEnderlingEntity.class).setAlertOthers().setUnseenMemoryTicks(500));
		      this.targetSelector.addGoal(1, new EnderlingTargetGoal<>(this, PlayerEntity.class, true).setUnseenMemoryTicks(500));
		      
		      
		      //this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, AbstractEndermanVariant.class, true, false));
		   }
	   
	   public CreatureAttribute getMobType() {
		      return CreatureAttribute.ARTHROPOD;
		   }
	   
	   @Override
	protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
		   this.playSound(this.getStepSound(), 0.75F, 1.0F);
	}
	   
	   protected SoundEvent getStepSound() {
		      return ModSoundEvents.SNARELING_STEP.get();
		   }
	   

	protected SoundEvent getAmbientSound() {
		return ModSoundEvents.SNARELING_IDLE.get();
	}
	
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return ModSoundEvents.SNARELING_HURT.get();
	}
	
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.SNARELING_DEATH.get();
	}
	   
	public void baseTick() {
		super.baseTick();
		
		if (this.getTarget() != null && this.getTarget().isAlive() && this.distanceTo(this.getTarget()) > 5 && this.getTarget().hasEffect(ModEffects.ENSNARED.get()) && this.random.nextInt(10) == 0) {
			this.teleport(this.getTarget().getX() - 3 + this.random.nextInt(6), this.getTarget().getY(), this.getTarget().getZ() - 3 + this.random.nextInt(6));
		}
		
	    if (this.getTarget() != null && this.getTarget().isAlive() && !this.getTarget().hasEffect(ModEffects.ENSNARED.get()) && this.canSee(this.getTarget()) && this.getShootTime() <= 0 && this.random.nextInt(10) == 0) {
	    	this.setShootTime(80);
		    this.playSound(ModSoundEvents.SNARELING_PREPARE_SHOOT.get(), 2.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
	    	//this.playSound(SoundEvents.EVOKER_CAST_SPELL, 3.0F, 1.0F);
	    }
	    
		if (this.getShootTime() > 0) {
			this.setShootTime(this.getShootTime() - 1);
		}
		
		if (!this.level.isClientSide && this.getShootTime() == 15 && this.getTarget() != null && this.getTarget().isAlive()) {
	         
            this.performRangedAttack(this.getTarget(), 2.0F);
		}
		
		if (this.isAttacking() == 29) {
			this.playSound(ModSoundEvents.SNARELING_ATTACK.get(), 1.0F, 1.0F);
		}
		
		if (this.getShootTime() > 0) {
			this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
		} else {
			this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.3F);
		}
	}
	
	public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {
	      SnarelingGlobEntity snowballentity = new SnarelingGlobEntity(this.level, this);
	      double d0 = p_82196_1_.getEyeY() - 1.75F;
	      double d1 = p_82196_1_.getX() - this.getX();
	      double d2 = d0 - snowballentity.getY();
	      double d3 = p_82196_1_.getZ() - this.getZ();
	      float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
	      snowballentity.shoot(d1, d2 + (double)f, d3, 1.6F, 2.0F);
	      this.playSound(ModSoundEvents.SNARELING_SHOOT.get(), 2.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
	      this.level.addFreshEntity(snowballentity);
	   }
	   
	   protected void defineSynchedData() {
		      super.defineSynchedData();
		      this.entityData.define(SHOOT_TIME, 0);
		   }
	   
	   public int getShootTime() {
		      return this.entityData.get(SHOOT_TIME);
		   }

		   public void setShootTime(int p_189794_1_) {
		      this.entityData.set(SHOOT_TIME, p_189794_1_);
		   }
	   
		@Override
		public void registerControllers(AnimationData data) {
			data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
		}
	   
		private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.getShootTime() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("snareling_shoot", true));
			} else if (this.isAttacking() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("snareling_attack", true));
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("snareling_walk", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("snareling_idle", true));
			}
			return PlayState.CONTINUE;
		}
		
		@Override
		public AnimationFactory getFactory() {
			return factory;
		}
		
		class AttackGoal extends MeleeAttackGoal {
			   
			   public final EntityPredicate slimePredicate = (new EntityPredicate()).range(20.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();
			   
			      public AttackGoal() {
			         super(SnarelingEntity.this, 1.0D, true);
			      }
				      
				    public boolean canContinueToUse() {
						    return SnarelingEntity.this.getShootTime() > 0 ? false : super.canContinueToUse();
				    	}
			      
			      protected double getAttackReachSqr(LivingEntity p_179512_1_) {
			          return (double)(this.mob.getBbWidth() * 3.0F * this.mob.getBbWidth() * 3.0F + p_179512_1_.getBbWidth());
			       }

			      
			      protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
			         double d0 = this.getAttackReachSqr(p_190102_1_);
			         if (p_190102_2_ <= d0 && this.isTimeToAttack() && SnarelingEntity.this.getShootTime() <= 0) {
			            this.resetAttackCooldown();
			            this.mob.doHurtTarget(p_190102_1_);
			         } else if (p_190102_2_ <= d0 * 1.5D) {
			            if (this.isTimeToAttack()) {
			               this.resetAttackCooldown();
			            }

			            if (this.getTicksUntilNextAttack() <= 30) {
			            	SnarelingEntity.this.setAttacking(30);
			            }
			         } else {
			            this.resetAttackCooldown();
			         }
			      }
		   }
		
		public class AvoidEntityGoal<T extends LivingEntity> extends Goal {
			   protected final CreatureEntity mob;
			   private final double walkSpeedModifier;
			   private final double sprintSpeedModifier;
			   protected LivingEntity toAvoid;
			   protected final float maxDist;
			   protected Path path;
			   protected final PathNavigator pathNav;
			   protected final Predicate<LivingEntity> avoidPredicate;
			   protected final Predicate<LivingEntity> predicateOnAvoidEntity;
			   private final EntityPredicate avoidEntityTargeting;

			   public AvoidEntityGoal(CreatureEntity p_i46404_1_, float p_i46404_3_, double p_i46404_4_, double p_i46404_6_) {
			      this(p_i46404_1_, (p_200828_0_) -> {
			         return true;
			      }, p_i46404_3_, p_i46404_4_, p_i46404_6_, EntityPredicates.NO_CREATIVE_OR_SPECTATOR::test);
			   }

			   public AvoidEntityGoal(CreatureEntity p_i48859_1_, Predicate<LivingEntity> p_i48859_3_, float p_i48859_4_, double p_i48859_5_, double p_i48859_7_, Predicate<LivingEntity> p_i48859_9_) {
			      this.mob = p_i48859_1_;
			      this.avoidPredicate = p_i48859_3_;
			      this.maxDist = p_i48859_4_;
			      this.walkSpeedModifier = p_i48859_5_;
			      this.sprintSpeedModifier = p_i48859_7_;
			      this.predicateOnAvoidEntity = p_i48859_9_;
			      this.pathNav = p_i48859_1_.getNavigation();
			      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
			      this.avoidEntityTargeting = (new EntityPredicate()).range((double)p_i48859_4_).selector(p_i48859_9_.and(p_i48859_3_));
			   }

			   public AvoidEntityGoal(CreatureEntity p_i48860_1_, float p_i48860_3_, double p_i48860_4_, double p_i48860_6_, Predicate<LivingEntity> p_i48860_8_) {
			      this(p_i48860_1_, (p_203782_0_) -> {
			         return true;
			      }, p_i48860_3_, p_i48860_4_, p_i48860_6_, p_i48860_8_);
			   }

			   public boolean canUse() {
			      this.toAvoid = SnarelingEntity.this.getTarget();
			      if (this.toAvoid == null || this.mob.distanceTo(this.toAvoid) > this.maxDist) {
			         return false;
			      } else {
			         Vector3d vector3d = RandomPositionGenerator.getPosAvoid(this.mob, 16, 7, this.toAvoid.position());
			         if (vector3d == null) {
			            return false;
			         } else if (this.toAvoid.distanceToSqr(vector3d.x, vector3d.y, vector3d.z) < this.toAvoid.distanceToSqr(this.mob)) {
			            return false;
			         } else {
			            this.path = this.pathNav.createPath(vector3d.x, vector3d.y, vector3d.z, 0);
			            return SnarelingEntity.this.getTarget() != null && SnarelingEntity.this.getTarget().isAlive() && !SnarelingEntity.this.getTarget().hasEffect(ModEffects.ENSNARED.get()) && this.path != null;
			         }
			      }
			   }

			   public boolean canContinueToUse() {
			      return SnarelingEntity.this.getTarget() != null && SnarelingEntity.this.getTarget().isAlive() &&  !SnarelingEntity.this.getTarget().hasEffect(ModEffects.ENSNARED.get()) && !this.pathNav.isDone();
			   }

			   public void start() {
			      this.pathNav.moveTo(this.path, this.walkSpeedModifier);
			   }

			   public void stop() {
			      this.toAvoid = null;
			   }

			   public void tick() {
				   SnarelingEntity.this.setShootTime(0);
			      if (this.mob.distanceToSqr(this.toAvoid) < 49.0D) {
			         this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
			      } else {
			         this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
			      }

			   }
			}

}
