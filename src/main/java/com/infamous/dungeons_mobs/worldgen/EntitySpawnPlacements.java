package com.infamous.dungeons_mobs.worldgen;

import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.infamous.dungeons_mobs.entities.jungle.VineEntity;
import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.JungleZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.MossySkeletonEntity;
import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.Heightmap;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class EntitySpawnPlacements {

    public static EntitySpawnPlacementRegistry.PlacementType IN_WATER_ON_GROUND;

    public static void createPlacementTypes(){
        IN_WATER_ON_GROUND = EntitySpawnPlacementRegistry.PlacementType.create("in_water_on_ground",
                (iWorldReader, blockPos, entityType) -> {
                    BlockState blockstate = iWorldReader.getBlockState(blockPos);
                    FluidState fluidstate = iWorldReader.getFluidState(blockPos);
                    BlockPos above = blockPos.above();
                    BlockPos below = blockPos.below();
                    BlockState stateAtPos = iWorldReader.getBlockState(blockPos);
                    boolean inWater = fluidstate.is(FluidTags.WATER) && iWorldReader.getFluidState(below).is(FluidTags.WATER) && !iWorldReader.getBlockState(above).isRedstoneConductor(iWorldReader, above);
                    if (!stateAtPos.canCreatureSpawn(iWorldReader, below, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, entityType)) {
                        return false;
                    } else {
                        boolean validEmptySpawn = isValidEmptySpawnBlockNoFluidCheck(iWorldReader, blockPos, blockstate, entityType) && isValidEmptySpawnBlockNoFluidCheck(iWorldReader, above, iWorldReader.getBlockState(above), entityType);
                        return validEmptySpawn && inWater;
                    }
                });
    }

    public static boolean isValidEmptySpawnBlockNoFluidCheck(IBlockReader blockReader, BlockPos blockPos, BlockState blockState, EntityType<?> entityType) {
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
                Heightmap.Type.MOTION_BLOCKING,
                JungleZombieEntity::canJungleZombieSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.MOSSY_SKELETON.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
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
        EntitySpawnPlacementRegistry.register(ModEntityTypes.SKELETON_HORSEMAN.get(),
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
                Heightmap.Type.MOTION_BLOCKING,
                EntitySpawnPlacements::canJungleMobSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.LEAPLEAF.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
                EntitySpawnPlacements::canJungleMobSpawn);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.QUICK_GROWING_VINE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
                VineEntity::canVineSpawnInLight);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.POISON_QUILL_VINE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING,
                VineEntity::canVineSpawnInLight);


        EntitySpawnPlacementRegistry.register(ModEntityTypes.ARMORED_PIGLIN.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkPiglinSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.FUNGUS_THROWER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkPiglinSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ZOMBIFIED_ARMORED_PIGLIN.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkZombifiedPiglinSpawnRules);

        EntitySpawnPlacementRegistry.register(ModEntityTypes.ZOMBIFIED_FUNGUS_THROWER.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkZombifiedPiglinSpawnRules);



        EntitySpawnPlacementRegistry.register(ModEntityTypes.WAVEWHISPERER.get(),
                EntitySpawnPlacementRegistry.PlacementType.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.QUICK_GROWING_ANEMONE.get(),
                IN_WATER_ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.POISON_ANEMONE.get(),
                IN_WATER_ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ARMORED_DROWNED.get(),
                EntitySpawnPlacementRegistry.PlacementType.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.DROWNED_NECROMANCER.get(),
                EntitySpawnPlacementRegistry.PlacementType.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.SUNKEN_SKELETON.get(),
                EntitySpawnPlacementRegistry.PlacementType.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);
        EntitySpawnPlacementRegistry.register(ModEntityTypes.ARMORED_SUNKEN_SKELETON.get(),
                EntitySpawnPlacementRegistry.PlacementType.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                EntitySpawnPlacements::checkAquaticMobSpawnRules);

    }

    public static boolean checkAquaticMobSpawnRules(EntityType<? extends MobEntity> type, IServerWorld serverWorld, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        Optional<RegistryKey<Biome>> biomeName = serverWorld.getBiomeName(blockPos);
        boolean canSpawn = serverWorld.getDifficulty() != Difficulty.PEACEFUL && MonsterEntity.isDarkEnoughToSpawn(serverWorld, blockPos, random) && (spawnReason == SpawnReason.SPAWNER || serverWorld.getFluidState(blockPos).is(FluidTags.WATER));
        if (!Objects.equals(biomeName, Optional.of(Biomes.RIVER)) && !Objects.equals(biomeName, Optional.of(Biomes.FROZEN_RIVER))) {
            return random.nextInt(40) == 0 && IAquaticMob.isDeepEnoughToSpawn(serverWorld, blockPos) && canSpawn;
        } else {
            return random.nextInt(15) == 0 && canSpawn;
        }
    }

    public static boolean checkDrownedSpawnRules(EntityType<DrownedEntity> type, IServerWorld serverWorld, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        Optional<RegistryKey<Biome>> biomeName = serverWorld.getBiomeName(blockPos);
        boolean canSpawn = serverWorld.getDifficulty() != Difficulty.PEACEFUL && MonsterEntity.isDarkEnoughToSpawn(serverWorld, blockPos, random) && (spawnReason == SpawnReason.SPAWNER || serverWorld.getFluidState(blockPos).is(FluidTags.WATER));
        if (!Objects.equals(biomeName, Optional.of(Biomes.RIVER)) && !Objects.equals(biomeName, Optional.of(Biomes.FROZEN_RIVER))) {
            return random.nextInt(40) == 0 && isDeepEnoughToSpawn(serverWorld, blockPos) && canSpawn;
        } else {
            return random.nextInt(15) == 0 && canSpawn;
        }
    }

    private static boolean isDeepEnoughToSpawn(IWorld world, BlockPos blockPos) {
        return blockPos.getY() < world.getSeaLevel() - 5;
    }

    public static boolean checkZombifiedPiglinSpawnRules(EntityType<? extends ZombifiedPiglinEntity> p_234351_0_, IWorld p_234351_1_, SpawnReason p_234351_2_, BlockPos p_234351_3_, Random p_234351_4_) {
        return p_234351_1_.getDifficulty() != Difficulty.PEACEFUL && p_234351_1_.getBlockState(p_234351_3_.below()).getBlock() != Blocks.NETHER_WART_BLOCK;
    }

    public static boolean checkPiglinSpawnRules(EntityType<? extends PiglinEntity> p_234418_0_, IWorld p_234418_1_, SpawnReason p_234418_2_, BlockPos p_234418_3_, Random p_234418_4_) {
        return !p_234418_1_.getBlockState(p_234418_3_.below()).is(Blocks.NETHER_WART_BLOCK);
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
