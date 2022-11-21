package com.infamous.dungeons_mobs.entities.undead;

import java.util.UUID;

import javax.annotation.Nullable;

import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_mobs.entities.AnimatableMeleeAttackMob;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.BasicModdedAttackGoal;
import com.infamous.dungeons_mobs.goals.LookAtTargetGoal;
import com.infamous.dungeons_mobs.goals.UseShieldGoal;
import com.infamous.dungeons_mobs.interfaces.IShieldUser;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.infamous.dungeons_mobs.entities.SpawnArmoredHelper.equipArmorSet;

public class SkeletonVanguardEntity extends SkeletonEntity implements IShieldUser, IAnimatable, SpawnArmoredMob, AnimatableMeleeAttackMob {

	private static final UUID SPEED_MODIFIER_BLOCKING_UUID = UUID.fromString("e4c96392-42f5-4028-ac44-cad469c10d51");
	private static final AttributeModifier SPEED_MODIFIER_BLOCKING = new AttributeModifier(SPEED_MODIFIER_BLOCKING_UUID,
			"Blocking speed decrease", -0.05D, AttributeModifier.Operation.ADDITION);

	AnimationFactory factory = new AnimationFactory(this);

	private int shieldCooldownTime;

	public int attackAnimationTick;
	public int attackAnimationLength = 22;
	public int attackAnimationActionPoint = 10;

	public SkeletonVanguardEntity(World worldIn) {
		super(ModEntityTypes.SKELETON_VANGUARD.get(), worldIn);
	}

	public SkeletonVanguardEntity(EntityType<? extends SkeletonVanguardEntity> p_i48555_1_, World p_i48555_2_) {
		super(p_i48555_1_, p_i48555_2_);
		this.shieldCooldownTime = 0;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new UseShieldGoal(this, 10D, 60, 120, 10, 60, true));
		this.goalSelector.addGoal(1, new BasicModdedAttackGoal(this, ModSoundEvents.SKELETON_VANGUARD_ATTACK.get(), 20));
		this.goalSelector.addGoal(2, new ApproachTargetGoal(this, 0, 1.0D, true));
        this.goalSelector.addGoal(3, new LookAtTargetGoal(this));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, WolfEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false,
				TurtleEntity.BABY_ON_LAND_SELECTOR));
	}
	
	@Override
	protected boolean isSunBurnTick() {
		return false;
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		return SkeletonEntity.createAttributes().add(Attributes.FOLLOW_RANGE, 26.0D).add(Attributes.ARMOR, 6.0D).add(Attributes.ATTACK_KNOCKBACK, 1.5D).add(Attributes.KNOCKBACK_RESISTANCE, 0.3D);
	}

	protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
		equipArmorSet(ModItems.VANGUARD_ARMOR, this);

		if (ModList.get().isLoaded("dungeons_gear")) {

			Item GLAIVE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "glaive"));
			ItemStack glaive = new ItemStack(GLAIVE);

			this.setItemSlot(EquipmentSlotType.MAINHAND, glaive);
		} else {
			this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
		}
		//this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.SKELETON_VANGUARD_HELMET.get()));
		this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.VANGUARD_SHIELD.get()));
	}

	@Nullable
	public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance,
			SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityDataIn,
			@Nullable CompoundNBT compoundNBT) {
		livingEntityDataIn = super.finalizeSpawn(world, difficultyInstance, spawnReason, livingEntityDataIn,
				compoundNBT);

		return livingEntityDataIn;
	}

	protected SoundEvent getAmbientSound() {
		return ModSoundEvents.SKELETON_VANGUARD_IDLE.get();
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return ModSoundEvents.SKELETON_VANGUARD_HURT.get();
	}

	protected SoundEvent getDeathSound() {
		return ModSoundEvents.SKELETON_VANGUARD_DEATH.get();
	}

	protected SoundEvent getStepSound() {
		return ModSoundEvents.SKELETON_VANGUARD_STEP.get();
	}

	@Override
	public boolean isLeftHanded() {
		return true;
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
			event.getController().setAnimation(new AnimationBuilder().addAnimation("skeleton_vanguard_attack", true));
		} else if (this.isBlocking()) {
			if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
				event.getController()
						.setAnimation(new AnimationBuilder().addAnimation("skeleton_vanguard_new_walk_blocking", true));
			} else {
				event.getController()
						.setAnimation(new AnimationBuilder().addAnimation("skeleton_vanguard_new_blocking", true));
			}
		} else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("skeleton_vanguard_new_walk", true));
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("skeleton_vanguard_new_idle", true));
		}
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	// SHIELD STUFF

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.shieldCooldownTime > 0) {
			this.shieldCooldownTime--;
		} else if (this.shieldCooldownTime < 0) {
			this.shieldCooldownTime = 0;
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
				this.useItem.hurtAndBreak(i, this, (skeletonVanguardEntity) -> {
					skeletonVanguardEntity.broadcastBreakEvent(hand);
					// Forge would have called onPlayerDestroyItem here
				});
				if (this.useItem.isEmpty()) {
					if (hand == Hand.MAIN_HAND) {
						this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
					} else {
						this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
					}

					this.useItem = ItemStack.EMPTY;
					this.playSound(SoundEvents.SHIELD_BREAK, 1.0F, 0.8F + this.level.random.nextFloat() * 0.4F);
				}
			}
		}
	}

	@Override
	public int getAttackAnimationTick() {
		return attackAnimationTick;
	}

	@Override
	public void setAttackAnimationTick(int attackAnimationTick) {
		this.attackAnimationTick = attackAnimationTick;
	}

	@Override
	public int getAttackAnimationLength() {
		return attackAnimationLength;
	}

	@Override
	public int getAttackAnimationActionPoint() {
		return attackAnimationActionPoint;
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
	public void disableShield(boolean guaranteeDisable) {
		float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
		if (guaranteeDisable) {
			f += 0.75F;
		}
		if (this.random.nextFloat() < f) {
			this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
			this.shieldCooldownTime = 100;
			this.stopUsingItem();
			this.level.broadcastEntityEvent(this, (byte) 30);
		}
	}

	@Override
	public boolean isShieldDisabled() {
		return this.shieldCooldownTime > 0;
	}

	@Override
	public ArmorSet getArmorSet() {
		return ModItems.VANGUARD_ARMOR;
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
