package com.infamous.dungeons_mobs.utils;

import com.infamous.dungeons_mobs.entities.jungle.VineEntity;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

public class GeomancyHelper {
    private static final int[] NORTH_ROW = new int[]{2, 3, 4, 5, 6};
    private static final int[] EAST_ROW = new int[]{6, 7, 8, 9, 10};
    private static final int[] SOUTH_ROW = new int[]{10, 11, 12, 13, 14};
    private static final int[] WEST_ROW = new int[]{14, 15, 0, 1, 2};
    public static final int[][] ROWS = new int[][]{NORTH_ROW, EAST_ROW, SOUTH_ROW, WEST_ROW};
    private static final Direction[] DIRECTIONS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    private static boolean isValueInArray(int[] arr, int toCheckValue)
    {
        for (int element : arr) {
            if (element == toCheckValue) {
               return true;
            }
        }
        return false;
    }

    private static double getZShift(int i, double zshift) {
        if (i >= 10 && i <= 14) {
            zshift = -2.0D;
        }
        if (i >= 2 && i <= 6) {
            zshift = 2.0D;
        }
        if (i == 9 || i == 15) {
            zshift = -1.0D;
        }
        if (i == 1 || i == 7) {
            zshift = 1.0D;
        }
        return zshift;
    }

    private static double getXShift(int i, double xshift) {
        if (i == 0 || i == 1 || i == 2 || i == 14 || i == 15) {
            xshift = -2.0D;
        }
        if (i >= 6 && i <= 10) {
            xshift = 2.0D;
        }

        if (i == 3 || i == 13) {
            xshift = -1.0D;
        }
        if (i == 5 || i == 11) {
            xshift = 1.0D;
        }
        return xshift;
    }

    private static BlockPos createCenteredBlockPosOnTarget(Entity targetEntity) {
        return new BlockPos(
                Math.floor(targetEntity.getX()),
                Math.floor(targetEntity.getY()),
                Math.floor(targetEntity.getZ()));
    }

    private static void summonAreaDenialConstruct(LivingEntity casterEntity, Entity targetEntity, EntityType<? extends ConstructEntity> wallEntityType, double xshift, double zshift, Direction pillarFacing) {
        BlockPos targetPos = createCenteredBlockPosOnTarget(targetEntity).offset(xshift, 0, zshift);
        // verify that the construct will be summoned on valid ground
        if(canAllowBlockEntitySpawn(casterEntity, targetPos)){
            ConstructEntity constructEntity = wallEntityType.create(casterEntity.level);
            if (constructEntity != null) {
                constructEntity.setCaster(casterEntity);
                constructEntity.setPos(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                constructEntity.setLifeTicks(100);
                constructEntity.faceDirection(pillarFacing);
                casterEntity.level.addFreshEntity(constructEntity);
            }
        }
    }

    private static void summonAreaDenialConstruct(LivingEntity casterEntity, BlockPos targetPos, EntityType<? extends ConstructEntity> wallEntityType, double xshift, double zshift, Direction pillarFacing) {
        targetPos = targetPos.offset(xshift, 0, zshift);
        // verify that the construct will be summoned on valid ground
        if(canAllowBlockEntitySpawn(casterEntity, targetPos)){
            ConstructEntity constructEntity = wallEntityType.create(casterEntity.level);
            if (constructEntity != null) {
                constructEntity.setCaster(casterEntity);
                constructEntity.setPos(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                constructEntity.setLifeTicks(100);
                constructEntity.faceDirection(pillarFacing);
                casterEntity.level.addFreshEntity(constructEntity);
            }
        }
    }

    public static void summonOffensiveVine(LivingEntity casterEntity, LivingEntity targetEntity, EntityType<? extends VineEntity> entityType) {
        BlockPos targetPos = createCenteredBlockPosOnTarget(targetEntity);
        // verify that the construct will be summoned on valid ground
        if(canAllowBlockEntitySpawn(casterEntity, targetPos)){
            VineEntity vineEntity = entityType.create(casterEntity.level);
            if (vineEntity != null) {
                vineEntity.setCaster(casterEntity);
                vineEntity.setPos(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                casterEntity.level.addFreshEntity(vineEntity);
            }
        }
    }

    private static void summonAreaDenialVine(LivingEntity casterEntity, LivingEntity targetEntity, EntityType<? extends VineEntity> entityType, double xshift, double zshift, Direction pillarFacing) {
        BlockPos targetPos = createCenteredBlockPosOnTarget(targetEntity).offset(xshift, 0, zshift);
        // verify that the construct will be summoned on valid ground
        if(canAllowBlockEntitySpawn(casterEntity, targetPos)){
            VineEntity vineEntity = entityType.create(casterEntity.level);
            if (vineEntity != null) {
                vineEntity.setCaster(casterEntity);
                vineEntity.setLifeTicks(100);
                vineEntity.setPos(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                casterEntity.level.addFreshEntity(vineEntity);
            }
        }
    }

    public static void summonOffensiveConstruct(LivingEntity casterEntity, Entity targetEntity, EntityType<? extends ConstructEntity> entityType) {
        BlockPos targetPos = createCenteredBlockPosOnTarget(targetEntity);
        // verify that the construct will be summoned on valid ground
        if(canAllowBlockEntitySpawn(casterEntity, targetPos)){
            ConstructEntity constructEntity = entityType.create(casterEntity.level);
            if (constructEntity != null) {
                constructEntity.setCaster(casterEntity);
                constructEntity.setPos(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                constructEntity.setLifeTicks(100);
                casterEntity.level.addFreshEntity(constructEntity);
            }
        }
    }

    public static void summonOffensiveConstruct(LivingEntity casterEntity, BlockPos targetPos, EntityType<? extends ConstructEntity> entityType) {
        // verify that the construct will be summoned on valid ground
        if(canAllowBlockEntitySpawn(casterEntity, targetPos)){
            ConstructEntity constructEntity = entityType.create(casterEntity.level);
            if (constructEntity != null) {
                constructEntity.setCaster(casterEntity);
                constructEntity.setPos(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                constructEntity.setLifeTicks(100);
                casterEntity.level.addFreshEntity(constructEntity);
            }
        }
    }

    public static void summonAreaDenialTrap(LivingEntity casterEntity, Entity targetEntity, EntityType<? extends ConstructEntity> entityType, int[] rowToRemove) {

        for(int constructPositionIndex = 0; constructPositionIndex <= 15; constructPositionIndex++){

            if(isValueInArray(rowToRemove, constructPositionIndex)){
                continue;
            }

            double xshift = 0;
            double zshift = 0;

            xshift = getXShift(constructPositionIndex, xshift);
            zshift = getZShift(constructPositionIndex, zshift);
            Direction pillarFacing = Util.getRandom(DIRECTIONS, casterEntity.getRandom());

            summonAreaDenialConstruct(casterEntity, targetEntity, entityType, xshift, zshift, pillarFacing);
        }
    }

    public static void summonAreaDenialTrap(LivingEntity casterEntity, BlockPos targetPos, EntityType<? extends ConstructEntity> entityType, int[] rowToRemove) {

        for(int constructPositionIndex = 0; constructPositionIndex <= 15; constructPositionIndex++){

            if(isValueInArray(rowToRemove, constructPositionIndex)){
                continue;
            }

            double xshift = 0;
            double zshift = 0;

            xshift = getXShift(constructPositionIndex, xshift);
            zshift = getZShift(constructPositionIndex, zshift);
            Direction pillarFacing = Util.getRandom(DIRECTIONS, casterEntity.getRandom());

            summonAreaDenialConstruct(casterEntity, targetPos, entityType, xshift, zshift, pillarFacing);
        }
    }

    public static void summonAreaDenialVineTrap(LivingEntity casterEntity, LivingEntity targetEntity, EntityType<? extends VineEntity> entityType, int[] rowToRemove) {

        for(int constructPositionIndex = 0; constructPositionIndex <= 15; constructPositionIndex++){

            if(isValueInArray(rowToRemove, constructPositionIndex)){
                continue;
            }

            double xshift = 0;
            double zshift = 0;

            xshift = getXShift(constructPositionIndex, xshift);
            zshift = getZShift(constructPositionIndex, zshift);
            Direction pillarFacing = Util.getRandom(DIRECTIONS, casterEntity.getRandom());

            summonAreaDenialVine(casterEntity, targetEntity, entityType, xshift, zshift, pillarFacing);
        }
    }

    public static boolean canAllowBlockEntitySpawn(Entity entity, BlockPos blockPos){
        return (entity.level.isEmptyBlock(blockPos) || entity.level.getBlockState(blockPos).canBeReplaced(Fluids.EMPTY)) && !entity.level.isEmptyBlock(blockPos.below());
    }
}
