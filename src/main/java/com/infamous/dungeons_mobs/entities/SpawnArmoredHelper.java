package com.infamous.dungeons_mobs.entities;

import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class SpawnArmoredHelper {
	public static void equipArmorSet(ArmorSet armorSet, Mob mobEntity) {
		if (armorSet.getHead() != null) {
			mobEntity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(armorSet.getHead().get()));
		}
		if (armorSet.getChest() != null) {
			mobEntity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(armorSet.getChest().get()));
		}
		if (armorSet.getLegs() != null) {
			mobEntity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(armorSet.getLegs().get()));
		}
		if (armorSet.getFeet() != null) {
			mobEntity.setItemSlot(EquipmentSlot.FEET, new ItemStack(armorSet.getFeet().get()));
		}
		if (DungeonsMobsConfig.COMMON.ENABLE_MOB_ARMOR_DROPS.get()) {
			mobEntity.setDropChance(EquipmentSlot.HEAD, 0.05F);
			mobEntity.setDropChance(EquipmentSlot.CHEST, 0.05F);
			mobEntity.setDropChance(EquipmentSlot.LEGS, 0.05F);
			mobEntity.setDropChance(EquipmentSlot.FEET, 0.05F);
		} else {
			mobEntity.setDropChance(EquipmentSlot.HEAD, 0.0F);
			mobEntity.setDropChance(EquipmentSlot.CHEST, 0.0F);
			mobEntity.setDropChance(EquipmentSlot.LEGS, 0.0F);
			mobEntity.setDropChance(EquipmentSlot.FEET, 0.0F);
		}
	}
}
