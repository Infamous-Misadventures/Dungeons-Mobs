package com.infamous.dungeons_mobs.items.shield;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class ShieldTextures {

    public static final Material LOCATION_ROYAL_GUARD_SHIELD_BASE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(MODID, "entity/shield/royal_guard_shield_base"));
    public static final Material LOCATION_ROYAL_GUARD_SHIELD_NO_PATTERN = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(MODID, "entity/shield/royal_guard_shield_base_no_pattern"));
    public static final Material LOCATION_VANGUARD_SHIELD = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(MODID, "entity/shield/vanguard_shield"));

}
