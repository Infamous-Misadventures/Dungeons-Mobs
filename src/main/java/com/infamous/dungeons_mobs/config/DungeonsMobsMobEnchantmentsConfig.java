package com.infamous.dungeons_mobs.config;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class DungeonsMobsMobEnchantmentsConfig {
    public static class MobEnchantmentsConfig {
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ENCHANTS_ON_SPAWN;
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ENCHANTS_ON_PASSIVES;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ENCHANT_ON_SPAWN_EXCLUSION_ENCHANTMENTS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ENCHANT_ON_SPAWN_EXCLUSION_MOBS;
        public final ForgeConfigSpec.ConfigValue<Double> ENCHANT_ON_SPAWN_CHANCE;

        public MobEnchantmentsConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Mob Enchantments Configuration").push("mob_enchantment_configuration");
            builder.comment("Enchantments on Spawn Configuration").push("spawn_mob_enchantment_configuration");
            ENABLE_ENCHANTS_ON_SPAWN = builder
                    .comment("Enable the the spawning of enchanted mobs. [true / false]")
                    .define("enableEnchantedMobs", true);
            ENABLE_ENCHANTS_ON_PASSIVES = builder
                    .comment("Enable the the spawning of enchanted passive mobs. [true / false]")
                    .define("enableEnchantedPassiveMobs", true);
            ENCHANT_ON_SPAWN_EXCLUSION_ENCHANTMENTS = builder
                    .comment("Disables specific mob enchantments from appearing on mobs. Use the full name, eg: dungeons_mobs:protection. Defaults to empty list")
                    .defineList("enchantOnSpawnExclusionEnchantments", Lists.newArrayList(), o -> o instanceof String);
            ENCHANT_ON_SPAWN_EXCLUSION_MOBS = builder
                    .comment("Disables specific mob from receiveing enchantments on spawn. Use the full name, eg: dungeons_mobs:protection. Defaults to bosses.")
                    .defineList("enchantOnSpawnExclusionMobs", Lists.newArrayList("minecraft:wither", "minecraft:ender_dragon"), o -> o instanceof String);
            ENCHANT_ON_SPAWN_CHANCE = builder
                    .comment("Chance for a mob to spawn with enchantments. Is multiplied with the difficulty level (1 - 3). Defaults to 0.015.")
                    .defineInRange("enchantOnSpawnChance", 0.015D, 0.0D, 1.0D);
            builder.pop();
        }
    }
}
