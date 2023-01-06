package com.infamous.dungeons_mobs.interfaces;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.tags.BiomeTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.biome.Biome;

public enum BiomeSpecificRaider {
    MOUNTAINEER(
            ModEntityTypes.MOUNTAINEER.get(),
            EntityType.VINDICATOR,
            BiomeTags.MOUNTAINEER_SPAWN_BIOMES),
    ICEOLOGER(
            ModEntityTypes.ICEOLOGER.get(),
            EntityType.EVOKER,
            BiomeTags.ICEOLOGER_SPAWN_BIOMES),
    WINDCALLER(
            ModEntityTypes.WINDCALLER.get(),
            EntityType.EVOKER,
            BiomeTags.WINDCALLER_SPAWN_BIOMES),
    SQUALL_GOLEM(
            ModEntityTypes.SQUALL_GOLEM.get(),
            EntityType.RAVAGER,
            BiomeTags.SQUALL_GOLEM_SPAWN_BIOMES),

    /*,
    ILLUSIONER(
            EntityType.ILLUSIONER,
            EntityType.ILLUSIONER,
            DungeonsMobsConfig.COMMON.ILLUSIONER_BIOME_TYPES.get())
     */;

    private final EntityType<? extends Raider> entityType;
    private final EntityType<? extends Raider> equivalentType;
    private final TagKey<Biome> biomeTag;

    BiomeSpecificRaider(EntityType<? extends Raider> entityTypeIn, EntityType<? extends Raider> equivalentTypeIn, TagKey<Biome> biomeTag) {
        this.entityType = entityTypeIn;
        this.equivalentType = equivalentTypeIn;
        this.biomeTag = biomeTag;
    }

    public EntityType<? extends Raider> getType() {
        return this.entityType;
    }

    public EntityType<? extends Raider> getEquivalentType() {
        return this.equivalentType;
    }

    public TagKey<Biome> getBiomeTag() {
        return biomeTag;
    }
}
