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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;

import javax.annotation.Nullable;
import java.util.Map;

public class ArmoredMountaineerEntity extends MountaineerEntity {
    private static final DataParameter<Boolean> IS_DIAMOND = EntityDataManager.createKey(ArmoredMountaineerEntity.class, DataSerializers.BOOLEAN);

    public ArmoredMountaineerEntity(World world){
        super(ModEntityTypes.ARMORED_MOUNTAINEER.get(), world);
    }

    public ArmoredMountaineerEntity(EntityType<? extends MountaineerEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(IS_DIAMOND, false);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return VindicatorEntity.func_234322_eI_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 36.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultyInstance) {
        if(this.getRaid() == null){
            if (this.isDiamond()) {
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.DIAMOND_MOUNTAINEER_AXE.get()));
            } else {
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.GOLD_MOUNTAINEER_AXE.get()));
            }
        }
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
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {

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

    @Override
    public void applyWaveBonus(int waveAmount, boolean b) {
        ItemStack mainhandWeapon = this.isDiamond() ?
                new ItemStack(ModItems.DIAMOND_MOUNTAINEER_AXE.get()) :
                new ItemStack(ModItems.GOLD_MOUNTAINEER_AXE.get());
        Raid raid = this.getRaid();
        int enchantmentLevel = 1;
        if (waveAmount > raid.getWaves(Difficulty.NORMAL)) {
            enchantmentLevel = 2;
        }

        boolean applyEnchant = this.rand.nextFloat() <= raid.getEnchantOdds();
        if (applyEnchant) {
            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.SHARPNESS, Integer.valueOf(enchantmentLevel));
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
}
