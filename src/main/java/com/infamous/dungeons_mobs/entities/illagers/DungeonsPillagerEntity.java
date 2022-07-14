package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.capabilities.cloneable.CloneableHelper;
import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;
import com.infamous.dungeons_mobs.entities.illagers.minibosses.DungeonsEvokerEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.ModProjectileHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
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
import java.util.Map;

public class DungeonsPillagerEntity extends AbstractIllagerEntity implements IAnimatable, ICrossbowUser {

	private static final DataParameter<Boolean> IS_CHARGING_CROSSBOW = EntityDataManager.defineId(DungeonsPillagerEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> CS = EntityDataManager.defineId(DungeonsPillagerEntity.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Integer> LIFT_TICKS = EntityDataManager.defineId(DungeonsPillagerEntity.class, DataSerializers.INT);
	public static final DataParameter<Integer> DUPLICATE_TICKS = EntityDataManager.defineId(DungeonsPillagerEntity.class, DataSerializers.INT);
	public static final DataParameter<Boolean> ANGRY = EntityDataManager.defineId(DungeonsPillagerEntity.class, DataSerializers.BOOLEAN);

	public int liftInterval = 0;
	public int duplicateInterval = 0;

	AnimationFactory factory = new AnimationFactory(this);

    public DungeonsPillagerEntity(World world){
        super(ModEntityTypes.PILLAGER.get(), world);
    }

    public DungeonsPillagerEntity(EntityType<? extends AbstractIllagerEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
		this.targetSelector.addGoal(2, new FindTargetGoal(this, 10F));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new DungeonsPillagerEntity.DuplicateGoal());
        this.goalSelector.addGoal(4, new DungeonsPillagerEntity.LiftMobGoal());
		this.goalSelector.addGoal(5, new DungeonsPillagerEntity.RkGoal(this, 1.13));
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

		public DungeonsPillagerEntity v = DungeonsPillagerEntity.this;

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
				this.delayCounter = 23 + v.getRandom().nextInt(15);
				if (v.distanceToSqr(livingentity) <= 240) {
					v.getNavigation().moveTo(
							livingentity.getX() + (v.getRandom().nextInt(3) + 6) * (v.getRandom().nextBoolean() ? 3.14 : -3.14),
							livingentity.getY(),
							livingentity.getZ() + (v.getRandom().nextInt(3) + 6) * (v.getRandom().nextBoolean() ? 3.14 : -3.14),
							(double) this.moveSpeed * 1.1051
					);
					DungeonsPillagerEntity.this.entityData.set(CS, false);

				}else {
					if (v.distanceToSqr(livingentity) >= 40) {
						v.getNavigation().moveTo(livingentity,
								(double) this.moveSpeed
						);
					}
				}
				if (v.distanceToSqr(livingentity) >= 40 && v.distanceToSqr(livingentity) <= 240) {
					v.getNavigation().stop();
					DungeonsPillagerEntity.this.entityData.set(CS, true);
					this.attackTimer = 0;
				}
			}
			if (this.attackTimer == 240) {
				v.getNavigation().stop();
				DungeonsPillagerEntity.this.entityData.set(CS, true);
				this.attackTimer = 0;
			}
			this.attackTimer = (int) Math.max(this.attackTimer + 1, 160);
		}

		@Override
		public void stop() {
			v.getNavigation().stop();
			if (v.getTarget() == null) {
				v.setAggressive(false);
			}
		}

		public DungeonsPillagerEntity.RkGoal setMaxAttackTick(int max) {
			this.maxAttackTimer = max;
			return this;
		}
	}

    public void baseTick() {
    	super.baseTick();
    	
    	if (this.getTarget() != null) {
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
		data.addAnimationController(new AnimationController(this, "controller", 3, this::predicate));
		data.addAnimationController(new AnimationController(this, "r", 0, this::c));
	}

	private <P extends IAnimatable> PlayState c(AnimationEvent<P> event) {
		if (CrossbowItem.isCharged(this.getMainHandItem())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.charged", true));
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.no_charged", true));
		}
		return PlayState.CONTINUE;
	}
   
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.isAngry() && this.getLiftTicks() > 0) {
			event.getController().animationSpeed = 1;
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.shot", false));
		} else if (this.getDuplicateTicks() > 0) {
			    int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, DungeonsPillagerEntity.this.getMainHandItem());
				event.getController().animationSpeed = Math.max(0, 1 - 0.2 * i);
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.charging", true));
			} else if (this.getLiftTicks() > 0) {
			event.getController().animationSpeed = 1;
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.attack", true));
			} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().animationSpeed = 1.35;
					event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.walk", true));
			} else {
			event.getController().animationSpeed = 1;
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pillager.idle", true));
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
		this.entityData.define(IS_CHARGING_CROSSBOW, false);
		this.entityData.define(CS, false);
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
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MAX_HEALTH, 24.0D);
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
	public SoundEvent getCelebrateSound() {
		return SoundEvents.PILLAGER_CELEBRATE;
	}

	@Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.PILLAGER_DEATH.get();
	}

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PILLAGER_HURT;
    }

	@OnlyIn(Dist.CLIENT)
	public boolean isChargingCrossbow() {
		return this.entityData.get(IS_CHARGING_CROSSBOW);
	}

	public void setChargingCrossbow(boolean p_213671_1_) {
		this.entityData.set(IS_CHARGING_CROSSBOW, p_213671_1_);
	}



	@Override
	public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
		this.shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
	}

	@Override
	public void onCrossbowAttackPerformed() {
		this.noActionTime = 0;
	}

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
		this.populateDefaultEquipmentSlots(p_213386_2_);
		this.applyEnchantmentBuffs();
		return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
	}
	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
	}

	@Override
	public void performCrossbowAttack(LivingEntity p_234281_1_, float p_234281_2_) {
		Hand hand = ProjectileHelper.getWeaponHoldingHand(p_234281_1_, item -> item instanceof CrossbowItem);
		ItemStack itemstack = p_234281_1_.getItemInHand(hand);
		CrossbowItem.performShooting(p_234281_1_.level, p_234281_1_, hand, itemstack, p_234281_2_, (float)(14 - p_234281_1_.level.getDifficulty().getId() * 4));

		this.onCrossbowAttackPerformed();
	}

	public void applyEnchantmentBuffs() {
		if (this.getRandom().nextInt(100) <= 10 || (this.level.getDifficulty().getId() == 2 && this.getRandom().nextInt(100) <= 40) || (this.level.getDifficulty().getId() == 3 && this.getRandom().nextInt(100) <= 75)) {
			ItemStack mainhandWeapon = this.getMainHandItem();
			Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
			float e = this.getRandom().nextFloat();
			enchantmentIntegerMap.put(Enchantments.QUICK_CHARGE, Math.min(this.getRandom().nextInt(2) + 1, 2));
			if (e <= 0.25) {
				enchantmentIntegerMap.put(Enchantments.MULTISHOT, 1);
			}
			EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);
			this.applyEnchantmentBuffs();
		}
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {

		int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, this.getMainHandItem());
		int j = i == 0 ? 1 : 2;
		AbstractArrowEntity fireworkrocketentity = ProjectileHelper.getMobArrow(this, this.getProjectile(this.getMainHandItem()), distanceFactor);
		double xDifference = target.getX() - this.getX();
		double yDifference = target.getY(0.3333333333333333D) - fireworkrocketentity.getY();
		double zDifference = target.getZ() - this.getZ();
		double horizontalDifference = (double) MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference);
		fireworkrocketentity.shoot(
				xDifference ,
				yDifference + horizontalDifference * (double) 0.2F,
				zDifference, 1.6F, (float) (16 - this.level.getDifficulty().getId() * 5));
		fireworkrocketentity.setShotFromCrossbow(true);
		this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.level.addFreshEntity(fireworkrocketentity);
		{
			{
				fireworkrocketentity = ProjectileHelper.getMobArrow(this, new ItemStack(Items.ARROW), distanceFactor);
				fireworkrocketentity.shoot(
						xDifference,
						yDifference + horizontalDifference * (double) 0.2F,
						zDifference, 1.6F, (float) -20);
				fireworkrocketentity.setShotFromCrossbow(true);
				this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
				this.level.addFreshEntity(fireworkrocketentity);
			}
			{
				fireworkrocketentity = ProjectileHelper.getMobArrow(this, new ItemStack(Items.ARROW), distanceFactor);
				fireworkrocketentity.shoot(
						xDifference,
						yDifference + horizontalDifference * (double) 0.2F,
						zDifference, 1.6F, (float) 20);
				fireworkrocketentity.setShotFromCrossbow(true);
				this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
				this.level.addFreshEntity(fireworkrocketentity);
			}
			{
				fireworkrocketentity = ProjectileHelper.getMobArrow(this, new ItemStack(Items.ARROW), distanceFactor);
				fireworkrocketentity.shoot(
						xDifference,
						yDifference + horizontalDifference * (double) 0.2F,
						zDifference, 1.6F, (float) 40);
				fireworkrocketentity.setShotFromCrossbow(true);
				this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
				this.level.addFreshEntity(fireworkrocketentity);
			}
			{
				fireworkrocketentity = ProjectileHelper.getMobArrow(this, new ItemStack(Items.ARROW), distanceFactor);
				fireworkrocketentity.shoot(
						xDifference,
						yDifference + horizontalDifference * (double) 0.2F,
						zDifference, 1.6F, (float) -40);
				fireworkrocketentity.setShotFromCrossbow(true);
				this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
				this.level.addFreshEntity(fireworkrocketentity);
			}
		}
	}

	class LiftMobGoal extends Goal {

		private boolean d;

	      public LiftMobGoal() {
	         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
	      }

	      public boolean canUse() {
	         return DungeonsPillagerEntity.this.getTarget() != null &&
					 DungeonsPillagerEntity.this.getLiftTicks() == 0 &&
					 CrossbowItem.isCharged(DungeonsPillagerEntity.this.getMainHandItem());
	      }
	      
	      public boolean canContinueToUse() {
	    	  	return DungeonsPillagerEntity.this.getTarget() != null && DungeonsPillagerEntity.this.getLiftTicks() > 0;
	      }
	    
	    public void start() {
	    super.start();
		this.d = true;
			DungeonsPillagerEntity.this.entityData.set(ANGRY,false);
	    DungeonsPillagerEntity.this.liftInterval = 70;
	    DungeonsPillagerEntity.this.setLiftTicks(20 + DungeonsPillagerEntity.this.getRandom().nextInt(10));
	    }
	    public void tick() {
			DungeonsPillagerEntity mob = DungeonsPillagerEntity.this;
			if (this.d)
				DungeonsPillagerEntity.this.getLookControl().setLookAt(DungeonsPillagerEntity.this.getTarget(), (float) DungeonsPillagerEntity.this.getMaxHeadYRot(), (float) DungeonsPillagerEntity.this.getMaxHeadXRot());
	    	LivingEntity target = DungeonsPillagerEntity.this.getTarget();
	    	
	    	mob.getNavigation().stop();

			if (mob.getLiftTicks() == 16 && !this.d) {
				mob.performRangedAttack(target,1.6F);
				CrossbowItem.setCharged(mob.getMainHandItem(), false);
			}

			if (mob.getLiftTicks() == 1 && this.d) {
				mob.entityData.set(ANGRY,true);
				this.d = false;
				DungeonsPillagerEntity.this.setLiftTicks(23);
			}
	    }
	    
	    public void stop() {
			  super.stop();
			DungeonsPillagerEntity.this.entityData.set(ANGRY,false);
			DungeonsPillagerEntity.this.setLiftTicks(0);
			DungeonsPillagerEntity.this.entityData.set(CS, false);
	    }

		@Override
		public boolean isInterruptable() {
			return false;
		}
	}

    class DuplicateGoal extends Goal {

		public DungeonsPillagerEntity v = DungeonsPillagerEntity.this;
	      public DuplicateGoal() {
	         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
	      }

	      public boolean canUse() {
			  return DungeonsPillagerEntity.this.getTarget() != null &&
					  DungeonsPillagerEntity.this.entityData.get(CS) &&
					  !CrossbowItem.isCharged(DungeonsPillagerEntity.this.getMainHandItem());
	      }
	      
	      public boolean canContinueToUse() {
			  return DungeonsPillagerEntity.this.getTarget() != null &&
					  DungeonsPillagerEntity.this.getDuplicateTicks() > 0;
	      }
	      
	      @Override
	    public boolean isInterruptable() {
	    	return false;
	    }
	    
	    public void start() {
			  super.start();
			int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, DungeonsPillagerEntity.this.getMainHandItem());
			  DungeonsPillagerEntity.this.setDuplicateTicks(30 - 6 * i);
	    }
	      
	    public void tick() {
            DungeonsPillagerEntity.this.getLookControl().setLookAt(DungeonsPillagerEntity.this.getTarget(), (float) DungeonsPillagerEntity.this.getMaxHeadYRot(), (float) DungeonsPillagerEntity.this.getMaxHeadXRot());
	    	LivingEntity target = DungeonsPillagerEntity.this.getTarget();
	    	DungeonsPillagerEntity mob = DungeonsPillagerEntity.this;
			mob.startUsingItem(Hand.MAIN_HAND);
	    	
	    	mob.getNavigation().stop();

			if (mob.getDuplicateTicks() == 1) {
				DungeonsPillagerEntity.this.playSound(SoundEvents.CROSSBOW_LOADING_END, 1.0F, 0.80F);
				mob.stopUsingItem();
				CrossbowItem.setCharged(v.getMainHandItem(), true);
           }
	    }
	    
	    public void stop() {
			  super.stop();
			DungeonsPillagerEntity.this.entityData.set(CS, false);
			  DungeonsPillagerEntity.this.setDuplicateTicks(0);
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
