package com.infamous.dungeons_mobs.worldgen;

import com.infamous.dungeons_mobs.config.DungeonsMobsConfig;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class BiomeSpawnEntries {

    public static final BiomeDictionary.Type CRIMSON = BiomeDictionary.Type.getType("CRIMSON");
    public static final BiomeDictionary.Type WARPED = BiomeDictionary.Type.getType("WARPED");
    public static final BiomeDictionary.Type DELTA = BiomeDictionary.Type.getType("DELTA");
    public static final BiomeDictionary.Type SOULSAND = BiomeDictionary.Type.getType("SOULSAND");

    public static void addCustomTypesToBiomes(){
        BiomeDictionary.addTypes(Biomes.CRIMSON_FOREST, CRIMSON);
        BiomeDictionary.addTypes(Biomes.WARPED_FOREST, WARPED);
        BiomeDictionary.addTypes(Biomes.BASALT_DELTAS, DELTA);
        BiomeDictionary.addTypes(Biomes.SOUL_SAND_VALLEY, SOULSAND);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoadingEvent(BiomeLoadingEvent event){
        ResourceLocation biomeRegistryName = event.getName();
        if (biomeRegistryName != null) {
            RegistryKey<Biome> biomeRegistryKey = RegistryKey.create(Registry.BIOME_REGISTRY, biomeRegistryName);
            MobSpawnInfoBuilder mobSpawnInfoBuilder = event.getSpawns();
            addMonsterSpawnsToBiome(biomeRegistryKey, mobSpawnInfoBuilder);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onBiomeLoadingEventForVariants(BiomeLoadingEvent event){
        Biome.Category biomeCategory = event.getCategory();
        MobSpawnInfoBuilder mobSpawnInfoBuilder = event.getSpawns();
        replaceMonsterSpawnsWithVariants(mobSpawnInfoBuilder, biomeCategory);
    }

    @SuppressWarnings("unchecked")
    private static void addMonsterSpawnsToBiome(RegistryKey<Biome> biomeRegistryKey, MobSpawnInfoBuilder mobSpawnInfoBuilder){
        //for(int i = 0; i < 1; i++){
            List<String> iceologerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.ICEOLOGER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, iceologerBiomeTypes,
                    ModEntityTypes.ICEOLOGER.get(),
                    DungeonsMobsConfig.COMMON.ICEOLOGER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.ICEOLOGER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.ICEOLOGER_MAX_GROUP_SIZE.get());

            List<String> wraithBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.WRAITH_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, wraithBiomeTypes, ModEntityTypes.WRAITH.get(),
                    DungeonsMobsConfig.COMMON.WRAITH_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.WRAITH_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.WRAITH_MAX_GROUP_SIZE.get());

            List<String> necromancerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.NECROMANCER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, necromancerBiomeTypes, ModEntityTypes.NECROMANCER.get(),
                    DungeonsMobsConfig.COMMON.NECROMANCER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.NECROMANCER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.NECROMANCER_MAX_GROUP_SIZE.get());

            List<String> skeletonHorsemanBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.SKELETON_HORSEMAN_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, skeletonHorsemanBiomeTypes, ModEntityTypes.SKELETON_HORSEMAN.get(),
                    DungeonsMobsConfig.COMMON.SKELETON_HORSEMAN_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.SKELETON_HORSEMAN_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.SKELETON_HORSEMAN_MAX_GROUP_SIZE.get());

            List<String> mountaineerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.MOUNTAINEER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, mountaineerBiomeTypes, ModEntityTypes.MOUNTAINEER.get(),
                    DungeonsMobsConfig.COMMON.MOUNTAINEER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.MOUNTAINEER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.MOUNTAINEER_MAX_GROUP_SIZE.get());

            List<String> windcallerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.WINDCALLER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, windcallerBiomeTypes, ModEntityTypes.WINDCALLER.get(),
                    DungeonsMobsConfig.COMMON.WINDCALLER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.WINDCALLER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.WINDCALLER_MAX_GROUP_SIZE.get());

            List<String> geomancerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.GEOMANCER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, geomancerBiomeTypes, ModEntityTypes.GEOMANCER.get(),
                    DungeonsMobsConfig.COMMON.GEOMANCER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.GEOMANCER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.GEOMANCER_MAX_GROUP_SIZE.get());

            List<String> illusionerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.ILLUSIONER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, illusionerBiomeTypes, ModEntityTypes.ILLUSIONER.get(),
                    DungeonsMobsConfig.COMMON.ILLUSIONER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.ILLUSIONER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.ILLUSIONER_MAX_GROUP_SIZE.get());

            List<String> vindicatorBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.VINDICATOR_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, vindicatorBiomeTypes, EntityType.VINDICATOR,
                    DungeonsMobsConfig.COMMON.VINDICATOR_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.VINDICATOR_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.VINDICATOR_MAX_GROUP_SIZE.get());

            List<String> evokerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.EVOKER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, evokerBiomeTypes, EntityType.EVOKER,
                    DungeonsMobsConfig.COMMON.EVOKER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.EVOKER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.EVOKER_MAX_GROUP_SIZE.get());

            List<String> pillagerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.PILLAGER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, pillagerBiomeTypes, EntityType.PILLAGER,
                    DungeonsMobsConfig.COMMON.PILLAGER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.PILLAGER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.PILLAGER_MAX_GROUP_SIZE.get());

            List<String> whispererBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.WHISPERER_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, whispererBiomeTypes, ModEntityTypes.WHISPERER.get(),
                    DungeonsMobsConfig.COMMON.WHISPERER_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.WHISPERER_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.WHISPERER_MAX_GROUP_SIZE.get());

            List<String> leapleafBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.LEAPLEAF_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, leapleafBiomeTypes, ModEntityTypes.LEAPLEAF.get(),
                    DungeonsMobsConfig.COMMON.LEAPLEAF_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.LEAPLEAF_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.LEAPLEAF_MAX_GROUP_SIZE.get());

            List<String> quickGrowingVineBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.QUICK_GROWING_VINE_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, quickGrowingVineBiomeTypes, ModEntityTypes.QUICK_GROWING_VINE.get(),
                    DungeonsMobsConfig.COMMON.QUICK_GROWING_VINE_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.QUICK_GROWING_VINE_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.QUICK_GROWING_VINE_MAX_GROUP_SIZE.get());

            List<String> poisonQuillVineBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.POISON_QUILL_VINE_BIOME_TYPES.get();
            tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, poisonQuillVineBiomeTypes, ModEntityTypes.POISON_QUILL_VINE.get(),
                    DungeonsMobsConfig.COMMON.POISON_QUILL_VINE_SPAWN_WEIGHT.get(),
                    DungeonsMobsConfig.COMMON.POISON_QUILL_VINE_MIN_GROUP_SIZE.get(),
                    DungeonsMobsConfig.COMMON.POISON_QUILL_VINE_MAX_GROUP_SIZE.get());

        List<String> fungusThrowerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.FUNGUS_THROWER_BIOME_TYPES.get();
        tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, fungusThrowerBiomeTypes, ModEntityTypes.FUNGUS_THROWER.get(),
                DungeonsMobsConfig.COMMON.FUNGUS_THROWER_SPAWN_WEIGHT.get(),
                DungeonsMobsConfig.COMMON.FUNGUS_THROWER_MIN_GROUP_SIZE.get(),
                DungeonsMobsConfig.COMMON.FUNGUS_THROWER_MAX_GROUP_SIZE.get());

        List<String> zombifiedFungusThrowerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.ZOMBIFIED_FUNGUS_THROWER_BIOME_TYPES.get();
        tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, zombifiedFungusThrowerBiomeTypes, ModEntityTypes.ZOMBIFIED_FUNGUS_THROWER.get(),
                DungeonsMobsConfig.COMMON.ZOMBIFIED_FUNGUS_THROWER_SPAWN_WEIGHT.get(),
                DungeonsMobsConfig.COMMON.ZOMBIFIED_FUNGUS_THROWER_MIN_GROUP_SIZE.get(),
                DungeonsMobsConfig.COMMON.ZOMBIFIED_FUNGUS_THROWER_MAX_GROUP_SIZE.get());

        List<String> witherSkeletonBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.WITHER_SKELETON_BIOME_TYPES.get();
        tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, witherSkeletonBiomeTypes, EntityType.WITHER_SKELETON,
                DungeonsMobsConfig.COMMON.WITHER_SKELETON_SPAWN_WEIGHT.get(),
                DungeonsMobsConfig.COMMON.WITHER_SKELETON_MIN_GROUP_SIZE.get(),
                DungeonsMobsConfig.COMMON.WITHER_SKELETON_MAX_GROUP_SIZE.get());

        List<String> sunkenSkeletonBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.SUNKEN_SKELETON_BIOME_TYPES.get();
        tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, sunkenSkeletonBiomeTypes, ModEntityTypes.SUNKEN_SKELETON.get(),
                DungeonsMobsConfig.COMMON.SUNKEN_SKELETON_SPAWN_WEIGHT.get(),
                DungeonsMobsConfig.COMMON.SUNKEN_SKELETON_MIN_GROUP_SIZE.get(),
                DungeonsMobsConfig.COMMON.SUNKEN_SKELETON_MAX_GROUP_SIZE.get());

        List<String> drownedNecromancerBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.DROWNED_NECROMANCER_BIOME_TYPES.get();
        tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, drownedNecromancerBiomeTypes, ModEntityTypes.SUNKEN_SKELETON.get(),
                DungeonsMobsConfig.COMMON.DROWNED_NECROMANCER_SPAWN_WEIGHT.get(),
                DungeonsMobsConfig.COMMON.DROWNED_NECROMANCER_MIN_GROUP_SIZE.get(),
                DungeonsMobsConfig.COMMON.DROWNED_NECROMANCER_MAX_GROUP_SIZE.get());

        List<String> wavewhispererBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.WAVEWHISPERER_BIOME_TYPES.get();
        tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, wavewhispererBiomeTypes, ModEntityTypes.WAVEWHISPERER.get(),
                DungeonsMobsConfig.COMMON.WAVEWHISPERER_SPAWN_WEIGHT.get(),
                DungeonsMobsConfig.COMMON.WAVEWHISPERER_MIN_GROUP_SIZE.get(),
                DungeonsMobsConfig.COMMON.WAVEWHISPERER_MAX_GROUP_SIZE.get());

        List<String> quickGrowingAnemoneBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.QUICK_GROWING_ANEMONE_BIOME_TYPES.get();
        tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, quickGrowingAnemoneBiomeTypes, ModEntityTypes.QUICK_GROWING_ANEMONE.get(),
                DungeonsMobsConfig.COMMON.QUICK_GROWING_ANEMONE_SPAWN_WEIGHT.get(),
                DungeonsMobsConfig.COMMON.QUICK_GROWING_ANEMONE_MIN_GROUP_SIZE.get(),
                DungeonsMobsConfig.COMMON.QUICK_GROWING_ANEMONE_MAX_GROUP_SIZE.get());

        List<String> poisonAnemoneBiomeTypes = (List<String>) DungeonsMobsConfig.COMMON.POISON_ANEMONE_BIOME_TYPES.get();
        tryAddMonsterSpawnToBiome(biomeRegistryKey, mobSpawnInfoBuilder, poisonAnemoneBiomeTypes, ModEntityTypes.POISON_ANEMONE.get(),
                DungeonsMobsConfig.COMMON.POISON_ANEMONE_SPAWN_WEIGHT.get(),
                DungeonsMobsConfig.COMMON.POISON_ANEMONE_MIN_GROUP_SIZE.get(),
                DungeonsMobsConfig.COMMON.POISON_ANEMONE_MAX_GROUP_SIZE.get());

        //}
    }

    private static void replaceMonsterSpawnsWithVariants(MobSpawnInfoBuilder mobSpawnInfoBuilder, Biome.Category foundCategory) {
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_ZOMBIE_REPLACES_ZOMBIE.get()){
            handleVariantReplacement(mobSpawnInfoBuilder, EntityType.ZOMBIE, ModEntityTypes.ARMORED_ZOMBIE.get(), 0.9);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_SKELETON_REPLACES_SKELETON.get()){
            handleVariantReplacement(mobSpawnInfoBuilder, EntityType.SKELETON, ModEntityTypes.ARMORED_SKELETON.get(), 0.9);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_MOUNTAINEER_REPLACES_MOUNTAINEER.get()){
            handleVariantReplacement(mobSpawnInfoBuilder, ModEntityTypes.MOUNTAINEER.get(), ModEntityTypes.ARMORED_MOUNTAINEER.get(), 0.9);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_VINDICATOR_REPLACES_VINDICATOR.get()){
            handleVariantReplacement(mobSpawnInfoBuilder, EntityType.VINDICATOR, ModEntityTypes.ARMORED_VINDICATOR.get(), 0.9);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_PILLAGER_REPLACES_PILLAGER.get()){
            handleVariantReplacement(mobSpawnInfoBuilder, EntityType.PILLAGER, ModEntityTypes.ARMORED_PILLAGER.get(), 0.9);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_PIGLIN_REPLACES_PIGLIN.get()){
            handleVariantReplacement(mobSpawnInfoBuilder, EntityType.PIGLIN, ModEntityTypes.ARMORED_PIGLIN.get(), 0.9);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_ZOMBIFIED_ARMORED_PIGLIN_REPLACES_ZOMBIFIED_PIGLIN.get()){
            handleVariantReplacement(mobSpawnInfoBuilder, EntityType.ZOMBIFIED_PIGLIN, ModEntityTypes.ZOMBIFIED_ARMORED_PIGLIN.get(), 0.9);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_JUNGLE_ZOMBIE_REPLACES_ZOMBIE.get()){
            handleVariantReplacementWithCategoryCheck(mobSpawnInfoBuilder, foundCategory, Biome.Category.JUNGLE, EntityType.ZOMBIE, ModEntityTypes.JUNGLE_ZOMBIE.get(), 0.8);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_MOSSY_SKELETON_REPLACES_SKELETON.get()){
            handleVariantReplacementWithCategoryCheck(mobSpawnInfoBuilder, foundCategory, Biome.Category.JUNGLE, EntityType.SKELETON, ModEntityTypes.MOSSY_SKELETON.get(), 0.8);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_FROZEN_ZOMBIE_REPLACES_ZOMBIE.get()){
            handleVariantReplacementWithCategoryCheck(mobSpawnInfoBuilder, foundCategory, Biome.Category.ICY, EntityType.ZOMBIE, ModEntityTypes.FROZEN_ZOMBIE.get(), 0.8);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_ICY_CREEPER_REPLACES_CREEPER.get()){
            handleVariantReplacementWithCategoryCheck(mobSpawnInfoBuilder, foundCategory, Biome.Category.ICY, EntityType.CREEPER, ModEntityTypes.ICY_CREEPER.get(), 0.8);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_DROWNED_REPLACES_DROWNED.get()){
            handleVariantReplacement(mobSpawnInfoBuilder, EntityType.DROWNED, ModEntityTypes.ARMORED_DROWNED.get(), 0.9);
        }
        if(DungeonsMobsConfig.COMMON.ENABLE_ARMORED_SUNKEN_SKELETON_REPLACES_SUNKEN_SKELETON.get()){
            handleVariantReplacement(mobSpawnInfoBuilder, ModEntityTypes.SUNKEN_SKELETON.get(), ModEntityTypes.ARMORED_SUNKEN_SKELETON.get(), 0.9);
        }
    }

    private static void tryAddMonsterSpawnToBiome(RegistryKey<Biome> biomeRegistryKey, MobSpawnInfoBuilder mobSpawnInfoBuilder, List<String> configuredBiomeTypes, EntityType entityType, int spawnWeight, int minGroupSize, int maxGroupSize) {
        if(configuredBiomeTypes.isEmpty()) return;

        List<String> disallowedBiomeTypes = new ArrayList<>();
        for(String configuredBiomeType : configuredBiomeTypes){
            if(configuredBiomeType.startsWith("!") && configuredBiomeType.length() > 1){
                disallowedBiomeTypes.add(configuredBiomeType.substring(1));
            }
        }
        Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biomeRegistryKey);
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
            MobSpawnInfo.Spawners monsterSpawnEntry = new MobSpawnInfo.Spawners(entityType, spawnWeight, minGroupSize, maxGroupSize);
            mobSpawnInfoBuilder.getSpawner(EntityClassification.MONSTER).add(monsterSpawnEntry);
        }
    }

    private static void handleVariantReplacementWithCategoryCheck(MobSpawnInfoBuilder mobSpawnInfoBuilder, Biome.Category foundCategory, Biome.Category requiredCategory, EntityType typeToReplace, EntityType typeReplaceBy, double retainAmount) {
        if(foundCategory == requiredCategory){
            handleVariantReplacement(mobSpawnInfoBuilder, typeToReplace, typeReplaceBy, retainAmount);
        }
    }

    private static void handleVariantReplacement(MobSpawnInfoBuilder mobSpawnInfoBuilder, EntityType typeToReplace, EntityType typeReplaceBy, double retainAmount) {
        retainAmount = MathHelper.clamp(retainAmount, 0 , 1);
        double replaceAmount = MathHelper.clamp(1 - retainAmount, 0 , 1);

        List<MobSpawnInfo.Spawners> monsterSpawnList = mobSpawnInfoBuilder.getSpawner(EntityClassification.MONSTER);
        for(int i = 0; i < monsterSpawnList.size(); i++) {
            MobSpawnInfo.Spawners spawnListEntry = monsterSpawnList.get(i);
            int weight = spawnListEntry.weight;
            int minGroupCount = spawnListEntry.minCount;
            int maxGroupCount = spawnListEntry.maxCount;
            EntityType<?> entityType = spawnListEntry.type;
            if (entityType == typeToReplace) {
                MobSpawnInfo.SpawnCosts spawnCosts = mobSpawnInfoBuilder.getCost(typeToReplace);
                if(spawnCosts != null){
                    double maxSpawnCost = spawnCosts.getEnergyBudget();
                    double entitySpawnCost = spawnCosts.getCharge();
                    mobSpawnInfoBuilder.addMobCharge(typeToReplace, maxSpawnCost * retainAmount, entitySpawnCost * retainAmount);
                    mobSpawnInfoBuilder.addMobCharge(typeReplaceBy, maxSpawnCost * replaceAmount, entitySpawnCost * replaceAmount);
                }
                MobSpawnInfo.Spawners typeReplaceByEntry = new MobSpawnInfo.Spawners(typeReplaceBy, (int) (weight * replaceAmount), minGroupCount, maxGroupCount);
                MobSpawnInfo.Spawners typeToReplaceEntry = new MobSpawnInfo.Spawners(typeToReplace, (int) (weight * retainAmount), minGroupCount, maxGroupCount);
                monsterSpawnList.set(i, typeToReplaceEntry);
                monsterSpawnList.add(typeReplaceByEntry);
            }
        }
    }
}
