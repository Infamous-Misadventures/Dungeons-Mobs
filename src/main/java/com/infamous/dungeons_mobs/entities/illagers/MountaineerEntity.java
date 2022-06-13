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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.raid.Raid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.monster.AbstractIllagerEntity.ArmPose;

public class MountaineerEntity extends VindicatorEntity {

    public MountaineerEntity(World worldIn){
        super(ModEntityTypes.MOUNTAINEER.get(), worldIn);
    }

    public MountaineerEntity(EntityType<? extends MountaineerEntity> entityType, World world) {
        super(entityType, world);
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VindicatorEntity.createAttributes();
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        if(this.getCurrentRaid() == null){
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.MOUNTAINEER_AXE.get()));
        }
    }

    public void applyRaidBuffs(int waveNumber, boolean bool) {
        ItemStack itemStack = new ItemStack(ModItems.MOUNTAINEER_AXE.get());
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
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(map, itemStack);
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, itemStack);
    }

    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose =  super.getArmPose();
        if(illagerArmPose == ArmPose.CROSSED){
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }
}
