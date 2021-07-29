package com.infamous.dungeons_mobs.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;

import net.minecraft.item.Item.Properties;

public class MountaineerAxeItem extends PickaxeItem {
    public MountaineerAxeItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return (enchantment.category.canEnchant(Items.IRON_SWORD)
                || enchantment.category.canEnchant(Items.IRON_AXE))
                && enchantment != Enchantments.SWEEPING_EDGE;
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }
}
