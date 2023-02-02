package com.infamous.dungeons_mobs.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class DungeonsMobsConfig {

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_WRAITH_FIRE_SUMMON;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ICY_CREEPER_GRIEFING;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_MOB_ARMOR_DROPS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_MOB_HELD_ITEM_DROPS;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> NECROMANCER_MOB_SUMMONS;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> DROWNED_NECROMANCER_MOB_SUMMONS;

        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_FROZEN_ZOMBIE_REPLACES_ZOMBIE;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ICY_CREEPER_REPLACES_CREEPER;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JUNGLE_ZOMBIE_REPLACES_ZOMBIE;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_MOSSY_SKELETON_REPLACES_SKELETON;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_3D_SLEEVES;

        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_OUTPOST_VANILLA_VINDICATOR;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_OUTPOST_VANILLA_EVOKER;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_OUTPOST_DUNGEONS_MOBS_MELEE;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_OUTPOST_DUNGEONS_MOBS_CASTER;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_MANSION_VANILLA;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_MANSION_DUNGEONS_MOBS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BASTION_VANILLA;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BASTION_DUNGEONS_MOBS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_NETHER_FORTRESS_DUNGEONS_MOBS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_DESERT_PYRAMID_DUNGEONS_MOBS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_JUNGLE_TEMPLE_DUNGEONS_MOBS;

        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BIOME_SPECIFIC_RAIDERS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_MOUNTAINEERS_IN_RAIDS;
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

        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_STRONGER_HUSKS;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_RANGED_SPIDERS;

        public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ITEM_TAB;

        public Common(ForgeConfigSpec.Builder builder) {
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
            ENABLE_MOB_ARMOR_DROPS = builder
                    .comment("Enable mobs added/tweaked by Dungeons Mobs to drop their armor when killed. \n" +
                            "If you prefer these mobs to not drop their armor, disable this feature. [true / false]")
                    .define("enableMobArmorDrops", true);
            ENABLE_MOB_HELD_ITEM_DROPS = builder
                    .comment("Enable mobs added/tweaked by Dungeons Mobs mobs to drop their held items when killed. \n" +
                            "If you prefer these mobs to not drop their held items, disable this feature. [true / false]")
                    .define("enableMobHeldItemDrops", true);
            NECROMANCER_MOB_SUMMONS = builder
                    .comment("Add mobs (preferably undead) that the Necromancer can summon. \n"
                            + "To do so, enter the namespace ID of the mob, like \"minecraft:zombie\".\n" +
                            "If this list is empty, zombies will be summoned instead.\n" +
                            "If a mob chosen from this list cannot be spawned, a zombie will be summoned instead.")
                    .defineList("necromancerMobSummons", Lists.newArrayList(
                                    "minecraft:zombie",
                                    "minecraft:skeleton"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            DROWNED_NECROMANCER_MOB_SUMMONS = builder
                    .comment("Add mobs (preferably undead and aquatic) that the Drowned Necromancer can summon. \n"
                            + "To do so, enter the namespace ID of the mob, like \"minecraft:drowned\".\n" +
                            "If this list is empty, drowned will be summoned instead.\n" +
                            "If a mob chosen from this list cannot be spawned, a drowned will be summoned instead.")
                    .defineList("drownedNecromancerMobSummons", Lists.newArrayList(
                                    "minecraft:drowned",
                                    "dungeons_mobs:sunken_skeleton"
                            ),
                            (itemRaw) -> itemRaw instanceof String);

            builder.pop();

            builder.comment("Texture Configuration").push("texture_configuration");
            ENABLE_3D_SLEEVES = builder
                    .comment("Enable 3D sleeves on the various Illagers. \n" +
                            "Can be combined with the  resource pack to \n" +
                            "If you prefer a more vanilla look, disable this feature. [true / false]")
                    .define("enable3DSleeves", true);
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

            builder.pop();

            //STRUCTURE SPAWN CONFIGURATION
            builder.comment("Structure Spawn Configuration").push("structure_spawn_configuration");
            ENABLE_OUTPOST_VANILLA_VINDICATOR = builder
                    .comment("Enables logic for allowing vindicators to spawn in Outposts. Defaults to true [true / false]")
                    .define("enableOutpostVanillaVindicator", true);
            ENABLE_OUTPOST_VANILLA_EVOKER = builder
                    .comment("Enables logic for allowing evokers to spawn in Outposts. Defaults to false [true / false]")
                    .define("enableOutpostVanillaEvoker", false);
            ENABLE_OUTPOST_DUNGEONS_MOBS_MELEE = builder
                    .comment("Enables logic for allowing melee Dungeons Mobs to spawn in Outposts. Defaults to true [true / false]")
                    .define("enableOutpostDungeonsMobsMelee", true);
            ENABLE_OUTPOST_DUNGEONS_MOBS_CASTER = builder
                    .comment("Enables logic for allowing caster Dungeons Mobs to spawn in Outposts. Defaults to false [true / false]")
                    .define("enableOutpostDungeonsMobsCaster", false);
            ENABLE_MANSION_VANILLA = builder
                    .comment("Enables logic for allowing vanilla mobs to spawn in Mansions. Defaults to true [true / false]")
                    .define("enableMansionVanilla", true);
            ENABLE_MANSION_DUNGEONS_MOBS = builder
                    .comment("Enables logic for allowing Dungeons Mobs to spawn in Mansions. Defaults to true [true / false]")
                    .define("enableMansionDungeonsMobs", true);
            ENABLE_BASTION_VANILLA = builder
                    .comment("Enables logic for allowing vanilla mobs to spawn in Bastions. Defaults to true [true / false]")
                    .define("enableBastionVanilla", true);
            ENABLE_BASTION_DUNGEONS_MOBS = builder
                    .comment("Enables logic for allowing Dungeons Mobs to spawn in Bastions. Defaults to true [true / false]")
                    .define("enableBastionDungeonsMobs", true);
            ENABLE_NETHER_FORTRESS_DUNGEONS_MOBS = builder
                    .comment("Enables logic for allowing Dungeons Mobs to spawn in Nether Fortresses. Defaults to true [true / false]")
                    .define("enableNetherFortressDungeonsMobs", true);
            ENABLE_DESERT_PYRAMID_DUNGEONS_MOBS = builder
                    .comment("Enables logic for allowing Dungeons Mobs to spawn in Desert Pyramids. Defaults to true [true / false]")
                    .define("enableDesertPyramidDungeonsMobs", true);
            ENABLE_JUNGLE_TEMPLE_DUNGEONS_MOBS = builder
                    .comment("Enables logic for allowing Dungeons Mobs to spawn in Jungle Temples. Defaults to true [true / false]")
                    .define("enableJungleTempleDungeonsMobs", true);

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

            ENABLE_MOUNTAINEERS_IN_RAIDS = builder
                    .comment("Enable the addition of Mountaineers to raids. [true / false]")
                    .define("enableMountaineersInRaids", false);
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

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}