package com.infamous.dungeons_mobs.worldgen;

import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.piglin.FungusThrowerEntity;
import com.infamous.dungeons_mobs.entities.piglin.ZombifiedFungusThrowerEntity;
import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.JungleZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.MossySkeletonEntity;
import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;

public class EntitySpawnPlacements {

    public static SpawnPlacements.Type IN_WATER_ON_GROUND;

    public static SpawnPlacements.Type ON_GROUND_ALLOW_LEAVES;

    public static void createPlacementTypes() {
        IN_WATER_ON_GROUND = SpawnPlacements.Type.create("in_water_on_ground",
                (iWorldReader, blockPos, entityType) -> {
                    BlockState blockstate = iWorldReader.getBlockState(blockPos);
                    FluidState fluidstate = iWorldReader.getFluidState(blockPos);
                    BlockPos above = blockPos.above();
                    BlockPos below = blockPos.below();
                    BlockState stateAtPos = iWorldReader.getBlockState(blockPos);
                    boolean inWater = fluidstate.is(FluidTags.WATER) && iWorldReader.getFluidState(below).is(FluidTags.WATER) && !iWorldReader.getBlockState(above).isRedstoneConductor(iWorldReader, above);
                    if (!stateAtPos.isValidSpawn(iWorldReader, below, SpawnPlacements.Type.ON_GROUND, entityType)) {
                        return false;
                    } else {
                        boolean validEmptySpawn = isValidEmptySpawnBlockNoFluidCheck(iWorldReader, blockPos, blockstate, entityType) && isValidEmptySpawnBlockNoFluidCheck(iWorldReader, above, iWorldReader.getBlockState(above), entityType);
                        return validEmptySpawn && inWater;
                    }
                });
        ON_GROUND_ALLOW_LEAVES = SpawnPlacements.Type.create("on_ground_allow_leaves",
                ((levelReader, blockPos, entityType) -> {
                    BlockState blockstate = levelReader.getBlockState(blockPos);
                    FluidState fluidstate = levelReader.getFluidState(blockPos);
                    BlockPos above = blockPos.above();
                    BlockPos below = blockPos.below();
                    BlockState stateBelow = levelReader.getBlockState(below);
                    if (!stateBelow.isValidSpawn(levelReader, below, SpawnPlacements.Type.ON_GROUND, entityType) && !(stateBelow.is(BlockTags.LEAVES))) {
                        return false;
                    } else {
                        return NaturalSpawner.isValidEmptySpawnBlock(levelReader, blockPos, blockstate, fluidstate, entityType)
                                && NaturalSpawner.isValidEmptySpawnBlock(levelReader, above, levelReader.getBlockState(above), levelReader.getFluidState(above), entityType);
                    }
                }));
    }

    public static boolean isValidEmptySpawnBlockNoFluidCheck(BlockGetter blockReader, BlockPos blockPos, BlockState blockState, EntityType<?> entityType) {
        if (blockState.isCollisionShapeFullBlock(blockReader, blockPos)) {
            return false;
        } else if (blockState.isSignalSource()) {
            return false;
        } else if (blockState.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)) {
            return false;
        } else {
            return !entityType.isBlockDangerous(blockState);
        }
    }

    public static void initSpawnPlacements() {
        SpawnPlacements.register(ModEntityTypes.WRAITH.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.FROZEN_ZOMBIE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                FrozenZombieEntity::canFrozenZombieSpawn);
        SpawnPlacements.register(ModEntityTypes.ICY_CREEPER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                IcyCreeperEntity::canIcyCreeperSpawn);
        SpawnPlacements.register(ModEntityTypes.JUNGLE_ZOMBIE.get(),
                ON_GROUND_ALLOW_LEAVES,
                Heightmap.Types.MOTION_BLOCKING,
                JungleZombieEntity::canJungleZombieSpawn);
        SpawnPlacements.register(ModEntityTypes.MOSSY_SKELETON.get(),
                ON_GROUND_ALLOW_LEAVES,
                Heightmap.Types.MOTION_BLOCKING,
                MossySkeletonEntity::canMossySkeletonSpawn);


        SpawnPlacements.register(ModEntityTypes.SKELETON_VANGUARD.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.NECROMANCER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.ROYAL_GUARD.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        SpawnPlacements.register(ModEntityTypes.MOUNTAINEER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        SpawnPlacements.register(ModEntityTypes.GEOMANCER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        SpawnPlacements.register(ModEntityTypes.ICEOLOGER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        SpawnPlacements.register(ModEntityTypes.ILLUSIONER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        SpawnPlacements.register(ModEntityTypes.ILLUSIONER_CLONE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);
        SpawnPlacements.register(ModEntityTypes.WINDCALLER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canIllagerSpawn);

        SpawnPlacements.register(ModEntityTypes.SQUALL_GOLEM.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::canRaiderSpawn);

        SpawnPlacements.register(ModEntityTypes.REDSTONE_GOLEM.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.REDSTONE_CUBE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.CONJURED_SLIME.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules);

        // Jungle
        SpawnPlacements.register(ModEntityTypes.WHISPERER.get(),
                ON_GROUND_ALLOW_LEAVES,
                Heightmap.Types.MOTION_BLOCKING,
                EntitySpawnPlacements::canJungleMobSpawn);
        SpawnPlacements.register(ModEntityTypes.LEAPLEAF.get(),
                ON_GROUND_ALLOW_LEAVES,
                Heightmap.Types.MOTION_BLOCKING,
                EntitySpawnPlacements::canJungleMobSpawn);
        // COMMENTED OUT BECAUSE THEY SHOULDN'T SPAWN WITHOUT DUNGEONS WORLD
        /*EntitySpawnPlacementRegistry.register(ModEntityTypes.QUICK_GROWING_VINE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
                AbstractVineEntity::canVineSpawnInLight);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.POISON_QUILL_VINE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
                VineEntity::canVineSpawnInLight);*/

        // Piglin
        SpawnPlacements.register(ModEntityTypes.FUNGUS_THROWER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                FungusThrowerEntity::checkFungusThrowerSpawnRules);

        SpawnPlacements.register(ModEntityTypes.ZOMBIFIED_FUNGUS_THROWER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ZombifiedFungusThrowerEntity::checkZombifiedFungusThrowerSpawnRules);


        SpawnPlacements.register(ModEntityTypes.WILDFIRE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules);


        // Ocean
        SpawnPlacements.register(ModEntityTypes.WAVEWHISPERER.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);
        // COMMENTED OUT BECAUSE THEY SHOULDN'T SPAWN WITHOUT DUNGEONS WORLD
        /*EntitySpawnPlacementRegistry.register(ModEntityTypes.QUICK_GROWING_KELP.get(),
                IN_WATER_ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.POISON_ANEMONE.get(),
                IN_WATER_ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);*/
        SpawnPlacements.register(ModEntityTypes.DROWNED_NECROMANCER.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);
        SpawnPlacements.register(ModEntityTypes.SUNKEN_SKELETON.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);

        //Enderlings
        SpawnPlacements.register(ModEntityTypes.BLASTLING.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.WATCHLING.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.SNARELING.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.ENDERSENT.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules);

    }

    public static boolean checkAquaticMobSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor pServerLevel, MobSpawnType pMobSpawnType, BlockPos pPos, RandomSource pRandom) {
        if (!pServerLevel.getFluidState(pPos.below()).is(FluidTags.WATER)) {
            return false;
        } else {
            Holder<Biome> holder = pServerLevel.getBiome(pPos);
            boolean flag = pServerLevel.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(pServerLevel, pPos, pRandom) && (pMobSpawnType == MobSpawnType.SPAWNER || pServerLevel.getFluidState(pPos).is(FluidTags.WATER));
            if (holder.is(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
                return pRandom.nextInt(15) == 0 && flag;
            } else {
                return pRandom.nextInt(40) == 0 && IAquaticMob.isDeepEnoughToSpawn(pServerLevel, pPos) && flag;
            }
        }
    }

    public static boolean canJungleMobSpawn(EntityType<? extends Monster> entityType, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos blockPos, RandomSource rand) {
        return Monster.checkMonsterSpawnRules(entityType, world, spawnReason, blockPos, rand);
    }

    public static boolean canIllagerSpawn(EntityType<? extends AbstractIllager> entityType, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos blockPos, RandomSource rand) {
        return Monster.checkMonsterSpawnRules(entityType, world, spawnReason, blockPos, rand);
    }

    public static boolean canRaiderSpawn(EntityType<? extends Raider> entityType, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos blockPos, RandomSource rand) {
        return Monster.checkMonsterSpawnRules(entityType, world, spawnReason, blockPos, rand);
    }

    public static boolean canSeeSkyLight(ServerLevelAccessor world, BlockPos blockPos) {
        return world.getBrightness(LightLayer.SKY, blockPos) > 4;
    }

}
