package com.infamous.dungeons_mobs.entities.illagers;

import java.util.EnumSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.entities.projectiles.WindcallerBlastProjectileEntity;
import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.entities.summonables.WindcallerTornadoEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
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
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
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

public class WindcallerEntity extends AbstractIllagerEntity implements IAnimatable, SpawnArmoredMob {

	public int liftAttackAnimationTick;
	public int liftAttackAnimationLength = 25;
	public int liftAttackAnimationActionPoint = 15;
	
	public int blastAttackAnimationTick;
	public int blastAttackAnimationLength = 32;
	public int blastAttackAnimationActionPoint = 20;

    AnimationFactory factory = new AnimationFactory(this);

    public int soundLoopTick;
    
    public WindcallerEntity(World world){
        super(ModEntityTypes.WINDCALLER.get(), world);
    }

    public WindcallerEntity(EntityType<? extends WindcallerEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new WindcallerEntity.BlastAttackGoal(this));
        this.goalSelector.addGoal(1, new WindcallerEntity.LiftAttackGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, IronGolemEntity.class, 5.0F, 1.2D, 1.15D));
        this.goalSelector.addGoal(3, new ApproachTargetGoal(this, 6, 1.1D, true));
        this.goalSelector.addGoal(4, new LookAtTargetGoal(this));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(600));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(600));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false).setUnseenMemoryTicks(600));
    }
    
	public void handleEntityEvent(byte p_28844_) {
		if (p_28844_ == 4) {
			this.liftAttackAnimationTick = liftAttackAnimationLength;
		} else if (p_28844_ == 11) {
			this.blastAttackAnimationTick = blastAttackAnimationLength;
		} else {
			super.handleEntityEvent(p_28844_);
		}
	}
	
	boolean steepDropBelow() {
		boolean blockBeneath = false;
		
		for (int i = 0; i < 4; i++) {
			if (!this.level.getBlockState(new BlockPos(this.blockPosition().getX(), this.blockPosition().getY() - i, this.blockPosition().getZ())).isAir()) {
				blockBeneath = true;
			}
		}
		
		return !this.level.isClientSide && blockBeneath == false;
	}
	
	   public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
		      return false;
		   }

    public void baseTick() {
        super.baseTick();
        this.tickDownAnimTimers();
        
        if (this.getTarget() != null && this.distanceTo(this.getTarget()) > 4 && ((!this.isOnGround() && steepDropBelow()) || ((this.getTarget().getY() > this.getY() + 4 || this.getY() < this.getTarget().getY() - 2) && this.distanceTo(this.getTarget()) > 4) || this.distanceTo(this.getTarget()) > 10)) {
        	if (this.getY() < this.getTarget().getY() + 4) {
        		this.setDeltaMovement(0.0D, 0.05D, 0.0D);
        	} else {
        		this.setDeltaMovement(0.0D, -0.01D, 0.0D);
        	}

			double x = this.getTarget().getX() - this.getX();
			double y = this.getTarget().getY() - this.getY();
			double z = this.getTarget().getZ() - this.getZ();
			double d = Math.sqrt(x * x + y * y + z * z);
			this.setDeltaMovement(this.getDeltaMovement()
					.add(x / d * 0.20000000298023224D, y / d * 0.20000000298023224D, z / d * 0.20000000298023224D)
					.scale(1.5D));
			this.move(MoverType.SELF, this.getDeltaMovement());
			
			this.getNavigation().stop();
			this.lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(this.getTarget().getX(), this.getTarget().getEyeY(), this.getTarget().getZ()));
        }
        
        this.soundLoopTick ++;
        
        if (this.soundLoopTick % 40 == 0) {
        	this.playSound(ModSoundEvents.WINDCALLER_FLY_LOOP.get(), 0.75F, 1.0F);
        }
    }
    
    public void aiStep() {
    	
        if (!this.onGround && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.5D, 1.0D));
         }
        
        if (this.level.isClientSide) {
            this.level.addParticle(ModParticleTypes.WIND.get(), this.getRandomX(0.1D), this.getY() + 0.05D, this.getRandomZ(0.1D), (this.random.nextDouble() - 0.5D) * 1.0D, 0.0, (this.random.nextDouble() - 0.5D) * 1.0D);
        }
         
    	super.aiStep();
    }
    
	public void tickDownAnimTimers() {
		if (this.liftAttackAnimationTick > 0) {
			this.liftAttackAnimationTick--;
		}
		
		if (this.blastAttackAnimationTick > 0) {
			this.blastAttackAnimationTick--;
		}
	}

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }


    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
    	if (this.liftAttackAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("windcaller_lift", true));
    	} else if (this.blastAttackAnimationTick > 10) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("windcaller_blast", true));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("windcaller_fly", true));
        } else {
        	if (this.isCelebrating()) {
        		event.getController().setAnimation(new AnimationBuilder().addAnimation("windcaller_celebrate", true));
        	} else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("windcaller_idle", true));       		
        	}
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
        super.populateDefaultEquipmentSlots(p_180481_1_);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.WINDCALLER_STAFF.get()));
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.WINDCALLER_CLOTHES.getHead().get()));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.WINDCALLER_CLOTHES.getChest().get()));
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        ILivingEntityData iLivingEntityData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
        this.populateDefaultEquipmentSlots(p_213386_2_);
        this.populateDefaultEquipmentEnchantments(p_213386_2_);
        return iLivingEntityData;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.FOLLOW_RANGE, 20D).add(Attributes.MAX_HEALTH, 30.0D);
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
        return ModSoundEvents.WINDCALLER_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.WINDCALLER_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.WINDCALLER_HURT.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.WINDCALLER_BLAST_VOCAL.get();
    }

	@Override
	public ResourceLocation getArmorSet() {
		return ModItems.WINDCALLER_CLOTHES.getArmorSet();
	}

	class LiftAttackGoal extends Goal {
		public WindcallerEntity mob;
		@Nullable
		public LivingEntity target;
		
		private final Predicate<Entity> TORNADO = (p_33346_) -> {
			return p_33346_ instanceof WindcallerTornadoEntity;
		};
		
		public LiftAttackGoal(WindcallerEntity mob) {
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
			
			int nearbyTornadoes = 1;
			
			if (target != null) {
				nearbyTornadoes = mob.level.getEntities(mob, target.getBoundingBox().inflate(5.0D), TORNADO)
					.size();
			}
			
			return target != null && target.isOnGround() && mob.random.nextInt(30) == 0 && mob.distanceTo(target) <= 16 && mob.distanceTo(target) > 3 && nearbyTornadoes <= 0 && mob.canSee(target) && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.playSound(ModSoundEvents.WINDCALLER_LIFT_VOCAL.get(), 1.0F, mob.getVoicePitch());
			mob.liftAttackAnimationTick = mob.liftAttackAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 4);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.liftAttackAnimationTick == mob.liftAttackAnimationActionPoint) {
	            WindcallerTornadoEntity tornado = ModEntityTypes.TORNADO.get().create(mob.level);
	            tornado.moveTo(target.blockPosition(), 0, 0);
	            tornado.playSound(ModSoundEvents.WINDCALLER_LIFT_WIND.get(), 1.5F, 1.0F);
	            ((ServerWorld)mob.level).addFreshEntityWithPassengers(tornado);
			}
		}

		public boolean animationsUseable() {
			return mob.liftAttackAnimationTick <= 0;
		}

	}
    
    class BlastAttackGoal extends Goal {
		public WindcallerEntity mob;
		@Nullable
		public LivingEntity target;
		
		private final Predicate<Entity> TORNADO = (p_33346_) -> {
			return p_33346_ instanceof WindcallerTornadoEntity;
		};
		
		public BlastAttackGoal(WindcallerEntity mob) {
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
			
			int nearbyTornadoes = 1;
			
			if (target != null) {
				nearbyTornadoes = mob.level.getEntities(mob, target.getBoundingBox().inflate(5.0D), TORNADO)
					.size();
			}
			
			return target != null && mob.random.nextInt(5) == 0 && (mob.distanceTo(target) <= 4 || (!target.isOnGround() && mob.random.nextInt(10) == 0 && mob.distanceTo(target) < 7.5)) && nearbyTornadoes <= 0 && mob.canSee(target) && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.playSound(ModSoundEvents.WINDCALLER_BLAST_VOCAL.get(), 1.0F, mob.getVoicePitch());
			mob.blastAttackAnimationTick = mob.blastAttackAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 11);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.blastAttackAnimationTick == mob.blastAttackAnimationActionPoint) {
				double d1 = target.getX() - mob.getX();
	               double d2 = target.getY(0.5D) - mob.getY(0.5D);
	               double d3 = target.getZ() - mob.getZ();
				WindcallerBlastProjectileEntity smallfireballentity = new WindcallerBlastProjectileEntity(mob.level, mob, d1, 0, d3);
                smallfireballentity.setPos(smallfireballentity.getX(), mob.getY(0.25D), smallfireballentity.getZ());
                mob.level.addFreshEntity(smallfireballentity);
	            WindcallerTornadoEntity tornado = ModEntityTypes.TORNADO.get().create(mob.level);
	            tornado.moveTo(mob.blockPosition(), 0, 0);
	            tornado.playSound(ModSoundEvents.WINDCALLER_BLAST_WIND.get(), 1.5F, 1.0F);
	            tornado.setBlast(true);
	            mob.lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(target.getX(), target.getY(), target.getZ()));
	            tornado.yRot = -mob.yHeadRot - 90;
	            ((ServerWorld)mob.level).addFreshEntityWithPassengers(tornado);
			}
		}

		public boolean animationsUseable() {
			return mob.blastAttackAnimationTick <= 0;
		}

	}
}