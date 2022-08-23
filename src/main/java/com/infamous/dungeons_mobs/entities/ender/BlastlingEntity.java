package com.infamous.dungeons_mobs.entities.ender;

import java.util.EnumSet;
import java.util.function.Predicate;

import com.infamous.dungeons_mobs.entities.projectiles.BlastlingBulletEntity;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
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

public class BlastlingEntity extends AbstractEnderlingEntity implements IAnimatable, IRangedAttackMob {
	
	public static final DataParameter<Integer> SHOOT_TIME = EntityDataManager.defineId(BlastlingEntity.class, DataSerializers.INT);
	
	AnimationFactory factory = new AnimationFactory(this);
	
	public float flameTicks;

	public BlastlingEntity(EntityType<? extends BlastlingEntity> p_i50210_1_, World p_i50210_2_) {
		super(p_i50210_1_, p_i50210_2_);
	}
	
	   protected void registerGoals() {
		      this.goalSelector.addGoal(0, new SwimGoal(this));
		      this.goalSelector.addGoal(0, new RangedAttackGoal(this, 1.15D, 20, 10.0F));
		      this.goalSelector.addGoal(0, new BlastlingEntity.AvoidEntityGoal<>(this, 5, 1.0D, 1.0D));
		      this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
		      this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		      this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		      this.targetSelector.addGoal(1, new AbstractEnderlingEntity.FindPlayerGoal(this, null));
		      this.targetSelector.addGoal(2, new HurtByTargetGoal(this, AbstractEnderlingEntity.class).setAlertOthers().setUnseenMemoryTicks(500));
		      this.targetSelector.addGoal(1, new EnderlingTargetGoal<>(this, PlayerEntity.class, true).setUnseenMemoryTicks(500));

		      //this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, AbstractEndermanVariant.class, true, false));
		   }
	   
	   public CreatureAttribute getMobType() {
		      return CreatureAttribute.UNDEAD;
		   }
	   
	   public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		      return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, (double)0.225F).add(Attributes.ATTACK_DAMAGE, 7.0D).add(Attributes.FOLLOW_RANGE, 32.0D);
		   }
	   
	protected SoundEvent getAmbientSound() {
		return ModSoundEvents.BLASTLING_IDLE.get();
	}
	
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return ModSoundEvents.BLASTLING_HURT.get();
	}
	
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.BLASTLING_DEATH.get();
	}
	
	   @Override
	protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
		   this.playSound(this.getStepSound(), 0.75F, 1.0F);
	}
	   
	   protected SoundEvent getStepSound() {
		      return ModSoundEvents.BLASTLING_STEP.get();
		   }
	   
	public void baseTick() {
		super.baseTick();
		
		flameTicks = flameTicks + 0.25F;
		
	    if (this.getTarget() != null && this.getShootTime() <= 2) {
	    	this.setShootTime(15);
	    	//this.playSound(SoundEvents.EVOKER_CAST_SPELL, 3.0F, 1.0F);
	    }
	    
		if (this.getShootTime() > 0) {
			this.setShootTime(this.getShootTime() - 1);
		}
		
		if (!this.level.isClientSide && (this.getShootTime() == 2 || this.getShootTime() == 8) && this.getTarget() != null && this.getTarget().isAlive()) {
	         
            this.shoot(this.getShootTime() == 2, this.getTarget());
		}
	}
	
	   private void shoot(boolean leftArm, LivingEntity p_82216_2_) {
		      this.readyShoot(leftArm, p_82216_2_.getX(), p_82216_2_.getY() + (double)p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.getZ());
		      this.playSound(ModSoundEvents.BLASTLING_SHOOT.get(), 2.0F, this.getVoicePitch());
		   }
	
	   private void readyShoot(boolean p_82209_1_, double p_82209_2_, double p_82209_4_, double p_82209_6_) {
	      double d0 = this.getHeadX(p_82209_1_);
	      double d1 = this.getHeadY(p_82209_1_);
	      double d2 = this.getHeadZ(p_82209_1_);
	      double d3 = p_82209_2_ - d0;
	      double d4 = p_82209_4_ - d1;
	      double d5 = p_82209_6_ - d2;
	      BlastlingBulletEntity witherskullentity = new BlastlingBulletEntity(this.level, this, d3, d4, d5);
	      witherskullentity.setOwner(this);

	      witherskullentity.setPosRaw(d0, d1, d2);
	      this.level.addFreshEntity(witherskullentity);
	}
	   
	   private double getHeadX(boolean p_82214_1_) {
		         float f = (this.yBodyRot + (float)(180 * (p_82214_1_ ? 0 : 0.75))) * ((float)Math.PI / 180F);
		         float f1 = MathHelper.cos(f);
		         return this.getX() + (double)f1 * 1.3D;
		   }

		   private double getHeadY(boolean p_82208_1_) {
		      return this.getY() + 1.75D;
		   }

		   private double getHeadZ(boolean p_82213_1_) {
		         float f = (this.yBodyRot + (float)(180 * (p_82213_1_ ? 0 : 0.75))) * ((float)Math.PI / 180F);
		         float f1 = MathHelper.sin(f);
		         return this.getZ() + (double)f1 * 1.3D;
		   }
	
	public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {

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
				event.getController().setAnimation(new AnimationBuilder().addAnimation("blastling_shoot", true));
			} else {
				if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
						event.getController().setAnimation(new AnimationBuilder().addAnimation("blastling_walk", true));
				} else {
					    event.getController().setAnimation(new AnimationBuilder().addAnimation("blastling_idle", true));
				}
			}
			return PlayState.CONTINUE;
		}
		
		@Override
		public AnimationFactory getFactory() {
			return factory;
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
			      this.toAvoid = BlastlingEntity.this.getTarget();
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
			            return BlastlingEntity.this.getTarget() != null && BlastlingEntity.this.getTarget().isAlive() && this.path != null;
			         }
			      }
			   }

			   public boolean canContinueToUse() {
			      return BlastlingEntity.this.getTarget() != null && BlastlingEntity.this.getTarget().isAlive() && !this.pathNav.isDone();
			   }

			   public void start() {
			      this.pathNav.moveTo(this.path, this.walkSpeedModifier);
			   }

			   public void stop() {
			      this.toAvoid = null;
			   }

			   public void tick() {
				   BlastlingEntity.this.setShootTime(0);
			      if (this.mob.distanceToSqr(this.toAvoid) < 49.0D) {
			         this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
			      } else {
			         this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
			      }

			   }
			}

}
