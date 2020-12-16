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
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class SkeletonVanguardEntity extends ArmoredSkeletonEntity implements IShieldUser {
    private int shieldCooldownTime;
    private boolean isShieldDisabled;

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
        return ArmoredSkeletonEntity.setCustomAttributes().createMutableAttribute(Attributes.ARMOR, 6.0D);
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        if(ModList.get().isLoaded("dungeons_gear")){

            Item GLAIVE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "glaive"));
            ItemStack glaive = new ItemStack(GLAIVE);

            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, glaive);
        }
        else{
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
        this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.SKELETON_VANGUARD_HELMET.get()));
        this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.SKELETON_VANGUARD_SHIELD.get()));
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData livingEntityDataIn, @Nullable CompoundNBT compoundNBT) {
        livingEntityDataIn = super.onInitialSpawn(world, difficultyInstance, spawnReason, livingEntityDataIn, compoundNBT);

        return livingEntityDataIn;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }

    // SHIELD STUFF

    @Override
    public void livingTick() {
        super.livingTick();
        if(this.shieldCooldownTime > 0){
            this.shieldCooldownTime--;
        }
        else{
            if(this.isShieldDisabled){
                this.isShieldDisabled = false;
            }
        }
    }

    @Override
    protected void playHurtSound(DamageSource damageSource) {
        if(this.isActiveItemStackBlocking()){
            this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 0.8F + this.world.rand.nextFloat() * 0.4F);
        }
        else{
            super.playHurtSound(damageSource);
        }
    }

    @Override
    public void blockUsingShield(LivingEntity livingEntity) {
        super.blockUsingShield(livingEntity);
        if (livingEntity.getHeldItemMainhand().canDisableShield(this.activeItemStack, this, livingEntity)) {
            this.disableShield(true);
        }
    }

    @Override
    protected void damageShield(float amount) {
        if (this.activeItemStack.isShield(this)) {
            if (amount >= 3.0F) {
                int i = 1 + MathHelper.floor(amount);
                Hand hand = this.getActiveHand();
                this.activeItemStack.damageItem(i, this, (skeletonVanguardEntity) -> {
                    skeletonVanguardEntity.sendBreakAnimation(hand);
                    // Forge would have called onPlayerDestroyItem here
                });
                if (this.activeItemStack.isEmpty()) {
                    if (hand == Hand.MAIN_HAND) {
                        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                    } else {
                        this.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                    }

                    this.activeItemStack = ItemStack.EMPTY;
                    this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
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
        float f = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
        if (guaranteeDisable) {
            f += 0.75F;
        }
        if (this.rand.nextFloat() < f) {
            this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            this.shieldCooldownTime = 100;
            this.isShieldDisabled = true;
            this.resetActiveHand();
            this.world.setEntityState(this, (byte)30);
        }
    }

    @Override
    public boolean isShieldDisabled() {
        return this.isShieldDisabled;
    }

    static class GuardAndAttackGoal extends ShieldAndMeleeAttackGoal {
        GuardAndAttackGoal(SkeletonVanguardEntity skeletonVanguardEntity, double speedTowardsTarget, boolean useLongMemory) {
            super(skeletonVanguardEntity, speedTowardsTarget, useLongMemory);
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            if(ModList.get().isLoaded("dungeons_gear")){
                Item GLAIVE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "glaive"));
                Item VENOM_GLAIVE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "venom_glaive"));
                Item GRAVE_BANE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "grave_bane"));

                boolean isHoldingGlaive = this.attacker.getHeldItemMainhand().getItem() == GLAIVE
                        || this.attacker.getHeldItemMainhand().getItem() == VENOM_GLAIVE
                        || this.attacker.getHeldItemMainhand().getItem() == GRAVE_BANE;

                if (isHoldingGlaive) {
                    float glaiveAttackReach = 3.0F;
                    float attackerWidthSquaredTimes4 = this.attacker.getWidth() * glaiveAttackReach * this.attacker.getWidth() * glaiveAttackReach;
                    return (double)(attackerWidthSquaredTimes4 + attackTarget.getWidth());
                }
            }
            return super.getAttackReachSqr(attackTarget);
        }
    }
}
