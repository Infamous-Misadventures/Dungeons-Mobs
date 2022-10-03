package com.infamous.dungeons_mobs.items;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class CosmeticArmorMaterial implements IArmorMaterial {

    public static final IArmorMaterial INSTANCE = new CosmeticArmorMaterial();
    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};

    @Override
    public int getDurabilityForSlot(EquipmentSlotType p_200896_1_) {
        return HEALTH_PER_SLOT[p_200896_1_.getIndex()] * 5;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType p_200902_1_) {
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
