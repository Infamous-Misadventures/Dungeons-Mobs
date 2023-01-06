package com.infamous.dungeons_mobs.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class CosmeticArmorMaterial implements ArmorMaterial {

    public static final ArmorMaterial INSTANCE = new CosmeticArmorMaterial();
    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};

    @Override
    public int getDurabilityForSlot(EquipmentSlot p_200896_1_) {
        return HEALTH_PER_SLOT[p_200896_1_.getIndex()] * 5;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot p_200902_1_) {
        return 0;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.WOOL_HIT;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ItemTags.WOOL);
    }

    @Override
    public String getName() {
        return "cosmetic";
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
