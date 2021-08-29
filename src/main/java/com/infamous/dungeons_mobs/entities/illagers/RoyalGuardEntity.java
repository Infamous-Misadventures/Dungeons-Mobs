package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.interfaces.IShieldUser;
import com.infamous.dungeons_mobs.goals.switchcombat.ShieldAndMeleeAttackGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

import net.minecraft.entity.monster.AbstractIllagerEntity.ArmPose;

public class RoyalGuardEntity extends ArmoredVindicatorEntity implements IShieldUser {
    private int shieldCooldownTime;

    public RoyalGuardEntity(World world){
        super(ModEntityTypes.ROYAL_GUARD.get(), world);
    }

    public RoyalGuardEntity(EntityType<? extends ArmoredVindicatorEntity> p_i50189_1_, World p_i50189_2_) {
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
        this.goalSelector.addGoal(4, new GuardAndAttackGoal(this, 1.0D, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return ArmoredVindicatorEntity.setCustomAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {

        if(ModList.get().isLoaded("dungeons_gear")){
            Item MACE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "mace"));
            Item ROYAL_GUARD_HELMET = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "royal_guard_helmet"));
            Item ROYAL_GUARD_CHESTPLATE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "royal_guard_chestplate"));

            ItemStack mace = new ItemStack(MACE);
            ItemStack royalGuardHelmet = new ItemStack(ROYAL_GUARD_HELMET);
            ItemStack royalGuardChestplate = new ItemStack(ROYAL_GUARD_CHESTPLATE);
            if (this.getCurrentRaid() == null) {
                this.setItemSlot(EquipmentSlotType.MAINHAND, mace);
            }
            this.setItemSlot(EquipmentSlotType.HEAD, royalGuardHelmet);
            this.setItemSlot(EquipmentSlotType.CHEST, royalGuardChestplate);
        }
        else{
            if (this.getCurrentRaid() == null) {
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            }
        }
        this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.ROYAL_GUARD_SHIELD.get()));
    }

    @Override
    public void applyRaidBuffs(int waveAmount, boolean b) {
        ItemStack mainhandWeapon = new ItemStack(Items.IRON_AXE);
        if(ModList.get().isLoaded("dungeons_gear")){
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
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
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

    // Disabling method inherited from Armored Vindicator
    @Override
    public boolean isDiamond() {
        return false;
    }

    // Disabling method inherited from Armored Vindicator
    @Override
    public void setDiamond(boolean isDiamond) {
    }

    static class GuardAndAttackGoal extends ShieldAndMeleeAttackGoal {
        GuardAndAttackGoal(RoyalGuardEntity royalGuardEntity, double speedTowardsTarget, boolean useLongMemory) {
            super(royalGuardEntity, speedTowardsTarget, useLongMemory, 3.0D, 40, 5);
        }

        protected double getAttackReachSqr(LivingEntity livingEntity) {
            if (this.mob.getVehicle() instanceof RavagerEntity) {
                float width = this.mob.getVehicle().getBbWidth() - 0.1F;
                return (double)(width * 2.0F * width * 2.0F + livingEntity.getBbWidth());
            } else {
                return super.getAttackReachSqr(livingEntity);
            }
        }
    }
}
