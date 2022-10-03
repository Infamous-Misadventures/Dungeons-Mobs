package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class GroupDungeonsMobsItems extends ItemGroup {

    public GroupDungeonsMobsItems(String groupName) {
        super(groupName);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.GOLD_PIGLIN_HELMET.get());
    }
}
