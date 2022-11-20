package com.infamous.dungeons_mobs.entities.jungle;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.entities.projectiles.PoisonQuillEntity;
import com.infamous.dungeons_mobs.entities.summonables.AreaDamageEntity;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PositionUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
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

public class PoisonQuillVineEntity extends AbstractVineEntity {

	AnimationFactory factory = new AnimationFactory(this);

	public int delayedBehaviourTime;
	
	public int shootAnimationTick;
	public int shootAnimationLength = 18;
	public int shootAnimationActionPoint = 8;
	
	public boolean open;
	
	public PoisonQuillVineEntity(EntityType<? extends PoisonQuillVineEntity> p_i50147_1_, World p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PoisonQuillVineEntity.OpenGoal());
		this.goalSelector.addGoal(0, new PoisonQuillVineEntity.CloseGoal());
		this.goalSelector.addGoal(1, new PoisonQuillVineEntity.ShootAttackGoal(this));
		this.goalSelector.addGoal(6, new LookAtTargetGoal(this));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
	}
	
	   protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
		      return this.isOut() ? this.getBbHeight() - 0.75F : super.getStandingEyeHeight(p_213348_1_, p_213348_2_);
		   }
	
	   protected BodyController createBodyControl() {
		      return new PoisonQuillVineEntity.BodyHelperController(this);
		   }
	   
	   class BodyHelperController extends BodyController {
		      public BodyHelperController(MobEntity p_i50612_2_) {
		         super(p_i50612_2_);
		      }

		      public void clientTick() {
		      }
		   }

	   public int getMaxHeadXRot() {
		      return 180;
		   }

		   public int getMaxHeadYRot() {
		      return 180;
		   }


	public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
		return MonsterEntity.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 25.0D)
				.add(Attributes.FOLLOW_RANGE, 25.0D);
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", this.getAnimationTransitionTime(), this::predicate));
	}
	
	@Override
	public int getAnimationTransitionTime() {
		return 5;
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.deathTime > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("poison_quill_vine_retract", true));
		} else if (this.burstAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("poison_quill_vine_burst", true));
		} else if (this.retractAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("poison_quill_vine_retract", true));
		} else if (this.shootAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("poison_quill_vine_shoot", true));
		} else {
			if (this.isOut() || this.burstAnimationTick > 0) {
				if (this.open) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("poison_quill_vine_idle_open", true));
				} else {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("poison_quill_vine_idle", true));
				}
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("poison_quill_vine_idle_underground", true));
			}
		}
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public int getBurstAnimationLength() {
		return 20;
	}

	@Override
	public int getRetractAnimationLength() {
		return 15;
	}

	@Override
	protected SoundEvent getAmbientSoundFoley() {
		return null;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return ModSoundEvents.POISON_QUILL_VINE_IDLE.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return ModSoundEvents.POISON_QUILL_VINE_HURT_VOCAL.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.POISON_QUILL_VINE_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSoundFoley(DamageSource p_184601_1_) {
		return ModSoundEvents.POISON_QUILL_VINE_HURT_FOLEY.get();
	}

	@Override
	public SoundEvent getBurstSound() {
		return ModSoundEvents.POISON_QUILL_VINE_BURST.get();
	}

	@Override
	public SoundEvent getRetractSound() {
		return ModSoundEvents.POISON_QUILL_VINE_BURST.get();
	}

    public SoundEvent getShootSound() {
    	return ModSoundEvents.POISON_QUILL_VINE_SHOOT.get();
    }
    
    public SoundEvent getOpenSound() {
    	return ModSoundEvents.POISON_QUILL_VINE_OPEN.get();
    }
    
    public SoundEvent getCloseSound() {
    	return ModSoundEvents.POISON_QUILL_VINE_CLOSE.get();
    }
    
	@Override
	public SoundEvent getBurstSoundFoley() {
		return null;
	}

	@Override
	public SoundEvent getRetractSoundFoley() {
		return null;
	}
	
	@Override
	public boolean isKelp() {
		return false;
	}

	@Override
	public boolean shouldDieInWrongHabitat() {
		return true;
	}

	@Override
	public int wrongHabitatDieChance() {
		return 100;
	}
	
    public int getLengthInSegments() {
        return MathHelper.clamp(this.entityData.get(LENGTH), 2, 26);
    }

	@Override
	public void spawnAreaDamage() {
		AreaDamageEntity areaDamage = AreaDamageEntity.spawnAreaDamage(this.level, this.position(), this, 5.0F, DamageSource.mobAttack(this), 0.0F, 1.5F, 0.25F, 0.25F, 10, false, false, 0.75, 0.25, false, 0, 1);
		this.level.addFreshEntity(areaDamage);
	}

	@Override
	public void setDefaultFeatures() {
		this.setLengthInSegments(2 + this.random.nextInt(3));
		this.setVanishes(false);
		this.setAlwaysOut(false);
		this.setDetectionDistance(7.5F);
	}

	@Override
	public float getExtraHitboxY() {
		return 1.625F;
	}
	
	@Override
	public void burst() {
		super.burst();
		this.delayedBehaviourTime = 10;
	}
	
	@Override
	public void retract() {
		super.retract();
		this.delayedBehaviourTime = 20;
	}
	
    @Override
    public void handleEntityEvent(byte p_70103_1_) {
    	if (p_70103_1_ == 7) {
    		this.open = true;
    	} else if (p_70103_1_ == 8) {
    		this.open = false;
    	} else if (p_70103_1_ == 9) {
    		this.shootAnimationTick = this.shootAnimationLength;
    	} else {
    		super.handleEntityEvent(p_70103_1_);
    	}
    }
    
    @Override
    public void baseTick() {
    	super.baseTick();
    	
    	if (this.delayedBehaviourTime > 0) {
    		this.delayedBehaviourTime --;
    	}
    }
    
    @Override
    public void tickDownAnimTimers() {
    	super.tickDownAnimTimers();
    	
    	if (this.shootAnimationTick > 0) {
    		this.shootAnimationTick --;
    	}
    }

	@Override
	public float distanceTo(Entity p_70032_1_) {
		float f = (float)(this.getX() - p_70032_1_.getX());
		float f1 = (float)(this.getEyeY() - p_70032_1_.getY());
		float f2 = (float)(this.getZ() - p_70032_1_.getZ());
		return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
	}
    
	class OpenGoal extends Goal {

		public OpenGoal() {

		}

		@Override
		public boolean canUse() {
			return PoisonQuillVineEntity.this.isOut() && PoisonQuillVineEntity.this.delayedBehaviourTime <= 0 && PoisonQuillVineEntity.this.getTarget() != null && !PoisonQuillVineEntity.this.open && PoisonQuillVineEntity.this.distanceTo(PoisonQuillVineEntity.this.getTarget()) <= 15;
		}
		
		@Override
		public void start() {
			super.start();
			PoisonQuillVineEntity.this.open = true;
			PoisonQuillVineEntity.this.level.broadcastEntityEvent(PoisonQuillVineEntity.this, (byte) 7);
			PoisonQuillVineEntity.this.delayedBehaviourTime = 10;
			PoisonQuillVineEntity.this.playSound(PoisonQuillVineEntity.this.getOpenSound(), 1.0F, 1.0F);
		}
	}
	
	class CloseGoal extends Goal {

		public CloseGoal() {

		}

		@Override
		public boolean canUse() {
			return PoisonQuillVineEntity.this.isOut() && PoisonQuillVineEntity.this.delayedBehaviourTime <= 0 && PoisonQuillVineEntity.this.open && (PoisonQuillVineEntity.this.getTarget() == null || PoisonQuillVineEntity.this.distanceTo(PoisonQuillVineEntity.this.getTarget()) > 17.5);
		}
		
		@Override
		public void start() {
			super.start();
			PoisonQuillVineEntity.this.open = false;
			PoisonQuillVineEntity.this.level.broadcastEntityEvent(PoisonQuillVineEntity.this, (byte) 8);
			PoisonQuillVineEntity.this.delayedBehaviourTime = 40;
			PoisonQuillVineEntity.this.playSound(PoisonQuillVineEntity.this.getCloseSound(), 1.0F, 1.0F);
		}
	}
	
	class ShootAttackGoal extends Goal {
		public PoisonQuillVineEntity mob;
		@Nullable
		public LivingEntity target;
	      
		public ShootAttackGoal(PoisonQuillVineEntity mob) {
			this.mob = mob;
			this.target = mob.getTarget();
		}

		@Override
		public boolean isInterruptable() {
			return false;
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public boolean canUse() {
			target = mob.getTarget();
			return mob.open && mob.delayedBehaviourTime <= 0 && target != null && mob.distanceTo(target) <= 12.5 && mob.canSee(target) && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return mob.open && mob.delayedBehaviourTime <= 0 && target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.shootAnimationTick = mob.shootAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 9);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			this.mob.getNavigation().stop();

			if (target != null && mob.shootAnimationTick == mob.shootAnimationActionPoint) {
				Vector3d pos = PositionUtils.getOffsetPos(mob, 0, mob.getStandingEyeHeight(mob.getPose(), mob.getDimensions(mob.getPose())), 1.0, yHeadRot);
				double d1 = target.getX() - pos.x;
				double d2 = target.getY(0.6D) - pos.y;
				double d3 = target.getZ() - pos.z;
				PoisonQuillEntity poisonQuill = new PoisonQuillEntity(mob.level, mob, d1, d2, d3);
				poisonQuill.setKelp(mob.isKelp());
				poisonQuill.rotateToMatchMovement();
				poisonQuill.moveTo(pos.x, pos.y, pos.z);
				mob.level.addFreshEntity(poisonQuill);
				mob.playSound(mob.getShootSound(), 1.25F, 1.0F);
			}
		}

		public boolean animationsUseable() {
			return mob.shootAnimationTick <= 0;
		}

	}
}