package com.infamous.dungeons_mobs.items.shield;

import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class ShieldTextures {

    public static final RenderMaterial LOCATION_ROYAL_GUARD_SHIELD_BASE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(MODID, "entity/shield/royal_guard_shield_base"));
    public static final RenderMaterial LOCATION_ROYAL_GUARD_SHIELD_NO_PATTERN = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(MODID,"entity/shield/royal_guard_shield_base_no_pattern"));
    public static final RenderMaterial LOCATION_SKELETON_VANGUARD_SHIELD = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(MODID,"entity/shield/skeleton_vanguard_shield"));

}
