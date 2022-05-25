package com.infamous.dungeons_mobs.config;

import com.google.common.collect.Lists;
import cpw.mods.modlauncher.LaunchPluginHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class DungeonsMobsConfig {

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_WRAITH_FIRE_SUMMON;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ICY_CREEPER_GRIEFING;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> NECROMANCER_MOB_SUMMONS;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> DROWNED_NECROMANCER_MOB_SUMMONS;

        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_ZOMBIE_REPLACES_ZOMBIE;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_SKELETON_REPLACES_SKELETON;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_MOUNTAINEER_REPLACES_MOUNTAINEER;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_VINDICATOR_REPLACES_VINDICATOR;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_PILLAGER_REPLACES_PILLAGER;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_FROZEN_ZOMBIE_REPLACES_ZOMBIE;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ICY_CREEPER_REPLACES_CREEPER;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JUNGLE_ZOMBIE_REPLACES_ZOMBIE;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_MOSSY_SKELETON_REPLACES_SKELETON;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_PIGLIN_REPLACES_PIGLIN;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ZOMBIFIED_ARMORED_PIGLIN_REPLACES_ZOMBIFIED_PIGLIN;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_DROWNED_REPLACES_DROWNED;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_SUNKEN_SKELETON_REPLACES_SUNKEN_SKELETON;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ICEOLOGER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> ICEOLOGER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> ICEOLOGER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> ICEOLOGER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> WRAITH_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> WRAITH_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> WRAITH_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> WRAITH_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> NECROMANCER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> NECROMANCER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> NECROMANCER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> NECROMANCER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> SKELETON_HORSEMAN_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> SKELETON_HORSEMAN_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> SKELETON_HORSEMAN_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> SKELETON_HORSEMAN_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> MOUNTAINEER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> MOUNTAINEER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> MOUNTAINEER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> MOUNTAINEER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> WINDCALLER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> WINDCALLER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> WINDCALLER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> WINDCALLER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> GEOMANCER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> GEOMANCER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> GEOMANCER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> GEOMANCER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ILLUSIONER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> ILLUSIONER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> ILLUSIONER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> ILLUSIONER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> VINDICATOR_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> VINDICATOR_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> VINDICATOR_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> VINDICATOR_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> EVOKER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> EVOKER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> EVOKER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> EVOKER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> PILLAGER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> PILLAGER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> PILLAGER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> PILLAGER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> WHISPERER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> WHISPERER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> WHISPERER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> WHISPERER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> LEAPLEAF_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> LEAPLEAF_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> LEAPLEAF_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> LEAPLEAF_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> SQUALL_GOLEM_BIOME_TYPES;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> QUICK_GROWING_VINE_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> QUICK_GROWING_VINE_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> QUICK_GROWING_VINE_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> QUICK_GROWING_VINE_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> POISON_QUILL_VINE_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> POISON_QUILL_VINE_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> POISON_QUILL_VINE_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> POISON_QUILL_VINE_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> FUNGUS_THROWER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> FUNGUS_THROWER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> FUNGUS_THROWER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> FUNGUS_THROWER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ZOMBIFIED_FUNGUS_THROWER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> ZOMBIFIED_FUNGUS_THROWER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> ZOMBIFIED_FUNGUS_THROWER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> ZOMBIFIED_FUNGUS_THROWER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> WITHER_SKELETON_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> WITHER_SKELETON_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> WITHER_SKELETON_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> WITHER_SKELETON_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> SUNKEN_SKELETON_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> SUNKEN_SKELETON_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> SUNKEN_SKELETON_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> SUNKEN_SKELETON_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> DROWNED_NECROMANCER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> DROWNED_NECROMANCER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> DROWNED_NECROMANCER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> DROWNED_NECROMANCER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> WAVEWHISPERER_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> WAVEWHISPERER_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> WAVEWHISPERER_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> WAVEWHISPERER_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> QUICK_GROWING_ANEMONE_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> QUICK_GROWING_ANEMONE_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> QUICK_GROWING_ANEMONE_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> QUICK_GROWING_ANEMONE_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> POISON_ANEMONE_BIOME_TYPES;
        public final ForgeConfigSpec.ConfigValue<Integer> POISON_ANEMONE_SPAWN_WEIGHT;
        public final ForgeConfigSpec.ConfigValue<Integer> POISON_ANEMONE_MIN_GROUP_SIZE;
        public final ForgeConfigSpec.ConfigValue<Integer> POISON_ANEMONE_MAX_GROUP_SIZE;

        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BIOME_SPECIFIC_RAIDERS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_MOUNTAINEERS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_VINDICATOR_CHEFS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_VINDICATORS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_MOUNTAINEERS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ROYAL_GUARDS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_GEOMANCERS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ILLUSIONERS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_MAGES_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ICEOLOGERS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_WINDCALLERS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_PILLAGERS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SQUALL_GOLEMS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_REDSTONE_GOLEMS_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_TOWER_GUARD_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_HEARY_ARMORED_GUARD_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ILLAGER_WARRIOR_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_VINDICATOR_RAID_CAPTAIN_IN_RAIDS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_RAMPART_CAPTAIN_IN_RAIDS;

        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_STRONGER_HUSKS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_RANGED_SPIDERS;

        public Common(ForgeConfigSpec.Builder builder){
            // MOB CONFIGURATION
            builder.comment("Mob Configuration").push("mob_configuration");
            ENABLE_WRAITH_FIRE_SUMMON = builder
                    .comment("Enable the default ability of Wraiths to summon Wraith Fire around the player as an attack. \n" +
                            "If you prefer a less griefy attack using Soul Fireballs akin to the Blaze, disable this feature. [true / false]")
                    .define("enableWraithFireSummon", true);
            ENABLE_ICY_CREEPER_GRIEFING = builder
                    .comment("Enable the default ability of Icy Creeper Explosions to grief the environment. \n" +
                            "If you prefer their explosions to not damage the environment, disable this feature. [true / false]")
                    .define("enablyIcyCreeperGriefing", true);
            NECROMANCER_MOB_SUMMONS = builder
                    .comment("Add mobs (preferably undead) that the Necromancer can summon. \n"
                            + "To do so, enter the namespace ID of the mob, like \"minecraft:zombie\".\n" +
                            "If this list is empty, zombies will be summoned instead.\n" +
                            "If a mob chosen from this list cannot be spawned, a zombie will be summoned instead." )
                    .defineList("necromancerMobSummons", Lists.newArrayList(
                            "minecraft:zombie",
                            "minecraft:skeleton",
                            "dungeons_mobs:armored_zombie",
                            "dungeons_mobs:armored_skeleton",
                            "dungeons_mobs:wraith"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            DROWNED_NECROMANCER_MOB_SUMMONS = builder
                    .comment("Add mobs (preferably undead and aquatic) that the Drowned Necromancer can summon. \n"
                            + "To do so, enter the namespace ID of the mob, like \"minecraft:drowned\".\n" +
                            "If this list is empty, drowned will be summoned instead.\n" +
                            "If a mob chosen from this list cannot be spawned, a drowned will be summoned instead." )
                    .defineList("drownedNecromancerMobSummons", Lists.newArrayList(
                            "minecraft:drowned",
                            "dungeons_mobs:armored_drowned"
                            ),
                            (itemRaw) -> itemRaw instanceof String);

            builder.pop();

            // SPAWN CONFIGURATION
            builder.comment("Spawn Configuration").push("spawn_configuration");
            ENABLE_FROZEN_ZOMBIE_REPLACES_ZOMBIE = builder
                    .comment("Enable Frozen Zombies replacing 80% of  Zombie spawns in ICY biomes. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableFrozenZombieReplacesZombie", true);
            ENABLE_ICY_CREEPER_REPLACES_CREEPER = builder
                    .comment("Enable Icy Creepers replacing 80% of Creeper spawns in ICY biomes. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableIcyCreeperReplacesCreeper", true);
            ENABLE_JUNGLE_ZOMBIE_REPLACES_ZOMBIE = builder
                    .comment("Enable Jungle Zombies replacing 80% of Zombie spawns in JUNGLE biomes. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableJungleZombieReplacesZombie", true);
            ENABLE_MOSSY_SKELETON_REPLACES_SKELETON = builder
                    .comment("Enable Mossy Skeletons  80% of Skeleton spawns in JUNGLE biomes. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableMossySkeletonReplacesSkeleton", true);
            ENABLE_ARMORED_ZOMBIE_REPLACES_ZOMBIE = builder
                    .comment("Enable Armored Zombies replacing 10% of Zombie spawns in the biomes they can spawn in. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableArmoredZombieReplacesZombie", true);
            ENABLE_ARMORED_SKELETON_REPLACES_SKELETON = builder
                    .comment("Enable Armored Skeletons replacing 10% of Skeleton spawns in the biomes they can spawn in. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableArmoredSkeletonReplacesSkeleton", true);
            ENABLE_ARMORED_MOUNTAINEER_REPLACES_MOUNTAINEER = builder
                    .comment("Enable Armored Mountaineers replacing 10% of Mountaineer spawns in the biomes they can spawn in. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableArmoredMountaineerReplacesMountaineer", true);
            ENABLE_ARMORED_VINDICATOR_REPLACES_VINDICATOR = builder
                    .comment("Enable Armored Vindicators replacing 10% of Vindicator spawns in the biomes they can spawn in. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableArmoredVindicatorReplacesVindicator", true);
            ENABLE_ARMORED_PILLAGER_REPLACES_PILLAGER = builder
                    .comment("Enable Armored Pillagers replacing 10% of Pillager spawns in the biomes they can spawn in. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableArmoredPillagerReplacesPillager", true);
            ENABLE_ARMORED_PIGLIN_REPLACES_PIGLIN = builder
                    .comment("Enable Armored Piglins replacing 10% of Piglins spawns in the biomes they can spawn in. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableArmoredPiglinReplacesPiglin", true);
            ENABLE_ZOMBIFIED_ARMORED_PIGLIN_REPLACES_ZOMBIFIED_PIGLIN = builder
                    .comment("Enable Zombified Armored Piglins replacing 10% of Zombified Piglin spawns in the biomes they can spawn in. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enabledArmoredZombifiedPiglinReplacesZombifiedPiglin", true);

            ENABLE_ARMORED_DROWNED_REPLACES_DROWNED = builder
                    .comment("Enable Armored Drowned replacing 10% of Drowned spawns in the biomes they can spawn in. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enabledArmoredDrownedReplacesDrowned", true);

            ENABLE_ARMORED_SUNKEN_SKELETON_REPLACES_SUNKEN_SKELETON = builder
                    .comment("Enable Armored Sunken Skeletons replacing 10% of Sunken Skeleton spawns in the biomes they can spawn in. \n" +
                            "If you prefer to not have them do this, disable this feature. [true / false]")
                    .define("enableArmoredSunkenSkeletonReplacesSunkenSkeleton", true);

            ICEOLOGER_BIOME_TYPES = builder
                    .comment("Add biome types that the Iceologer can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                                + "Leave this blank if you don't want them to spawn at all.")
                            .defineList("iceologerBiomeTypes", Lists.newArrayList(
                                    "SNOWY",
                                    "!MUSHROOM",
                                    "!NETHER",
                                    "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            ICEOLOGER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Iceologers in the biomes they can spawn in. [default:5]")
                    .defineInRange("iceologerSpawnWeight", 5, 0, 1024);
            ICEOLOGER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Iceologers in the biomes they can spawn in. [default:1]")
                    .defineInRange("iceologerMinGroupSize", 1, 0, 128);
            ICEOLOGER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Iceologers in the biomes they can spawn in. [default:1]")
                    .defineInRange("iceologerMaxGroupSize", 1, 0, 128);

            WRAITH_BIOME_TYPES = builder
                    .comment("Add biome types that the Wraith can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("wraithBiomeTypes", Lists.newArrayList(
                            "OVERWORLD",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            WRAITH_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Wraiths in the biomes they can spawn in. [default:5]")
                    .defineInRange("wraithSpawnWeight", 5, 0, 1024);
            WRAITH_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Wraiths in the biomes they can spawn in. [default:1]")
                    .defineInRange("wraithMinGroupSize", 1, 0, 128);
            WRAITH_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Wraiths in the biomes they can spawn in. [default:1]")
                    .defineInRange("wraithMaxGroupSize", 1, 0, 128);

            NECROMANCER_BIOME_TYPES = builder
                    .comment("Add biome types that the Necromancer can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("necromancerBiomeTypes", Lists.newArrayList(
                            "SANDY",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            NECROMANCER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Necromancers in the biomes they can spawn in. [default:5]")
                    .defineInRange("necromancerSpawnWeight", 5, 0, 1024);
            NECROMANCER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Necromancers in the biomes they can spawn in. [default:1]")
                    .defineInRange("necromancerMinGroupSize", 1, 0, 128);
            NECROMANCER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Necromancers in the biomes they can spawn in. [default:1]")
                    .defineInRange("necromancerMaxGroupSize", 1, 0, 128);

            SKELETON_HORSEMAN_BIOME_TYPES = builder
                    .comment("Add biome types that the Skeleton Horseman can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("skeletonHorsemanBiomeTypes", Lists.newArrayList(
                            "OVERWORLD",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            SKELETON_HORSEMAN_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Skeleton Horsemen in the biomes they can spawn in. [default:1]")
                    .defineInRange("skeletonHorsemanSpawnWeight", 1, 0, 1024);
            SKELETON_HORSEMAN_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Skeleton Horsemen in the biomes they can spawn in. [default:1]")
                    .defineInRange("skeletonHorsemanMinGroupSize", 1, 0, 32);
            SKELETON_HORSEMAN_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Skeleton Horsemen in the biomes they can spawn in. [default:1]")
                    .defineInRange("skeletonHorsemanMaxGroupSize", 1, 0, 32);

            MOUNTAINEER_BIOME_TYPES = builder
                    .comment("Add biome types that the Mountaineer can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("mountaineerBiomeTypes", Lists.newArrayList(
                            "MOUNTAIN",
                            "!SANDY",
                            "!JUNGLE",
                            "!FOREST",
                            "!MUSHROOM",
                            "!HOT",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            MOUNTAINEER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Mountaineers in the biomes they can spawn in. [default:5]")
                    .defineInRange("mountaineerSpawnWeight", 5, 0, 1024);
            MOUNTAINEER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Mountaineers in the biomes they can spawn in. [default:1]")
                    .defineInRange("mountaineerMinGroupSize", 1, 0, 32);
            MOUNTAINEER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Mountaineers in the biomes they can spawn in. [default:4]")
                    .defineInRange("mountaineerMaxGroupSize", 4, 0, 32);

            WINDCALLER_BIOME_TYPES = builder
                    .comment("Add biome types that the Windcaller can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("windcallerBiomeTypes", Lists.newArrayList(
                            "MOUNTAIN",
                            "!SANDY",
                            "!JUNGLE",
                            "!FOREST",
                            "!MUSHROOM",
                            "!HOT",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            WINDCALLER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Windcallers in the biomes they can spawn in. [default:5]")
                    .defineInRange("windcallerSpawnWeight", 5, 0, 1024);
            WINDCALLER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Windcallers in the biomes they can spawn in. [default:1]")
                    .defineInRange("windcallerMinGroupSize", 1, 0, 128);
            WINDCALLER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Windcallers in the biomes they can spawn in. [default:1]")
                    .defineInRange("windcallerMaxGroupSize", 1, 0, 128);

            GEOMANCER_BIOME_TYPES = builder
                    .comment("Add biome types that the Geomancer can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("geomancerBiomeTypes", Lists.newArrayList(
                            "SAVANNA",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            GEOMANCER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Geomancers in the biomes they can spawn in. [default:5]")
                    .defineInRange("geomancerSpawnWeight", 5, 0, 1024);
            GEOMANCER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Geomancers in the biomes they can spawn in. [default:1]")
                    .defineInRange("geomancerMinGroupSize", 1, 0, 128);
            GEOMANCER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Geomancers in the biomes they can spawn in. [default:1]")
                    .defineInRange("geomancerMaxGroupSize", 1, 0, 128);

            ILLUSIONER_BIOME_TYPES = builder
                    .comment("Add biome types that the Illusioner can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("illusionerBiomeTypes", Lists.newArrayList(
                            "SNOWY",
                            "MOUNTAIN",
                            "!SANDY",
                            "!JUNGLE",
                            "!FOREST",
                            "!HOT",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            ILLUSIONER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Illusioners in the biomes they can spawn in. [default:1]")
                    .defineInRange("illusionerSpawnWeight", 1, 0, 1024);
            ILLUSIONER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Illusioners in the biomes they can spawn in. [default:1]")
                    .defineInRange("illusionerMinGroupSize", 1, 0, 128);
            ILLUSIONER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Illusioners in the biomes they can spawn in. [default:1]")
                    .defineInRange("illusionerMaxGroupSize", 1, 0, 128);

            VINDICATOR_BIOME_TYPES = builder
                    .comment("Add biome types that the Vindicator can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("vindicatorBiomeTypes", Lists.newArrayList(
                            "SPOOKY",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            VINDICATOR_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Vindicators in the biomes they can spawn in. [default:5]")
                    .defineInRange("vindicatorSpawnWeight", 5, 0, 1024);
            VINDICATOR_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Vindicators in the biomes they can spawn in. [default:1]")
                    .defineInRange("vindicatorMinGroupSize", 1, 0, 128);
            VINDICATOR_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Vindicators in the biomes they can spawn in. [default:4]")
                    .defineInRange("vindicatorMaxGroupSize", 4, 0, 128);

            EVOKER_BIOME_TYPES = builder
                    .comment("Add biome types that the Evoker can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("evokerBiomeTypes", Lists.newArrayList(
                            "SPOOKY",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            EVOKER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Evokers in the biomes they can spawn in. [default:1]")
                    .defineInRange("evokerSpawnWeight", 1, 0, 1024);
            EVOKER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Evokers in the biomes they can spawn in. [default:1]")
                    .defineInRange("evokerMinGroupSize", 1, 0, 128);
            EVOKER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Evokers in the biomes they can spawn in. [default:1]")
                    .defineInRange("evokerMaxGroupSize", 1, 0, 128);

            PILLAGER_BIOME_TYPES = builder
                    .comment("Add biome types that the Pillager can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("pillagerBiomeTypes", Lists.newArrayList(
                            "SPOOKY",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            PILLAGER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Pillagers in the biomes they can spawn in. [default:5]")
                    .defineInRange("pillagerSpawnWeight", 5, 0, 1024);
            PILLAGER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Pillagers in the biomes they can spawn in. [default:1]")
                    .defineInRange("pillagerMinGroupSize", 1, 0, 128);
            PILLAGER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Pillagers in the biomes they can spawn in. [default:4]")
                    .defineInRange("pillagerMaxGroupSize", 4, 0, 128);

            WHISPERER_BIOME_TYPES = builder
                    .comment("Add biome types that the Whisperer can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("whispererBiomeTypes", Lists.newArrayList(
                            "JUNGLE",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            WHISPERER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Whisperers in the biomes they can spawn in. [default:5]")
                    .defineInRange("whispererSpawnWeight", 5, 0, 1024);
            WHISPERER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Whisperers in the biomes they can spawn in. [default:1]")
                    .defineInRange("whispererMinGroupSize", 1, 0, 128);
            WHISPERER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Whisperers in the biomes they can spawn in. [default:4]")
                    .defineInRange("whispererMaxGroupSize", 1, 0, 128);

            LEAPLEAF_BIOME_TYPES = builder
                    .comment("Add biome types that the Leapleaf can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("leapleafBiomeTypes", Lists.newArrayList(
                            "JUNGLE",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            LEAPLEAF_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Leapleafs in the biomes they can spawn in. [default:5]")
                    .defineInRange("leapleafSpawnWeight", 5, 0, 1024);
            LEAPLEAF_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Leapleafs in the biomes they can spawn in. [default:1]")
                    .defineInRange("leapleafMinGroupSize", 1, 0, 128);
            LEAPLEAF_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Leapleafs in the biomes they can spawn in. [default:4]")
                    .defineInRange("leapleafMaxGroupSize", 4, 0, 128);

            QUICK_GROWING_VINE_BIOME_TYPES = builder
                    .comment("Add biome types that the Quick-Growing Vines can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("quickGrowingVineBiomeTypes", Lists.newArrayList(
                            "JUNGLE",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            QUICK_GROWING_VINE_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Quick-Growing Vines in the biomes they can spawn in. [default:5]")
                    .defineInRange("quickGrowingVineSpawnWeight", 5, 0, 1024);
            QUICK_GROWING_VINE_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Quick-Growing Vines in the biomes they can spawn in. [default:1]")
                    .defineInRange("quickGrowingVineMinGroupSize", 1, 0, 128);
            QUICK_GROWING_VINE_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Quick-Growing Vines in the biomes they can spawn in. [default:1]")
                    .defineInRange("quickGrowingVineMaxGroupSize", 1, 0, 128);

            POISON_QUILL_VINE_BIOME_TYPES = builder
                    .comment("Add biome types that the Poison-Quill Vines can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("poisonQuillVineBiomeTypes", Lists.newArrayList(
                            "JUNGLE",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            POISON_QUILL_VINE_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Poison-Quill Vines in the biomes they can spawn in. [default:5]")
                    .defineInRange("poisonQuillVineSpawnWeight", 5, 0, 1024);
            POISON_QUILL_VINE_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Poison-Quill Vines in the biomes they can spawn in. [default:1]")
                    .defineInRange("poisonQuillVineMinGroupSize", 1, 0, 128);
            POISON_QUILL_VINE_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Poison-Quill Vines in the biomes they can spawn in. [default:1]")
                    .defineInRange("poisonQuillVineMaxGroupSize", 1, 0, 128);

            FUNGUS_THROWER_BIOME_TYPES = builder
                    .comment("Add biome types that the Fungus Thrower can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("fungusThrowerBiomeTypes", Lists.newArrayList(
                            "NETHER",
                            "CRIMSON",
                            "!WARPED",
                            "!SOULSAND",
                            "!DELTA",
                            "!OVERWORLD",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            FUNGUS_THROWER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Fungus Throwers in the biomes they can spawn in. [default:5]")
                    .defineInRange("fungusThrowerSpawnWeight", 5, 0, 1024);
            FUNGUS_THROWER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Fungus Throwers in the biomes they can spawn in. [default:3]")
                    .defineInRange("fungusThrowerMinGroupSize", 3, 0, 128);
            FUNGUS_THROWER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Fungus Throwers in the biomes they can spawn in. [default:4]")
                    .defineInRange("fungusThrowerMaxGroupSize", 4, 0, 128);

            ZOMBIFIED_FUNGUS_THROWER_BIOME_TYPES = builder
                    .comment("Add biome types that the Zombified Fungus Thrower can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("zombifiedFungusThrowerBiomeTypes", Lists.newArrayList(
                            "NETHER",
                            "CRIMSON",
                            "!WARPED",
                            "!SOULSAND",
                            "!DELTA",
                            "!OVERWORLD",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            ZOMBIFIED_FUNGUS_THROWER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Zombified Fungus Throwers in the biomes they can spawn in. [default:5]")
                    .defineInRange("zombifiedFungusThrowerSpawnWeight", 5, 0, 1024);
            ZOMBIFIED_FUNGUS_THROWER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Zombified Fungus Throwers in the biomes they can spawn in. [default:3]")
                    .defineInRange("zombifiedFungusThrowerMinGroupSize", 3, 0, 128);
            ZOMBIFIED_FUNGUS_THROWER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Zombified Fungus Throwers in the biomes they can spawn in. [default:4]")
                    .defineInRange("zombifiedFungusThrowerMaxGroupSize", 4, 0, 128);


            WITHER_SKELETON_BIOME_TYPES = builder
                    .comment("Add biome types that the Wither Skeleton can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("witherSkeletonBiomeTypes", Lists.newArrayList(
                            "NETHER",
                            "SOULSAND",
                            "!CRIMSON",
                            "!WARPED",
                            "!DELTA",
                            "!OVERWORLD",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            WITHER_SKELETON_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Wither Skeletons in the biomes they can spawn in. [default:5]")
                    .defineInRange("witherSkeletonSpawnWeight", 5, 0, 1024);
            WITHER_SKELETON_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Wither Skeletons in the biomes they can spawn in. [default:1]")
                    .defineInRange("witherSkeletonMinGroupSize", 1, 0, 128);
            WITHER_SKELETON_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Wither Skeletons in the biomes they can spawn in. [default:4]")
                    .defineInRange("witherSkeletonMaxGroupSize", 4, 0, 128);

            SUNKEN_SKELETON_BIOME_TYPES = builder
                    .comment("Add biome types that the Sunken Skeleton can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("sunkenSkeletonBiomeTypes", Lists.newArrayList(
                            "WATER",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            SUNKEN_SKELETON_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Sunken Skeletons in the biomes they can spawn in. [default:5]")
                    .defineInRange("sunkenSkeletonSpawnWeight", 5, 0, 1024);
            SUNKEN_SKELETON_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Sunken Skeletons in the biomes they can spawn in. [default:1]")
                    .defineInRange("sunkenSkeletonMinGroupSize", 1, 0, 128);
            SUNKEN_SKELETON_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Sunken Skeletons in the biomes they can spawn in. [default:4]")
                    .defineInRange("sunkenSkeletonMaxGroupSize", 1, 0, 128);

            DROWNED_NECROMANCER_BIOME_TYPES = builder
                    .comment("Add biome types that the Drowned Necromancer can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("drownedNecromancerBiomeTypes", Lists.newArrayList(
                            "OCEAN",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            DROWNED_NECROMANCER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Drowned Necromancers in the biomes they can spawn in. [default:5]")
                    .defineInRange("drownedNecromancerSpawnWeight", 5, 0, 1024);
            DROWNED_NECROMANCER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Drowned Necromancers in the biomes they can spawn in. [default:1]")
                    .defineInRange("drownedNecromancerMinGroupSize", 1, 0, 128);
            DROWNED_NECROMANCER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Drowned Necromancers in the biomes they can spawn in. [default:4]")
                    .defineInRange("drownedNecromancerMaxGroupSize", 1, 0, 128);


            WAVEWHISPERER_BIOME_TYPES = builder
                    .comment("Add biome types that the Wavehisperer can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("wavewhispererBiomeTypes", Lists.newArrayList(
                            "OCEAN",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            WAVEWHISPERER_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Wavehisperers in the biomes they can spawn in. [default:5]")
                    .defineInRange("wavewhispererSpawnWeight", 5, 0, 1024);
            WAVEWHISPERER_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Wavehisperers in the biomes they can spawn in. [default:1]")
                    .defineInRange("wavewhispererMinGroupSize", 1, 0, 128);
            WAVEWHISPERER_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Wavehisperers in the biomes they can spawn in. [default:4]")
                    .defineInRange("wavewhispererMaxGroupSize", 1, 0, 128);

            QUICK_GROWING_ANEMONE_BIOME_TYPES = builder
                    .comment("Add biome types that the Quick-Growing Anemone can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("quickGrowingAnemoneBiomeTypes", Lists.newArrayList(
                            "OCEAN",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            QUICK_GROWING_ANEMONE_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Quick-Growing Anemone in the biomes they can spawn in. [default:5]")
                    .defineInRange("quickGrowingAnemoneSpawnWeight", 5, 0, 1024);
            QUICK_GROWING_ANEMONE_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Quick-Growing Anemone in the biomes they can spawn in. [default:1]")
                    .defineInRange("quickGrowingAnemoneMinGroupSize", 1, 0, 128);
            QUICK_GROWING_ANEMONE_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Quick-Growing Anemone in the biomes they can spawn in. [default:1]")
                    .defineInRange("quickGrowingAnemoneMaxGroupSize", 1, 0, 128);

            POISON_ANEMONE_BIOME_TYPES = builder
                    .comment("Add biome types that the Poison Anemone can spawn in. \n"
                            + "Put a \"!\" before the type to prevent spawning in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn at all.")
                    .defineList("poisonAnemoneBiomeTypes", Lists.newArrayList(
                            "OCEAN",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            POISON_ANEMONE_SPAWN_WEIGHT = builder
                    .comment("Spawn weight of Poison Anemone in the biomes they can spawn in. [default:5]")
                    .defineInRange("poisonAnemoneSpawnWeight", 5, 0, 1024);
            POISON_ANEMONE_MIN_GROUP_SIZE = builder
                    .comment("Minimum spawn group size of Poison Anemone in the biomes they can spawn in. [default:1]")
                    .defineInRange("poisonAnemoneMinGroupSize", 1, 0, 128);
            POISON_ANEMONE_MAX_GROUP_SIZE = builder
                    .comment("Maximum spawn group size of Poison Anemone in the biomes they can spawn in. [default:1]")
                    .defineInRange("poisonAnemoneMaxGroupSize", 1, 0, 128);

            builder.pop();

            // RAID CONFIGURATION
            builder.comment("Raid Configuration").push("raid_configuration");
            ENABLE_BIOME_SPECIFIC_RAIDERS = builder
                    .comment("Enables logic for only allowing certain raiders to spawn as a part of raids in certain biome types.  \n"
                            + "This will make the Mountaineers, Windcaller, Iceologer and Squall Golem only spawn as raiders in their allowed biomes. \n"
                            + "If they are not allowed to spawn as part of a raid in a given biome, \n"
                            + "another equivalent Illager type (Vindicator, Evoker, Ravager) will spawn instead. \n"
                            + "If they are not configured to be added to raids, \n"
                            + "they will replace their equivalent Illager type (Vindicator, Evoker, Ravager) in raids taking place in their allowed biomes. [true / false]")
                    .define("enableBiomeSpecificRaiders", false);


            SQUALL_GOLEM_BIOME_TYPES = builder
                    .comment("Add biome types that the Squall Golem can spawn in (if biome-specific raiding is enabled). \n"
                            + "Put a \"!\" before the type to prevent spawning (in biome-specific raids) in that biome type. \n"
                            + "Leave this blank if you don't want them to spawn (in biome-specific raids) at all.")
                    .defineList("squallGolemBiomeTypes", Lists.newArrayList(
                            "MOUNTAIN",
                            "!SANDY",
                            "!JUNGLE",
                            "!FOREST",
                            "!MUSHROOM",
                            "!HOT",
                            "!MUSHROOM",
                            "!NETHER",
                            "!END"
                            ),
                            (itemRaw) -> itemRaw instanceof String);

            ENABLE_MOUNTAINEERS_IN_RAIDS = builder
                    .comment("Enable the addition of Mountaineers to raids. [true / false]")
                    .define("enableMountaineersInRaids", false);
            ENABLE_VINDICATOR_CHEFS_IN_RAIDS = builder
                    .comment("Enable the addition of Vindicator Chefs to raids. [true / false]")
                    .define("enableVindicatorChefsInRaids", false);
            ENABLE_ARMORED_VINDICATORS_IN_RAIDS = builder
                    .comment("Enable the addition of Armored Vindicators to raids. [true / false]")
                    .define("enableArmoredVindicatorsInRaids", true);
            ENABLE_ARMORED_MOUNTAINEERS_IN_RAIDS = builder
                    .comment("Enable the addition of Armored Mountaineers to raids. [true / false]")
                    .define("enableArmoredMountaineersInRaids", false);
            ENABLE_ROYAL_GUARDS_IN_RAIDS = builder
                    .comment("Enable the addition of Royal Guards to raids. [true / false]")
                    .define("enableRoyalGuardsInRaids", false);
            ENABLE_GEOMANCERS_IN_RAIDS = builder
                    .comment("Enable the addition of Geomancers to raids. [true / false]")
                    .define("enableGeomancersInRaids", true);
            ENABLE_ILLUSIONERS_IN_RAIDS = builder
                    .comment("Enable the addition of Illusioners to raids. [true / false]")
                    .define("enableIllusionersInRaids", false);
            ENABLE_MAGES_IN_RAIDS = builder
                    .comment("Enable the addition of Mages to raids. [true / false]")
                    .define("enableMagesInRaids", false);
            ENABLE_ICEOLOGERS_IN_RAIDS = builder
                    .comment("Enable the addition of Iceologers to raids. [true / false]")
                    .define("enableIceologersInRaids", false);
            ENABLE_WINDCALLERS_IN_RAIDS = builder
                    .comment("Enable the addition of Windcallers to raids. [true / false]")
                    .define("enableWindcallersInRaids", false);
            ENABLE_ARMORED_PILLAGERS_IN_RAIDS = builder
                    .comment("Enable the addition of Armored Pillagers to raids. [true / false]")
                    .define("enableArmoredPillagersInRaids", true);
            ENABLE_SQUALL_GOLEMS_IN_RAIDS = builder
                    .comment("Enable the addition of Squall Golems to raids. [true / false]")
                    .define("enableSquallGolemsInRaids", true);
            ENABLE_REDSTONE_GOLEMS_IN_RAIDS = builder
                    .comment("Enable the addition of Redstone Golems to raids. [true / false]")
                    .define("enableRedstoneGolemsInRaids", true);
            builder.pop();
            ENABLE_ILLAGER_WARRIOR_IN_RAIDS = builder
                    .comment("Enable the addition of Illager Warriors to raids. [true / false]")
                    .define("enableIWInRaids", false);
            builder.pop();
            ENABLE_TOWER_GUARD_IN_RAIDS = builder
                    .comment("Enable the addition of Tower Guard to raids. [true / false]")
                    .define("enableTGInRaids", false);
            builder.pop();
            ENABLE_HEARY_ARMORED_GUARD_IN_RAIDS = builder
                    .comment("Enable the addition of Heavy Armored Guard to raids. [true / false]")
                    .define("enableHAGInRaids", true);
            builder.pop();
            ENABLE_VINDICATOR_RAID_CAPTAIN_IN_RAIDS = builder
                    .comment("Enable the addition of Vindicator raid captain to raids. [true / false]")
                    .define("enableVRCInRaids", true);
            builder.pop();
            ENABLE_RAMPART_CAPTAIN_IN_RAIDS = builder
                    .comment("Enable the addition of Rampart captain to raids. [true / false]")
                    .define("enableRCInRaids", true);
            builder.pop();


            builder.comment("Vanilla Mob Configuration").push("vanilla_mob_configuration");
            ENABLE_STRONGER_HUSKS = builder
                    .comment("Enable the addition of additional attributes to Husks to make them as powerful as they are in Minecraft Dungeons. [true / false]")
                    .define("enableStrongerHusks", true);
            ENABLE_RANGED_SPIDERS = builder
                    .comment("Enables Spiders and Cave Spiders shooting webs as a ranged attack like they do in Minecraft Dungeons. [true / false]")
                    .define("enableRangedSpiders", true);
            builder.pop();

        }
    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    public static final ForgeConfigSpec ENCHANTS_SPEC;
    public static final DungeonsMobsMobEnchantmentsConfig.MobEnchantmentsConfig ENCHANTS;

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
        final Pair<DungeonsMobsMobEnchantmentsConfig.MobEnchantmentsConfig, ForgeConfigSpec> enchantsSpecPair = new ForgeConfigSpec.Builder().configure(DungeonsMobsMobEnchantmentsConfig.MobEnchantmentsConfig::new);
        ENCHANTS_SPEC = enchantsSpecPair.getRight();
        ENCHANTS = enchantsSpecPair.getLeft();
    }
}
