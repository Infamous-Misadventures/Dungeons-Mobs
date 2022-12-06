package com.infamous.dungeons_mobs.tags;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;

public class BiomeTags {

    public static final TagKey<Biome> IS_CRIMSON = forgeTag("is_crimson");
    public static final TagKey<Biome> IS_WARPED = forgeTag("is_warped");
    public static final TagKey<Biome> IS_DELTA = forgeTag("is_basalt");
    public static final TagKey<Biome> IS_SOULSAND = forgeTag("is_soulsand");

    public static final TagKey<Biome> MOUNTAINEER_SPAWN_BIOMES = tag("mountaineer_spawn_biomes");
    public static final TagKey<Biome> ICEOLOGER_SPAWN_BIOMES = tag("iceologer_spawn_biomes");
    public static final TagKey<Biome> WINDCALLER_SPAWN_BIOMES = tag("windcaller_spawn_biomes");
    public static final TagKey<Biome> SQUALL_GOLEM_SPAWN_BIOMES = tag("squall_golem_spawn_biomes");

    private static TagKey<Biome> tag(String name) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(DungeonsMobs.MODID, name));
    }
    private static TagKey<Biome> forgeTag(String name) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("forge", name));
    }

    public static void register() {
        // NOOP
    }
}
