package com.infamous.dungeons_mobs.worldgen;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;
import static net.minecraft.world.gen.feature.structure.Structure.*;

@Mod.EventBusSubscriber(modid = MODID)
public class StructureSpawnEntries {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void structureSpawns(StructureSpawnListGatherEvent event){
        DungeonsMobsConfig.Common config = DungeonsMobsConfig.COMMON;
        if(event.getStructure().equals(DESERT_PYRAMID) && config.ENABLE_DESERT_PYRAMID_DUNGEONS_MOBS.get()){
            event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.SKELETON_VANGUARD.get(), config.SKELETON_VANGUARD_SPAWN_WEIGHT.get(), config.FUNGUS_THROWER_MIN_GROUP_SIZE.get(), config.FUNGUS_THROWER_MAX_GROUP_SIZE.get()));
        }
        if(event.getStructure().equals(JUNGLE_TEMPLE) && config.ENABLE_JUNGLE_TEMPLE_DUNGEONS_MOBS.get()){
            event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.WHISPERER.get(), config.WHISPERER_SPAWN_WEIGHT.get(), config.WHISPERER_MIN_GROUP_SIZE.get(), config.WHISPERER_MAX_GROUP_SIZE.get()));
            event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.LEAPLEAF.get(), config.LEAPLEAF_SPAWN_WEIGHT.get(), config.LEAPLEAF_MIN_GROUP_SIZE.get(), config.LEAPLEAF_MAX_GROUP_SIZE.get()));
        }
        if(event.getStructure().equals(BASTION_REMNANT)){
            if(config.ENABLE_BASTION_VANILLA.get()) {
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.PIGLIN, 20, 1, 3));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.PIGLIN_BRUTE, 10, 1, 2));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.HOGLIN, 5, 1, 1));
            }
            if(config.ENABLE_BASTION_DUNGEONS_MOBS.get()) {
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.FUNGUS_THROWER.get(), config.FUNGUS_THROWER_SPAWN_WEIGHT.get(), config.FUNGUS_THROWER_MIN_GROUP_SIZE.get(), config.FUNGUS_THROWER_MAX_GROUP_SIZE.get()));
            }
        }
        if(event.getStructure().equals(NETHER_BRIDGE) && config.ENABLE_NETHER_FORTRESS_DUNGEONS_MOBS.get()){
            event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.WILDFIRE.get(), config.WILDFIRE_SPAWN_WEIGHT.get(), config.WILDFIRE_MIN_GROUP_SIZE.get(), config.WILDFIRE_MAX_GROUP_SIZE.get()));
        }
        if(event.getStructure().equals(WOODLAND_MANSION)){
            if(config.ENABLE_MANSION_VANILLA.get()) {
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.PILLAGER, config.PILLAGER_SPAWN_WEIGHT.get(), config.PILLAGER_MIN_GROUP_SIZE.get(), config.PILLAGER_MAX_GROUP_SIZE.get()));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.VINDICATOR, config.VINDICATOR_SPAWN_WEIGHT.get(), config.VINDICATOR_MIN_GROUP_SIZE.get(), config.VINDICATOR_MAX_GROUP_SIZE.get()));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.EVOKER, config.EVOKER_SPAWN_WEIGHT.get(), config.EVOKER_MIN_GROUP_SIZE.get(), config.EVOKER_MAX_GROUP_SIZE.get()));
            }
            if(config.ENABLE_MANSION_DUNGEONS_MOBS.get()) {
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.GEOMANCER.get(), config.GEOMANCER_SPAWN_WEIGHT.get(), config.GEOMANCER_MIN_GROUP_SIZE.get(), config.GEOMANCER_MAX_GROUP_SIZE.get()));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.ICEOLOGER.get(), config.ICEOLOGER_SPAWN_WEIGHT.get(), config.ICEOLOGER_MIN_GROUP_SIZE.get(), config.ICEOLOGER_MAX_GROUP_SIZE.get()));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.WINDCALLER.get(), config.WINDCALLER_SPAWN_WEIGHT.get(), config.WINDCALLER_MIN_GROUP_SIZE.get(), config.WINDCALLER_MAX_GROUP_SIZE.get()));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.ILLUSIONER.get(), config.ILLUSIONER_SPAWN_WEIGHT.get(), config.ILLUSIONER_MIN_GROUP_SIZE.get(), config.ILLUSIONER_MAX_GROUP_SIZE.get()));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.MOUNTAINEER.get(), config.MOUNTAINEER_SPAWN_WEIGHT.get(), config.MOUNTAINEER_MIN_GROUP_SIZE.get(), config.MOUNTAINEER_MAX_GROUP_SIZE.get()));
            }
        }
        if(event.getStructure().equals(PILLAGER_OUTPOST)){
            event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.PILLAGER, 99, config.PILLAGER_MIN_GROUP_SIZE.get(), config.PILLAGER_MAX_GROUP_SIZE.get()));
            if(config.ENABLE_OUTPOST_VANILLA_VINDICATOR.get()) {
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.VINDICATOR, config.VINDICATOR_SPAWN_WEIGHT.get(), config.VINDICATOR_MIN_GROUP_SIZE.get(), config.VINDICATOR_MAX_GROUP_SIZE.get()));
            }
            if(config.ENABLE_OUTPOST_VANILLA_EVOKER.get()) {
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.EVOKER, config.EVOKER_SPAWN_WEIGHT.get(), config.EVOKER_MIN_GROUP_SIZE.get(), config.EVOKER_MAX_GROUP_SIZE.get()));
            }
            if(config.ENABLE_OUTPOST_DUNGEONS_MOBS_MELEE.get()) {
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.MOUNTAINEER.get(), config.MOUNTAINEER_SPAWN_WEIGHT.get(), config.MOUNTAINEER_MIN_GROUP_SIZE.get(), config.MOUNTAINEER_MAX_GROUP_SIZE.get()));
            }
            if(config.ENABLE_OUTPOST_DUNGEONS_MOBS_CASTER.get()) {
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.GEOMANCER.get(), config.GEOMANCER_SPAWN_WEIGHT.get(), config.GEOMANCER_MIN_GROUP_SIZE.get(), config.GEOMANCER_MAX_GROUP_SIZE.get()));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.ICEOLOGER.get(), config.ICEOLOGER_SPAWN_WEIGHT.get(), config.ICEOLOGER_MIN_GROUP_SIZE.get(), config.ICEOLOGER_MAX_GROUP_SIZE.get()));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.WINDCALLER.get(), config.WINDCALLER_SPAWN_WEIGHT.get(), config.WINDCALLER_MIN_GROUP_SIZE.get(), config.WINDCALLER_MAX_GROUP_SIZE.get()));
                event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntityTypes.ILLUSIONER.get(), config.ILLUSIONER_SPAWN_WEIGHT.get(), config.ILLUSIONER_MIN_GROUP_SIZE.get(), config.ILLUSIONER_MAX_GROUP_SIZE.get()));
            }
        }
    }
}
