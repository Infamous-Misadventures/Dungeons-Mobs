package com.infamous.dungeons_mobs.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GroupDungeonsMobs extends CreativeModeTab {

	public GroupDungeonsMobs(String groupName) {
		super(groupName);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(Items.ZOMBIE_SPAWN_EGG);
	}
}
