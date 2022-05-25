package com.infamous.dungeons_mobs.entities.illagers;

import com.electronwill.nightconfig.core.conversion.ConversionTable;
import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.*;
import net.minecraft.world.raid.Raid;

import javax.annotation.Nullable;
import java.util.Map;

import net.minecraft.entity.monster.AbstractIllagerEntity.ArmPose;

public class ArmoredPillagerEntity extends PillagerEntity {
    private static final DataParameter<Boolean> IS_DIAMOND = EntityDataManager.defineId(ArmoredPillagerEntity.class, DataSerializers.BOOLEAN);

    public ArmoredPillagerEntity(World world){
        super(ModEntityTypes.ARMORED_PILLAGER.get(), world);
    }


    public ArmoredPillagerEntity(EntityType<? extends PillagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_DIAMOND, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return PillagerEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 36.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.isDiamond()){
            compound.putBoolean("Diamond", true);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Diamond", 99)) {
            this.setDiamond(compound.getBoolean("Diamond"));
        }
    }

    public boolean isDiamond(){
        return this.entityData.get(IS_DIAMOND);
    }

    public void setDiamond(boolean isDiamond){
        this.entityData.set(IS_DIAMOND, isDiamond);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {

        float diamondChance = random.nextFloat();
        if(diamondChance < 0.25F){
            this.setDiamond(true);
            this.applyDiamondArmorBoosts();
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private void applyDiamondArmorBoosts() {
        this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("Diamond armor boost", 10.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Diamond knockback resistance boost", 0.6D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Diamond attack boost", 1.0D, AttributeModifier.Operation.ADDITION));
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.getCurrentRaid() == null){
            if(this.isDiamond()){
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.DIAMOND_PILLAGER_HELMET.get()));
            }
            else{
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.GOLD_PILLAGER_HELMET.get()));
            }
            ItemStack mainhandWeapon = new ItemStack(Items.CROSSBOW);
            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.PIERCING, 1);
            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);

            this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        }
    }

    @Override
    public void applyRaidBuffs(int waveAmount, boolean p_213660_2_) {
        ItemStack crossbow = new ItemStack(Items.CROSSBOW);
        ItemStack helmet = this.isDiamond() ? new ItemStack(ModItems.DIAMOND_PILLAGER_HELMET.get()) : new ItemStack(ModItems.GOLD_PILLAGER_HELMET.get());
        Raid raid = this.getCurrentRaid();
        boolean applyEnchant = this.random.nextFloat() <= raid.getEnchantOdds();
        if (applyEnchant) {
            Map<Enchantment, Integer> weaponEnchantmentMap = Maps.newHashMap();
            Map<Enchantment, Integer> armorEnchantmentMap = Maps.newHashMap();
            if (waveAmount > raid.getNumGroups(Difficulty.NORMAL)) {
                weaponEnchantmentMap.put(Enchantments.QUICK_CHARGE, 2);
                armorEnchantmentMap.put(Enchantments.ALL_DAMAGE_PROTECTION, 2);
            } else if (waveAmount > raid.getNumGroups(Difficulty.EASY)) {
                weaponEnchantmentMap.put(Enchantments.QUICK_CHARGE, 1);
                armorEnchantmentMap.put(Enchantments.ALL_DAMAGE_PROTECTION, 1);
            }

            weaponEnchantmentMap.put(Enchantments.MULTISHOT, 1);
            EnchantmentHelper.setEnchantments(weaponEnchantmentMap, crossbow);
            EnchantmentHelper.setEnchantments(armorEnchantmentMap, helmet);
        }
        Map<Enchantment, Integer> guaranteedEnchantMap = Maps.newHashMap();
        guaranteedEnchantMap.put(Enchantments.PIERCING, 1);
        EnchantmentHelper.setEnchantments(guaranteedEnchantMap, crossbow);
        this.setItemSlot(EquipmentSlotType.MAINHAND, crossbow);

        this.setItemSlot(EquipmentSlotType.HEAD, helmet);
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