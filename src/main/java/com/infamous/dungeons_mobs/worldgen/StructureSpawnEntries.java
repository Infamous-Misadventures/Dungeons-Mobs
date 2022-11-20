package com.infamous.dungeons_mobs.worldgen;

import com.google.common.collect.ImmutableList;
import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;
import static net.minecraft.world.level.levelgen.feature.StructureFeature.*;

@Mod.EventBusSubscriber(modid = MODID)
public class StructureSpawnEntries {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void structureSpawns(StructureSpawnListGatherEvent event) {
        DungeonsMobsConfig.Common config = DungeonsMobsConfig.COMMON;
        if (event.getStructure().equals(DESERT_PYRAMID) && config.ENABLE_DESERT_PYRAMID_DUNGEONS_MOBS.get()) {
            event.addEntitySpawns(MobCategory.MONSTER,
                    ImmutableList.of(
                            new MobSpawnSettings.SpawnerData(ModEntityTypes.SKELETON_VANGUARD.get(), config.SKELETON_VANGUARD_SPAWN_WEIGHT.get(), config.FUNGUS_THROWER_MIN_GROUP_SIZE.get(), config.FUNGUS_THROWER_MAX_GROUP_SIZE.get())
                    ));
        }
        if (event.getStructure().equals(JUNGLE_TEMPLE) && config.ENABLE_JUNGLE_TEMPLE_DUNGEONS_MOBS.get()) {
            event.addEntitySpawns(MobCategory.MONSTER,
                    ImmutableList.of(
                            new MobSpawnSettings.SpawnerData(ModEntityTypes.WHISPERER.get(), config.WHISPERER_SPAWN_WEIGHT.get(), config.WHISPERER_MIN_GROUP_SIZE.get(), config.WHISPERER_MAX_GROUP_SIZE.get()),
                            new MobSpawnSettings.SpawnerData(ModEntityTypes.LEAPLEAF.get(), config.LEAPLEAF_SPAWN_WEIGHT.get(), config.LEAPLEAF_MIN_GROUP_SIZE.get(), config.LEAPLEAF_MAX_GROUP_SIZE.get())
                    ));
        }
        if (event.getStructure().equals(BASTION_REMNANT)) {
            if (config.ENABLE_BASTION_VANILLA.get()) {
                event.addEntitySpawns(MobCategory.MONSTER,
                        ImmutableList.of(
                                new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 20, 1, 3),
                                new MobSpawnSettings.SpawnerData(EntityType.PIGLIN_BRUTE, 10, 1, 2),
                                new MobSpawnSettings.SpawnerData(EntityType.HOGLIN, 5, 1, 1)
                        ));
            }
            if (config.ENABLE_BASTION_DUNGEONS_MOBS.get()) {
                event.addEntitySpawns(MobCategory.MONSTER,
                        ImmutableList.of(
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.FUNGUS_THROWER.get(), config.FUNGUS_THROWER_SPAWN_WEIGHT.get(), config.FUNGUS_THROWER_MIN_GROUP_SIZE.get(), config.FUNGUS_THROWER_MAX_GROUP_SIZE.get())
                        ));
            }
        }
        if (event.getStructure().equals(FORTRESS) && config.ENABLE_NETHER_FORTRESS_DUNGEONS_MOBS.get()) {
            event.addEntitySpawns(MobCategory.MONSTER,
                    ImmutableList.of(
                            new MobSpawnSettings.SpawnerData(ModEntityTypes.WILDFIRE.get(), config.WILDFIRE_SPAWN_WEIGHT.get(), config.WILDFIRE_MIN_GROUP_SIZE.get(), config.WILDFIRE_MAX_GROUP_SIZE.get())
                    ));
        }
        if (event.getStructure().equals(WOODLAND_MANSION)) {
            if (config.ENABLE_MANSION_VANILLA.get()) {
                event.addEntitySpawns(MobCategory.MONSTER,
                        ImmutableList.of(
                                new MobSpawnSettings.SpawnerData(EntityType.PILLAGER, config.PILLAGER_SPAWN_WEIGHT.get(), config.PILLAGER_MIN_GROUP_SIZE.get(), config.PILLAGER_MAX_GROUP_SIZE.get()),
                                new MobSpawnSettings.SpawnerData(EntityType.VINDICATOR, config.VINDICATOR_SPAWN_WEIGHT.get(), config.VINDICATOR_MIN_GROUP_SIZE.get(), config.VINDICATOR_MAX_GROUP_SIZE.get()),
                                new MobSpawnSettings.SpawnerData(EntityType.EVOKER, config.EVOKER_SPAWN_WEIGHT.get(), config.EVOKER_MIN_GROUP_SIZE.get(), config.EVOKER_MAX_GROUP_SIZE.get())
                        ));
            }
            if (config.ENABLE_MANSION_DUNGEONS_MOBS.get()) {
                event.addEntitySpawns(MobCategory.MONSTER,
                        ImmutableList.of(
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.GEOMANCER.get(), config.GEOMANCER_SPAWN_WEIGHT.get(), config.GEOMANCER_MIN_GROUP_SIZE.get(), config.GEOMANCER_MAX_GROUP_SIZE.get()),
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.ICEOLOGER.get(), config.ICEOLOGER_SPAWN_WEIGHT.get(), config.ICEOLOGER_MIN_GROUP_SIZE.get(), config.ICEOLOGER_MAX_GROUP_SIZE.get()),
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.WINDCALLER.get(), config.WINDCALLER_SPAWN_WEIGHT.get(), config.WINDCALLER_MIN_GROUP_SIZE.get(), config.WINDCALLER_MAX_GROUP_SIZE.get()),
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.ILLUSIONER.get(), config.ILLUSIONER_SPAWN_WEIGHT.get(), config.ILLUSIONER_MIN_GROUP_SIZE.get(), config.ILLUSIONER_MAX_GROUP_SIZE.get()),
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.MOUNTAINEER.get(), config.MOUNTAINEER_SPAWN_WEIGHT.get(), config.MOUNTAINEER_MIN_GROUP_SIZE.get(), config.MOUNTAINEER_MAX_GROUP_SIZE.get()),
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.ROYAL_GUARD.get(), config.ROYAL_GUARD_SPAWN_WEIGHT.get(), config.ROYAL_GUARD_MIN_GROUP_SIZE.get(), config.ROYAL_GUARD_MAX_GROUP_SIZE.get())
                        ));
            }
        }
        if (event.getStructure().equals(PILLAGER_OUTPOST)) {
            event.addEntitySpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PILLAGER, 99, config.PILLAGER_MIN_GROUP_SIZE.get(), config.PILLAGER_MAX_GROUP_SIZE.get()));
            if (config.ENABLE_OUTPOST_VANILLA_VINDICATOR.get()) {
                event.addEntitySpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.VINDICATOR, config.VINDICATOR_SPAWN_WEIGHT.get(), config.VINDICATOR_MIN_GROUP_SIZE.get(), config.VINDICATOR_MAX_GROUP_SIZE.get()));
            }
            if (config.ENABLE_OUTPOST_VANILLA_EVOKER.get()) {
                event.addEntitySpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.EVOKER, config.EVOKER_SPAWN_WEIGHT.get(), config.EVOKER_MIN_GROUP_SIZE.get(), config.EVOKER_MAX_GROUP_SIZE.get()));
            }
            if (config.ENABLE_OUTPOST_DUNGEONS_MOBS_MELEE.get()) {
                event.addEntitySpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntityTypes.MOUNTAINEER.get(), config.MOUNTAINEER_SPAWN_WEIGHT.get(), config.MOUNTAINEER_MIN_GROUP_SIZE.get(), config.MOUNTAINEER_MAX_GROUP_SIZE.get()));
            }
            if (config.ENABLE_OUTPOST_DUNGEONS_MOBS_CASTER.get()) {
                event.addEntitySpawns(MobCategory.MONSTER,
                        ImmutableList.of(new MobSpawnSettings.SpawnerData(ModEntityTypes.GEOMANCER.get(), config.GEOMANCER_SPAWN_WEIGHT.get(), config.GEOMANCER_MIN_GROUP_SIZE.get(), config.GEOMANCER_MAX_GROUP_SIZE.get()),
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.ICEOLOGER.get(), config.ICEOLOGER_SPAWN_WEIGHT.get(), config.ICEOLOGER_MIN_GROUP_SIZE.get(), config.ICEOLOGER_MAX_GROUP_SIZE.get()),
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.WINDCALLER.get(), config.WINDCALLER_SPAWN_WEIGHT.get(), config.WINDCALLER_MIN_GROUP_SIZE.get(), config.WINDCALLER_MAX_GROUP_SIZE.get()),
                                new MobSpawnSettings.SpawnerData(ModEntityTypes.ILLUSIONER.get(), config.ILLUSIONER_SPAWN_WEIGHT.get(), config.ILLUSIONER_MIN_GROUP_SIZE.get(), config.ILLUSIONER_MAX_GROUP_SIZE.get())
                        ));
            }
        }
    }
}
