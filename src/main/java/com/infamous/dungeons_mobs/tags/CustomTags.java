package com.infamous.dungeons_mobs.tags;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class CustomTags {
    public static final Tags.IOptionalNamedTag<EntityType<?>> DONT_SHIELD_AGAINST = tag("dont_shield_against");

    public static final Tags.IOptionalNamedTag<EntityType<?>> CONVERTS_IN_WATER =
            tag("converts_in_water");

    private static Tags.IOptionalNamedTag<EntityType<?>> tag(String name)
    {
        return EntityTypeTags.createOptional(new ResourceLocation(DungeonsMobs.MODID, name));
    }
}
