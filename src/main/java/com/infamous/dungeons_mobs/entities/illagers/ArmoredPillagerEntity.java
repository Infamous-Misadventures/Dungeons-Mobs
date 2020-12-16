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
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.*;
import net.minecraft.world.raid.Raid;

import javax.annotation.Nullable;
import java.util.Map;

public class ArmoredPillagerEntity extends PillagerEntity {
    private static final DataParameter<Boolean> IS_DIAMOND = EntityDataManager.createKey(ArmoredPillagerEntity.class, DataSerializers.BOOLEAN);

    public ArmoredPillagerEntity(World world){
        super(ModEntityTypes.ARMORED_PILLAGER.get(), world);
    }

    public ArmoredPillagerEntity(EntityType<? extends PillagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(IS_DIAMOND, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return PillagerEntity.func_234296_eI_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 36.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.isDiamond()){
            compound.putBoolean("Diamond", true);
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("Diamond", 99)) {
            this.setDiamond(compound.getBoolean("Diamond"));
        }
    }

    public boolean isDiamond(){
        return this.dataManager.get(IS_DIAMOND);
    }

    public void setDiamond(boolean isDiamond){
        this.dataManager.set(IS_DIAMOND, isDiamond);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {

        float diamondChance = rand.nextFloat();
        if(diamondChance < 0.25F){
            this.setDiamond(true);
            this.applyDiamondArmorBoosts();
        }

        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private void applyDiamondArmorBoosts() {
        this.getAttribute(Attributes.ARMOR).applyPersistentModifier(new AttributeModifier("Diamond armor boost", 10.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).applyPersistentModifier(new AttributeModifier("Diamond knockback resistance boost", 0.6D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_DAMAGE).applyPersistentModifier(new AttributeModifier("Diamond attack boost", 1.0D, AttributeModifier.Operation.ADDITION));
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        if(this.getRaid() == null){
            if(this.isDiamond()){
                this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.DIAMOND_PILLAGER_HELMET.get()));
            }
            else{
                this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.GOLD_PILLAGER_HELMET.get()));
            }
            ItemStack mainhandWeapon = new ItemStack(Items.CROSSBOW);
            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.PIERCING, 1);
            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);

            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        }
    }

    @Override
    public void applyWaveBonus(int waveAmount, boolean p_213660_2_) {
        ItemStack crossbow = new ItemStack(Items.CROSSBOW);
        ItemStack helmet = this.isDiamond() ? new ItemStack(ModItems.DIAMOND_PILLAGER_HELMET.get()) : new ItemStack(ModItems.GOLD_PILLAGER_HELMET.get());
        Raid raid = this.getRaid();
        boolean applyEnchant = this.rand.nextFloat() <= raid.getEnchantOdds();
        if (applyEnchant) {
            Map<Enchantment, Integer> weaponEnchantmentMap = Maps.newHashMap();
            Map<Enchantment, Integer> armorEnchantmentMap = Maps.newHashMap();
            if (waveAmount > raid.getWaves(Difficulty.NORMAL)) {
                weaponEnchantmentMap.put(Enchantments.QUICK_CHARGE, 2);
                armorEnchantmentMap.put(Enchantments.PROTECTION, 2);
            } else if (waveAmount > raid.getWaves(Difficulty.EASY)) {
                weaponEnchantmentMap.put(Enchantments.QUICK_CHARGE, 1);
                armorEnchantmentMap.put(Enchantments.PROTECTION, 1);
            }

            weaponEnchantmentMap.put(Enchantments.MULTISHOT, 1);
            weaponEnchantmentMap.put(Enchantments.PIERCING, 1);
            EnchantmentHelper.setEnchantments(weaponEnchantmentMap, crossbow);
            EnchantmentHelper.setEnchantments(armorEnchantmentMap, helmet);
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, crossbow);
            this.setItemStackToSlot(EquipmentSlotType.HEAD, helmet);
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
