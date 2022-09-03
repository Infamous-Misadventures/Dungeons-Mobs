package com.infamous.dungeons_mobs.entities.illagers;

import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.goals.UseShieldGoal;
import com.infamous.dungeons_mobs.interfaces.IShieldUser;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RoyalGuardEntity extends AbstractIllagerEntity implements IAnimatable, IShieldUser {

	private static final UUID SPEED_MODIFIER_BLOCKING_UUID = UUID.fromString("05cd371b-0ff4-4ded-8630-b380232ed7b1");
	private static final AttributeModifier SPEED_MODIFIER_BLOCKING = new AttributeModifier(SPEED_MODIFIER_BLOCKING_UUID,
			"Blocking speed decrease", -0.1D, AttributeModifier.Operation.ADDITION);

	AnimationFactory factory = new AnimationFactory(this);

    private int shieldCooldownTime;
    
	public int attackAnimationTick;
	public int attackAnimationLength = 27;
	public int attackAnimationActionPoint = 15;

	public RoyalGuardEntity(World world) {
		super(ModEntityTypes.ROYAL_GUARD.get(), world);
		this.shieldCooldownTime = 0;
	}

	public RoyalGuardEntity(EntityType<? extends RoyalGuardEntity> p_i50189_1_, World p_i50189_2_) {
		super(p_i50189_1_, p_i50189_2_);
		this.shieldCooldownTime = 0;
	}

	@Override
	public boolean canBeLeader() {
		return false;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(0, new UseShieldGoal(this, 7.5D, 60, 160, 15, 100, false));
		this.goalSelector.addGoal(1, new RoyalGuardEntity.BasicAttackGoal(this));
		this.goalSelector.addGoal(2, new ApproachTargetGoal(this, 0, 1.0D, true));
        this.goalSelector.addGoal(3, new LookAtTargetGoal(this));
		this.goalSelector.addGoal(1, new AbstractIllagerEntity.RaidOpenDoorGoal(this));
		this.goalSelector.addGoal(3, new AbstractRaiderEntity.FindTargetGoal(this, 10.0F));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
		this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
	}

	@Nullable
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_,
			SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
		ILivingEntityData ilivingentitydata = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_,
				p_213386_5_);
		((GroundPathNavigator) this.getNavigation()).setCanOpenDoors(true);
		this.populateDefaultEquipmentSlots(p_213386_2_);
		this.populateDefaultEquipmentEnchantments(p_213386_2_);
		return ilivingentitydata;
	}
	
	@Override
	public boolean isLeftHanded() {
		return false;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.VINDICATOR_AMBIENT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.VINDICATOR_DEATH;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.VINDICATOR_HURT;
	}

	public SoundEvent getCelebrateSound() {
		return SoundEvents.VINDICATOR_CELEBRATE;
	}

	public void handleEntityEvent(byte p_28844_) {
		if (p_28844_ == 4) {
			this.attackAnimationTick = attackAnimationLength;
		} else {
			super.handleEntityEvent(p_28844_);
		}
	}

	public void baseTick() {
        super.baseTick();
        ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        
        if (this.isBlocking()) {
            if (!modifiableattributeinstance.hasModifier(SPEED_MODIFIER_BLOCKING)) {
                modifiableattributeinstance.addTransientModifier(SPEED_MODIFIER_BLOCKING);
             }
        } else {
        	modifiableattributeinstance.removeModifier(SPEED_MODIFIER_BLOCKING);
        }
        
        this.tickDownAnimTimers();
    }

	public void tickDownAnimTimers() {
		if (this.attackAnimationTick > 0) {
			this.attackAnimationTick--;
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.attackAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_attack", true));
		} else if (this.isBlocking()) {
			if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_new_walk_blocking", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_new_blocking", true));
			}
		} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_new_walk", true));
		} else {
			if (this.isCelebrating()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_celebrate", true));
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("royal_guard_new_idle", true));
			}
		}
		return PlayState.CONTINUE;
	}

	@Override
	protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
		this.playSound(ModSoundEvents.ROYAL_GUARD_STEP.get(), 0.5F, 1.0F);
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		return VindicatorEntity.createAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.325F).add(Attributes.FOLLOW_RANGE, 18.0D).add(Attributes.ATTACK_KNOCKBACK, 1.0D);
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
		this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.ROYAL_GUARD_HELMET.get()));
		this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.ROYAL_GUARD_CHESTPLATE.get()));
		this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(ModItems.ROYAL_GUARD_LEGS.get()));
		this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(ModItems.ROYAL_GUARD_SABATONS.get()));

		if (ModList.get().isLoaded("dungeons_gear")) {
			Item MACE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "mace"));

			ItemStack mace = new ItemStack(MACE);
			if (this.getCurrentRaid() == null) {
				this.setItemSlot(EquipmentSlotType.MAINHAND, mace);
			}
		} else {
			if (this.getCurrentRaid() == null) {
				this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
			}
		}
		this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.ROYAL_GUARD_SHIELD.get()));
	}

	@Override
	public void applyRaidBuffs(int waveAmount, boolean b) {
		ItemStack mainhandWeapon = new ItemStack(Items.IRON_AXE);
		if (ModList.get().isLoaded("dungeons_gear")) {
			Item MACE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "mace"));

			mainhandWeapon = new ItemStack(MACE);
		}
		Raid raid = this.getCurrentRaid();
		int enchantmentLevel = 1;
		if (raid != null && waveAmount > raid.getNumGroups(Difficulty.NORMAL)) {
			enchantmentLevel = 2;
		}

		boolean applyEnchant = false;
		if (raid != null) {
			applyEnchant = this.random.nextFloat() <= raid.getEnchantOdds();
		}
		if (applyEnchant) {
			Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
			enchantmentIntegerMap.put(Enchantments.SHARPNESS, enchantmentLevel);
			EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);
		}

		this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
	}

	@Override
	public ArmPose getArmPose() {
		ArmPose illagerArmPose = super.getArmPose();
		if (illagerArmPose == ArmPose.CROSSED) {
			return ArmPose.NEUTRAL;
		}
		return illagerArmPose;
	}

	// SHIELD STUFF
	
    @Override
    public void aiStep() {
        super.aiStep();
        if(this.shieldCooldownTime > 0){
            this.shieldCooldownTime--;
        }
        else if(this.shieldCooldownTime < 0){
            this.shieldCooldownTime = 0;
        }
    }
    

    @Override
    public int getShieldCooldownTime() {
        return this.shieldCooldownTime;
    }

    @Override
    public void setShieldCooldownTime(int shieldCooldownTime) {
        this.shieldCooldownTime = shieldCooldownTime;
    }
    
    @Override
    public boolean isShieldDisabled() {
        return this.shieldCooldownTime > 0;
    }

    @Override
	public void disableShield(boolean guaranteeDisable) {
		float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
		if (guaranteeDisable) {
			f += 0.75F;
		}
		if (this.random.nextFloat() < f) {
            this.shieldCooldownTime = 100;
			this.stopUsingItem();
			this.level.broadcastEntityEvent(this, (byte) 30);
		}
	}

	@Override
	protected void playHurtSound(DamageSource damageSource) {
		if (this.shieldCooldownTime == 100) {
			this.playSound(SoundEvents.SHIELD_BREAK, 1.0F, 0.8F + this.level.random.nextFloat() * 0.4F);
		} else if (this.isBlocking()) {
			this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level.random.nextFloat() * 0.4F);
		} else {
			super.playHurtSound(damageSource);
		}
	}

	@Override
	public void blockUsingShield(LivingEntity livingEntity) {
		super.blockUsingShield(livingEntity);
		if (livingEntity.getMainHandItem().canDisableShield(this.useItem, this, livingEntity)) {
			this.disableShield(true);
		}
	}

	@Override
	protected void hurtCurrentlyUsedShield(float amount) {
		if (this.useItem.isShield(this)) {
			if (amount >= 3.0F) {
				int i = 1 + MathHelper.floor(amount);
				Hand hand = this.getUsedItemHand();
				this.useItem.hurtAndBreak(i, this, (royalGuardEntity) -> {
					royalGuardEntity.broadcastBreakEvent(hand);
					// Forge would have called onPlayerDestroyItem here
				});
				if (this.useItem.isEmpty()) {
					if (hand == Hand.MAIN_HAND) {
						this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
					} else {
						this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
					}

					this.useItem = ItemStack.EMPTY;
					this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
				}
			}
		}
	}

	class BasicAttackGoal extends Goal {

		public RoyalGuardEntity mob;
		@Nullable
		public LivingEntity target;

		public BasicAttackGoal(RoyalGuardEntity mob) {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
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
			return target != null && !mob.isBlocking() && mob.distanceTo(target) <= 2.5 && animationsUseable()
					&& mob.canSee(target);
		}

		@Override
		public boolean canContinueToUse() {
			return target != null && !animationsUseable();
		}

		@Override
		public void start() {
			mob.attackAnimationTick = mob.attackAnimationLength;
			mob.level.broadcastEntityEvent(mob, (byte) 4);
		}

		@Override
		public void tick() {
			target = mob.getTarget();

			if (mob.attackAnimationTick == mob.attackAnimationActionPoint) {
				mob.playSound(ModSoundEvents.ROYAL_GUARD_ATTACK.get(), 1.25F, 1.0F);
			}

			if (target != null && mob.distanceTo(target) < 3.5
					&& mob.attackAnimationTick == mob.attackAnimationActionPoint) {
				mob.doHurtTarget(target);
				RoyalGuardEntity.disableShield(target, 60);
			}
		}
		
		@Override
		public void stop() {
			if (target != null && !isShieldDisabled(mob) && shouldBlockForTarget(target) && mob.getOffhandItem().getItem().isShield(mob.getOffhandItem(), mob) && mob.random.nextInt(6) == 0) {
				mob.startUsingItem(Hand.OFF_HAND);
			}
		}
		
		public boolean isShieldDisabled(CreatureEntity shieldUser) {
			if (shieldUser instanceof IShieldUser && ((IShieldUser)shieldUser).isShieldDisabled()) {
				return true;
			} else {
				return false;
			}
		}
		
		public boolean shouldBlockForTarget(LivingEntity target) {
			if (target instanceof MobEntity && ((MobEntity)target).getTarget() != mob) {
				return false;
			} else {
				return true;
			}
		}

		public boolean animationsUseable() {
			return mob.attackAnimationTick <= 0;
		}	

	}

	public static void disableShield(LivingEntity livingEntity, int ticks) {
		if (livingEntity instanceof PlayerEntity && livingEntity.isBlocking()) {
			((PlayerEntity) livingEntity).getCooldowns()
					.addCooldown(livingEntity.getItemInHand(livingEntity.getUsedItemHand()).getItem(), ticks);
			livingEntity.stopUsingItem();
			livingEntity.level.broadcastEntityEvent(livingEntity, (byte) 30);
		}
	}
}
