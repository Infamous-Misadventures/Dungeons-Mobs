package com.infamous.dungeons_mobs.interfaces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IHasItemStackData {

    ItemStack getDataItem();

    void setDataItem(ItemStack dataItem);

    default void writeDataItem(CompoundTag tag, String key) {
        ItemStack itemStack = this.getDataItem();
        if (!itemStack.isEmpty()) {
            tag.put(key, itemStack.save(new CompoundTag()));
        }
    }

    default void readDataItem(CompoundTag tag, String key) {
        ItemStack itemstack = ItemStack.of(tag.getCompound(key));
        this.setDataItem(itemstack);
    }
}
