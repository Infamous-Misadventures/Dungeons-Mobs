package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.entities.illagers.minibosses.MinecraftIllusionerEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
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
import java.util.UUID;

public class IllusionerCloneCloneEntity extends SpellcastingIllagerEntity implements IAnimatable,IRangedAttackMob {

	public static final DataParameter<Integer> LIFT_TICKS = EntityDataManager.defineId(IllusionerCloneCloneEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> DUPLICATE_TICKS = EntityDataManager.defineId(IllusionerCloneCloneEntity.class, DataSerializers.INT);
	public static final DataParameter<Boolean> ANGRY = EntityDataManager.defineId(IllusionerCloneCloneEntity.class, DataSerializers.BOOLEAN);

	public int liftInterval = 0;
	public int duplicateInterval = 0;


	private LivingEntity caster;
	private UUID casterUuid;
	private int lifeTicks;
	AnimationFactory factory = new AnimationFactory(this);

	public IllusionerCloneCloneEntity(World world){
		super(ModEntityTypes.ILLUSIONER_CLONE_C.get(), world);
	}

	public IllusionerCloneCloneEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
		super(type, world);
	}

	public IllusionerCloneCloneEntity(World worldIn, LivingEntity caster, int lifeTicks, LivingEntity target) {
		this(ModEntityTypes.ILLUSIONER_CLONE_C.get(), worldIn);
		this.setCaster(caster);
		this.lifeTicks = lifeTicks;
	}

	public void setCaster(@Nullable LivingEntity caster) {
		this.caster = caster;
		this.casterUuid = caster == null ? null : caster.getUUID();
	}

	@Override
	public boolean canJoinRaid() {
		return false;
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
		for(int i = 0; i < 24; ++i) {
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			double d2 = this.random.nextGaussian() * 0.02D;
			this.level.addParticle(ParticleTypes.CLOUD, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
		}
	}

	public void aiStep() {
		super.aiStep();
		if(!this.level.isClientSide){
			this.lifeTicks--;
			if(this.lifeTicks <= 0 || this.getCaster() == null || !(this.getCaster().hurtTime <= 0 && this.getCaster() != null) || (!this.getCaster().isAlive() && this.getCaster() != null)){
				this.remove();
			}
		}
	}

	@Override
	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		if (!this.isInvulnerable()) {
			this.remove();
		}
		return false;
	}


	@Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
		this.goalSelector.addGoal(5, new IllusionerCloneCloneEntity.RkGoal(this,1));
		this.goalSelector.addGoal(1, new IllusionerCloneCloneEntity.CastingSpellGoal());
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 5.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(4, new IllusionerCloneCloneEntity.LiftMobGoal());
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

		public IllusionerCloneCloneEntity v = IllusionerCloneCloneEntity.this;

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

		public IllusionerCloneCloneEntity.RkGoal setMaxAttackTick(int max) {
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

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
		return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
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

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
	}

	class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (IllusionerCloneCloneEntity.this.getTarget() != null) {
                IllusionerCloneCloneEntity.this.getLookControl().setLookAt(IllusionerCloneCloneEntity.this.getTarget(), (float) IllusionerCloneCloneEntity.this.getMaxHeadYRot(), (float) IllusionerCloneCloneEntity.this.getMaxHeadXRot());
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
	         return (IllusionerCloneCloneEntity.this.getCaster() != null &&((MinecraftIllusionerEntity)IllusionerCloneCloneEntity.this.getCaster()).getLiftTicks() > 0) &&
					 IllusionerCloneCloneEntity.this.getCaster() instanceof MinecraftIllusionerEntity &&
					 IllusionerCloneCloneEntity.this.getCaster() != null;
	      }
	      
	      public boolean canContinueToUse() {
	    	  	return IllusionerCloneCloneEntity.this.getLiftTicks() > 0;
	      }
	    
	    public void start() {
			  super.start();
			  this.d =true;
			  IllusionerCloneCloneEntity.this.setLiftTicks(21);
			  IllusionerCloneCloneEntity.this.entityData.set(ANGRY,false);
	    }
	      
	    public void tick() {
			  if (IllusionerCloneCloneEntity.this.getTarget() != null) {
				  IllusionerCloneCloneEntity.this.getLookControl().setLookAt(IllusionerCloneCloneEntity.this.getTarget(), (float) IllusionerCloneCloneEntity.this.getMaxHeadYRot(), (float) IllusionerCloneCloneEntity.this.getMaxHeadXRot());
			  }
			  LivingEntity target = IllusionerCloneCloneEntity.this.getTarget();
	    	IllusionerCloneCloneEntity mob = IllusionerCloneCloneEntity.this;
	    	
	    	mob.getNavigation().stop();

			mob.getNavigation().stop();

			if (mob.getLiftTicks() == 1 && this.d) {
				IllusionerCloneCloneEntity.this.setLiftTicks(14);
				IllusionerCloneCloneEntity.this.setAngry(true);
				this.d =false;
			}
	    }
	    
	    public void stop() {
			  super.stop();
			  IllusionerCloneCloneEntity.this.setAngry(false);
			  IllusionerCloneCloneEntity.this.setLiftTicks(0);
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
