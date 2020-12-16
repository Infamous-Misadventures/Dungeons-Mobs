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

public class RoyalGuardEntity extends ArmoredVindicatorEntity implements IShieldUser {
    private int shieldCooldownTime;
    private boolean isShieldDisabled;

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
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {

        if(ModList.get().isLoaded("dungeons_gear")){
            Item MACE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "mace"));
            Item ROYAL_GUARD_HELMET = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "royal_guard_helmet"));
            Item ROYAL_GUARD_CHESTPLATE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "royal_guard_chestplate"));

            ItemStack mace = new ItemStack(MACE);
            ItemStack royalGuardHelmet = new ItemStack(ROYAL_GUARD_HELMET);
            ItemStack royalGuardChestplate = new ItemStack(ROYAL_GUARD_CHESTPLATE);
            if (this.getRaid() == null) {
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, mace);
            }
            this.setItemStackToSlot(EquipmentSlotType.HEAD, royalGuardHelmet);
            this.setItemStackToSlot(EquipmentSlotType.CHEST, royalGuardChestplate);
        }
        else{
            if (this.getRaid() == null) {
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
                this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
                this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            }
        }
        this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.ROYAL_GUARD_SHIELD.get()));
    }

    @Override
    public void applyWaveBonus(int waveAmount, boolean b) {
        ItemStack mainhandWeapon = new ItemStack(Items.IRON_AXE);
        if(ModList.get().isLoaded("dungeons_gear")){
            Item MACE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "mace"));

            mainhandWeapon = new ItemStack(MACE);
        }
        Raid raid = this.getRaid();
        int enchantmentLevel = 1;
        if (raid != null && waveAmount > raid.getWaves(Difficulty.NORMAL)) {
            enchantmentLevel = 2;
        }

        boolean applyEnchant = false;
        if (raid != null) {
            applyEnchant = this.rand.nextFloat() <= raid.getEnchantOdds();
        }
        if (applyEnchant) {
            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.SHARPNESS, enchantmentLevel);
            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);
        }

        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
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
                this.activeItemStack.damageItem(i, this, (royalGuardEntity) -> {
                    royalGuardEntity.sendBreakAnimation(hand);
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
            super(royalGuardEntity, speedTowardsTarget, useLongMemory);
        }

        protected double getAttackReachSqr(LivingEntity livingEntity) {
            if (this.attacker.getRidingEntity() instanceof RavagerEntity) {
                float width = this.attacker.getRidingEntity().getWidth() - 0.1F;
                return (double)(width * 2.0F * width * 2.0F + livingEntity.getWidth());
            } else {
                return super.getAttackReachSqr(livingEntity);
            }
        }
    }
}
