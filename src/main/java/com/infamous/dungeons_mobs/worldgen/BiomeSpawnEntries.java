package com.infamous.dungeons_mobs.worldgen;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BiomeSpawnEntries {

    @SuppressWarnings("unchecked")
    public static void initBiomeSpawnEntries(){

        // Adding entries for Nether Biomes that were missing
        BiomeDictionary.addTypes(Biomes.CRIMSON_FOREST, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.getType("FUNGUS"));
        BiomeDictionary.addTypes(Biomes.WARPED_FOREST, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.getType("FUNGUS"));
        BiomeDictionary.addTypes(Biomes.BASALT_DELTAS, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.getType("DELTA"));
        BiomeDictionary.addTypes(Biomes.SOUL_SAND_VALLEY, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.getType("SOUL"));

        for(Biome biome : ForgeRegistries.BIOMES){
            List<String> iceologerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.ICEOLOGER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, iceologerBiomeTypes,
                    ModEntityTypes.ICEOLOGER.get(),
                    DungeonsMobsConfig.COMMON.ICEOLOGER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.ICEOLOGER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.ICEOLOGER_MAX_GROUP_SIZE.get());

            List<String> wraithBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.WRAITH_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, wraithBiomeTypes, ModEntityTypes.WRAITH.get(),
                    DungeonsMobsConfig.COMMON.WRAITH_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.WRAITH_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.WRAITH_MAX_GROUP_SIZE.get());

            List<String> necromancerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.NECROMANCER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, necromancerBiomeTypes, ModEntityTypes.NECROMANCER.get(),
                    DungeonsMobsConfig.COMMON.NECROMANCER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.NECROMANCER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.NECROMANCER_MAX_GROUP_SIZE.get());

            List<String> skeletonHorsemanBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.SKELETON_HORSEMAN_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, skeletonHorsemanBiomeTypes, ModEntityTypes.SKELETON_HORSEMAN.get(),
                    DungeonsMobsConfig.COMMON.SKELETON_HORSEMAN_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.SKELETON_HORSEMAN_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.SKELETON_HORSEMAN_MAX_GROUP_SIZE.get());

            List<String> mountaineerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.MOUNTAINEER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, mountaineerBiomeTypes, ModEntityTypes.MOUNTAINEER.get(),
                    DungeonsMobsConfig.COMMON.MOUNTAINEER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.MOUNTAINEER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.MOUNTAINEER_MAX_GROUP_SIZE.get());

            List<String> windcallerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.WINDCALLER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, windcallerBiomeTypes, ModEntityTypes.WINDCALLER.get(),
                    DungeonsMobsConfig.COMMON.WINDCALLER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.WINDCALLER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.WINDCALLER_MAX_GROUP_SIZE.get());

            List<String> geomancerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.GEOMANCER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, geomancerBiomeTypes, ModEntityTypes.GEOMANCER.get(),
                    DungeonsMobsConfig.COMMON.GEOMANCER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.GEOMANCER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.GEOMANCER_MAX_GROUP_SIZE.get());

            List<String> illusionerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.ILLUSIONER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, illusionerBiomeTypes, EntityType.ILLUSIONER,
                    DungeonsMobsConfig.COMMON.ILLUSIONER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.ILLUSIONER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.ILLUSIONER_MAX_GROUP_SIZE.get());

            List<String> vindicatorBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.VINDICATOR_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, vindicatorBiomeTypes, EntityType.VINDICATOR,
                    DungeonsMobsConfig.COMMON.VINDICATOR_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.VINDICATOR_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.VINDICATOR_MAX_GROUP_SIZE.get());

            List<String> evokerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.EVOKER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, evokerBiomeTypes, EntityType.EVOKER,
                    DungeonsMobsConfig.COMMON.EVOKER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.EVOKER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.EVOKER_MAX_GROUP_SIZE.get());

            List<String> pillagerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.PILLAGER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, pillagerBiomeTypes, EntityType.PILLAGER,
                    DungeonsMobsConfig.COMMON.PILLAGER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.PILLAGER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.PILLAGER_MAX_GROUP_SIZE.get());

            List<String> whispererBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.WHISPERER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, whispererBiomeTypes, ModEntityTypes.WHISPERER.get(),
                    DungeonsMobsConfig.COMMON.WHISPERER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.WHISPERER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.WHISPERER_MAX_GROUP_SIZE.get());

            List<String> leapleafBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.LEAPLEAF_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, leapleafBiomeTypes, ModEntityTypes.LEAPLEAF.get(),
                    DungeonsMobsConfig.COMMON.LEAPLEAF_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.LEAPLEAF_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.LEAPLEAF_MAX_GROUP_SIZE.get());

            List<String> quickGrowingVineBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.QUICK_GROWING_VINE_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, quickGrowingVineBiomeTypes, ModEntityTypes.QUICK_GROWING_VINE.get(),
                    DungeonsMobsConfig.COMMON.QUICK_GROWING_VINE_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.QUICK_GROWING_VINE_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.QUICK_GROWING_VINE_MAX_GROUP_SIZE.get());

            List<String> poisonQuillVineBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.POISON_QUILL_VINE_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biome, poisonQuillVineBiomeTypes, ModEntityTypes.POISON_QUILL_VINE.get(),
                    DungeonsMobsConfig.COMMON.POISON_QUILL_VINE_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.POISON_QUILL_VINE_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.POISON_QUILL_VINE_MAX_GROUP_SIZE.get());

            if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_ZOMBIE_REPLACES_ZOMBIE.get()){
                handleVariantReplacement(biome, EntityType.ZOMBIE, ModEntityTypes.ARMORED_ZOMBIE.get(), 0.9);
            }
            if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_SKELETON_REPLACES_SKELETON.get()){
                handleVariantReplacement(biome, EntityType.SKELETON, ModEntityTypes.ARMORED_SKELETON.get(), 0.9);
            }
            if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_MOUNTAINEER_REPLACES_MOUNTAINEER.get()){
                handleVariantReplacement(biome, ModEntityTypes.MOUNTAINEER.get(), ModEntityTypes.ARMORED_MOUNTAINEER.get(), 0.9);
            }
            if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_VINDICATOR_REPLACES_VINDICATOR.get()){
                handleVariantReplacement(biome, EntityType.VINDICATOR, ModEntityTypes.ARMORED_VINDICATOR.get(), 0.9);
            }
            if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_PILLAGER_REPLACES_PILLAGER.get()){
                handleVariantReplacement(biome, EntityType.PILLAGER, ModEntityTypes.ARMORED_PILLAGER.get(), 0.9);
            }
            if(DungeonsMobsConfig.COMMON.ENABLE_JUNGLE_ZOMBIE_REPLACES_ZOMBIE.get()){
                handleVariantReplacementWithCategoryCheck(biome, Biome.Category.JUNGLE, EntityType.ZOMBIE, ModEntityTypes.JUNGLE_ZOMBIE.get(), 0.8);
            }
            if(DungeonsMobsConfig.COMMON.ENABLE_MOSSY_SKELETON_REPLACES_SKELETON.get()){
                handleVariantReplacementWithCategoryCheck(biome, Biome.Category.JUNGLE, EntityType.SKELETON, ModEntityTypes.MOSSY_SKELETON.get(), 0.8);
            }
            if(DungeonsMobsConfig.COMMON.ENABLE_FROZEN_ZOMBIE_REPLACES_ZOMBIE.get()){
                handleVariantReplacementWithCategoryCheck(biome, Biome.Category.ICY, EntityType.ZOMBIE, ModEntityTypes.FROZEN_ZOMBIE.get(), 0.8);
            }
            if(DungeonsMobsConfig.COMMON.ENABLE_ICY_CREEPER_REPLACES_CREEPER.get()){
                handleVariantReplacementWithCategoryCheck(biome, Biome.Category.ICY, EntityType.CREEPER, ModEntityTypes.ICY_CREEPER.get(), 0.8);
            }
        }
    }

    private static void tryAddMonsterSpawnToBiome(Biome biome, List<String> configuredBiomeTypes, EntityType entityType, int spawnWeight, int minGroupSize, int maxGroupSize) {
        if(configuredBiomeTypes.isEmpty()) return;

        List<String> disallowedBiomeTypes = new ArrayList<>();
        for(String configuredBiomeType : configuredBiomeTypes){
            if(configuredBiomeType.startsWith("!") && configuredBiomeType.length() > 1){
                disallowedBiomeTypes.add(configuredBiomeType.substring(1));
            }
        }
        Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biome);
        boolean containsAllowedType = false;
        boolean containsDisallowedType = false;
        for(BiomeDictionary.Type biomeType : biomeTypes){
            String biomeTypeName = biomeType.getName();
            if(disallowedBiomeTypes.contains(biomeTypeName)){
                containsDisallowedType = true;
                break;
            }
            if(configuredBiomeTypes.contains(biomeTypeName)){
                containsAllowedType = true;
            }
        }
        if(containsAllowedType && !containsDisallowedType){
            Biome.SpawnListEntry monsterSpawnEntry = new Biome.SpawnListEntry(entityType, spawnWeight, minGroupSize, maxGroupSize);
            biome.getSpawns(EntityClassification.MONSTER).add(monsterSpawnEntry);
        }
    }

    private static void handleVariantReplacementWithCategoryCheck(Biome biome, Biome.Category category, EntityType typeToReplace, EntityType typeReplaceBy, double retainAmount) {
        if(biome.getCategory() == category){
            handleVariantReplacement(biome, typeToReplace, typeReplaceBy, retainAmount);
        }
    }

    private static void handleVariantReplacement(Biome biome, EntityType typeToReplace, EntityType typeReplaceBy, double retainAmount) {
        retainAmount = MathHelper.clamp(retainAmount, 0 , 1);
        double replaceAmount = MathHelper.clamp(1 - retainAmount, 0 , 1);

        List<Biome.SpawnListEntry> monsterSpawnList = biome.getSpawns(EntityClassification.MONSTER);
        for(int i = 0; i < monsterSpawnList.size(); i++) {
            Biome.SpawnListEntry spawnListEntry = monsterSpawnList.get(i);
            int weight = spawnListEntry.itemWeight;
            int minGroupCount = spawnListEntry.minGroupCount;
            int maxGroupCount = spawnListEntry.maxGroupCount;
            EntityType<?> entityType = spawnListEntry.entityType;
            if (entityType == typeToReplace) {
                Biome.EntityDensity entityDensity = biome.func_235058_a_(typeToReplace);
                if(entityDensity != null){
                    double val1 = entityDensity.func_235119_a_();
                    double val2 = entityDensity.func_235120_b_();
                    biome.func_235059_a_(typeToReplace, val1 * retainAmount, val2 * retainAmount);
                    biome.func_235059_a_(typeReplaceBy, val1 * replaceAmount, val2 * replaceAmount);
                }
                Biome.SpawnListEntry typeReplaceByEntry = new Biome.SpawnListEntry(typeReplaceBy, (int) (weight * replaceAmount), minGroupCount, maxGroupCount);
                Biome.SpawnListEntry typeToReplaceEntry = new Biome.SpawnListEntry(typeToReplace, (int) (weight * retainAmount), minGroupCount, maxGroupCount);
                monsterSpawnList.set(i, typeToReplaceEntry);
                monsterSpawnList.add(typeReplaceByEntry);
            }
        }
    }
}
