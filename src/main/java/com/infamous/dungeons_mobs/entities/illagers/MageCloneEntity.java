package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_mobs.entities.projectiles.MageMissileEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PositionUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;

import net.minecraft.world.entity.Entity.RemovalReason;

public class MageCloneEntity extends AbstractIllager implements IAnimatable, SpawnArmoredMob {

	private static final EntityDataAccessor<Boolean> DELAYED_APPEAR = SynchedEntityData.defineId(MageCloneEntity.class,
			EntityDataSerializers.BOOLEAN);
	
	public int shootAnimationTick;
	public int shootAnimationLength = 40;
	public int shootAnimationActionPoint = 20;

	public int appearAnimationTick;
	public int appearAnimationLength = 25;
	
	public int lifeTime;
	
	   private Mob owner;

	AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public MageCloneEntity(EntityType<? extends MageCloneEntity> type, Level world) {
		super(type, world);
		this.xpReward = 0;
	}
	
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new MageCloneEntity.RemainStationaryGoal());
        this.goalSelector.addGoal(1, new MageCloneEntity.ShootAttackGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, AbstractVillager.class, 5.0F, 1.2D, 1.15D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 5.0F, 1.2D, 1.2D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, IronGolem.class, 5.0F, 1.3D, 1.15D));
        this.goalSelector.addGoal(3, new ApproachTargetGoal(this, 14, 1.0D, true));
        this.goalSelector.addGoal(4, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.targetSelector.addGoal(1, new MageCloneEntity.CopyOwnerTargetGoal(this));
    }
	
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		
		this.entityData.define(DELAYED_APPEAR, false);
	}
	
	public boolean hasDelayedAppear() {
		return this.entityData.get(DELAYED_APPEAR);
	}

	public void setDelayedAppear(boolean attached) {
		this.entityData.set(DELAYED_APPEAR, attached);
	}
	
	@Override
	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		if (p_70097_1_.getEntity() != null && this.isAlliedTo(p_70097_1_.getEntity()) && p_70097_1_ != DamageSource.OUT_OF_WORLD) {
			return false;
		} else {
			return super.hurt(p_70097_1_, p_70097_2_);
		}
	}
	
	   public Mob getOwner() {
		      return this.owner;
		   }
	   
	   public void setOwner(Mob p_190658_1_) {
		      this.owner = p_190658_1_;
		   }
	
	@Override
	   protected void tickDeath() {
	      ++this.deathTime;
	      if (this.deathTime == 1) {
	         this.remove(RemovalReason.DISCARDED);
	         for(int i = 0; i < 20; ++i) {
	            double d0 = this.random.nextGaussian() * 0.02D;
	            double d1 = this.random.nextGaussian() * 0.02D;
	            double d2 = this.random.nextGaussian() * 0.02D;
	            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
	         }
	      }

	   }
	
	public boolean shouldBeStationary() {
		return this.appearAnimationTick > 0;
	}

	@Override
	public boolean isLeftHanded() {
		return false;
	}

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.FOLLOW_RANGE, 30.0D).add(Attributes.MAX_HEALTH, 40.0D);
	}

	public void handleEntityEvent(byte p_28844_) {
		if (p_28844_ == 4) {
			this.shootAnimationTick = shootAnimationLength;
		} else if (p_28844_ == 8) {
			this.appearAnimationTick = appearAnimationLength;
		} else if (p_28844_ == 11) {
			for(int i = 0; i < 20; ++i) {
	            double d0 = this.random.nextGaussian() * 0.02D;
	            double d1 = this.random.nextGaussian() * 0.02D;
	            double d2 = this.random.nextGaussian() * 0.02D;
	            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
	         }
		} else {
			super.handleEntityEvent(p_28844_);
		}
	}

	public void baseTick() {
		super.baseTick();
		this.tickDownAnimTimers();
		
		this.lifeTime++;
		
		if (!this.level.isClientSide && this.hasDelayedAppear()) {
			this.appearAnimationTick = this.appearAnimationLength;
			this.level.broadcastEntityEvent(this, (byte) 8);
			this.setDelayedAppear(false);
		}
		
		int lifeTimeByDifficulty = this.level.getCurrentDifficultyAt(this.blockPosition()).getDifficulty().getId();
		
		if (!this.level.isClientSide && (this.hurtTime > 0 || ((this.lifeTime >= lifeTimeByDifficulty * 100) || this.getOwner() != null && (this.getOwner().isDeadOrDying() || this.getOwner().hurtTime > 0 || this.getOwner().getTarget() == null)))) {
			if (this.hurtTime > 0) {
				this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getVoicePitch());
			} else {
				this.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, this.getSoundVolume(), 1.0F);
			}
			this.remove(RemovalReason.DISCARDED);
			this.level.broadcastEntityEvent(this, (byte) 11);
		}
		
		if (!this.level.isClientSide && this.getOwner() != null) {
			this.setHealth(this.getOwner().getHealth());
		}
	}

	public void tickDownAnimTimers() {
		if (this.shootAnimationTick > 0) {
			this.shootAnimationTick--;
		}

		if (this.appearAnimationTick > 0) {
			this.appearAnimationTick--;
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
	}

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.appearAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_appear", EDefaultLoopTypes.LOOP));
        } else if (this.shootAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_shoot", EDefaultLoopTypes.LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_walk", EDefaultLoopTypes.LOOP));
        } else {
        	if (this.isCelebrating()) {
        		event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_celebrate", EDefaultLoopTypes.LOOP));
        	} else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("mage_idle", EDefaultLoopTypes.LOOP));       		
        	}
        }
        return PlayState.CONTINUE;
    }

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	/**
	 * Returns whether this Entity is on the same team as the given Entity.
	 */
	public boolean isAlliedTo(Entity entityIn) {
		if (super.isAlliedTo(entityIn)) {
			return true;
		} else if (entityIn instanceof LivingEntity
				&& ((LivingEntity) entityIn).getMobType() == MobType.ILLAGER) {
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
        return ModSoundEvents.ENCHANTER_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.ENCHANTER_HURT.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

	@Override
	public ArmorSet getArmorSet() {
		return ModItems.MAGE_ARMOR;
	}
	
	class ShootAttackGoal extends Goal {
        public MageCloneEntity mob;
        @Nullable
        public LivingEntity target;

        public ShootAttackGoal(MageCloneEntity mob) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
            this.mob = mob;
            this.target = mob.getTarget();
        }

        @Override
        public boolean isInterruptable() {
            return mob.shouldBeStationary();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();
            return target != null && !mob.shouldBeStationary() && mob.distanceTo(target) <= 16 && mob.distanceTo(target) > 5 && mob.hasLineOfSight(target) && animationsUseable();
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && !mob.shouldBeStationary() && !animationsUseable();
        }

        @Override
        public void start() {
            mob.shootAnimationTick = mob.shootAnimationLength;
            mob.level.broadcastEntityEvent(mob, (byte) 4);
        }

        @Override
        public void tick() {
            target = mob.getTarget();

            this.mob.getNavigation().stop();

            if (target != null && mob.shootAnimationTick == mob.shootAnimationActionPoint) {
                Vec3 pos = PositionUtils.getOffsetPos(mob, 0.3, 1.5, 0.5, mob.yBodyRot);
                double d1 = target.getX() - pos.x;
                double d2 = target.getY(0.6D) - pos.y;
                double d3 = target.getZ() - pos.z;
                MageMissileEntity mageMissile = new MageMissileEntity(mob.level, mob, d1 / 5, 5, d3 / 5);
                mageMissile.rotateToMatchMovement();
                mageMissile.moveTo(pos.x, pos.y, pos.z);
                mageMissile.setOwner(mob);
                mob.level.addFreshEntity(mageMissile);
                mob.playSound(ModSoundEvents.NECROMANCER_SHOOT.get(), 1.0F, 2.0F);
            }
        }

        public boolean animationsUseable() {
            return mob.shootAnimationTick <= 0;
        }

    }
	
	   class CopyOwnerTargetGoal extends TargetGoal {
		      private final TargetingConditions copyOwnerTargeting = TargetingConditions.forCombat().ignoreInvisibilityTesting();

		      public CopyOwnerTargetGoal(PathfinderMob p_i47231_2_) {
		         super(p_i47231_2_, false);
		      }

		      public boolean canUse() {
		         return MageCloneEntity.this.owner != null && MageCloneEntity.this.owner.getTarget() != null && this.canAttack(MageCloneEntity.this.owner.getTarget(), this.copyOwnerTargeting);
		      }

		      public void start() {
		    	  MageCloneEntity.this.setTarget(MageCloneEntity.this.owner.getTarget());
		         super.start();
		      }
		   }
	   
	class RemainStationaryGoal extends Goal {

		public RemainStationaryGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET, Goal.Flag.JUMP));
		}

		@Override
		public boolean canUse() {
			return MageCloneEntity.this.shouldBeStationary();
		}
	}
	
}