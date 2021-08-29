package com.infamous.dungeons_mobs.worldgen;

import com.google.common.collect.ImmutableMap;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;

import java.util.HashMap;
import java.util.Map;

public class SensorMapModifier {


    public static void replaceSensorMaps(){
        ImmutableMap<EntityType<?>, Float> oldImmutableVillagerHostiles = VillagerHostilesSensor.ACCEPTABLE_DISTANCE_FROM_HOSTILES;
        Map<EntityType<?>, Float> villagerHostiles = new HashMap<>(oldImmutableVillagerHostiles);

        // only use for Zombies and Raiders
        villagerHostiles.put(ModEntityTypes.ARMORED_PILLAGER.get(), 15.0F);

        villagerHostiles.put(ModEntityTypes.ICEOLOGER.get(), 12.0F);
        villagerHostiles.put(ModEntityTypes.ILLUSIONER_CLONE.get(), 12.0F);
        villagerHostiles.put(ModEntityTypes.WINDCALLER.get(), 12.0F);
        villagerHostiles.put(ModEntityTypes.GEOMANCER.get(), 12.0F);

        villagerHostiles.put(ModEntityTypes.REDSTONE_GOLEM.get(), 12.0F);
        villagerHostiles.put(ModEntityTypes.SQUALL_GOLEM.get(), 12.0F);

        villagerHostiles.put(ModEntityTypes.VINDICATOR_CHEF.get(), 10.0F);
        villagerHostiles.put(ModEntityTypes.ARMORED_VINDICATOR.get(), 10.0F);
        villagerHostiles.put(ModEntityTypes.ROYAL_GUARD.get(), 10.0F);
        villagerHostiles.put(ModEntityTypes.MOUNTAINEER.get(), 10.0F);
        villagerHostiles.put(ModEntityTypes.ARMORED_MOUNTAINEER.get(), 10.0F);

        villagerHostiles.put(ModEntityTypes.ARMORED_ZOMBIE.get(), 8.0F);
        villagerHostiles.put(ModEntityTypes.FROZEN_ZOMBIE.get(), 8.0F);
        villagerHostiles.put(ModEntityTypes.JUNGLE_ZOMBIE.get(), 8.0F);

        VillagerHostilesSensor.ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.copyOf(villagerHostiles);
    }
}
