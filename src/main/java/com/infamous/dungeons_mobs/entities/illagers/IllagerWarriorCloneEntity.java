package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class IllagerWarriorCloneEntity extends SpellcastingIllagerEntity implements IAnimatable {

	public static final DataParameter<Integer> LIVING_TICKS = EntityDataManager.defineId(IllagerWarriorCloneEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> LIFT_TICKS = EntityDataManager.defineId(IllagerWarriorCloneEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> DUPLICATE_TICKS = EntityDataManager.defineId(IllagerWarriorCloneEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> ANGRY = EntityDataManager.defineId(IllagerWarriorCloneEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> POWERFUL_ATTACK = EntityDataManager.defineId(IllagerWarriorCloneEntity.class, DataSerializers.INT);

	public boolean SpellAttacking;
	public int liftInterval = 0;
	public int duplicateInterval = 0;
	public int powerfulAttackInterval = 0;
	public int spellInterval = 0;
	public int timer = 0;

	AnimationFactory factory = new AnimationFactory(this);


    public IllagerWarriorCloneEntity(EntityType<? extends SpellcastingIllagerEntity> type,World world){
        super(ModEntityTypes.ILLAGER_WARRIOR_CLONE.get(), world);
    }

	public IllagerWarriorCloneEntity(World worldIn,int ff,boolean pp,LivingEntity target) {
		this(ModEntityTypes.ILLAGER_WARRIOR_CLONE.get(), worldIn);
		this.setLivingTicks(ff);
		this.setInvulnerable(pp);
		this.setTarget(target);
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
		for(int i = 0; i < 20; ++i) {
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			double d2 = this.random.nextGaussian() * 0.02D;
			this.level.addParticle(ParticleTypes.WITCH, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getBoundingBoxForCulling() {
		return this.getBoundingBox().inflate(3.0D, 0.0D, 3.0D);
	}

	@Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, AbstractVillagerEntity.class, 3.0F, 1.4D, 1.35D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PlayerEntity.class, 3.0F, 1.4D, 1.4D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, IronGolemEntity.class, 3.0F, 1.4D, 1.35D));
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new IllagerWarriorCloneEntity.PowerfulAttackGoal());
		this.goalSelector.addGoal(1, new IllagerWarriorCloneEntity.SpellAttackGoal());
        this.goalSelector.addGoal(3, new IllagerWarriorCloneEntity.CastingSpellGoal());
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

		if (this.getLivingTick() > 0) {
			this.setLivingTicks(this.getLivingTick() - 1);
		}else {
			this.remove();
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
		this.entityData.define(LIVING_TICKS, 0);
    }

	public int getLivingTick() {
		return this.entityData.get(LIVING_TICKS);
	}

	public void setLivingTicks(int p_189794_1_) {
		this.entityData.set(LIVING_TICKS, p_189794_1_);
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
				.add(Attributes.FOLLOW_RANGE, 64.0D)
				.add(Attributes.ARMOR, 30.0D)
				.add(Attributes.MAX_HEALTH, 888.0D);
    }

	@Override
	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		if (!this.isInvulnerable()){
			this.remove();
		}
		return super.hurt(p_70097_1_, p_70097_2_);
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
            if (IllagerWarriorCloneEntity.this.getTarget() != null) {
                IllagerWarriorCloneEntity.this.getLookControl().setLookAt(IllagerWarriorCloneEntity.this.getTarget(), (float) IllagerWarriorCloneEntity.this.getMaxHeadYRot(), (float) IllagerWarriorCloneEntity.this.getMaxHeadXRot());
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
				BlockState blockstate = IllagerWarriorCloneEntity.this.level.getBlockState(blockpos1);
				if (blockstate.isFaceSturdy(IllagerWarriorCloneEntity.this.level, blockpos1, Direction.UP)) {
					if (!IllagerWarriorCloneEntity.this.level.isEmptyBlock(blockpos)) {
						BlockState blockstate1 = IllagerWarriorCloneEntity.this.level.getBlockState(blockpos);
						VoxelShape voxelshape = blockstate1.getCollisionShape(IllagerWarriorCloneEntity.this.level, blockpos);
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
				IllagerWarriorCloneEntity.this.level.addFreshEntity(new EvokerFangsEntity(IllagerWarriorCloneEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, IllagerWarriorCloneEntity.this));
			}

		}
		public SpellAttackGoal() {
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
		}

		public boolean canUse() {
			return IllagerWarriorCloneEntity.this.getTarget() != null && IllagerWarriorCloneEntity.this.getLiftTicks() == 0 && IllagerWarriorCloneEntity.this.spellInterval == 0 && IllagerWarriorCloneEntity.this.random.nextInt(20) == 0;
		}

		public boolean canContinueToUse() {
			return IllagerWarriorCloneEntity.this.getTarget() != null && IllagerWarriorCloneEntity.this.isAngry() > 0;
		}

		public void start() {
			super.start();
			IllagerWarriorCloneEntity.this.SpellAttacking = true;
			IllagerWarriorCloneEntity.this.setAngry(24);
			IllagerWarriorCloneEntity.this.spellInterval = (int) ((IllagerWarriorCloneEntity.this.getRandom().nextInt(30) + 25) * (IllagerWarriorCloneEntity.this.getHealth() / IllagerWarriorCloneEntity.this.getMaxHealth()));
			IllagerWarriorCloneEntity.this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, IllagerWarriorCloneEntity.this.getSoundVolume(), IllagerWarriorCloneEntity.this.getVoicePitch());
		}

		public void tick() {
			IllagerWarriorCloneEntity.this.getLookControl().setLookAt(IllagerWarriorCloneEntity.this.getTarget(), (float) IllagerWarriorCloneEntity.this.getMaxHeadYRot(), (float) IllagerWarriorCloneEntity.this.getMaxHeadXRot());
			IllagerWarriorCloneEntity mob = IllagerWarriorCloneEntity.this;

			mob.getNavigation().stop();
			if (mob.isAngry() == 12) {

				LivingEntity livingentity = IllagerWarriorCloneEntity.this.getTarget();
				double d0 = Math.min(livingentity.getY(), IllagerWarriorCloneEntity.this.getY());
				double d1 = Math.max(livingentity.getY(), IllagerWarriorCloneEntity.this.getY()) + 1.0D;
				float f = (float)MathHelper.atan2(livingentity.getZ() - IllagerWarriorCloneEntity.this.getZ(), livingentity.getX() - IllagerWarriorCloneEntity.this.getX());
				int k;

				if (mob.getTarget() != null)
				if (IllagerWarriorCloneEntity.this.distanceToSqr(livingentity) < 24.0D) {
					float f2;

					for (k = 0; k < 5; ++k) {
						f2 = f + (float) k * 3.1415927F * 0.4F;
						this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 1.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 1.5D, d0, d1, f2, (int) (0));
					}

					for (k = 0; k < 8; ++k) {
						f2 = f + (float) k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
						this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 2.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, (int) (5));
					}

					for (k = 0; k < 11; ++k) {
						f2 = f + (float) k * 3.1415927F * 3.0F / 11.0F + 2.2566371F;
						this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 3.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 3.5D, d0, d1, f2, (int) (10));
					}

					for (k = 0; k < 14; ++k) {
						f2 = f + (float) k * 3.1415927F * 4.0F / 14.0F + 3.2566371F;
						this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 4.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 4.5D, d0, d1, f2, (int) (15));
					}

					for (k = 0; k < 17; ++k) {
						f2 = f + (float) k * 3.1415927F * 5.0F / 17.0F + 4.2566371F;
						this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 5.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 5.5D, d0, d1, f2, (int) (20));
					}

					for (k = 0; k < 20; ++k) {
						f2 = f + (float) k * 3.1415927F * 6.0F / 20.0F + 5.2566371F;
						this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 6.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 6.5D, d0, d1, f2, (int) (20));
					}

					for (k = 0; k < 23; ++k) {
						f2 = f + (float) k * 3.1415927F * 7.0F / 23.0F + 6.2566371F;
						this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 7.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 7.5D, d0, d1, f2, (int) (20));
					}
				} else {
					int x = IllagerWarriorCloneEntity.this.getRandom().nextInt(12);
					if (x == 1) {
						for (k = 0; k < Math.min(32,(int) (IllagerWarriorCloneEntity.this.distanceToSqr(livingentity))); ++k) {
							double d2 = 1.25D * (double) (k + 1);
							int j = k;
							this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
						}
						float f2;
						for (k = 0; k < 5; ++k) {
							f2 = f + (float) k * 3.1415927F * 0.4F;
							this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 1.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 1.5D, d0, d1, f2, (int) (9));
						}

						for (k = 0; k < 8; ++k) {
							f2 = f + (float) k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
							this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 2.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, (int) (12));
						}

						for (k = 0; k < 11; ++k) {
							f2 = f + (float) k * 3.1415927F * 3.0F / 11.0F + 2.2566371F;
							this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 3.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 3.5D, d0, d1, f2, (int) (15));
						}

						for (k = 0; k < 14; ++k) {
							f2 = f + (float) k * 3.1415927F * 4.0F / 14.0F + 3.2566371F;
							this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 4.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 4.5D, d0, d1, f2, (int) (18));
						}
					}else if (x <= 3){
						x = IllagerWarriorCloneEntity.this.getRandom().nextInt(4);
						if (x == 1 ) {
							for (k = 0; k < 32; ++k) {
								double e = Math.tan(k * 90) * 3;
								double d2 = 1.25D * (double) (k + 1);
								int j = (int) (k / 3.2);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
							}
						}else if (x == 2) {
							for (k = 0; k < 32; ++k){
								double d2 = 1.25D * (double) (k + 1);
								double e = Math.log(k+1) * 9;
								int j = (int) (k / 1.2);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 , d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 , IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}
						}else {
							for (k = 0; k < 32; ++k) {
								double e = Math.cos(k * 90) * 9;
								double d2 = 1.25D * (double) (k + 1);
								int j = (int) (k / 3.2);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
							}
						}
					}else {
						for (k = 0; k < 32; ++k){
							double d2 = 1.25D * (double) (k + 1);
							int j = (int) (k / 1.2);
							this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
						}
					}
				}

			}

		}

		public void stop() {
			super.stop();
			IllagerWarriorCloneEntity.this.setLiftTicks(0);
		}
		@Override
		public boolean isInterruptable() {
			return false;
		}
	}

	class PowerfulAttackGoal extends Goal {
		private void createSpellEntity(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
			BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
			boolean flag = false;
			double d0 = 0.0D;

			do {
				BlockPos blockpos1 = blockpos.below();
				BlockState blockstate = IllagerWarriorCloneEntity.this.level.getBlockState(blockpos1);
				if (blockstate.isFaceSturdy(IllagerWarriorCloneEntity.this.level, blockpos1, Direction.UP)) {
					if (!IllagerWarriorCloneEntity.this.level.isEmptyBlock(blockpos)) {
						BlockState blockstate1 = IllagerWarriorCloneEntity.this.level.getBlockState(blockpos);
						VoxelShape voxelshape = blockstate1.getCollisionShape(IllagerWarriorCloneEntity.this.level, blockpos);
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
				IllagerWarriorCloneEntity.this.level.addFreshEntity(new EvokerFangsEntity(IllagerWarriorCloneEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, IllagerWarriorCloneEntity.this));
			}

		}
		public PowerfulAttackGoal() {
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
		}

		public boolean canUse() {
			return IllagerWarriorCloneEntity.this.getTarget() != null && IllagerWarriorCloneEntity.this.getPowerfulAttack() == 0 && IllagerWarriorCloneEntity.this.powerfulAttackInterval == 0 && IllagerWarriorCloneEntity.this.getHealth() <= (IllagerWarriorCloneEntity.this.getMaxHealth() / 2);
		}

		public boolean canContinueToUse() {
			return IllagerWarriorCloneEntity.this.getTarget() != null && IllagerWarriorCloneEntity.this.getPowerfulAttack() > 0;
		}

		public void start() {
			super.start();
			IllagerWarriorCloneEntity.this.setPowerfulAttack(24);
			IllagerWarriorCloneEntity.this.powerfulAttackInterval = (int) (160 * (IllagerWarriorCloneEntity.this.getHealth() / IllagerWarriorCloneEntity.this.getMaxHealth() + 0.5));
			IllagerWarriorCloneEntity.this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, IllagerWarriorCloneEntity.this.getSoundVolume(), IllagerWarriorCloneEntity.this.getVoicePitch());
		}

		public void tick() {
			IllagerWarriorCloneEntity.this.getLookControl().setLookAt(IllagerWarriorCloneEntity.this.getTarget(), (float) IllagerWarriorCloneEntity.this.getMaxHeadYRot(), (float) IllagerWarriorCloneEntity.this.getMaxHeadXRot());
			IllagerWarriorCloneEntity mob = IllagerWarriorCloneEntity.this;

			mob.getNavigation().stop();
			if (mob.getPowerfulAttack() == 12) {

				LivingEntity livingentity = IllagerWarriorCloneEntity.this.getTarget();
				double d0 = Math.min(livingentity.getY(), IllagerWarriorCloneEntity.this.getY());
				double d1 = Math.max(livingentity.getY(), IllagerWarriorCloneEntity.this.getY()) + 1.0D;
				float f = (float) MathHelper.atan2(livingentity.getZ() - IllagerWarriorCloneEntity.this.getZ(), livingentity.getX() - IllagerWarriorCloneEntity.this.getX());
				int k;

				if (mob.getTarget() != null) {

					if (mob.getTarget() != null)
						if (IllagerWarriorCloneEntity.this.distanceToSqr(livingentity) < 30.0D) {
							float f2;

							for (k = 0; k < 5; ++k) {
								f2 = f + (float) k * 3.1415927F * 0.4F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 1.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 1.5D, d0, d1, f2, (int) (0));
							}

							for (k = 0; k < 8; ++k) {
								f2 = f + (float) k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 2.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, (int) (4));
							}

							for (k = 0; k < 11; ++k) {
								f2 = f + (float) k * 3.1415927F * 3.0F / 11.0F + 2.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 3.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 3.5D, d0, d1, f2, (int) (8));
							}

							for (k = 0; k < 14; ++k) {
								f2 = f + (float) k * 3.1415927F * 4.0F / 14.0F + 3.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 4.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 4.5D, d0, d1, f2, (int) (12));
							}

							for (k = 0; k < 17; ++k) {
								f2 = f + (float) k * 3.1415927F * 5.0F / 17.0F + 4.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 5.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 5.5D, d0, d1, f2, (int) (16));
							}

							for (k = 0; k < 20; ++k) {
								f2 = f + (float) k * 3.1415927F * 6.0F / 20.0F + 5.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 6.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 6.5D, d0, d1, f2, (int) (20));
							}

							for (k = 0; k < 23; ++k) {
								f2 = f + (float) k * 3.1415927F * 7.0F / 23.0F + 6.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 7.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 7.5D, d0, d1, f2, (int) (24));
							}

							for (k = 0; k < 26; ++k) {
								f2 = f + (float) k * 3.1415927F * 8.0F / 26.0F + 7.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 8.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 8.5D, d0, d1, f2, (int) (28));
							}

							for (k = 0; k < 29; ++k) {
								f2 = f + (float) k * 3.1415927F * 9.0F / 29.0F + 8.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 9.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 9.5D, d0, d1, f2, (int) (32));
							}

							for (k = 0; k < 32; ++k) {
								f2 = f + (float) k * 3.1415927F * 10.0F / 32.0F + 9.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 10.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 10.5D, d0, d1, f2, (int) (36));
							}

							for (k = 0; k < 35; ++k) {
								f2 = f + (float) k * 3.1415927F * 11.0F / 35.0F + 10.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 11.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 11.5D, d0, d1, f2, (int) (36));
							}

							for (k = 0; k < 38; ++k) {
								f2 = f + (float) k * 3.1415927F * 12.0F / 38.0F + 11.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 12.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 12.5D, d0, d1, f2, (int) (36));
							}

							for (k = 0; k < 41; ++k) {
								f2 = f + (float) k * 3.1415927F * 13.0F / 41.0F + 12.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 13.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 13.5D, d0, d1, f2, (int) (36));
							}

							for (k = 0; k < 44; ++k) {
								f2 = f + (float) k * 3.1415927F * 14.0F / 44.0F + 13.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 14.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 14.5D, d0, d1, f2, (int) (36));
							}

							for (k = 0; k < 47; ++k) {
								f2 = f + (float) k * 3.1415927F * 15.0F / 47.0F + 14.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 15.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 15.5D, d0, d1, f2, (int) (36));
							}

							for (k = 0; k < 50; ++k) {
								f2 = f + (float) k * 3.1415927F * 16.0F / 48.0F + 15.2566371F;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f2) * 16.5D, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f2) * 16.5D, d0, d1, f2, (int) (36));
							}

							for (k = 0; k < 16; ++k) {
								f = (float) MathHelper.atan2(livingentity.getZ() - mob.getZ(), livingentity.getX() - mob.getX());
								double d2 = 1.25D * (double) (k + 1);
								int j = k + 36;
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}
						} else {
							int x = IllagerWarriorCloneEntity.this.getRandom().nextInt(6);
							if (x == 1) {
								for (k = 0; k < 32; ++k) {
									double d2 = 1.25D * (double) (k + 1);
									int j = k;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								}
								float f2;
								for (k = 0; k < 5; ++k) {
									f2 = f + (float) k * 3.1415927F * 0.4F;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 1.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 1.5D, d0, d1, f2, (int) (10));
								}

								for (k = 0; k < 8; ++k) {
									f2 = f + (float) k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 2.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, (int) (14));
								}

								for (k = 0; k < 11; ++k) {
									f2 = f + (float) k * 3.1415927F * 3.0F / 11.0F + 2.2566371F;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 3.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 3.5D, d0, d1, f2, (int) (18));
								}

								for (k = 0; k < 14; ++k) {
									f2 = f + (float) k * 3.1415927F * 4.0F / 14.0F + 3.2566371F;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 4.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 4.5D, d0, d1, f2, (int) (22));
								}

								for (k = 0; k < 17; ++k) {
									f2 = f + (float) k * 3.1415927F * 5.0F / 17.0F + 4.2566371F;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 5.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 5.5D, d0, d1, f2, (int) (26));
								}

								for (k = 0; k < 20; ++k) {
									f2 = f + (float) k * 3.1415927F * 6.0F / 20.0F + 5.2566371F;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 6.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 6.5D, d0, d1, f2, (int) (30));
								}

								for (k = 0; k < 23; ++k) {
									f2 = f + (float) k * 3.1415927F * 7.0F / 23.0F + 6.2566371F;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 7.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 7.5D, d0, d1, f2, (int) (34));
								}

								for (k = 0; k < 26; ++k) {
									f2 = f + (float) k * 3.1415927F * 8.0F / 26.0F + 7.2566371F;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 8.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 8.5D, d0, d1, f2, (int) (38));
								}

								for (k = 0; k < 29; ++k) {
									f2 = f + (float) k * 3.1415927F * 8.0F / 29.0F + 8.2566371F;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 9.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 9.5D, d0, d1, f2, (int) (38));
								}

								for (k = 0; k < 32; ++k) {
									f2 = f + (float) k * 3.1415927F * 8.0F / 32.0F + 9.2566371F;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getTarget().getX() + (double) MathHelper.cos(f2) * 10.5D, IllagerWarriorCloneEntity.this.getTarget().getZ() + (double) MathHelper.sin(f2) * 10.5D, d0, d1, f2, (int) (38));
								}

								for (k = 0; k < 16; ++k) {
									f = (float) MathHelper.atan2(mob.getZ() - mob.getTarget().getZ(), mob.getX() - livingentity.getX());
									double d2 = 1.25D * (double) (k + 1);
									int j = k + 38;
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								}

							} else if (x <= 4) {
								x = IllagerWarriorCloneEntity.this.getRandom().nextInt(4);
								if (x == 1) {
									for (k = 0; k < 32; ++k) {
										double d2 = 1.25D * (double) (k + 1);
										int j = (int) (k / 1.2);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									}
								} else if (x == 2) {
									for (k = 0; k < 32; ++k) {
										double d2 = 1.25D * (double) (k + 1);
										double e = Math.log(k + 1) * 9;
										int j = (int) (k / 1.2);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 + e, d0, d1, f, j);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									}
								} else {
									for (k = 0; k < 32; ++k) {
										double e = Math.cos(k * 90) * 9;
										double d2 = 1.25D * (double) (k + 1);
										int j = (int) (k / 3.2);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 + e, d0, d1, f, j);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
										this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
									}
								}
							} else {
								for (k = 0; k < 32; ++k) {
									double d2 = 1.25D * (double) (k + 1);
									double e = Math.log(k + 1) * 9;
									int j = (int) (k / 1.2);
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 + e, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2 + e, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorCloneEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorCloneEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								}
							}
						}
				}
			}

		}

		public void stop() {
			super.stop();
			IllagerWarriorCloneEntity.this.setPowerfulAttack(0);
		}
		@Override
		public boolean isInterruptable() {
			return false;
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
