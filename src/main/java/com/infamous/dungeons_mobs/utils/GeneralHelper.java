package com.infamous.dungeons_mobs.utils;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import net.minecraft.resources.ResourceLocation;

public class GeneralHelper {
    public static ResourceLocation modLoc(String resource) {
        return new ResourceLocation(MODID, resource);
    }
}
