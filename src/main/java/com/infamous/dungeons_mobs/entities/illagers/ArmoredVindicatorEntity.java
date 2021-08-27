package com.infamous.dungeons_mobs.entities.illagers;

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
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.*;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;

import net.minecraft.entity.monster.AbstractIllagerEntity.ArmPose;

public class ArmoredVindicatorEntity extends VindicatorEntity {
    private static final DataParameter<Boolean> IS_DIAMOND = EntityDataManager.defineId(ArmoredVindicatorEntity.class, DataSerializers.BOOLEAN);

    public ArmoredVindicatorEntity(World world){
        super(ModEntityTypes.ARMORED_VINDICATOR.get(), world);
    }

    public ArmoredVindicatorEntity(EntityType<? extends VindicatorEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_DIAMOND, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return VindicatorEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 36.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.getCurrentRaid() == null){
            if(this.isDiamond()){
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.DIAMOND_VINDICATOR_HELMET.get()));
            }
            else{
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.GOLD_VINDICATOR_HELMET.get()));
            }

            this.setWeaponBasedOnMod();
        }
    }

    private void setWeaponBasedOnMod() {
        if(ModList.get().isLoaded("dungeons_gear")){
            Item DOUBLE_AXE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "double_axe"));
            Item WHIRLWIND = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "whirlwind"));

            ItemStack doubleAxe = new ItemStack(DOUBLE_AXE);
            ItemStack whirlwind = new ItemStack(WHIRLWIND);
            ItemStack mainhandWeapon = this.isDiamond() ? whirlwind : doubleAxe;

            this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        }
        else{
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
        }
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

    @Override
    public void applyRaidBuffs(int waveAmount, boolean b) {
        ItemStack mainhandWeapon = this.getWeaponBasedOnMod();
        ItemStack helmet = this.isDiamond() ? new ItemStack(ModItems.DIAMOND_VINDICATOR_HELMET.get()) : new ItemStack(ModItems.GOLD_VINDICATOR_HELMET.get());
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = 1;
        if (waveAmount > raid.getNumGroups(Difficulty.NORMAL)) {
            enchantmentLevel = 2;
        }

        boolean applyEnchant = this.random.nextFloat() <= raid.getEnchantOdds();
        if (applyEnchant) {
            Map<Enchantment, Integer> weaponEnchantmentMap = Maps.newHashMap();
            Map<Enchantment, Integer> armorEnchantmentMap = Maps.newHashMap();
            weaponEnchantmentMap.put(Enchantments.SHARPNESS, enchantmentLevel);
            armorEnchantmentMap.put(Enchantments.ALL_DAMAGE_PROTECTION, enchantmentLevel);
            EnchantmentHelper.setEnchantments(weaponEnchantmentMap, mainhandWeapon);
            EnchantmentHelper.setEnchantments(armorEnchantmentMap, helmet);
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        this.setItemSlot(EquipmentSlotType.HEAD, helmet);
    }

    private ItemStack getWeaponBasedOnMod() {
        ItemStack mainhandWeapon;
        if(ModList.get().isLoaded("dungeons_gear")){
            Item DOUBLE_AXE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "double_axe"));
            Item WHIRLWIND = ForgeRegistries.ITEMS.getValue(new ResourceLocation("dungeons_gear", "whirlwind"));

            ItemStack doubleAxe = new ItemStack(DOUBLE_AXE);
            ItemStack whirlwind = new ItemStack(WHIRLWIND);

            mainhandWeapon = this.isDiamond() ? whirlwind : doubleAxe;
        }
        else{
            mainhandWeapon = new ItemStack(Items.IRON_AXE);
        }
        return mainhandWeapon;
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
