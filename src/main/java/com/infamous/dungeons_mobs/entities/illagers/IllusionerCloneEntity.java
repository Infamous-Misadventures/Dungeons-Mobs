package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
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

public class IllusionerCloneEntity extends AbstractIllagerEntity implements IAnimatable, SpawnArmoredMob {

	private static final DataParameter<Boolean> DELAYED_APPEAR = EntityDataManager.defineId(IllusionerCloneEntity.class,
			DataSerializers.BOOLEAN);
	
	public int shootAnimationTick;
	public int shootAnimationLength = 38;
	public int shootAnimationActionPoint = 16;

	public int appearAnimationTick;
	public int appearAnimationLength = 20;
	
	public int lifeTime;
	
	   private MobEntity owner;

	AnimationFactory factory = new AnimationFactory(this);

	public IllusionerCloneEntity(World world) {
		super(ModEntityTypes.ILLUSIONER_CLONE.get(), world);
	}

	public IllusionerCloneEntity(EntityType<? extends IllusionerCloneEntity> type, World world) {
		super(type, world);
		this.xpReward = 0;
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(0, new IllusionerCloneEntity.RemainStationaryGoal());
		this.goalSelector.addGoal(1, new IllusionerCloneEntity.ShootAttackGoal(this));
		this.goalSelector.addGoal(2, new ApproachTargetGoal(this, 7.5, 1.0D, true));
		this.goalSelector.addGoal(3, new LookAtTargetGoal(this));
		this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.addGoal(1, new IllusionerCloneEntity.CopyOwnerTargetGoal(this));
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
	
	   public MobEntity getOwner() {
		      return this.owner;
		   }
	   
	   public void setOwner(MobEntity p_190658_1_) {
		      this.owner = p_190658_1_;
		   }
	
	@Override
	   protected void tickDeath() {
	      ++this.deathTime;
	      if (this.deathTime == 1) {
	         this.remove();
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

	public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.FOLLOW_RANGE, 25.0D).add(Attributes.MAX_HEALTH, 50.0D);
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
			this.remove();
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
		String suffix = "_uncrossed";
		if(IllagerArmsUtil.armorHasCrossedArms(this, this.getItemBySlot(EquipmentSlotType.CHEST))){
			suffix = "";
		}
		if (this.appearAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_appear"+suffix, true));
		} else if (this.shootAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_clone_shoot"+suffix, true));
		} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_walk"+suffix, true));
		} else {
			if (this.isCelebrating()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_celebrate", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("illusioner_idle"+suffix, true));
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
				&& ((LivingEntity) entityIn).getMobType() == CreatureAttribute.ILLAGER) {
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
	public SoundEvent getCelebrateSound() {
		return SoundEvents.ILLUSIONER_AMBIENT;
	}
	
	   public void shootArrow(LivingEntity target) {
		      {
		    	  ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.item.BowItem)));
		          AbstractArrowEntity abstractarrowentity = this.getArrow(itemstack, 0);
		          if (this.getMainHandItem().getItem() instanceof net.minecraft.item.BowItem)
		             abstractarrowentity = ((net.minecraft.item.BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
		          double d0 = target.getX() - this.getX();
		          double d1 = target.getY(0.3333333333333333D) - abstractarrowentity.getY();
		          double d2 = target.getZ() - this.getZ();
		          double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
		          abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
		          this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		          this.level.addFreshEntity(abstractarrowentity);
		      }
		   }
	   
	   protected AbstractArrowEntity getArrow(ItemStack p_213624_1_, float p_213624_2_) {
		      return ProjectileHelper.getMobArrow(this, p_213624_1_, p_213624_2_);
		   }

	@Override
	public ResourceLocation getArmorSet() {
		return ModItems.ILLUSIONER_ARMOR.getArmorSet();
	}

	class ShootAttackGoal extends Goal {
		public IllusionerCloneEntity mob;
		@Nullable
		public LivingEntity target;

		public int cooldown;
		
		public ShootAttackGoal(IllusionerCloneEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
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

			if (this.cooldown > 0) {
				this.cooldown --;
			}
			
			return target != null && mob.distanceTo(target) <= 12.5 && this.cooldown <= 0 && mob.canSee(target) && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.shootAnimationTick = mob.shootAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 4);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			mob.getNavigation().stop();
			
			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.shootAnimationTick == mob.shootAnimationActionPoint) {
	            mob.shootArrow(target);
			}
		}
		
		@Override
			public void stop() {
				super.stop();
				this.cooldown = 20 + mob.random.nextInt(40);
			}

		public boolean animationsUseable() {
			return mob.shootAnimationTick <= 0;
		}

	}
	
	   class CopyOwnerTargetGoal extends TargetGoal {
		      private final EntityPredicate copyOwnerTargeting = (new EntityPredicate()).allowUnseeable().ignoreInvisibilityTesting();

		      public CopyOwnerTargetGoal(CreatureEntity p_i47231_2_) {
		         super(p_i47231_2_, false);
		      }

		      public boolean canUse() {
		         return IllusionerCloneEntity.this.owner != null && IllusionerCloneEntity.this.owner.getTarget() != null && this.canAttack(IllusionerCloneEntity.this.owner.getTarget(), this.copyOwnerTargeting);
		      }

		      public void start() {
		    	  IllusionerCloneEntity.this.setTarget(IllusionerCloneEntity.this.owner.getTarget());
		         super.start();
		      }
		   }
	
	class RemainStationaryGoal extends Goal {

		public RemainStationaryGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET, Goal.Flag.JUMP));
		}

		@Override
		public boolean canUse() {
			return IllusionerCloneEntity.this.shouldBeStationary();
		}
	}
	
}