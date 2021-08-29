package com.infamous.dungeons_mobs.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class PotionHelper {

    public static void setColor(ItemStack stack, int color) {
        CompoundNBT compoundnbt = stack.getOrCreateTag();
        compoundnbt.putInt("CustomPotionColor", color);
    }
}
