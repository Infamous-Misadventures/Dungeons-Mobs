package com.infamous.dungeons_mobs.entities;

import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class SpawnArmoredHelper {
    public static void equipArmorSet(ArmorSet armorSet, MobEntity mobEntity) {
        if (armorSet.getHead() != null) {
            mobEntity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(armorSet.getHead().get()));
        }
        if (armorSet.getChest() != null) {
            mobEntity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(armorSet.getChest().get()));
        }
        if (armorSet.getLegs() != null) {
            mobEntity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(armorSet.getLegs().get()));
        }
        if (armorSet.getFeet() != null) {
            mobEntity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(armorSet.getFeet().get()));
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_MOB_ARMOR_DROPS.get()){
            mobEntity.setDropChance(EquipmentSlotType.HEAD, 0.05F);
            mobEntity.setDropChance(EquipmentSlotType.CHEST, 0.05F);
            mobEntity.setDropChance(EquipmentSlotType.LEGS, 0.05F);
            mobEntity.setDropChance(EquipmentSlotType.FEET, 0.05F);
        }else{
            mobEntity.setDropChance(EquipmentSlotType.HEAD, 0.0F);
            mobEntity.setDropChance(EquipmentSlotType.CHEST, 0.0F);
            mobEntity.setDropChance(EquipmentSlotType.LEGS, 0.0F);
            mobEntity.setDropChance(EquipmentSlotType.FEET, 0.0F);
        }
    }
}
