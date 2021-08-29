package com.infamous.dungeons_mobs.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class GroupDungeonsMobs extends ItemGroup {

    public GroupDungeonsMobs(String groupName) {
        super(groupName);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.ZOMBIE_SPAWN_EGG);
    }
}
