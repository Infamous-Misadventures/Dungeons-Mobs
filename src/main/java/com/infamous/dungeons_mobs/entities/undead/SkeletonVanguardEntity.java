package com.infamous.dungeons_mobs.entities.undead;

import com.infamous.dungeons_mobs.interfaces.IShieldUser;
import com.infamous.dungeons_mobs.goals.switchcombat.ShieldAndMeleeAttackGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class SkeletonVanguardEntity extends ArmoredSkeletonEntity implements IShieldUser {
    private int shieldCooldownTime;

    public SkeletonVanguardEntity(World worldIn) {
        super(ModEntityTypes.SKELETON_VANGUARD.get(), worldIn);
    }

    public SkeletonVanguardEntity(EntityType<? extends SkeletonVanguardEntity> p_i48555_1_, World p_i48555_2_) {
        super(p_i48555_1_, p_i48555_2_);
        this.shieldCooldownTime = 0;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new GuardAndAttackGoal(this, 1.2D, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return ArmoredSkeletonEntity.setCustomAttributes().add(Attributes.ARMOR, 6.0D);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(ModList.get().isLoaded("dungeons_gear")){

            Item GLAIVE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "glaive"));
            ItemStack glaive = new ItemStack(GLAIVE);

            this.setItemSlot(EquipmentSlotType.MAINHAND, glaive);
        }
        else{
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.SKELETON_VANGUARD_HELMET.get()));
        this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.SKELETON_VANGUARD_SHIELD.get()));
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityDataIn, @Nullable CompoundNBT compoundNBT) {
        livingEntityDataIn = super.finalizeSpawn(world, difficultyInstance, spawnReason, livingEntityDataIn, compoundNBT);

        return livingEntityDataIn;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
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
    protected void playHurtSound(DamageSource damageSource) {
        if(this.isBlocking()){
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level.random.nextFloat() * 0.4F);
        }
        else{
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
                    this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
                }
            }
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
    public void disableShield(boolean guaranteeDisable) {
        float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (guaranteeDisable) {
            f += 0.75F;
        }
        if (this.random.nextFloat() < f) {
            this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
            this.shieldCooldownTime = 100;
            this.stopUsingItem();
            this.level.broadcastEntityEvent(this, (byte)30);
        }
    }

    @Override
    public boolean isShieldDisabled() {
        return this.shieldCooldownTime > 0;
    }

    static class GuardAndAttackGoal extends ShieldAndMeleeAttackGoal {
        GuardAndAttackGoal(SkeletonVanguardEntity skeletonVanguardEntity, double speedTowardsTarget, boolean useLongMemory) {
            super(skeletonVanguardEntity, speedTowardsTarget, useLongMemory,3.0D, 40, 5);
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            if(ModList.get().isLoaded("dungeons_gear")){
                Item GLAIVE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "glaive"));
                Item VENOM_GLAIVE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "venom_glaive"));
                Item GRAVE_BANE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "grave_bane"));

                boolean isHoldingGlaive = this.mob.getMainHandItem().getItem() == GLAIVE
                        || this.mob.getMainHandItem().getItem() == VENOM_GLAIVE
                        || this.mob.getMainHandItem().getItem() == GRAVE_BANE;

                if (isHoldingGlaive) {
                    float glaiveAttackReach = 3.0F;
                    float attackerWidthSquaredTimes4 = this.mob.getBbWidth() * glaiveAttackReach * this.mob.getBbWidth() * glaiveAttackReach;
                    return (double)(attackerWidthSquaredTimes4 + attackTarget.getBbWidth());
                }
            }
            return super.getAttackReachSqr(attackTarget);
        }
    }
}
