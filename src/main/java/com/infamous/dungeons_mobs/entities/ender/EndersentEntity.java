package com.infamous.dungeons_mobs.entities.ender;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EndersentEntity extends AbstractEnderlingEntity implements IAnimatable {
	
	private final ServerBossInfo bossEvent = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setCreateWorldFog(true).setPlayBossMusic(true);
	   
	AnimationFactory factory = new AnimationFactory(this);
	
	   public static final DataParameter<Integer> TELEPORTING = EntityDataManager.defineId(EndersentEntity.class, DataSerializers.INT);

	public EndersentEntity(EntityType<? extends EndersentEntity> p_i50210_1_, World p_i50210_2_) {
		super(p_i50210_1_, p_i50210_2_);
		this.xpReward = 50;
	}
	
	   protected void registerGoals() {
		      this.goalSelector.addGoal(0, new SwimGoal(this));
		      this.goalSelector.addGoal(2, new EndersentEntity.AttackGoal(1.0D));
		      this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
		      this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		      this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		      this.targetSelector.addGoal(2, new HurtByTargetGoal(this, AbstractEnderlingEntity.class).setAlertOthers().setUnseenMemoryTicks(500));
		      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true).setUnseenMemoryTicks(500));
		      this.targetSelector.addGoal(1, new AbstractEnderlingEntity.FindPlayerGoal(this, null));
		      
		      
		      //this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, AbstractEndermanVariant.class, true, false));
		   }
	  
	//public void load(CompoundNBT p_70020_1_) {
	//	super.load(p_70020_1_);
	//	
	//	this.setMusicTicks(30);
	//}
	   
	   protected void defineSynchedData() {
		      super.defineSynchedData();
		      this.entityData.define(TELEPORTING, 0);
		   }
	   
	   public int isTeleporting() {
		      return this.entityData.get(TELEPORTING);
		   }

		   public void setTeleporting(int p_189794_1_) {
			   
				if (p_189794_1_ == 15) {
					if (this.getTarget() != null) {
						this.setPos(this.getTarget().getX() - 5 + this.random.nextInt(10), this.getTarget().getY(), this.getTarget().getZ() - 5 + this.random.nextInt(10));
			            this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, ModSoundEvents.ENDERSENT_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
			            this.playSound(ModSoundEvents.ENDERSENT_TELEPORT.get(), 1.0F, 1.0F);
					} else {
						this.setPos(this.getX() - 20 + this.random.nextInt(40), this.getY(), this.getZ() - 20 + this.random.nextInt(40));
			            this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, ModSoundEvents.ENDERSENT_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
			            this.playSound(ModSoundEvents.ENDERSENT_TELEPORT.get(), 1.0F, 1.0F);
					}
				}
				
		      this.entityData.set(TELEPORTING, p_189794_1_);
		   }
	   
		@Override
		   protected void tickDeath() {
			      ++this.deathTime;
			      if (this.deathTime == 100) {
			         this.remove();

			         for(int i = 0; i < 20; ++i) {
			            double d0 = this.random.nextGaussian() * 0.02D;
			            double d1 = this.random.nextGaussian() * 0.02D;
			            double d2 = this.random.nextGaussian() * 0.02D;
			            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
			         }
			      }

			   }
	   
	   public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
		      super.readAdditionalSaveData(p_70037_1_);
		      if (this.hasCustomName()) {
		         this.bossEvent.setName(this.getDisplayName());
		      }

		   }

		   public void setCustomName(@Nullable ITextComponent p_200203_1_) {
		      super.setCustomName(p_200203_1_);
		      this.bossEvent.setName(this.getDisplayName());
		   }
	   
	   public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		      return MonsterEntity.createMonsterAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0.85D).add(Attributes.MAX_HEALTH, 200.0D).add(Attributes.MOVEMENT_SPEED, (double)0.2F).add(Attributes.ATTACK_DAMAGE, 14.0D).add(Attributes.FOLLOW_RANGE, 32.0D);
		   }
	   
		protected SoundEvent getAmbientSound() {
			return ModSoundEvents.ENDERSENT_IDLE.get();
		}
		
		protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
			return ModSoundEvents.ENDERSENT_HURT.get();
		}
		
		protected SoundEvent getDeathSound() {
			return ModSoundEvents.ENDERSENT_DEATH.get();
		}
		
		public boolean doHurtTarget(Entity p_70652_1_) {
		    this.playSound(ModSoundEvents.ENDERSENT_ATTACK.get(), 1.0F, 1.0F);
			return super.doHurtTarget(p_70652_1_);
		}
		
		   @Override
		protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
			   this.playSound(this.getStepSound(), 1.25F, 1.0F);
		}
		   
		   @Override
		protected float getSoundVolume() {
			return 2.0F;
		}
		   
		   protected SoundEvent getStepSound() {
			      return ModSoundEvents.ENDERSENT_STEP.get();
			   }
	   
	public void setTarget(LivingEntity p_70624_1_) {
		//if (p_70624_1_ != null && p_70624_1_ != this.getTarget()) {
		//	this.teleportTo(p_70624_1_.getX() - 3 + this.random.nextInt(6), p_70624_1_.getY(), p_70624_1_.getZ() - 3 + this.random.nextInt(6));
		//}
		super.setTarget(p_70624_1_);
	}
	   
	public void baseTick() {
		super.baseTick();
		
		if (this.isTeleporting() > 0) {
			this.setTeleporting(this.isTeleporting() - 1);
		}		
		
		//System.out.print("\r\n" + this.tickCount);
		
		//PlayerEntity player = this.level.getNearestPlayer(this, EnderlingConfig.endersent_patrol_distance.get());
		
		
		if (this.random.nextInt(500) == 0 && this.getTarget() == null) {
				this.setTeleporting(50);
		} else if (this.random.nextInt(200) == 0 && this.getTarget() != null) {
				this.setTeleporting(50);
		}
		
		if (this.isTeleporting() > 0) {
			this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
		} else {
			this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.2F);
		}

        this.bossEvent.setPercent(this.getHealth() / this.getMaxHealth());
	}
	
	   public void startSeenByPlayer(ServerPlayerEntity p_184178_1_) {
		      super.startSeenByPlayer(p_184178_1_);
		      this.bossEvent.addPlayer(p_184178_1_);
		   }

		   public void stopSeenByPlayer(ServerPlayerEntity p_184203_1_) {
		      super.stopSeenByPlayer(p_184203_1_);
		      this.bossEvent.removePlayer(p_184203_1_);
		   }
	   
		@Override
		public void registerControllers(AnimationData data) {
			data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
		}
	   
		private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			if (this.deathTime > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("endersent_death", false));
			} else if (this.isTeleporting() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("endersent_teleport", false));
			} else if (this.isAttacking() > 0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("endersent_attack", true));
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
					event.getController().setAnimation(new AnimationBuilder().addAnimation("endersent_walk", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("endersent_idle", true));
			}
			return PlayState.CONTINUE;
		}
		
		@Override
		public AnimationFactory getFactory() {
			return factory;
		}
		
		   protected boolean teleport() {
			      if (!this.level.isClientSide() && this.isAlive()) {
			         double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 32.0D;
			         double d1 = this.getY() + (double)(this.random.nextInt(8) - 4);
			         double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 32.0D;
			         return this.teleport(d0, d1, d2);
			      } else {
			         return false;
			      }
			   }

			   private boolean teleportTowards(Entity p_70816_1_) {
			      Vector3d vector3d = new Vector3d(this.getX() - p_70816_1_.getX(), this.getY(0.5D) - p_70816_1_.getEyeY(), this.getZ() - p_70816_1_.getZ());
			      vector3d = vector3d.normalize();
			      double d0 = 16.0D;
			      double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.x * 16.0D;
			      double d2 = this.getY() + (double)(this.random.nextInt(16) - 8) - vector3d.y * 16.0D;
			      double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.z * 16.0D;
			      return this.teleport(d1, d2, d3);
			   }

			   protected boolean teleport(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
			      BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(p_70825_1_, p_70825_3_, p_70825_5_);

			      while(blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
			         blockpos$mutable.move(Direction.DOWN);
			      }

			      BlockState blockstate = this.level.getBlockState(blockpos$mutable);
			      boolean flag = blockstate.getMaterial().blocksMotion();
			      boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
			      if (flag && !flag1) {
			         EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, p_70825_1_, p_70825_3_, p_70825_5_);
			         if (event.isCanceled()) return false;
			         boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
			         if (flag2 && !this.isSilent()) {
			            this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, ModSoundEvents.ENDERSENT_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
			            this.playSound(ModSoundEvents.ENDERSENT_TELEPORT.get(), 1.0F, 1.0F);
			         }

			         return flag2;
			      } else {
			         return false;
			      }
			   }
		
		class AttackGoal extends MeleeAttackGoal {
			   
			   public final EntityPredicate slimePredicate = (new EntityPredicate()).range(20.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();
			   
			      public AttackGoal(double speed) {
			         super(EndersentEntity.this, speed, true);
			      }
				      
				    public boolean canContinueToUse() {
						    return super.canContinueToUse();
				    	}
			      
			      protected double getAttackReachSqr(LivingEntity p_179512_1_) {
			          return (double)(this.mob.getBbWidth() * 5.0F * this.mob.getBbWidth() * 5.0F + p_179512_1_.getBbWidth());
			       }
			      
			    public void tick() {
			    	super.tick();
			    	
			    	EndersentEntity.this.setRunning(10);
			    }

			      
			      protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
			         double d0 = this.getAttackReachSqr(p_190102_1_);
			         if (p_190102_2_ <= d0 && this.isTimeToAttack()) {
			            this.resetAttackCooldown();
			            this.mob.doHurtTarget(p_190102_1_);
			         } else if (p_190102_2_ <= d0 * 1.5D) {
			            if (this.isTimeToAttack()) {
			               this.resetAttackCooldown();
			            }

			            if (this.getTicksUntilNextAttack() <= 30) {
			            	EndersentEntity.this.setAttacking(30);
			            }
			         } else {
			            this.resetAttackCooldown();
			         }
			      }
		   }
		
}
