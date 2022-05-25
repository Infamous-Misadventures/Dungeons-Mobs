package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.capabilities.cloneable.CloneableHelper;
import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;
import com.infamous.dungeons_mobs.entities.allcustomentity.illagers.PowerfulRoyalGuardEntity;
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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
import java.util.UUID;

public class IllagerWarriorEntity extends SpellcastingIllagerEntity implements IAnimatable {

	public static final DataParameter<Integer> LIFT_TICKS = EntityDataManager.defineId(IllagerWarriorEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> DUPLICATE_TICKS = EntityDataManager.defineId(IllagerWarriorEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> ANGRY = EntityDataManager.defineId(IllagerWarriorEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> POWERFUL_ATTACK = EntityDataManager.defineId(IllagerWarriorEntity.class, DataSerializers.INT);

	public int cloneInterval = 0;
	public boolean SpellAttacking;
	public int liftInterval = 0;
	public int duplicateInterval = 0;
	public int powerfulAttackInterval = 0;
	public int spellInterval = 0;
	public int SCloneInterval = 0;
	public int timer = 0;

	AnimationFactory factory = new AnimationFactory(this);

    public IllagerWarriorEntity(World world){
        super(ModEntityTypes.ILLAGER_WARRIOR.get(), world);
    }

    public IllagerWarriorEntity(EntityType<? extends SpellcastingIllagerEntity> type, World world) {
        super(type, world);
		this.xpReward = 500;
    }

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
		for(int i=0;i<5;++i){
			IllagerWarriorEntity v = this;
			BlockPos vvf = v.blockPosition();
			int o =v.getRandom().nextInt(6);
			{
				RoyalGuardEntity vv = new RoyalGuardEntity(this.level);
				vv.moveTo(vvf,0F,0F);
				vv.populateDefaultEquipmentSlots(p_213386_2_);
				vv.setCanJoinRaid(true);
				v.level.addFreshEntity(vv);
			}
		}for(int i=0;i<3;++i){
			IllagerWarriorEntity v = this;
			BlockPos vvf = v.blockPosition();
			int o =v.getRandom().nextInt(6);
			{
				DungeonsEvokerEntity vv = new DungeonsEvokerEntity(this.level);
				vv.moveTo(vvf,0F,0F);
				vv.setCanJoinRaid(true);
				v.level.addFreshEntity(vv);
			}
		}
		return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
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
		this.goalSelector.addGoal(1, new IllagerWarriorEntity.SpellAttackGoal());
        this.goalSelector.addGoal(3, new IllagerWarriorEntity.CastingSpellGoal());
		this.goalSelector.addGoal(5, new IllagerWarriorEntity.SummonFangsGoal());
        this.goalSelector.addGoal(4, new IllagerWarriorEntity.DuplicateGoal());
		this.goalSelector.addGoal(2, new IllagerWarriorEntity.CloneGoal());
		this.goalSelector.addGoal(2, new IllagerWarriorEntity.SCloneGoal());
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

		if (this.cloneInterval > 0) {
			this.cloneInterval --;
		}

		if (this.spellInterval > 0) {
			this.spellInterval --;
		}

		if (this.SCloneInterval > 0) {
			this.SCloneInterval --;
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
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 64.0D)
				.add(Attributes.MAX_HEALTH, 500.0D);
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
            if (IllagerWarriorEntity.this.getTarget() != null) {
                IllagerWarriorEntity.this.getLookControl().setLookAt(IllagerWarriorEntity.this.getTarget(), (float) IllagerWarriorEntity.this.getMaxHeadYRot(), (float) IllagerWarriorEntity.this.getMaxHeadXRot());
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
				BlockState blockstate = IllagerWarriorEntity.this.level.getBlockState(blockpos1);
				if (blockstate.isFaceSturdy(IllagerWarriorEntity.this.level, blockpos1, Direction.UP)) {
					if (!IllagerWarriorEntity.this.level.isEmptyBlock(blockpos)) {
						BlockState blockstate1 = IllagerWarriorEntity.this.level.getBlockState(blockpos);
						VoxelShape voxelshape = blockstate1.getCollisionShape(IllagerWarriorEntity.this.level, blockpos);
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
				IllagerWarriorEntity.this.level.addFreshEntity(new EvokerFangsEntity(IllagerWarriorEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, IllagerWarriorEntity.this));
			}

		}
		public SpellAttackGoal() {
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
		}

		public boolean canUse() {
			return IllagerWarriorEntity.this.getTarget() != null && IllagerWarriorEntity.this.getLiftTicks() == 0 && IllagerWarriorEntity.this.spellInterval == 0 && IllagerWarriorEntity.this.random.nextInt(20) == 0;
		}

		public boolean canContinueToUse() {
			return IllagerWarriorEntity.this.getTarget() != null && IllagerWarriorEntity.this.isAngry() > 0;
		}

		public void start() {
			super.start();
			IllagerWarriorEntity.this.SpellAttacking = true;
			IllagerWarriorEntity.this.setAngry(24);
			IllagerWarriorEntity.this.spellInterval = (int) ((IllagerWarriorEntity.this.getRandom().nextInt(30) + 25) * (IllagerWarriorEntity.this.getHealth() / IllagerWarriorEntity.this.getMaxHealth()));
			IllagerWarriorEntity.this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, IllagerWarriorEntity.this.getSoundVolume(), IllagerWarriorEntity.this.getVoicePitch());
		}

		public void tick() {
			IllagerWarriorEntity.this.getLookControl().setLookAt(IllagerWarriorEntity.this.getTarget(), (float) IllagerWarriorEntity.this.getMaxHeadYRot(), (float) IllagerWarriorEntity.this.getMaxHeadXRot());
			IllagerWarriorEntity mob = IllagerWarriorEntity.this;
			LivingEntity target = IllagerWarriorEntity.this.getTarget();

			mob.getNavigation().stop();
			if (mob.isAngry() == 12) {

				LivingEntity livingentity = IllagerWarriorEntity.this.getTarget();
				double d0 = Math.min(livingentity.getY(), IllagerWarriorEntity.this.getY());
				double d1 = Math.max(livingentity.getY(), IllagerWarriorEntity.this.getY()) + 1.0D;
				float f = (float)MathHelper.atan2(livingentity.getZ() - IllagerWarriorEntity.this.getZ(), livingentity.getX() - IllagerWarriorEntity.this.getX());
				int k;

				if (mob.getTarget() != null)
					if (IllagerWarriorEntity.this.distanceToSqr(livingentity) < 30.0D) {
						float f2;

						for (k = 0; k < 5; ++k) {
							f2 = f + (float) k * 3.1415927F * 0.4F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 1.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 1.5D, d0, d1, f2, (int) (0));
						}

						for (k = 0; k < 8; ++k) {
							f2 = f + (float) k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 2.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, (int) (4));
						}

						for (k = 0; k < 11; ++k) {
							f2 = f + (float) k * 3.1415927F * 3.0F / 11.0F + 2.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 3.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 3.5D, d0, d1, f2, (int) (8));
						}

						for (k = 0; k < 14; ++k) {
							f2 = f + (float) k * 3.1415927F * 4.0F / 14.0F + 3.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 4.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 4.5D, d0, d1, f2, (int) (12));
						}

						for (k = 0; k < 17; ++k) {
							f2 = f + (float) k * 3.1415927F * 5.0F / 17.0F + 4.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 5.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 5.5D, d0, d1, f2, (int) (16));
						}

						for (k = 0; k < 20; ++k) {
							f2 = f + (float) k * 3.1415927F * 6.0F / 20.0F + 5.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 6.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 6.5D, d0, d1, f2, (int) (20));
						}

						for (k = 0; k < 23; ++k) {
							f2 = f + (float) k * 3.1415927F * 7.0F / 23.0F + 6.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 7.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 7.5D, d0, d1, f2, (int) (24));
						}

						for (k = 0; k < 26; ++k) {
							f2 = f + (float) k * 3.1415927F * 8.0F / 26.0F + 7.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 8.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 8.5D, d0, d1, f2, (int) (28));
						}

						for (k = 0; k < 29; ++k) {
							f2 = f + (float) k * 3.1415927F * 9.0F / 29.0F + 8.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 9.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 9.5D, d0, d1, f2, (int) (32));
						}

						for (k = 0; k < 32; ++k) {
							f2 = f + (float) k * 3.1415927F * 10.0F / 32.0F + 9.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 10.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 10.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 35; ++k) {
							f2 = f + (float) k * 3.1415927F * 11.0F / 35.0F + 10.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 11.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 11.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 38; ++k) {
							f2 = f + (float) k * 3.1415927F * 12.0F / 38.0F + 11.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 12.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 12.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 41; ++k) {
							f2 = f + (float) k * 3.1415927F * 13.0F / 41.0F + 12.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 13.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 13.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 44; ++k) {
							f2 = f + (float) k * 3.1415927F * 14.0F / 44.0F + 13.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 14.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 14.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 47; ++k) {
							f2 = f + (float) k * 3.1415927F * 15.0F / 47.0F + 14.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 15.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 15.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 50; ++k) {
							f2 = f + (float) k * 3.1415927F * 16.0F / 48.0F + 15.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 16.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 16.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 24; ++k) {
							f = (float) MathHelper.atan2(livingentity.getZ() - mob.getZ(), livingentity.getX() - mob.getX());
							double d2 = 1.25D * (double) (k + 1);
							int j = k + 36;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
						}
					} else {
						int x = IllagerWarriorEntity.this.getRandom().nextInt(6);
						if (x == 1) {
							for (k = 0; k < 64; ++k) {
								double d2 = 1.25D * (double) (k + 1);
								int j = k;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}
							float f2;

							for (k = 0; k < 5; ++k) {
								f2 = f + (float) k * 3.1415927F * 0.4F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 1.5D, target.getZ() + (double) MathHelper.sin(f2) * 1.5D, d0, d1, f2, (int) (10));
							}

							for (k = 0; k < 8; ++k) {
								f2 = f + (float) k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 2.5D, target.getZ() + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, (int) (14));
							}

							for (k = 0; k < 11; ++k) {
								f2 = f + (float) k * 3.1415927F * 3.0F / 11.0F + 2.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 3.5D, target.getZ() + (double) MathHelper.sin(f2) * 3.5D, d0, d1, f2, (int) (18));
							}

							for (k = 0; k < 14; ++k) {
								f2 = f + (float) k * 3.1415927F * 4.0F / 14.0F + 3.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 4.5D, target.getZ() + (double) MathHelper.sin(f2) * 4.5D, d0, d1, f2, (int) (22));
							}

							for (k = 0; k < 17; ++k) {
								f2 = f + (float) k * 3.1415927F * 5.0F / 17.0F + 4.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 5.5D, target.getZ() + (double) MathHelper.sin(f2) * 5.5D, d0, d1, f2, (int) (26));
							}

							for (k = 0; k < 20; ++k) {
								f2 = f + (float) k * 3.1415927F * 6.0F / 20.0F + 5.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 6.5D, target.getZ() + (double) MathHelper.sin(f2) * 6.5D, d0, d1, f2, (int) (30));
							}

							for (k = 0; k < 23; ++k) {
								f2 = f + (float) k * 3.1415927F * 7.0F / 23.0F + 6.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 7.5D, target.getZ() + (double) MathHelper.sin(f2) * 7.5D, d0, d1, f2, (int) (34));
							}

							for (k = 0; k < 26; ++k) {
								f2 = f + (float) k * 3.1415927F * 8.0F / 26.0F + 7.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 8.5D, target.getZ() + (double) MathHelper.sin(f2) * 8.5D, d0, d1, f2, (int) (38));
							}

							for (k = 0; k < 29; ++k) {
								f2 = f + (float) k * 3.1415927F * 9.0F / 29.0F + 8.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 9.5D, target.getZ() + (double) MathHelper.sin(f2) * 9.5D, d0, d1, f2, (int) (42));
							}

							for (k = 0; k < 32; ++k) {
								f2 = f + (float) k * 3.1415927F * 10.0F / 32.0F + 9.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 10.5D, target.getZ() + (double) MathHelper.sin(f2) * 10.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 35; ++k) {
								f2 = f + (float) k * 3.1415927F * 11.0F / 35.0F + 10.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 11.5D, target.getZ() + (double) MathHelper.sin(f2) * 11.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 38; ++k) {
								f2 = f + (float) k * 3.1415927F * 12.0F / 38.0F + 11.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 12.5D, target.getZ() + (double) MathHelper.sin(f2) * 12.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 41; ++k) {
								f2 = f + (float) k * 3.1415927F * 13.0F / 41.0F + 12.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 13.5D, target.getZ() + (double) MathHelper.sin(f2) * 13.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 44; ++k) {
								f2 = f + (float) k * 3.1415927F * 14.0F / 44.0F + 13.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 14.5D, target.getZ() + (double) MathHelper.sin(f2) * 14.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 47; ++k) {
								f2 = f + (float) k * 3.1415927F * 15.0F / 47.0F + 14.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 15.5D, target.getZ() + (double) MathHelper.sin(f2) * 15.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 50; ++k) {
								f2 = f + (float) k * 3.1415927F * 16.0F / 48.0F + 15.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 16.5D, target.getZ() + (double) MathHelper.sin(f2) * 16.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 24; ++k) {
								f = (float) MathHelper.atan2(livingentity.getZ() - mob.getZ(), livingentity.getX() - mob.getX());
								double d2 = 1.25D * (double) (k + 1);
								int j = k + 46;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f) * d2, target.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(target.getX() - (double) MathHelper.cos(f) * d2, target.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f) * d2, target.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(target.getX() - (double) MathHelper.cos(f) * d2, target.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}

						}else if (x <= 4){
							x = IllagerWarriorEntity.this.getRandom().nextInt(4);
							if (x == 1 ) {
								for (k = 0; k < 64; ++k) {
									double d2 = 1.25D * (double) (k + 1);
									int j = (int) (k / 1.2);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								}
							}else {
								for (k = 0; k < 64; ++k) {
									double e = Math.cos(k * 90) * 9;
									double d2 = 1.25D * (double) (k + 1);
									int j = (int) (k / 1.2);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 + e, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
								}
							}
						}else {
							for (k = 0; k < 32; ++k){
								double d2 = 1.25D * (double) (k + 1);
								double e = Math.log(k+1) * 9;
								int j = (int) (k / 1.2);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 +e , d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e , IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 -e , d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e , IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 + e, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}

							for (k = 0; k < 32; ++k) {
								f = (float) MathHelper.atan2(livingentity.getZ() - mob.getZ(), livingentity.getX() - mob.getX());
								double d2 = 1.25D * (double) (k + 1);
								int j = (int) (k / 1.2 + 26.6667);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}
						}
					}

			}

		}

		public void stop() {
			super.stop();
			IllagerWarriorEntity.this.setLiftTicks(0);
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
				BlockState blockstate = IllagerWarriorEntity.this.level.getBlockState(blockpos1);
				if (blockstate.isFaceSturdy(IllagerWarriorEntity.this.level, blockpos1, Direction.UP)) {
					if (!IllagerWarriorEntity.this.level.isEmptyBlock(blockpos)) {
						BlockState blockstate1 = IllagerWarriorEntity.this.level.getBlockState(blockpos);
						VoxelShape voxelshape = blockstate1.getCollisionShape(IllagerWarriorEntity.this.level, blockpos);
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
				IllagerWarriorEntity.this.level.addFreshEntity(new EvokerFangsEntity(IllagerWarriorEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, IllagerWarriorEntity.this));
			}

		}
	      public SummonFangsGoal() {
	         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
	      }

	      public boolean canUse() {
	         return IllagerWarriorEntity.this.getTarget() != null && IllagerWarriorEntity.this.getLiftTicks() == 0 && IllagerWarriorEntity.this.liftInterval == 0 && IllagerWarriorEntity.this.random.nextInt(20) == 0;
	      }
	      
	      public boolean canContinueToUse() {
			  return IllagerWarriorEntity.this.getTarget() != null && IllagerWarriorEntity.this.getLiftTicks() > 0;
	      }


	    
	    public void start() {
	    super.start();
		IllagerWarriorEntity.this.SpellAttacking = false;
	    IllagerWarriorEntity.this.liftInterval = 800;
		IllagerWarriorEntity.this.duplicateInterval = 400;
	    IllagerWarriorEntity.this.setLiftTicks(62);
		IllagerWarriorEntity.this.timer = 5;
	    IllagerWarriorEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, IllagerWarriorEntity.this.getSoundVolume(), IllagerWarriorEntity.this.getVoicePitch());
	    }
	      
	    public void tick() {
			IllagerWarriorEntity.this.getLookControl().setLookAt(IllagerWarriorEntity.this.getTarget(), (float) IllagerWarriorEntity.this.getMaxHeadYRot(), (float) IllagerWarriorEntity.this.getMaxHeadXRot());
			IllagerWarriorEntity mob = IllagerWarriorEntity.this;

			mob.getNavigation().stop();
			if (mob.getLiftTicks() >= 40) {
				if (mob.timer == 0) {
					int k;
					LivingEntity livingentity = IllagerWarriorEntity.this.getTarget();
					double d0 = Math.min(livingentity.getY(), IllagerWarriorEntity.this.getY());
					double d1 = Math.max(livingentity.getY(), IllagerWarriorEntity.this.getY()) + 1.0D;
					float f = (float) MathHelper.atan2(livingentity.getZ() - IllagerWarriorEntity.this.getZ(), livingentity.getX() - IllagerWarriorEntity.this.getX());
					for (k = 0; k < 32; ++k){
						double d2 = 1.25D * (double) (k + 1);
						int j = (int) (k / 1.2);
						this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 , IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2  , d0, d1, f, j);
					}
					mob.timer = 5;
				}
			}

			if (mob.getLiftTicks() == 32) {
				ServerWorld serverworld = (ServerWorld) IllagerWarriorEntity.this.level;
				int difficultyAsInt = IllagerWarriorEntity.this.level.getDifficulty().getId();
				int mobsToSummon = difficultyAsInt + 6; // 7 on easy, 8 on normal, 9 on hard
				for (int i = 0; i < mobsToSummon; ++i) {
					BlockPos blockpos = IllagerWarriorEntity.this.blockPosition().offset(-2 + IllagerWarriorEntity.this.random.nextInt(5), 1, -2 + IllagerWarriorEntity.this.random.nextInt(5));
					VexEntity vexentity = (VexEntity) EntityType.VEX.create(IllagerWarriorEntity.this.level);
					vexentity.moveTo(blockpos, 0.0F, 0.0F);
					vexentity.finalizeSpawn(serverworld, IllagerWarriorEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
					vexentity.setOwner(IllagerWarriorEntity.this);
					vexentity.setBoundOrigin(blockpos);
					vexentity.setLimitedLife(20 * (30 + IllagerWarriorEntity.this.random.nextInt(90)));
					serverworld.addFreshEntityWithPassengers(vexentity);
				}
			}
			if (mob.getLiftTicks() == 13) {

				LivingEntity livingentity = IllagerWarriorEntity.this.getTarget();
				double d0 = Math.min(livingentity.getY(), IllagerWarriorEntity.this.getY());
				double d1 = Math.max(livingentity.getY(), IllagerWarriorEntity.this.getY()) + 1.0D;
				float f = (float) MathHelper.atan2(livingentity.getZ() - IllagerWarriorEntity.this.getZ(), livingentity.getX() - IllagerWarriorEntity.this.getX());
				int k;

				if (mob.getTarget() != null)
					if (IllagerWarriorEntity.this.distanceToSqr(livingentity) < 30.0D) {
						float f2;

						for (k = 0; k < 5; ++k) {
							f2 = f + (float) k * 3.1415927F * 0.4F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 1.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 1.5D, d0, d1, f2, (int) (0));
						}

						for (k = 0; k < 8; ++k) {
							f2 = f + (float) k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 2.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, (int) (4));
						}

						for (k = 0; k < 11; ++k) {
							f2 = f + (float) k * 3.1415927F * 3.0F / 11.0F + 2.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 3.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 3.5D, d0, d1, f2, (int) (8));
						}

						for (k = 0; k < 14; ++k) {
							f2 = f + (float) k * 3.1415927F * 4.0F / 14.0F + 3.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 4.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 4.5D, d0, d1, f2, (int) (12));
						}

						for (k = 0; k < 17; ++k) {
							f2 = f + (float) k * 3.1415927F * 5.0F / 17.0F + 4.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 5.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 5.5D, d0, d1, f2, (int) (16));
						}

						for (k = 0; k < 20; ++k) {
							f2 = f + (float) k * 3.1415927F * 6.0F / 20.0F + 5.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 6.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 6.5D, d0, d1, f2, (int) (20));
						}

						for (k = 0; k < 23; ++k) {
							f2 = f + (float) k * 3.1415927F * 7.0F / 23.0F + 6.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 7.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 7.5D, d0, d1, f2, (int) (24));
						}

						for (k = 0; k < 26; ++k) {
							f2 = f + (float) k * 3.1415927F * 8.0F / 26.0F + 7.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 8.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 8.5D, d0, d1, f2, (int) (28));
						}

						for (k = 0; k < 29; ++k) {
							f2 = f + (float) k * 3.1415927F * 9.0F / 29.0F + 8.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 9.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 9.5D, d0, d1, f2, (int) (32));
						}

						for (k = 0; k < 32; ++k) {
							f2 = f + (float) k * 3.1415927F * 10.0F / 32.0F + 9.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 10.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 10.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 35; ++k) {
							f2 = f + (float) k * 3.1415927F * 11.0F / 35.0F + 10.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 11.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 11.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 38; ++k) {
							f2 = f + (float) k * 3.1415927F * 12.0F / 38.0F + 11.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 12.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 12.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 41; ++k) {
							f2 = f + (float) k * 3.1415927F * 13.0F / 41.0F + 12.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 13.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 13.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 44; ++k) {
							f2 = f + (float) k * 3.1415927F * 14.0F / 44.0F + 13.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 14.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 14.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 47; ++k) {
							f2 = f + (float) k * 3.1415927F * 15.0F / 47.0F + 14.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 15.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 15.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 50; ++k) {
							f2 = f + (float) k * 3.1415927F * 16.0F / 48.0F + 15.2566371F;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f2) * 16.5D, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f2) * 16.5D, d0, d1, f2, (int) (36));
						}

						for (k = 0; k < 24; ++k) {
							f = (float) MathHelper.atan2(livingentity.getZ() - mob.getZ(), livingentity.getX() - mob.getX());
							double d2 = 1.25D * (double) (k + 1);
							int j = k + 36;
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
						}
					} else {
						LivingEntity target = IllagerWarriorEntity.this.getTarget();
						int x = IllagerWarriorEntity.this.getRandom().nextInt(6);
						if (x == 1) {
							for (k = 0; k < 64; ++k) {
								double d2 = 1.25D * (double) (k + 1);
								int j = k;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}
							float f2;

							for (k = 0; k < 5; ++k) {
								f2 = f + (float) k * 3.1415927F * 0.4F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 1.5D, target.getZ() + (double) MathHelper.sin(f2) * 1.5D, d0, d1, f2, (int) (10));
							}

							for (k = 0; k < 8; ++k) {
								f2 = f + (float) k * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 2.5D, target.getZ() + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, (int) (14));
							}

							for (k = 0; k < 11; ++k) {
								f2 = f + (float) k * 3.1415927F * 3.0F / 11.0F + 2.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 3.5D, target.getZ() + (double) MathHelper.sin(f2) * 3.5D, d0, d1, f2, (int) (18));
							}

							for (k = 0; k < 14; ++k) {
								f2 = f + (float) k * 3.1415927F * 4.0F / 14.0F + 3.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 4.5D, target.getZ() + (double) MathHelper.sin(f2) * 4.5D, d0, d1, f2, (int) (22));
							}

							for (k = 0; k < 17; ++k) {
								f2 = f + (float) k * 3.1415927F * 5.0F / 17.0F + 4.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 5.5D, target.getZ() + (double) MathHelper.sin(f2) * 5.5D, d0, d1, f2, (int) (26));
							}

							for (k = 0; k < 20; ++k) {
								f2 = f + (float) k * 3.1415927F * 6.0F / 20.0F + 5.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 6.5D, target.getZ() + (double) MathHelper.sin(f2) * 6.5D, d0, d1, f2, (int) (30));
							}

							for (k = 0; k < 23; ++k) {
								f2 = f + (float) k * 3.1415927F * 7.0F / 23.0F + 6.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 7.5D, target.getZ() + (double) MathHelper.sin(f2) * 7.5D, d0, d1, f2, (int) (34));
							}

							for (k = 0; k < 26; ++k) {
								f2 = f + (float) k * 3.1415927F * 8.0F / 26.0F + 7.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 8.5D, target.getZ() + (double) MathHelper.sin(f2) * 8.5D, d0, d1, f2, (int) (38));
							}

							for (k = 0; k < 29; ++k) {
								f2 = f + (float) k * 3.1415927F * 9.0F / 29.0F + 8.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 9.5D, target.getZ() + (double) MathHelper.sin(f2) * 9.5D, d0, d1, f2, (int) (42));
							}

							for (k = 0; k < 32; ++k) {
								f2 = f + (float) k * 3.1415927F * 10.0F / 32.0F + 9.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 10.5D, target.getZ() + (double) MathHelper.sin(f2) * 10.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 35; ++k) {
								f2 = f + (float) k * 3.1415927F * 11.0F / 35.0F + 10.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 11.5D, target.getZ() + (double) MathHelper.sin(f2) * 11.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 38; ++k) {
								f2 = f + (float) k * 3.1415927F * 12.0F / 38.0F + 11.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 12.5D, target.getZ() + (double) MathHelper.sin(f2) * 12.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 41; ++k) {
								f2 = f + (float) k * 3.1415927F * 13.0F / 41.0F + 12.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 13.5D, target.getZ() + (double) MathHelper.sin(f2) * 13.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 44; ++k) {
								f2 = f + (float) k * 3.1415927F * 14.0F / 44.0F + 13.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 14.5D, target.getZ() + (double) MathHelper.sin(f2) * 14.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 47; ++k) {
								f2 = f + (float) k * 3.1415927F * 15.0F / 47.0F + 14.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 15.5D, target.getZ() + (double) MathHelper.sin(f2) * 15.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 50; ++k) {
								f2 = f + (float) k * 3.1415927F * 16.0F / 48.0F + 15.2566371F;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f2) * 16.5D, target.getZ() + (double) MathHelper.sin(f2) * 16.5D, d0, d1, f2, (int) (46));
							}

							for (k = 0; k < 24; ++k) {
								f = (float) MathHelper.atan2(livingentity.getZ() - mob.getZ(), livingentity.getX() - mob.getX());
								double d2 = 1.25D * (double) (k + 1);
								int j = k + 46;
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f) * d2, target.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(target.getX() - (double) MathHelper.cos(f) * d2, target.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(target.getX() + (double) MathHelper.cos(f) * d2, target.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(target.getX() - (double) MathHelper.cos(f) * d2, target.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}

						}else if (x <= 4){
							x = IllagerWarriorEntity.this.getRandom().nextInt(4);
							if (x == 1 ) {
								for (k = 0; k < 64; ++k) {
									double d2 = 1.25D * (double) (k + 1);
									int j = (int) (k / 1.2);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								}
							}else {
								for (k = 0; k < 64; ++k) {
									double e = Math.cos(k * 90) * 9;
									double d2 = 1.25D * (double) (k + 1);
									int j = (int) (k / 1.2);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 + e, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
									this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
								}
							}
						}else {
							for (k = 0; k < 32; ++k){
								double d2 = 1.25D * (double) (k + 1);
								double e = Math.log(k+1) * 9;
								int j = (int) (k / 1.2);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 +e , d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e , IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 - e, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 + e, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 -e , d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2 - e , IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2 + e, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}

							for (k = 0; k < 32; ++k) {
								f = (float) MathHelper.atan2(livingentity.getZ() - mob.getZ(), livingentity.getX() - mob.getX());
								double d2 = 1.25D * (double) (k + 1);
								int j = (int) (k / 1.2 + 26.6667);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() + (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() - (double) MathHelper.sin(f) * d2, d0, d1, f, j);
								this.createSpellEntity(IllagerWarriorEntity.this.getX() - (double) MathHelper.cos(f) * d2, IllagerWarriorEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
							}
						}
					}
			}

	    }
	    
	    public void stop() {
	    super.stop();
	    IllagerWarriorEntity.this.setLiftTicks(0);
	    }
	   }
    
    class DuplicateGoal extends Goal {
	      public DuplicateGoal() {
	         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
	      }

	      public boolean canUse() {
	         return IllagerWarriorEntity.this.getTarget() != null && IllagerWarriorEntity.this.getDuplicateTicks() == 0 && IllagerWarriorEntity.this.duplicateInterval == 0;
	      }
	      
	      public boolean canContinueToUse() {
	    	  	return IllagerWarriorEntity.this.getTarget() != null && IllagerWarriorEntity.this.getDuplicateTicks() > 0;
	      }
	      
	      @Override
	    public boolean isInterruptable() {
	    	return false;
	    }
	    
	    public void start() {
	    super.start();
		IllagerWarriorEntity.this.SpellAttacking = false;
	    IllagerWarriorEntity.this.duplicateInterval = 800;
		IllagerWarriorEntity.this.liftInterval = 400;
	    IllagerWarriorEntity.this.setDuplicateTicks(48);
	    IllagerWarriorEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, IllagerWarriorEntity.this.getSoundVolume(), IllagerWarriorEntity.this.getVoicePitch());
	    }
	      
	    public void tick() {
            IllagerWarriorEntity.this.getLookControl().setLookAt(IllagerWarriorEntity.this.getTarget(), (float) IllagerWarriorEntity.this.getMaxHeadYRot(), (float) IllagerWarriorEntity.this.getMaxHeadXRot());

			LivingEntity target = IllagerWarriorEntity.this.getTarget();
	    	IllagerWarriorEntity mob = IllagerWarriorEntity.this;
	    	
	    	mob.getNavigation().stop();
	    	
          if (mob.getDuplicateTicks() == 18) {
              this.summonIllusionerClones();
           }
	    }

           private void summonIllusionerClones(){
			   ServerWorld serverworld = (ServerWorld) IllagerWarriorEntity.this.level;
              int difficultyAsInt = IllagerWarriorEntity.this.level.getDifficulty().getId();
              int mobsToSummon = difficultyAsInt * 3 + 3; // 6 on easy, 9 on normal, 12 on hard
              for(int i = 0; i < mobsToSummon; ++i) {
				  BlockPos blockpos = IllagerWarriorEntity.this.blockPosition().offset(-2 + IllagerWarriorEntity.this.random.nextInt(5), 1, -2 + IllagerWarriorEntity.this.random.nextInt(5));
				  VexEntity vexentity = (VexEntity)EntityType.VEX.create(IllagerWarriorEntity.this.level);
				  vexentity.moveTo(blockpos, 0.0F, 0.0F);
				  vexentity.finalizeSpawn(serverworld, IllagerWarriorEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
				  vexentity.setOwner(IllagerWarriorEntity.this);
				  vexentity.setBoundOrigin(blockpos);
				  vexentity.setLimitedLife(20 * (80 + IllagerWarriorEntity.this.random.nextInt(80)));
				  serverworld.addFreshEntityWithPassengers(vexentity);
              }
           }

	   }
	class CloneGoal extends Goal {
		public CloneGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
		}

		public boolean canUse() {
			return IllagerWarriorEntity.this.getTarget() != null &&
					IllagerWarriorEntity.this.getDuplicateTicks() == 0 &&
					IllagerWarriorEntity.this.cloneInterval == 0 &&
					IllagerWarriorEntity.this.getHealth() < IllagerWarriorEntity.this.getMaxHealth() / 1.3;
		}

		public boolean canContinueToUse() {
			return IllagerWarriorEntity.this.getTarget() != null &&
					IllagerWarriorEntity.this.getDuplicateTicks() > 0;
		}

		@Override
		public boolean isInterruptable() {
			return false;
		}

		public void start() {
			super.start();
			IllagerWarriorEntity.this.cloneInterval = 480;
			IllagerWarriorEntity.this.SCloneInterval = 380;
			IllagerWarriorEntity.this.setDuplicateTicks(40);
			IllagerWarriorEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, IllagerWarriorEntity.this.getSoundVolume(), IllagerWarriorEntity.this.getVoicePitch());
		}

		public void tick() {
			IllagerWarriorEntity.this.getLookControl().setLookAt(IllagerWarriorEntity.this.getTarget(), (float)IllagerWarriorEntity.this.getMaxHeadYRot(), (float)IllagerWarriorEntity.this.getMaxHeadXRot());
			LivingEntity target = IllagerWarriorEntity.this.getTarget();
			IllagerWarriorEntity mob = IllagerWarriorEntity.this;

			mob.getNavigation().stop();

			if (mob.getDuplicateTicks() == 1) {
				this.summonIllusionerClones();
				if (!(target.getEntity() instanceof PlayerEntity || target.getEntity() instanceof ServerPlayerEntity)) {
					Entity copy = target.getType().create(target.level);
					CompoundNBT compoundNBT = new CompoundNBT();
					compoundNBT = target.saveWithoutId(compoundNBT);
					UUID uuid = copy.getUUID();
					copy.load(compoundNBT);
					copy.setUUID(uuid);
					target.level.addFreshEntity(copy);
					target.remove();
				}
				IllagerWarriorEntity.this.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, IllagerWarriorEntity.this.getSoundVolume(), IllagerWarriorEntity.this.getVoicePitch());
				BlockPos blockpos = IllagerWarriorEntity.this.getTarget().blockPosition().offset(-5 + IllagerWarriorEntity.this.getRandom().nextInt(10), 0, -5 + IllagerWarriorEntity.this.getRandom().nextInt(10));
				IllagerWarriorEntity.this.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
			}
		}

		private void summonIllusionerClones(){
			int difficultyAsInt = IllagerWarriorEntity.this.level.getDifficulty().getId();
			int mobsToSummon = difficultyAsInt * 4 + 3; // 6 on easy, 9 on normal, 12 on hard
			for(int i = 0; i < mobsToSummon; ++i) {
				BlockPos blockpos = IllagerWarriorEntity.this.getTarget().blockPosition().offset(-5 + IllagerWarriorEntity.this.getRandom().nextInt(8), 0, -5 + IllagerWarriorEntity.this.getRandom().nextInt(8));
				IllagerWarriorCloneEntity illusionerCloneEntity = new IllagerWarriorCloneEntity(IllagerWarriorEntity.this.level, 45, true,IllagerWarriorEntity.this.getTarget());
				DifficultyInstance difficultyForLocation = IllagerWarriorEntity.this.level.getCurrentDifficultyAt(blockpos);
				illusionerCloneEntity.moveTo(blockpos, 0.0F, 0.0F);
				illusionerCloneEntity.finalizeSpawn((IServerWorld) illusionerCloneEntity.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
				IllagerWarriorEntity.this.level.addFreshEntity(illusionerCloneEntity);
				ICloneable cloneable = CloneableHelper.getCloneableCapability(IllagerWarriorEntity.this);
				if(cloneable != null){
					cloneable.addClone(illusionerCloneEntity.getUUID());
				}
			}
		}

		public void stop() {
			super.stop();
			IllagerWarriorEntity.this.setDuplicateTicks(0);
			if (IllagerWarriorEntity.this.getRandom().nextInt(3) != 0) {
				IllagerWarriorEntity.this.spellInterval = 0;
				IllagerWarriorEntity.this.powerfulAttackInterval = 5;
			}
			else {
				IllagerWarriorEntity.this.powerfulAttackInterval = 0;
				IllagerWarriorEntity.this.spellInterval = 5;
			}
		}
	}

	class SCloneGoal extends Goal {
		public SCloneGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
		}

		public boolean canUse() {
			return IllagerWarriorEntity.this.getTarget() != null &&
					IllagerWarriorEntity.this.getDuplicateTicks() == 0 &&
					IllagerWarriorEntity.this.SCloneInterval == 0 ;
		}

		public boolean canContinueToUse() {
			return IllagerWarriorEntity.this.getTarget() != null &&
					IllagerWarriorEntity.this.getDuplicateTicks() > 0;
		}

		@Override
		public boolean isInterruptable() {
			return false;
		}

		public void start() {
			super.start();
			IllagerWarriorEntity.this.SCloneInterval = 380;
			IllagerWarriorEntity.this.setDuplicateTicks(40);
			IllagerWarriorEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, IllagerWarriorEntity.this.getSoundVolume(), IllagerWarriorEntity.this.getVoicePitch());
		}

		public void tick() {
			IllagerWarriorEntity.this.getLookControl().setLookAt(IllagerWarriorEntity.this.getTarget(), (float)IllagerWarriorEntity.this.getMaxHeadYRot(), (float)IllagerWarriorEntity.this.getMaxHeadXRot());
			LivingEntity target = IllagerWarriorEntity.this.getTarget();
			IllagerWarriorEntity mob = IllagerWarriorEntity.this;

			mob.getNavigation().stop();

			if (mob.getDuplicateTicks() == 1) {
				this.summonIllusionerClones();
				if (!(target.getEntity() instanceof PlayerEntity || target.getEntity() instanceof ServerPlayerEntity)) {
					Entity copy = target.getType().create(target.level);
					CompoundNBT compoundNBT = new CompoundNBT();
					compoundNBT = target.saveWithoutId(compoundNBT);
					UUID uuid = copy.getUUID();
					copy.load(compoundNBT);
					copy.setUUID(uuid);
					target.level.addFreshEntity(copy);
					target.remove();
				}
				IllagerWarriorEntity.this.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, IllagerWarriorEntity.this.getSoundVolume(), IllagerWarriorEntity.this.getVoicePitch());
				BlockPos blockpos = IllagerWarriorEntity.this.blockPosition().offset(-5 + IllagerWarriorEntity.this.getRandom().nextInt(10), 0, -5 + IllagerWarriorEntity.this.getRandom().nextInt(10));
				IllagerWarriorEntity.this.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
			}
		}

		private void summonIllusionerClones(){
			int difficultyAsInt = IllagerWarriorEntity.this.level.getDifficulty().getId();
			int mobsToSummon = difficultyAsInt * 4 + 4; // 6 on easy, 9 on normal, 12 on hard
			for(int i = 0; i < mobsToSummon; ++i) {
				BlockPos blockpos = IllagerWarriorEntity.this.blockPosition().offset(-5 + IllagerWarriorEntity.this.getRandom().nextInt(20), 0, -5 + IllagerWarriorEntity.this.getRandom().nextInt(20));
				IllagerWarriorCloneEntity illusionerCloneEntity = new IllagerWarriorCloneEntity(IllagerWarriorEntity.this.level, 320, false,IllagerWarriorEntity.this.getTarget());
				DifficultyInstance difficultyForLocation = IllagerWarriorEntity.this.level.getCurrentDifficultyAt(blockpos);
				illusionerCloneEntity.moveTo(blockpos, 0.0F, 0.0F);
				illusionerCloneEntity.finalizeSpawn((IServerWorld) illusionerCloneEntity.level, difficultyForLocation, SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
				IllagerWarriorEntity.this.level.addFreshEntity(illusionerCloneEntity);
				ICloneable cloneable = CloneableHelper.getCloneableCapability(IllagerWarriorEntity.this);
				if(cloneable != null){
					cloneable.addClone(illusionerCloneEntity.getUUID());
				}
			}
		}

		public void stop() {
			super.stop();
			IllagerWarriorEntity.this.setDuplicateTicks(0);
			if (IllagerWarriorEntity.this.getRandom().nextInt(3) != 0) {
				IllagerWarriorEntity.this.spellInterval = 0;
				IllagerWarriorEntity.this.powerfulAttackInterval = 5;
			}
			else {
				IllagerWarriorEntity.this.powerfulAttackInterval = 0;
				IllagerWarriorEntity.this.spellInterval = 5;
			}
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

	@Override
	public boolean removeWhenFarAway(double p_213397_1_) {
		return false;
	}
}
