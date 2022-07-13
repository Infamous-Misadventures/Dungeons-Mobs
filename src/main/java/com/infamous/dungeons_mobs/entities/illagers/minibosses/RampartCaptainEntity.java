package com.infamous.dungeons_mobs.entities.illagers.minibosses;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.entities.illagers.MountaineerEntity;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
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

public class RampartCaptainEntity extends MountaineerEntity implements IAnimatable {
    public RampartCaptainEntity(World world){
        super(ModEntityTypes.RAMPART_CAPTAIN.get(), world);
    }

    public RampartCaptainEntity(EntityType<? extends AbstractIllagerEntity> p_i50189_1_, World p_i50189_2_) {
        super(p_i50189_1_, p_i50189_2_);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return VindicatorEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.213D)
                .add(Attributes.MAX_HEALTH, 135.0D)
                .add(Attributes.ATTACK_DAMAGE, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3.2D)
                .add(Attributes.ATTACK_KNOCKBACK, 4.5D);
    }


    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        if(this.getCurrentRaid() == null){
            this.setWeaponBasedOnMod();
        }
    }

    private void setWeaponBasedOnMod() {
        ItemStack mainhandWeapon = new ItemStack(ModItems.DIAMOND_MOUNTAINEER_AXE.get()) ;

        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void applyRaidBuffs(int waveAmount, boolean b) {
        Raid raid = this.getCurrentRaid();
        int enchantmentLevel = (int) (5 + (waveAmount));
        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));

        boolean applyEnchant = false;
        ItemStack mainhandWeapon = this.getWeaponBasedOnMod();
        if (applyEnchant) {
            float e = this.getRandom().nextFloat();

            Map<Enchantment, Integer> enchantmentIntegerMap = Maps.newHashMap();
            enchantmentIntegerMap.put(Enchantments.SHARPNESS, enchantmentLevel);
            Map<Enchantment, Integer> helmetEnchantments = Maps.newHashMap();
            helmetEnchantments.put(Enchantments.ALL_DAMAGE_PROTECTION, enchantmentLevel);

            enchantmentIntegerMap.put(Enchantments.VANISHING_CURSE, 1);
            helmetEnchantments.put(Enchantments.VANISHING_CURSE, 1);

            if (e <= 0.65) {
                helmetEnchantments.put(Enchantments.PROJECTILE_PROTECTION, 1);
            }

            if (e <= 0.35) {
                helmetEnchantments.put(Enchantments.THORNS, 1);
            }

            EnchantmentHelper.setEnchantments(enchantmentIntegerMap, mainhandWeapon);
        }

        if (waveAmount > raid.getNumGroups(Difficulty.EASY) && !(waveAmount > raid.getNumGroups(Difficulty.NORMAL)) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 6.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attack knockback boost", 1.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("armor boost", 2.0D, AttributeModifier.Operation.ADDITION));
        }

        if (waveAmount > raid.getNumGroups(Difficulty.NORMAL) && applyEnchant) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("health boost", 8.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attack knockback boost", 1.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("armor boost", 6.0D, AttributeModifier.Operation.ADDITION));
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("knockback resistance boost", 0.35D, AttributeModifier.Operation.ADDITION));
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, mainhandWeapon);
    }

    @Override
    public void applyEnchantment() {
        float o = this.getRandom().nextFloat();

        ItemStack weapon = this.getMainHandItem();
        ItemStack helmet = this.getItemBySlot(EquipmentSlotType.HEAD);
        this.addEffect(new EffectInstance(Effects.HEAL, 10, 6, (false), (false)));

        if ((this.getCurrentRaid() == null && o <= 0.37)) {
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

    private ItemStack getWeaponBasedOnMod() {
        ItemStack mainhandWeapon;
        mainhandWeapon = new ItemStack(ModItems.DIAMOND_MOUNTAINEER_AXE.get());
        return mainhandWeapon;
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return false;
    }

}
