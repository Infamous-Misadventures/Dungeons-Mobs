package com.infamous.dungeons_mobs.worldgen;

import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.jungle.VineEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.JungleZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.MossySkeletonEntity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.Heightmap;

import java.util.Random;

public class EntitySpawnPlacements {

    public static void initSpawnPlacements(){
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ARMORED_ZOMBIE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MonsterEntity::checkMonsterSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ARMORED_SKELETON.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MonsterEntity::checkMonsterSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.WRAITH.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MonsterEntity::checkMonsterSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.FROZEN_ZOMBIE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                FrozenZombieEntity::canFrozenZombieSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ICY_CREEPER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                IcyCreeperEntity::canIcyCreeperSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.JUNGLE_ZOMBIE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                JungleZombieEntity::canJungleZombieSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.MOSSY_SKELETON.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MossySkeletonEntity::canMossySkeletonSpawn);


        EntitySpawnPlacementRegistry.register(ModEntityTypes.ARMORED_VINDICATOR.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ARMORED_PILLAGER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                PatrollerEntity::checkPatrollingMonsterSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.SKELETON_VANGUARD.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MonsterEntity::checkMonsterSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.NECROMANCER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MonsterEntity::checkMonsterSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ROYAL_GUARD.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.VINDICATOR_CHEF.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.MOUNTAINEER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ARMORED_MOUNTAINEER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.GEOMANCER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ICEOLOGER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ILLUSIONER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ILLUSIONER_CLONE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.WINDCALLER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);

        EntitySpawnPlacementRegistry.register(ModEntityTypes.SQUALL_GOLEM.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canRaiderSpawn);

        EntitySpawnPlacementRegistry.register(ModEntityTypes.REDSTONE_GOLEM.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MonsterEntity::checkMonsterSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.REDSTONE_CUBE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MonsterEntity::checkMonsterSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.CONJURED_SLIME.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MobEntity::checkMobSpawnRules);


        EntitySpawnPlacementRegistry.register(ModEntityTypes.WHISPERER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canJungleMobSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.LEAPLEAF.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canJungleMobSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.QUICK_GROWING_VINE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                VineEntity::canVineSpawnInLight);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.POISON_QUILL_VINE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                VineEntity::canVineSpawnInLight);

    }

    public static boolean canJungleMobSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason spawnReason, BlockPos blockPos, Random rand) {
        return MonsterEntity.checkMonsterSpawnRules(entityType, world, spawnReason, blockPos, rand)
                && (spawnReason == SpawnReason.SPAWNER || world.canSeeSky(blockPos));
    }

    public static boolean canIllagerSpawn(EntityType<? extends AbstractIllagerEntity> entityType, IServerWorld world, SpawnReason spawnReason, BlockPos blockPos, Random rand) {
        return MonsterEntity.checkMonsterSpawnRules(entityType, world, spawnReason, blockPos, rand)
                && (spawnReason == SpawnReason.SPAWNER || world.canSeeSky(blockPos));
    }

    public static boolean canRaiderSpawn(EntityType<? extends AbstractRaiderEntity> entityType, IServerWorld world, SpawnReason spawnReason, BlockPos blockPos, Random rand) {
        return MonsterEntity.checkMonsterSpawnRules(entityType, world, spawnReason, blockPos, rand)
                && (spawnReason == SpawnReason.SPAWNER || world.canSeeSky(blockPos));
    }

}
