package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Map;

public class ArmoredVindicatorEntity extends DungeonsVindicatorEntity {
    private static final DataParameter<Boolean> IS_DIAMOND = EntityDataManager.defineId(ArmoredVindicatorEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_GOLD = EntityDataManager.defineId(ArmoredVindicatorEntity.class, DataSerializers.BOOLEAN);

    AnimationFactory factory = new AnimationFactory(this);


    public ArmoredVindicatorEntity(World world){
        super(ModEntityTypes.ARMORED_VINDICATOR.get(), world);
    }

    public ArmoredVindicatorEntity(EntityType<? extends AbstractIllagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_DIAMOND, false);
        this.entityData.define(IS_GOLD, false);
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
            ItemStack diamondAxe = new ItemStack(Items.DIAMOND_AXE);
            ItemStack ironAxe = new ItemStack(Items.IRON_AXE);
            ItemStack mainhandWeapon = this.isDiamond() ? diamondAxe : ironAxe;

            this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.isDiamond()){
            compound.putBoolean("Diamond", true);
        }
        if (this.isGold()){
            compound.putBoolean("Gold", true);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Diamond", 99)) {
            this.setDiamond(compound.getBoolean("Diamond"));
        }
        if (compound.contains("Gold", 99)) {
            this.setGold(compound.getBoolean("Gold"));
        }
    }

    public boolean isDiamond(){
        return this.entityData.get(IS_DIAMOND);
    }

    public boolean isGold(){
        return this.entityData.get(IS_GOLD);
    }

    public void setDiamond(boolean isDiamond){
        this.entityData.set(IS_DIAMOND, isDiamond);
    }

    public void setGold(boolean IsGold){
        this.entityData.set(IS_GOLD, IsGold);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {

        float diamondChance = random.nextFloat();
        float goldChance = random.nextFloat();

        if (this.getCurrentRaid() == null) {
            if (diamondChance < 0.17F) {
                this.setDiamond(true);
                this.applyDiamondArmorBoosts();
            }
        }else {
            if (diamondChance < 0.37F) {
                this.setDiamond(true);
                this.applyDiamondArmorBoosts();
            }
        }

        if(goldChance < 0.5F && !this.isDiamond()){
            this.setGold(true);
            this.applyGoldArmorBoosts();
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }
    
    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return VindicatorEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.273D)
                .add(Attributes.ARMOR)
                .add(Attributes.MAX_HEALTH, 36.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.25D);
    }

    private void applyDiamondArmorBoosts() {
        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Diamond health boost", 24.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("Diamond attack knockback boost", 1.75D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("Diamond armor boost", 10.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Diamond knockback resistance boost", 1.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Diamond attack boost", 2.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Diamond speed boost", 0.035D, AttributeModifier.Operation.ADDITION));
    }

    private void applyGoldArmorBoosts() {
        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Gold health boost", 12.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("Gold attack knockback boost", 1.5D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("Gold armor boost", 6.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Gold knockback resistance boost", 0.6D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Gold attack boost", 1.0D, AttributeModifier.Operation.ADDITION));
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Gold speed boost", 0.01D, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public void applyEnchantment() {
        float o = this.getRandom().nextFloat();

        ItemStack weapon = this.getMainHandItem();
        ItemStack helmet = this.getItemBySlot(EquipmentSlotType.HEAD);
        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));

        if (this.getRandom().nextInt(100) <= 10 || (this.level.getDifficulty().getId() == 2 && this.getRandom().nextInt(100) <= 40) || (this.level.getDifficulty().getId() == 3 && this.getRandom().nextInt(100) <= 75) || this.isDiamond()) {
            Map<Enchantment, Integer> weaponEnchantmentMap = Maps.newHashMap();
            Map<Enchantment, Integer> armorEnchantmentMap = Maps.newHashMap();
            weaponEnchantmentMap.put(Enchantments.SHARPNESS, this.getRandom().nextInt(5) + 1);
            armorEnchantmentMap.put(Enchantments.ALL_DAMAGE_PROTECTION, this.getRandom().nextInt(3) + 1);
            if (o <= 0.05) {
                weaponEnchantmentMap.put(Enchantments.KNOCKBACK, this.getRandom().nextInt(3) + 1);
                armorEnchantmentMap.put(Enchantments.PROJECTILE_PROTECTION,  this.getRandom().nextInt(5) + 1);
                armorEnchantmentMap.put(Enchantments.BLAST_PROTECTION,  this.getRandom().nextInt(4) + 1);
                armorEnchantmentMap.put(Enchantments.THORNS,  this.getRandom().nextInt(2) + 1);
            }
            EnchantmentHelper.setEnchantments(weaponEnchantmentMap, weapon);
            EnchantmentHelper.setEnchantments(armorEnchantmentMap, helmet);
        }
    }

    @Override
    public void applyRaidBuffs(int waveAmount, boolean b) {
        ItemStack helmet = this.isDiamond() ?
                new ItemStack(ModItems.DIAMOND_VINDICATOR_HELMET.get()) :
                new ItemStack(ModItems.GOLD_VINDICATOR_HELMET.get());
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = (int) Math.min((2 + (waveAmount / 2.5)),5);
        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));

        boolean applyEnchant = this.random.nextFloat() + 0.15 <= raid.getEnchantOdds();
        ItemStack mainhandWeapon = this.getWeaponBasedOnMod();
        if (applyEnchant) {
            float e = this.getRandom().nextFloat();

            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.SHARPNESS, enchantmentLevel);
            Map<Enchantment, Integer> helmetEnchantments = Maps.newHashMap();
            helmetEnchantments.put(Enchantments.ALL_DAMAGE_PROTECTION, Math.min(4,enchantmentLevel));

            if (e <= 0.15) {
                enchantmentIntegerMap.put(Enchantments.FIRE_ASPECT, Math.min(2,enchantmentLevel));
                helmetEnchantments.put(Enchantments.FIRE_PROTECTION, 1);
            }

            if (e <= 0.65) {
                helmetEnchantments.put(Enchantments.PROJECTILE_PROTECTION, enchantmentLevel);
            }

            if (e <= 0.35) {
                helmetEnchantments.put(Enchantments.THORNS, Math.min(3,enchantmentLevel));
            }

            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);
            EnchantmentHelper.setEnchantments(helmetEnchantments, helmet);
        }
        if (this.isDiamond()) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 4.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attack knockback boost", 1.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("armor boost", 2.0D, AttributeModifier.Operation.ADDITION));
        }

        if (waveAmount > raid.getNumGroups(Difficulty.EASY) && !(waveAmount > raid.getNumGroups(Difficulty.NORMAL)) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 6.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attack knockback boost", 1.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("armor boost", 2.0D, AttributeModifier.Operation.ADDITION));
        }

        if (waveAmount > raid.getNumGroups(Difficulty.NORMAL) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 12.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attack knockback boost", 2.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("armor boost", 4.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("knockback resistance boost", 0.35D, AttributeModifier.Operation.ADDITION));
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
            ItemStack ironAxe = new ItemStack(Items.IRON_AXE);

            mainhandWeapon = this.isDiamond() ? whirlwind : this.isGold() ? doubleAxe : ironAxe;
        }
        else{
            mainhandWeapon = this.isDiamond() ? new ItemStack(Items.DIAMOND_AXE) : new ItemStack(Items.IRON_AXE);
        }
        return mainhandWeapon;
    }

}
