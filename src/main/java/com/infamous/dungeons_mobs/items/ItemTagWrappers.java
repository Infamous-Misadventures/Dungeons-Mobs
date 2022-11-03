package com.infamous.dungeons_mobs.items;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ItemTagWrappers {
    public static final Tags.IOptionalNamedTag<Item> CURIOS_ARTIFACTS = ItemTags.createOptional(new ResourceLocation("curios", "artifact"));
}
