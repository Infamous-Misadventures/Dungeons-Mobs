package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.mod.ModItems;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class GroupDungeonsMobsItems extends CreativeModeTab {

    public GroupDungeonsMobsItems(String groupName) {
        super(groupName);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.GOLD_PIGLIN_HELMET.get());
    }
}
