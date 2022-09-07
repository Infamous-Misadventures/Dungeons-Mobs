package com.infamous.dungeons_mobs.entities.illagers;

import java.util.EnumSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
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
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class IceologerEntity extends AbstractIllagerEntity implements IAnimatable {

	public int summonAnimationTick;
	public int summonAnimationLength = 60;
	public int summonAnimationActionPoint = 40;

    AnimationFactory factory = new AnimationFactory(this);

    public IceologerEntity(World world){
        super(ModEntityTypes.ICEOLOGER.get(), world);
    }

    public IceologerEntity(EntityType<? extends IceologerEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new IceologerEntity.SummonIceChunkGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, AbstractVillagerEntity.class, 3.0F, 1.2D, 1.15D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PlayerEntity.class, 3.0F, 1.2D, 1.2D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, IronGolemEntity.class, 3.0F, 1.3D, 1.15D));
        this.goalSelector.addGoal(2, new ApproachTargetGoal(this, 10, 1.0D, true));
        this.goalSelector.addGoal(3, new LookAtTargetGoal(this));
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
			this.summonAnimationTick = summonAnimationLength;
		} else {
			super.handleEntityEvent(p_28844_);
		}
	}

    public void baseTick() {
        super.baseTick();
        this.tickDownAnimTimers();
    }
    
	public void tickDownAnimTimers() {
		if (this.summonAnimationTick > 0) {
			this.summonAnimationTick--;
		}
	}

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }


    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.summonAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("iceologer_summon", true));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("iceologer_walk", true));
        } else {
        	if (this.isCelebrating()) {
        		event.getController().setAnimation(new AnimationBuilder().addAnimation("iceologer_celebrate", true));
        	} else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("iceologer_idle", true));       		
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
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.ICEOLOGER_CLOTHES.getHead().get()));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.ICEOLOGER_CLOTHES.getChest().get()));
        this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(ModItems.ICEOLOGER_CLOTHES.getLegs().get()));
        this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(ModItems.ICEOLOGER_CLOTHES.getFeet().get()));
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
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.FOLLOW_RANGE, 18D).add(Attributes.MAX_HEALTH, 20.0D);
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
        return ModSoundEvents.ICEOLOGER_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.ICEOLOGER_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.ICEOLOGER_HURT.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.ICEOLOGER_ATTACK.get();
    }
    
    class SummonIceChunkGoal extends Goal {
		public IceologerEntity mob;
		@Nullable
		public LivingEntity target;

		private final Predicate<Entity> ICE_CHUNK = (p_33346_) -> {
			return p_33346_ instanceof IceCloudEntity && ((IceCloudEntity)p_33346_).owner != null && ((IceCloudEntity)p_33346_).owner == mob;
		};
		
		public SummonIceChunkGoal(IceologerEntity mob) {
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
			int nearbyChunks = mob.level.getEntities(mob, mob.getBoundingBox().inflate(100.0D), ICE_CHUNK)
					.size();
			
			return target != null && mob.random.nextInt(20) == 0 && mob.distanceTo(target) <= 12 && nearbyChunks <= 0 && mob.canSee(target) && animationsUseable();
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.playSound(ModSoundEvents.ICEOLOGER_ATTACK.get(), 1.0F, mob.getVoicePitch());
			mob.summonAnimationTick = mob.summonAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 4);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			if (target != null) {
				mob.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
			}

			if (target != null && mob.summonAnimationTick == mob.summonAnimationActionPoint) {
	            IceCloudEntity.spawn(mob, target);
			}
		}

		public boolean animationsUseable() {
			return mob.summonAnimationTick <= 0;
		}

	}
}