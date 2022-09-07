package com.infamous.dungeons_mobs.entities.blaze;

import java.util.EnumSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
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

public class WildfireEntity extends MonsterEntity implements IAnimatable {

	private static final DataParameter<Integer> SHIELDS = EntityDataManager.defineId(WildfireEntity.class, DataSerializers.INT);
	private static final DataParameter<Float> SHIELD_HEALTH = EntityDataManager.defineId(WildfireEntity.class, DataSerializers.FLOAT);
	   
	public int shootAnimationTick;
	public int shootAnimationLength = 12;
	public int shootAnimationActionPoint = 5;
	
	public int shockwaveAnimationTick;
	public int shockwaveAnimationLength = 27;
	public int shockwaveAnimationActionPoint = 9;
	
	public int summonAnimationTick;
	public int summonAnimationLength = 47;
	public int summonAnimationActionPoint = 15;
	
	public int soundLoopTick;
	public int regenerateShieldTick;
	
	public int regenerateShieldTime = 150;
	public float individualShieldHealth = 15.0F;
	
    AnimationFactory factory = new AnimationFactory(this);
    
    private static final Predicate<Entity> NO_BLAZE_AND_ALIVE = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof BlazeEntity) && !(p_213685_0_ instanceof WildfireEntity);
     };
    
    public WildfireEntity(EntityType<? extends WildfireEntity> type, World world) {
        super(type, world);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
        this.setPathfindingMalus(PathNodeType.LAVA, 8.0F);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.xpReward = 25;
    }
    
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new WildfireEntity.ShockwaveAttackGoal(this));
        this.goalSelector.addGoal(2, new WildfireEntity.SummonBlazesGoal(this));
        this.goalSelector.addGoal(3, new WildfireEntity.ShootAttackGoal(this));
        this.goalSelector.addGoal(4, new ApproachTargetGoal(this, 10, 1.2D, true));
        this.goalSelector.addGoal(5, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(600));
    }
    
    private void shockwave() {
        if (this.isAlive()) {
           for(Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5.0D), NO_BLAZE_AND_ALIVE)) {
              if (!(entity instanceof AbstractIllagerEntity)) {
                 entity.hurt(DamageSource.mobAttack(this), 7.0F);
                 entity.setSecondsOnFire(3);
              }

              this.strongKnockback(entity);
           }
        }

     }

     private void strongKnockback(Entity p_213688_1_) {
        double d0 = p_213688_1_.getX() - this.getX();
        double d1 = p_213688_1_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(d0 / d2 * 3.0D, 0.2D, d1 / d2 * 3.0D);
     }
    
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_,
    		SpawnReason p_213386_3_, ILivingEntityData p_213386_4_, CompoundNBT p_213386_5_) {
    	this.setShieldHealth(individualShieldHealth * 4);
    	this.setShields(4);
        this.populateDefaultEquipmentSlots(p_213386_2_);
        this.populateDefaultEquipmentEnchantments(p_213386_2_);
    	return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
        super.populateDefaultEquipmentSlots(p_180481_1_);
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.NETHERPLATE_ARMOR.getHead().get()));
    }
    
    public int getShields() {
        return MathHelper.clamp(this.entityData.get(SHIELDS), 0, 4);
     }

     public void setShields(int p_191997_1_) {
        this.entityData.set(SHIELDS, p_191997_1_);
     }
     
     public float getShieldHealth() {
         return this.entityData.get(SHIELD_HEALTH);
      }

      public void setShieldHealth(float p_191997_1_) {
         this.entityData.set(SHIELD_HEALTH, p_191997_1_);
      }

     protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHIELDS, 0);
        this.entityData.define(SHIELD_HEALTH, 0.0F);
     }

     public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        p_213281_1_.putInt("Shields", this.getShields());
        p_213281_1_.putFloat("ShieldHealth", this.getShieldHealth());
     }

     public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        this.setShields(p_70037_1_.getInt("Shields"));
        this.setShieldHealth(p_70037_1_.getFloat("ShieldHealth"));
     }
    
    @Override
    protected SoundEvent getAmbientSound() {
    	return ModSoundEvents.WILDFIRE_IDLE.get();
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    	return this.getShields() > 0 && p_184601_1_ != DamageSource.OUT_OF_WORLD && p_184601_1_ != DamageSource.DROWN ? SoundEvents.BLAZE_HURT : ModSoundEvents.WILDFIRE_HURT.get();
    }
    
    @Override
    protected SoundEvent getDeathSound() {
    	return ModSoundEvents.WILDFIRE_DEATH.get();
    }
    
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(ModSoundEvents.WILDFIRE_MOVE.get(), 0.15F, 1.0F);
     }
    
    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.FOLLOW_RANGE, 24D).add(Attributes.MAX_HEALTH, 50.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }
    
	public void handleEntityEvent(byte p_28844_) {
		if (p_28844_ == 4) {
			this.shootAnimationTick = shootAnimationLength;
		} else if (p_28844_ == 11) {
			this.shockwaveAnimationTick = shockwaveAnimationLength;
		} else if (p_28844_ == 9) {
			this.summonAnimationTick = summonAnimationLength;
		} else {
			super.handleEntityEvent(p_28844_);
		}
	}
	
	@Override
	public boolean isSensitiveToWater() {
		return true;
	}
	
	   public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
		      return false;
		   }
	
	@Override
	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		if (this.getShields() > 0 && p_70097_1_ != DamageSource.OUT_OF_WORLD && p_70097_1_ != DamageSource.DROWN) {
			this.setShieldHealth(this.getShieldHealth() - p_70097_2_);	
			
			if (this.getShieldHealth() < individualShieldHealth * 3 && this.getShields() > 3 ||
					this.getShieldHealth() < individualShieldHealth * 2 && this.getShields() > 2 ||
					this.getShieldHealth() < individualShieldHealth * 1 && this.getShields() > 1 ||
					this.getShieldHealth() <= individualShieldHealth * 0 && this.getShields() > 0) {
				this.breakShield();
			} else {
				this.playHurtSound(p_70097_1_);	
			}			
			
			return false;
		} else {			
			return super.hurt(p_70097_1_, p_70097_2_);
		}
	}
	
	boolean steepDropBelow() {
		boolean blockBeneath = false;
		
		for (int i = 0; i < 8; i++) {
			if (!this.level.getBlockState(new BlockPos(this.blockPosition().getX(), this.blockPosition().getY() - i, this.blockPosition().getZ())).isAir()) {
				blockBeneath = true;
			}
		}
		
		return !this.level.isClientSide && blockBeneath == false;
	}

    public void baseTick() {
        super.baseTick();
        this.tickDownAnimTimers();
        
        if (this.getShields() < 4 && this.regenerateShieldTick > 0) {
        	this.regenerateShieldTick --;
        	if (this.regenerateShieldTick == 0) {
        		this.regenerateShield();
        	}
        }
        
        if (this.getShields() > 0 && this.getShieldHealth() <= 0) {
        	this.breakShield();
        }
        
        if (this.getTarget() != null && ((!this.isOnGround() && steepDropBelow()) || this.getTarget().getY() > this.getY() + 3 || this.getY() < this.getTarget().getY() + 2 || this.distanceTo(this.getTarget()) > 15)) {
        	if (this.getY() < this.getTarget().getY() + 5) {
        		this.setDeltaMovement(0.0D, 0.04D, 0.0D);
        	} else {
        		this.setDeltaMovement(0.0D, -0.01D, 0.0D);
        	}

			double x = this.getTarget().getX() - this.getX();
			double y = this.getTarget().getY() - this.getY();
			double z = this.getTarget().getZ() - this.getZ();
			double d = Math.sqrt(x * x + y * y + z * z);
			this.setDeltaMovement(this.getDeltaMovement()
					.add(x / d * 0.20000000298023224D, y / d * 0.20000000298023224D, z / d * 0.20000000298023224D)
					.scale(0.4D));
			this.move(MoverType.SELF, this.getDeltaMovement());
			
			this.lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(this.getTarget().getX(), this.getTarget().getEyeY(), this.getTarget().getZ()));
        }
        
        	this.soundLoopTick ++;
        
        if (this.soundLoopTick % 100 == 0) {
        	this.playSound(ModSoundEvents.WILDFIRE_IDLE_LOOP.get(), 0.5F, 1.0F);
        }
    }
    
    public void aiStep() {
    	
        if (!this.onGround && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.3D, 1.0D));
         }

         if (this.level.isClientSide) {
            for(int i = 0; i < 2; ++i) {
               this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
         }
         
    	super.aiStep();
    }
    
    public void breakShield() {
		this.regenerateShieldTick = this.regenerateShieldTime;
		this.setShields(this.getShields() - 1);
		this.playSound(ModSoundEvents.WILDFIRE_SHIELD_BREAK.get(), 1.0F, 1.0F);
    }
    
    public void regenerateShield() {
		this.setShields(this.getShields() + 1);
		this.setShieldHealth(this.getShieldHealth() + individualShieldHealth);
		this.playSound(ModSoundEvents.WILDFIRE_SHOOT.get(), 1.0F, 1.0F);
		this.regenerateShieldTick = this.regenerateShieldTime;
    }
    
	public void tickDownAnimTimers() {
		if (this.shootAnimationTick > 0) {
			this.shootAnimationTick--;
		}
		
		if (this.shockwaveAnimationTick > 0) {
			this.shockwaveAnimationTick--;
		}
		
		if (this.summonAnimationTick > 0) {
			this.summonAnimationTick--;
		}
	}

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }


    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.shockwaveAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("wildfire_shockwave", true));
        } else if (this.summonAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("wildfire_summon", true));
        } else if (this.shootAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("wildfire_shoot", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("wildfire_idle", true));       		
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    
    class SummonBlazesGoal extends Goal {
		public WildfireEntity mob;
		@Nullable
		public LivingEntity target;
		
		public int blazeSummonRange = 3;
		public int closeBlazeSummonRange = 1;
		
	      private final EntityPredicate blazeCountTargeting = (new EntityPredicate()).range(30.0D).ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();
	      
		public SummonBlazesGoal(WildfireEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
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
			int nearbyBlazes = mob.level.getNearbyEntities(BlazeEntity.class, blazeCountTargeting, mob, mob.getBoundingBox().inflate(30.0D)).size();
			
			return target != null && mob.random.nextInt((80 * (nearbyBlazes + 1))) == 0 && nearbyBlazes < 3 && mob.canSee(target) && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.playSound(ModSoundEvents.WILDFIRE_MOVE.get(), 1.0F, mob.getVoicePitch());
			mob.summonAnimationTick = mob.summonAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 9);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.summonAnimationTick == mob.summonAnimationActionPoint) {
	            for (int i = 0; i < 1 + mob.random.nextInt(1); i++) {
	            	BlazeEntity summonedBlaze = EntityType.BLAZE.create(mob.level);
	            	BlockPos summonPos = mob.blockPosition().offset(-blazeSummonRange + mob.random.nextInt((blazeSummonRange * 2) + 1), 0, -blazeSummonRange + mob.random.nextInt((blazeSummonRange * 2) + 1));
	            	summonedBlaze.moveTo(summonPos, 0.0F, 0.0F);
	            	
	            	// RELOCATES BLAZE CLOSER TO WILDFIRE IF SPAWNED IN A POSITION THAT MAY HINDER ITS ABILITY TO JOIN IN THE BATTLE
	            	if (mob.isInWall() || !mob.canSee(target)) {
	            		summonPos = mob.blockPosition().offset(-closeBlazeSummonRange + mob.random.nextInt((closeBlazeSummonRange * 2) + 1), 0, -closeBlazeSummonRange + mob.random.nextInt((closeBlazeSummonRange * 2) + 1));
	            	}
	            	
	            	// RELOCATES BLAZE TO WILDFIRE'S POSITION STILL IN A POSITION THAT MAY HINDER ITS ABILITY TO JOIN IN THE BATTLE
	            	if (mob.isInWall() || !mob.canSee(target)) {
	            		summonPos = mob.blockPosition();
	            	}
	            	
	            	summonedBlaze.setTarget(target);
	            	summonedBlaze.finalizeSpawn(((ServerWorld)mob.level), mob.level.getCurrentDifficultyAt(summonPos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
	            	mob.playSound(ModSoundEvents.WILDFIRE_PROJECTILE_HIT.get(), 1.0F, 1.0F);
					if (mob.getTeam() != null) {
						Scoreboard scoreboard = mob.level.getScoreboard();
						scoreboard.addPlayerToTeam(summonedBlaze.getScoreboardName(), scoreboard.getPlayerTeam(mob.getTeam().getName()));
					}
					((ServerWorld)mob.level).addFreshEntityWithPassengers(summonedBlaze);
	            }
			}
		}

		public boolean animationsUseable() {
			return mob.summonAnimationTick <= 0;
		}

	}
    
    class ShockwaveAttackGoal extends Goal {
		public WildfireEntity mob;
		@Nullable
		public LivingEntity target;
	      
		public ShockwaveAttackGoal(WildfireEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
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
			return target != null && mob.isOnGround() && mob.random.nextInt(20) == 0 && mob.distanceTo(target) < 4 && mob.canSee(target) && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.playSound(ModSoundEvents.WILDFIRE_SHOCKWAVE.get(), 1.0F, mob.getVoicePitch());
			mob.shockwaveAnimationTick = mob.shockwaveAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 11);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.shockwaveAnimationTick == mob.shockwaveAnimationActionPoint) {
	            mob.shockwave();
			}
		}

		public boolean animationsUseable() {
			return mob.shockwaveAnimationTick <= 0;
		}

	}
    
    class ShootAttackGoal extends Goal {
		public WildfireEntity mob;
		@Nullable
		public LivingEntity target;
	      
		public ShootAttackGoal(WildfireEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
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
			return target != null && mob.random.nextInt(15) == 0 && mob.distanceTo(target) > 4 && mob.distanceTo(target) < 13 && mob.canSee(target) && animationsUseable();
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

			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.shootAnimationTick == mob.shootAnimationActionPoint) {
	               double d1 = target.getX() - mob.getX();
	               double d2 = target.getY(0.5D) - mob.getY(0.75D);
	               double d3 = target.getZ() - mob.getZ();
                SmallFireballEntity smallfireballentity = new SmallFireballEntity(mob.level, mob, d1, d2, d3);
                smallfireballentity.setPos(smallfireballentity.getX(), mob.getY(0.5D) + 0.5D, smallfireballentity.getZ());
                mob.level.addFreshEntity(smallfireballentity);
                mob.playSound(ModSoundEvents.WILDFIRE_SHOOT.get(), 1.0F, 1.0F);
			}
		}

		public boolean animationsUseable() {
			return mob.shootAnimationTick <= 0;
		}

	}
    
}
