package com.infamous.dungeons_mobs.entities.illagers;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;

import java.util.Map;

import net.minecraft.entity.monster.AbstractIllagerEntity.ArmPose;

public class VindicatorChefEntity extends VindicatorEntity {

    public VindicatorChefEntity(World worldIn){
        super(ModEntityTypes.VINDICATOR_CHEF.get(), worldIn);
    }

    public VindicatorChefEntity(EntityType<? extends VindicatorChefEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VindicatorEntity.createAttributes();
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        if(this.getCurrentRaid() == null){
            this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.CHEF_HAT.get()));
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.SPATULA.get()));
        }
    }

    public void applyRaidBuffs(int waveNumber, boolean bool) {
        ItemStack helmet = new ItemStack(ModItems.CHEF_HAT.get());
        ItemStack weapon = new ItemStack(ModItems.SPATULA.get());
        Raid raid = this.getCurrentRaid();
        int i = 1;
        if (raid != null && waveNumber > raid.getNumGroups(Difficulty.NORMAL)) {
            i = 2;
        }

        boolean flag = false;
        if (raid != null) {
            flag = this.random.nextFloat() <= raid.getEnchantOdds();
        }
        if (flag) {
            Map<Enchantment, Integer> helmetMap = Maps.newHashMap();
            Map<Enchantment, Integer> weaponMap = Maps.newHashMap();
            helmetMap.put(Enchantments.ALL_DAMAGE_PROTECTION, i);
            weaponMap.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(helmetMap, helmet);
            EnchantmentHelper.setEnchantments(weaponMap, weapon);
        }

        this.setItemSlot(EquipmentSlotType.HEAD, helmet);
        this.setItemSlot(EquipmentSlotType.MAINHAND, weapon);
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
