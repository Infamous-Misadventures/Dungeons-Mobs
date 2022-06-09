package com.infamous.dungeons_mobs.entities.illagers.minibosses;

import com.infamous.dungeons_mobs.entities.illagers.RoyalGuardEntity;
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
import net.minecraft.util.*;
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

public class DungeonsEvokerEntity extends SpellcastingIllagerEntity implements IAnimatable {

	public static final DataParameter<Integer> LIFT_TICKS = EntityDataManager.defineId(DungeonsEvokerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> DUPLICATE_TICKS = EntityDataManager.defineId(DungeonsEvokerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> ANGRY = EntityDataManager.defineId(DungeonsEvokerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> POWERFUL_ATTACK = EntityDataManager.defineId(DungeonsEvokerEntity.class, DataSerializers.INT);

	public boolean SpellAttacking;
	public int liftInterval = 0;
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
        super.registerGoals();
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, AbstractVillagerEntity.class, 3.0F, 1.4D, 1.25D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PlayerEntity.class, 3.0F, 1.4D, 1.2D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, IronGolemEntity.class, 3.0F, 1.4D, 1.25D));
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
		data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
	}


	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.getDuplicateTicks() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_summon_spell", false));
			} else if (this.getLiftTicks() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_comber_spell", false));
			}else if (this.isAngry() > 0 || this.getPowerfulAttack() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_casting_attack_spell", false));
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_walk", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("evoker_idel", true));
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
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 26.0D)
				.add(Attributes.MAX_HEALTH, 68.0D);
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
			DungeonsEvokerEntity.this.setAngry(24);
			DungeonsEvokerEntity.this.spellInterval = 74;
			DungeonsEvokerEntity.this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, DungeonsEvokerEntity.this.getSoundVolume(), DungeonsEvokerEntity.this.getVoicePitch());
		}

		public void tick() {
			DungeonsEvokerEntity.this.getLookControl().setLookAt(DungeonsEvokerEntity.this.getTarget(), (float) DungeonsEvokerEntity.this.getMaxHeadYRot(), (float) DungeonsEvokerEntity.this.getMaxHeadXRot());
			DungeonsEvokerEntity mob = DungeonsEvokerEntity.this;

			mob.getNavigation().stop();
			if (mob.isAngry() == 12) {

				LivingEntity livingentity = DungeonsEvokerEntity.this.getTarget();
				double d0 = Math.min(livingentity.getY(), DungeonsEvokerEntity.this.getY());
				double d1 = Math.max(livingentity.getY(), DungeonsEvokerEntity.this.getY()) + 1.0D;
				float f = (float) MathHelper.atan2(livingentity.getZ() - DungeonsEvokerEntity.this.getZ(), livingentity.getX() - DungeonsEvokerEntity.this.getX());
				int k;

				if (mob.getTarget() != null)
					if (DungeonsEvokerEntity.this.distanceToSqr(livingentity) < 60.0D && mob.distanceToSqr(livingentity) > 45) {
						float f2;

						for (k = 0; k < 50; ++k) {
							f2 = f + (float) k * 3.1415927F * 16.0F / 48.0F + 15.2566371F;
							this.createSpellEntity(DungeonsEvokerEntity.this.getX() + (double) MathHelper.cos(f2) * 16.5D, DungeonsEvokerEntity.this.getZ() + (double) MathHelper.sin(f2) * 16.5D, d0, d1, f2, (int) (36));
						}

					} else {
						for (k = 0; k < 50; ++k) {
							{
								double d2 = 1.25D * (double) (k + 1);
								int j = (int) (k / 1.2);
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
		@Override
		public boolean isInterruptable() {
			return false;
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
	         return DungeonsEvokerEntity.this.getTarget() != null && DungeonsEvokerEntity.this.getLiftTicks() == 0 && DungeonsEvokerEntity.this.liftInterval == 0 && DungeonsEvokerEntity.this.random.nextInt(20) == 0;
	      }
	      
	      public boolean canContinueToUse() {
			  return DungeonsEvokerEntity.this.getTarget() != null && DungeonsEvokerEntity.this.getLiftTicks() > 0;
	      }


	    
	    public void start() {
	    super.start();
		DungeonsEvokerEntity.this.SpellAttacking = false;
	    DungeonsEvokerEntity.this.liftInterval = 800;
		DungeonsEvokerEntity.this.duplicateInterval = 400;
	    DungeonsEvokerEntity.this.setLiftTicks(62);
	    DungeonsEvokerEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, DungeonsEvokerEntity.this.getSoundVolume(), DungeonsEvokerEntity.this.getVoicePitch());
	    }
	      
	    public void tick() {
			DungeonsEvokerEntity.this.getLookControl().setLookAt(DungeonsEvokerEntity.this.getTarget(), (float) DungeonsEvokerEntity.this.getMaxHeadYRot(), (float) DungeonsEvokerEntity.this.getMaxHeadXRot());
			DungeonsEvokerEntity mob = DungeonsEvokerEntity.this;

			mob.getNavigation().stop();

			if (mob.getLiftTicks() == 32) {
				ServerWorld serverworld = (ServerWorld) DungeonsEvokerEntity.this.level;
				int difficultyAsInt = DungeonsEvokerEntity.this.level.getDifficulty().getId();
				int mobsToSummon = difficultyAsInt + 1; // 3 on easy, 6 on normal, 9 on hard
				for (int i = 0; i < mobsToSummon; ++i) {
					BlockPos blockpos = DungeonsEvokerEntity.this.blockPosition().offset(-2 + DungeonsEvokerEntity.this.random.nextInt(5), 1, -2 + DungeonsEvokerEntity.this.random.nextInt(5));
					VexEntity vexentity = (VexEntity) EntityType.VEX.create(DungeonsEvokerEntity.this.level);
					vexentity.moveTo(blockpos, 0.0F, 0.0F);
					vexentity.finalizeSpawn(serverworld, DungeonsEvokerEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
					vexentity.setOwner(DungeonsEvokerEntity.this);
					vexentity.setBoundOrigin(blockpos);
					vexentity.setLimitedLife(20 * (30 + DungeonsEvokerEntity.this.random.nextInt(90)));
					serverworld.addFreshEntityWithPassengers(vexentity);
				}
			}
			if (mob.getLiftTicks() == 13) {

				LivingEntity livingentity = DungeonsEvokerEntity.this.getTarget();
				double d0 = Math.min(livingentity.getY(), DungeonsEvokerEntity.this.getY());
				double d1 = Math.max(livingentity.getY(), DungeonsEvokerEntity.this.getY()) + 1.0D;
				float f = (float) MathHelper.atan2(livingentity.getZ() - DungeonsEvokerEntity.this.getZ(), livingentity.getX() - DungeonsEvokerEntity.this.getX());
				int k;

				if (mob.getTarget() != null)
					if (DungeonsEvokerEntity.this.distanceToSqr(livingentity) < 60.0D && mob.distanceToSqr(livingentity) > 45) {
						float f2;

						for (k = 0; k < 50; ++k) {
							f2 = f + (float) k * 3.1415927F * 16.0F / 48.0F + 15.2566371F;
							this.createSpellEntity(DungeonsEvokerEntity.this.getX() + (double) MathHelper.cos(f2) * 16.5D, DungeonsEvokerEntity.this.getZ() + (double) MathHelper.sin(f2) * 16.5D, d0, d1, f2, (int) (36));
						}

					} else {
						for (k = 0; k < 50; ++k) {
							{
								double d2 = 1.25D * (double) (k + 1);
								int j = (int) (k / 1.2);
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
	    DungeonsEvokerEntity.this.duplicateInterval = 800;
		DungeonsEvokerEntity.this.liftInterval = 400;
	    DungeonsEvokerEntity.this.setDuplicateTicks(48);
	    DungeonsEvokerEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, DungeonsEvokerEntity.this.getSoundVolume(), DungeonsEvokerEntity.this.getVoicePitch());
	    }
	      
	    public void tick() {
            DungeonsEvokerEntity.this.getLookControl().setLookAt(DungeonsEvokerEntity.this.getTarget(), (float) DungeonsEvokerEntity.this.getMaxHeadYRot(), (float) DungeonsEvokerEntity.this.getMaxHeadXRot());

			LivingEntity target = DungeonsEvokerEntity.this.getTarget();
	    	DungeonsEvokerEntity mob = DungeonsEvokerEntity.this;
	    	
	    	mob.getNavigation().stop();
	    	
          if (mob.getDuplicateTicks() == 18) {
              this.summonIllusionerClones();
           }
	    }

           private void summonIllusionerClones(){
			   ServerWorld serverworld = (ServerWorld)DungeonsEvokerEntity.this.level;
              int difficultyAsInt = DungeonsEvokerEntity.this.level.getDifficulty().getId();
              int mobsToSummon = difficultyAsInt + 1; // 2 on easy, 3 on normal, 4 on hard
              for(int i = 0; i < mobsToSummon; ++i) {
				  BlockPos blockpos = DungeonsEvokerEntity.this.blockPosition().offset(-2 + DungeonsEvokerEntity.this.random.nextInt(5), 1, -2 + DungeonsEvokerEntity.this.random.nextInt(5));
				  VexEntity vexentity = (VexEntity)EntityType.VEX.create(DungeonsEvokerEntity.this.level);
				  vexentity.moveTo(blockpos, 0.0F, 0.0F);
				  vexentity.finalizeSpawn(serverworld, DungeonsEvokerEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
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
