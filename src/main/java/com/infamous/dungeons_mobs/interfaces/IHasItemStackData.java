package com.infamous.dungeons_mobs.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public interface IHasItemStackData {

    ItemStack getDataItem();

    void setDataItem(ItemStack dataItem);

    default void writeDataItem(CompoundNBT tag, String key){
        ItemStack itemStack = this.getDataItem();
        if (!itemStack.isEmpty()) {
            tag.put(key, itemStack.save(new CompoundNBT()));
        }
    }
    default void readDataItem(CompoundNBT tag, String key){
        ItemStack itemstack = ItemStack.of(tag.getCompound(key));
        this.setDataItem(itemstack);
    }
}
