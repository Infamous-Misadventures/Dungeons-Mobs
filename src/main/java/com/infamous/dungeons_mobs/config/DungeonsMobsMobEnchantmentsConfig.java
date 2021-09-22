package com.infamous.dungeons_mobs.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class DungeonsMobsMobEnchantmentsConfig {
    public static class MobEnchantmentsConfig {
        public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ENCHANTED_MOBS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> DISABLED_SPAWN_ENCHANTMENTS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> DISABLED_MOBS_FOR_SPAWN_ENCHANTMENTS;

        public MobEnchantmentsConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Mob Enchantments Configuration").push("mob_enchantment_configuration");
            ENABLE_ENCHANTED_MOBS = builder
                    .comment("Enable the the spawning of enchanted mobs. [true / false]")
                    .define("enableEnchantedMobs", true);
            DISABLED_SPAWN_ENCHANTMENTS = builder
                    .comment("Disables specific mob enchantments from appearing on mobs. Use the full name, eg: dungeons_mobs:protection. Defaults to empty list")
                    .define("disabledSpawnEnchantments", Lists.newArrayList());
            DISABLED_MOBS_FOR_SPAWN_ENCHANTMENTS = builder
                    .comment("Disables specific mob from receiveing enchantments on spawn. Use the full name, eg: dungeons_mobs:protection. Defaults to bosses.")
                    .define("disabledMobsForSpawnEnchantments", Lists.newArrayList("minecraft:wither", "minecraft:ender_dragon"));
            builder.pop();
        }
    }
}
