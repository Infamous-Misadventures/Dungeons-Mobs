package com.infamous.dungeons_mobs.entities.illagers.minibosses;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.entities.illagers.RoyalGuardEntity;
import com.infamous.dungeons_mobs.entities.summonables.DungeonsVexEntity;
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
import java.util.List;

public class DungeonsEvokerEntity extends SpellcastingIllagerEntity implements IAnimatable {

	public static final DataParameter<Integer> LIFT_TICKS = EntityDataManager.defineId(DungeonsEvokerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> DUPLICATE_TICKS = EntityDataManager.defineId(DungeonsEvokerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> ANGRY = EntityDataManager.defineId(DungeonsEvokerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> POWERFUL_ATTACK = EntityDataManager.defineId(DungeonsEvokerEntity.class, DataSerializers.INT);

	public boolean SpellAttacking;
	public int liftInterval = 0;
	public int eliftInterval = 0;
	public int duplicateInterval = 0;
	public int powerfulAttackInterval = 0;
	public int spellInterval = 0;
	public int timer = 0;

	AnimationFactory factory = new AnimationFactory(this);

    public DungeonsEvokerEntity(World world){
        super(ModEntityTypes.DUNGEONS_EVOKER.get(), world);
    }

    public DungeonsEvokerEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
		this.xpReward = 40;
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
		this.goalSelector.addGoal(5, new RkGoal(this, 1.11));

		this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, AbstractVillagerEntity.class, 3.0F, 1.4D, 1.25D));
		this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, PlayerEntity.class, 3.0F, 1.4D, 1.2D));
		this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, IronGolemEntity.class, 3.0F, 1.4D, 1.25D));
		this.goalSelector.addGoal(0, new SwimGoal(this));
		/*this.goalSelector.addGoal(1, new DungeonsEvokerEntity.PowerfulAttackGoal());/*/
		this.goalSelector.addGoal(1, new DungeonsEvokerEntity.SpellAttackGoal());
        this.goalSelector.addGoal(3, new DungeonsEvokerEntity.CastingSpellGoal());
		this.goalSelector.addGoal(2, new DungeonsEvokerEntity.SummonFangsGoal());
        this.goalSelector.addGoal(3, new DungeonsEvokerEntity.DuplicateGoal());
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
			LivingEntity livingentity  = this.getTarget();
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

		List<Entity> list = Lists.newArrayList(this.level.getEntities(this, this.getBoundingBox().inflate(35.5, 17.5, 35.5)));
		for(Entity entity : list) {
			if(entity instanceof DungeonsVexEntity){
				DungeonsVexEntity livingEntity = (DungeonsVexEntity)entity;
				if (livingEntity.getOwner() == this && !(this.eliftInterval >= 300)) {
					this.liftInterval =30+this.getRandom().nextInt(2);
					this.duplicateInterval =30+this.getRandom().nextInt(2);
					this.eliftInterval++;
				}else
					this.eliftInterval =  0;
			}
			if(entity instanceof VexEntity){
				VexEntity livingEntity = (VexEntity)entity;
				if (livingEntity.getOwner() == this && !(this.eliftInterval >= 300)) {
					this.liftInterval =30+this.getRandom().nextInt(2);
					this.duplicateInterval =30+this.getRandom().nextInt(2);
					this.eliftInterval++;
				}else
					this.eliftInterval =  0;
			}
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
		data.addAnimationController(new AnimationController(this, "f", 12, this::fre));
		data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
	}

	public boolean g;

	public void setG(boolean G) {
		this.g = G;
	}

	public boolean getG() {
		return this.g;
	}

	public boolean h;

	public void setH(boolean G) {
		this.h = G;
	}

	public boolean getH() {
		return this.h;
	}

	private <P extends IAnimatable> PlayState fre(AnimationEvent<P> event) {
		if (this.isAggressive()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("facialexpression.angry", true));
		}else if (this.getH()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("facialexpression.see", true));
		}else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("facialexpression.null", true));
		}
		return PlayState.CONTINUE;
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.getDuplicateTicks() > 0) {
				event.getController().animationSpeed = 1;
				this.setG(false);
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_summon_spell", false));
			} else if (this.getLiftTicks() > 0) {
				event.getController().animationSpeed = 1;
				this.setG(false);
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_comber_spell", false));
			}else if (this.isAngry() > 0 || this.getPowerfulAttack() > 0) {
				event.getController().animationSpeed = 1;
				this.setG(false);
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_attack_spell", false));
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
				event.getController().animationSpeed = 1.6;
				this.setG(false);
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_walk", true));
			} else {
				if (this.getH()) {
					event.getController().animationSpeed = 1;
					this.setG(false);
					event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_idel", true));
				} else {
					event.getController().animationSpeed = 1;
					this.setG(true);
					event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_idel_look_around", true));
				}
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
        return MonsterEntity.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 25.0D)
				.add(Attributes.MAX_HEALTH, 80.0D);
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
    public void applyRaidBuffs(int waveAmount, boolean p_213660_2_) {
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
            if (DungeonsEvokerEntity.this.getTarget() != null) {
                DungeonsEvokerEntity.this.getLookControl().setLookAt(DungeonsEvokerEntity.this.getTarget(), (float) DungeonsEvokerEntity.this.getMaxHeadYRot(), (float) DungeonsEvokerEntity.this.getMaxHeadXRot());
            }

        }
    }

	class SpellAttackGoal extends Goal {
		private void createSpellEntity(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
			BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
			boolean flag = false;
			double d0 = 0.0D;

			do {
				BlockPos blockpos1 = blockpos.below();
				BlockState blockstate = DungeonsEvokerEntity.this.level.getBlockState(blockpos1);
				if (blockstate.isFaceSturdy(DungeonsEvokerEntity.this.level, blockpos1, Direction.UP)) {
					if (!DungeonsEvokerEntity.this.level.isEmptyBlock(blockpos)) {
						BlockState blockstate1 = DungeonsEvokerEntity.this.level.getBlockState(blockpos);
						VoxelShape voxelshape = blockstate1.getCollisionShape(DungeonsEvokerEntity.this.level, blockpos);
						if (!voxelshape.isEmpty()) {
							d0 = voxelshape.max(Direction.Axis.Y);
						}
					}

					flag = true;
					break;
				}

				blockpos = blockpos.below();
			} while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

			if (flag) {
				DungeonsEvokerEntity.this.level.addFreshEntity(new EvokerFangsEntity(DungeonsEvokerEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, DungeonsEvokerEntity.this));
			}

		}
		public SpellAttackGoal() {
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
		}

		public boolean canUse() {
			return DungeonsEvokerEntity.this.getTarget() != null && DungeonsEvokerEntity.this.getLiftTicks() == 0 && DungeonsEvokerEntity.this.spellInterval == 0 && DungeonsEvokerEntity.this.random.nextInt(20) == 0;
		}

		public boolean canContinueToUse() {
			return DungeonsEvokerEntity.this.getTarget() != null && DungeonsEvokerEntity.this.isAngry() > 0;
		}

		public void start() {
			super.start();
			DungeonsEvokerEntity.this.SpellAttacking = true;
			DungeonsEvokerEntity.this.setAngry(42);
			DungeonsEvokerEntity.this.setInvulnerable(true);
			DungeonsEvokerEntity.this.spellInterval = 82 - DungeonsEvokerEntity.this.getRandom().nextInt(40);
			DungeonsEvokerEntity.this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, DungeonsEvokerEntity.this.getSoundVolume(), DungeonsEvokerEntity.this.getVoicePitch());
		}

		public void tick() {
			DungeonsEvokerEntity.this.getLookControl().setLookAt(DungeonsEvokerEntity.this.getTarget(), (float) DungeonsEvokerEntity.this.getMaxHeadYRot(), (float) DungeonsEvokerEntity.this.getMaxHeadXRot());
			DungeonsEvokerEntity mob = DungeonsEvokerEntity.this;

			mob.getNavigation().stop();
			if (mob.isAngry() == 37) {

				LivingEntity livingentity = DungeonsEvokerEntity.this.getTarget();
				double d0 = Math.min(livingentity.getY(), DungeonsEvokerEntity.this.getY());
				double d1 = Math.max(livingentity.getY(), DungeonsEvokerEntity.this.getY()) + 1.0D;
				float f = (float) MathHelper.atan2(livingentity.getZ() - DungeonsEvokerEntity.this.getZ(), livingentity.getX() - DungeonsEvokerEntity.this.getX());
				int k;

			}
			if (mob.isAngry() == 17) {

				LivingEntity livingentity = DungeonsEvokerEntity.this.getTarget();
				double d0 = Math.min(livingentity.getY(), DungeonsEvokerEntity.this.getY());
				double d1 = Math.max(livingentity.getY(), DungeonsEvokerEntity.this.getY()) + 1.0D;
				float f = (float) MathHelper.atan2(livingentity.getZ() - DungeonsEvokerEntity.this.getZ(), livingentity.getX() - DungeonsEvokerEntity.this.getX());
				int k;

				if (mob.getTarget() != null) {
					if (DungeonsEvokerEntity.this.distanceToSqr(livingentity) < 60.0D && mob.distanceToSqr(livingentity) > 30) {
						float f2;

						for (k = 0; k < 29; ++k) {
							f2 = f + (float)k * (float)Math.PI * 5.0F / 29.0F + 6.2566371F;
							this.createSpellEntity(DungeonsEvokerEntity.this.getX() + (double)MathHelper.cos(f2) * 5.5D, DungeonsEvokerEntity.this.getZ() + (double)MathHelper.sin(f2) * 5.5D, d0, d1, f2, k / 8);
						}

					} else {
						for (k = 0; k < 28; ++k) {
							{
								double d2 = 1.15 * (double) (k + 1);
								int j = (int) (k / 1.62);
								this.createSpellEntity(DungeonsEvokerEntity.this.getX() + (double) MathHelper.cos(f) * d2, DungeonsEvokerEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}
						}
					}
				}
			}

		}

		public void stop() {
			super.stop();
			DungeonsEvokerEntity.this.setInvulnerable(false);
			DungeonsEvokerEntity.this.setLiftTicks(0);
		}
		@Override
		public boolean isInterruptable() {
			return false;
		}
	}

	class RkGoal extends MeleeAttackGoal {
		private int maxAttackTimer = 15;
		private final double moveSpeed;
		private int delayCounter;
		private int attackTimer;

		public DungeonsEvokerEntity v = DungeonsEvokerEntity.this;

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
				this.delayCounter = 5 + v.getRandom().nextInt(5);
				v.getNavigation().moveTo(
						livingentity.getX() + (livingentity.getRandom().nextInt(5) + 6) * livingentity.getRandom().nextInt(2) == 1 ? 1 : -1,
						livingentity.getY(),
						livingentity.getZ() + (livingentity.getRandom().nextInt(5) + 6) * livingentity.getRandom().nextInt(2) == 1 ? 1 : -1,
						(double) this.moveSpeed
				);
			}
			this.attackTimer = (int) Math.max(this.attackTimer - 1, 0);
		}

		@Override
		public void stop() {
			v.getNavigation().stop();
			if (v.getTarget() == null) {
				v.setAggressive(false);
			}
		}

		public DungeonsEvokerEntity.RkGoal setMaxAttackTick(int max) {
			this.maxAttackTimer = max;
			return this;
		}
	}

	class SummonFangsGoal extends Goal {


		@Override
		public boolean isInterruptable() {
			return false;
		}

		private void createSpellEntity(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
			BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
			boolean flag = false;
			double d0 = 0.0D;

			do {
				BlockPos blockpos1 = blockpos.below();
				BlockState blockstate = DungeonsEvokerEntity.this.level.getBlockState(blockpos1);
				if (blockstate.isFaceSturdy(DungeonsEvokerEntity.this.level, blockpos1, Direction.UP)) {
					if (!DungeonsEvokerEntity.this.level.isEmptyBlock(blockpos)) {
						BlockState blockstate1 = DungeonsEvokerEntity.this.level.getBlockState(blockpos);
						VoxelShape voxelshape = blockstate1.getCollisionShape(DungeonsEvokerEntity.this.level, blockpos);
						if (!voxelshape.isEmpty()) {
							d0 = voxelshape.max(Direction.Axis.Y);
						}
					}

					flag = true;
					break;
				}

				blockpos = blockpos.below();
			} while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

			if (flag) {
				DungeonsEvokerEntity.this.level.addFreshEntity(new EvokerFangsEntity(DungeonsEvokerEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, DungeonsEvokerEntity.this));
			}

		}
	      public SummonFangsGoal() {
	         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
	      }

	      public boolean canUse() {
	         return DungeonsEvokerEntity.this.getTarget() != null && DungeonsEvokerEntity.this.getLiftTicks() == 0 && DungeonsEvokerEntity.this.liftInterval == 0;
	      }
	      
	      public boolean canContinueToUse() {
			  return DungeonsEvokerEntity.this.getTarget() != null && DungeonsEvokerEntity.this.getLiftTicks() > 0;
	      }


	    
	    public void start() {
	    super.start();
		DungeonsEvokerEntity.this.SpellAttacking = false;
	    DungeonsEvokerEntity.this.liftInterval = 250;
		DungeonsEvokerEntity.this.duplicateInterval = 250;
	    DungeonsEvokerEntity.this.setLiftTicks(66);
	    DungeonsEvokerEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, DungeonsEvokerEntity.this.getSoundVolume(), DungeonsEvokerEntity.this.getVoicePitch());
	    }
	      
	    public void tick() {
			DungeonsEvokerEntity.this.getLookControl().setLookAt(DungeonsEvokerEntity.this.getTarget(), (float) DungeonsEvokerEntity.this.getMaxHeadYRot(), (float) DungeonsEvokerEntity.this.getMaxHeadXRot());
			DungeonsEvokerEntity mob = DungeonsEvokerEntity.this;

			mob.getNavigation().stop();

			if (mob.getLiftTicks() == 42) {
				ServerWorld serverworld = (ServerWorld) DungeonsEvokerEntity.this.level;
				int difficultyAsInt = DungeonsEvokerEntity.this.level.getDifficulty().getId();
				int mobsToSummon = difficultyAsInt + 1; // 3 on easy, 6 on normal, 9 on hard
				for (int i = 0; i < mobsToSummon; ++i) {
					BlockPos blockpos = DungeonsEvokerEntity.this.blockPosition().offset(-2 + DungeonsEvokerEntity.this.random.nextInt(5), 1, -2 + DungeonsEvokerEntity.this.random.nextInt(5));
					VexEntity vexentity = EntityType.VEX.create(serverworld);
					vexentity.moveTo(blockpos, 0.0F, 0.0F);
					vexentity.finalizeSpawn(serverworld, DungeonsEvokerEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
					vexentity.setOwner(DungeonsEvokerEntity.this);
					vexentity.setBoundOrigin(blockpos);
					vexentity.setLimitedLife(20 * (30 + DungeonsEvokerEntity.this.random.nextInt(90)));
					serverworld.addFreshEntityWithPassengers(vexentity);
				}
			}
			if (mob.getLiftTicks() == 16) {

				LivingEntity livingentity = DungeonsEvokerEntity.this.getTarget();
				double d0 = Math.min(livingentity.getY(), DungeonsEvokerEntity.this.getY());
				double d1 = Math.max(livingentity.getY(), DungeonsEvokerEntity.this.getY()) + 1.0D;
				float f = (float) MathHelper.atan2(livingentity.getZ() - DungeonsEvokerEntity.this.getZ(), livingentity.getX() - DungeonsEvokerEntity.this.getX());
				int k;

				if (mob.getTarget() != null)
					if (DungeonsEvokerEntity.this.distanceToSqr(livingentity) < 60.0D && mob.distanceToSqr(livingentity) > 30) {
						float f2;

						for (k = 0; k < 29; ++k) {
							f2 = f + (float)k * (float)Math.PI * 5.0F / 29.0F + 6.2566371F;
							this.createSpellEntity(DungeonsEvokerEntity.this.getX() + (double)MathHelper.cos(f2) * 5.5D, DungeonsEvokerEntity.this.getZ() + (double)MathHelper.sin(f2) * 5.5D, d0, d1, f2, k / 8);
						}

					} else {
						for (k = 0; k < 28; ++k) {
							{
								double d2 = 1.15 * (double) (k + 1);
								int j = (int) (k / 1.62);
								this.createSpellEntity(DungeonsEvokerEntity.this.getX() + (double) MathHelper.cos(f) * d2, DungeonsEvokerEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}
						}
					}
			}

	    }
	    
	    public void stop() {
			super.stop();
			DungeonsEvokerEntity.this.setLiftTicks(0);
		}
	}
    
    class DuplicateGoal extends Goal {
	      public DuplicateGoal() {
	         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
	      }

	      public boolean canUse() {
	         return DungeonsEvokerEntity.this.getTarget() != null && DungeonsEvokerEntity.this.getDuplicateTicks() == 0 && DungeonsEvokerEntity.this.duplicateInterval == 0;
	      }
	      
	      public boolean canContinueToUse() {
	    	  	return DungeonsEvokerEntity.this.getTarget() != null && DungeonsEvokerEntity.this.getDuplicateTicks() > 0;
	      }
	      
	      @Override
	    public boolean isInterruptable() {
	    	return false;
	    }
	    
	    public void start() {
	    super.start();
		DungeonsEvokerEntity.this.SpellAttacking = false;
	    DungeonsEvokerEntity.this.duplicateInterval = 250;
		DungeonsEvokerEntity.this.liftInterval = 250;
	    DungeonsEvokerEntity.this.setDuplicateTicks(44);
	    DungeonsEvokerEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, DungeonsEvokerEntity.this.getSoundVolume(), DungeonsEvokerEntity.this.getVoicePitch());
	    }
	      
	    public void tick() {
            DungeonsEvokerEntity.this.getLookControl().setLookAt(DungeonsEvokerEntity.this.getTarget(), (float) DungeonsEvokerEntity.this.getMaxHeadYRot(), (float) DungeonsEvokerEntity.this.getMaxHeadXRot());

			LivingEntity target = DungeonsEvokerEntity.this.getTarget();
	    	DungeonsEvokerEntity mob = DungeonsEvokerEntity.this;
	    	
	    	mob.getNavigation().stop();
	    	
          if (mob.getDuplicateTicks() == 20) {
              this.summonIllusionerClones();
           }
	    }

           private void summonIllusionerClones() {
			   ServerWorld serverworld = (ServerWorld) DungeonsEvokerEntity.this.level;
			   int difficultyAsInt = DungeonsEvokerEntity.this.level.getDifficulty().getId();
			   int mobsToSummon = difficultyAsInt + 1; // 3 on easy, 6 on normal, 9 on hard
			   for (int i = 0; i < mobsToSummon; ++i) {
				   BlockPos blockpos = DungeonsEvokerEntity.this.blockPosition().offset(-2 + DungeonsEvokerEntity.this.random.nextInt(5), 1, -2 + DungeonsEvokerEntity.this.random.nextInt(5));
				   VexEntity vexentity = EntityType.VEX.create(serverworld);
				   vexentity.moveTo(blockpos, 0.0F, 0.0F);
				   vexentity.finalizeSpawn(serverworld, DungeonsEvokerEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
				   vexentity.setOwner(DungeonsEvokerEntity.this);
				   vexentity.setBoundOrigin(blockpos);
				   vexentity.setLimitedLife(20 * (30 + DungeonsEvokerEntity.this.random.nextInt(90)));
				   serverworld.addFreshEntityWithPassengers(vexentity);
			   }
		   }

	   }
	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason reason, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
		ILivingEntityData spawnData = super.finalizeSpawn(p_213386_1_, p_213386_2_, reason, p_213386_4_, p_213386_5_);

		DungeonsEvokerEntity v = this;
		if (reason != SpawnReason.SPAWN_EGG && reason != SpawnReason.SPAWNER) {
			for (int i = 0; i < 6; i++) {
				BlockPos vvf = v.blockPosition();
				RoyalGuardEntity vv = new RoyalGuardEntity(this.level);
				vv.moveTo(vvf, 0F, 0F);
				vv.finalizeSpawn(p_213386_1_, p_213386_2_, reason, p_213386_4_, p_213386_5_);
				vv.setCurrentRaid(v.getCurrentRaid());
				vv.setCanJoinRaid(true);
				vv.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
				v.level.addFreshEntity(vv);
			}
		}

		return spawnData;
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