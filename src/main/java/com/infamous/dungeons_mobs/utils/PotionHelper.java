package com.infamous.dungeons_mobs.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class PotionHelper {

    public static void setColor(ItemStack stack, int color) {
        CompoundTag compoundnbt = stack.getOrCreateTag();
        compoundnbt.putInt("CustomPotionColor", color);
    }
}
