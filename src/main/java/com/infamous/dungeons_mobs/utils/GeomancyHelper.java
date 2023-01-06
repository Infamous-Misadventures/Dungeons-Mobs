package com.infamous.dungeons_mobs.utils;

import com.infamous.dungeons_mobs.entities.jungle.AbstractVineEntity;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GeomancyHelper {
    private static final int[] CONFIG_1_NORTH_ROW = new int[]{2, 3, 4, 5, 6};
    private static final int[] CONFIG_1_EAST_ROW = new int[]{6, 7, 8, 9, 10};
    private static final int[] CONFIG_1_SOUTH_ROW = new int[]{10, 11, 12, 13, 14};
    private static final int[] CONFIG_1_WEST_ROW = new int[]{14, 15, 0, 1, 2};
    public static final int[][] CONFIG_1_ROWS = new int[][]{CONFIG_1_NORTH_ROW, CONFIG_1_EAST_ROW, CONFIG_1_SOUTH_ROW, CONFIG_1_WEST_ROW};
    
    private static final int[] CONFIG_2_NORTH_ROW = new int[]{2, 3, 4, 5, 6, 7, 8, 1, 2};
    private static final int[] CONFIG_2_EAST_ROW = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final int[] CONFIG_2_SOUTH_ROW = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final int[] CONFIG_2_WEST_ROW = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final int[][] CONFIG_2_ROWS = new int[][]{CONFIG_2_NORTH_ROW, CONFIG_2_EAST_ROW, CONFIG_2_SOUTH_ROW, CONFIG_2_WEST_ROW};
    
    
    public static final int[][][] CONFIGS = new int[][][]{CONFIG_1_ROWS, CONFIG_2_ROWS};
    
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
        boolean flag = false;
        double yShift = 0.0D;

        do {
           BlockPos blockpos1 = targetPos.below();
           BlockState blockstate = casterEntity.level.getBlockState(blockpos1);
           if (blockstate.isFaceSturdy(casterEntity.level, blockpos1, Direction.UP)) {
              if (!casterEntity.level.isEmptyBlock(targetPos)) {
                 BlockState blockstate1 = casterEntity.level.getBlockState(targetPos);
                 VoxelShape voxelshape = blockstate1.getCollisionShape(casterEntity.level, targetPos);
                 if (!voxelshape.isEmpty()) {
                	 yShift = voxelshape.max(Direction.Axis.Y);
                 }
              }

              flag = true;
              break;
           }

           targetPos = targetPos.below();
        } while(targetPos.getY() >= Mth.floor(targetPos.getY()) - 1);
        
        // verify that the construct will be summoned on valid ground
        if(flag && canAllowBlockEntitySpawn(casterEntity, targetPos)){
            ConstructEntity constructEntity = wallEntityType.create(casterEntity.level);
            if (constructEntity != null) {
                constructEntity.setCaster(casterEntity);
                constructEntity.setPos(targetPos.getX(), targetPos.getY() + yShift, targetPos.getZ());
                constructEntity.setLifeTicks(100 + casterEntity.getRandom().nextInt(10));
                constructEntity.directionToFace = pillarFacing;
                constructEntity.spawnAreaDamage();
                casterEntity.level.addFreshEntity(constructEntity);
            }
        }
    }

    private static void summonAreaDenialConstruct(LivingEntity casterEntity, BlockPos targetPos, EntityType<? extends ConstructEntity> wallEntityType, double xshift, double zshift, Direction pillarFacing) {
        targetPos = targetPos.offset(xshift, 0, zshift);
        boolean flag = false;
        double yShift = 0.0D;

        do {
           BlockPos blockpos1 = targetPos.below();
           BlockState blockstate = casterEntity.level.getBlockState(blockpos1);
           if (blockstate.isFaceSturdy(casterEntity.level, blockpos1, Direction.UP)) {
              if (!casterEntity.level.isEmptyBlock(targetPos)) {
                 BlockState blockstate1 = casterEntity.level.getBlockState(targetPos);
                 VoxelShape voxelshape = blockstate1.getCollisionShape(casterEntity.level, targetPos);
                 if (!voxelshape.isEmpty()) {
                	 yShift = voxelshape.max(Direction.Axis.Y);
                 }
              }

              flag = true;
              break;
           }

           targetPos = targetPos.below();
        } while(targetPos.getY() >= Mth.floor(targetPos.getY()) - 1);
        
        // verify that the construct will be summoned on valid ground
        if(flag && canAllowBlockEntitySpawn(casterEntity, targetPos)){
            ConstructEntity constructEntity = wallEntityType.create(casterEntity.level);
            if (constructEntity != null) {
                constructEntity.setCaster(casterEntity);
                constructEntity.setPos(targetPos.getX(), targetPos.getY() + yShift, targetPos.getZ());
                constructEntity.setLifeTicks(100 + casterEntity.getRandom().nextInt(10));
                constructEntity.directionToFace = pillarFacing;
                constructEntity.spawnAreaDamage();
                casterEntity.level.addFreshEntity(constructEntity);
            }
        }
    }

    public static void summonOffensiveVine(LivingEntity casterEntity, LivingEntity targetEntity, EntityType<? extends AbstractVineEntity> entityType, int xShift, int zShift) {
        BlockPos targetPos = createCenteredBlockPosOnTarget(targetEntity);
        // verify that the construct will be summoned on valid ground
        if(canAllowBlockEntitySpawn(casterEntity, targetPos)){
            AbstractVineEntity vineEntity = entityType.create(casterEntity.level);
            if (vineEntity != null) {
                vineEntity.setPos(targetPos.getX() + xShift, targetPos.getY(), targetPos.getZ() + zShift);
                PositionUtils.moveToCorrectHeight(vineEntity);
                vineEntity.setDefaultFeatures();
            	vineEntity.setVanishes(true);
                vineEntity.setStayTime(300 + casterEntity.getRandom().nextInt(50));
            	vineEntity.setAlwaysOut(true);
            	vineEntity.setShouldRetract(false);
            	double vineLength = targetEntity.getY() - vineEntity.getY();
            	vineEntity.setLengthInBlocks((float)vineLength + 3 + vineEntity.getRandom().nextInt(6));
                casterEntity.level.addFreshEntity(vineEntity);
                if (casterEntity instanceof Mob) {
                	vineEntity.setTarget(((Mob)casterEntity).getTarget());
                }
            }
        }
    }

    private static void summonAreaDenialVine(LivingEntity casterEntity, LivingEntity targetEntity, EntityType<? extends AbstractVineEntity> entityType, double xshift, double zshift, Direction pillarFacing) {
        BlockPos targetPos = createCenteredBlockPosOnTarget(targetEntity).offset(xshift, 0, zshift);
        // verify that the construct will be summoned on valid ground
        if(canAllowBlockEntitySpawn(casterEntity, targetPos)){
        	AbstractVineEntity vineEntity = entityType.create(casterEntity.level);
            if (vineEntity != null) {
                vineEntity.setPos(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                PositionUtils.moveToCorrectHeight(vineEntity);
                vineEntity.setDefaultFeatures();
            	vineEntity.setVanishes(true);
                vineEntity.setStayTime(100 + casterEntity.getRandom().nextInt(20));
            	vineEntity.setAlwaysOut(true);
            	vineEntity.setShouldRetract(false);
            	double vineLength = targetEntity.getY() - vineEntity.getY();
            	vineEntity.setLengthInBlocks((float)vineLength + 3 + vineEntity.getRandom().nextInt(6));
                casterEntity.level.addFreshEntity(vineEntity);
            }
        }
    }

    public static void summonOffensiveConstruct(LivingEntity casterEntity, Entity targetEntity, EntityType<? extends ConstructEntity> wallEntityType, double xshift, double zshift, Direction pillarFacing) {
        BlockPos targetPos = createCenteredBlockPosOnTarget(targetEntity).offset(xshift, 0, zshift);
        boolean flag = false;
        double yShift = 0.0D;

        do {
           BlockPos blockpos1 = targetPos.below();
           BlockState blockstate = casterEntity.level.getBlockState(blockpos1);
           if (blockstate.isFaceSturdy(casterEntity.level, blockpos1, Direction.UP)) {
              if (!casterEntity.level.isEmptyBlock(targetPos)) {
                 BlockState blockstate1 = casterEntity.level.getBlockState(targetPos);
                 VoxelShape voxelshape = blockstate1.getCollisionShape(casterEntity.level, targetPos);
                 if (!voxelshape.isEmpty()) {
                	 yShift = voxelshape.max(Direction.Axis.Y);
                 }
              }

              flag = true;
              break;
           }

           targetPos = targetPos.below();
        } while(targetPos.getY() >= Mth.floor(targetPos.getY()) - 1);
        
        // verify that the construct will be summoned on valid ground
        if(flag && canAllowBlockEntitySpawn(casterEntity, targetPos)){
            ConstructEntity constructEntity = wallEntityType.create(casterEntity.level);
            if (constructEntity != null) {
                constructEntity.setCaster(casterEntity);
                constructEntity.setPos(targetPos.getX(), targetPos.getY() + yShift, targetPos.getZ());
                constructEntity.setLifeTicks(100 + casterEntity.getRandom().nextInt(10));
                constructEntity.directionToFace = pillarFacing;
                constructEntity.spawnAreaDamage();
                constructEntity.playSound(ModSoundEvents.GEOMANCER_BOMB_SPAWN.get(), 2.0F, 1.0F);
                casterEntity.level.addFreshEntity(constructEntity);
            }
        }
    }

    public static void summonOffensiveConstruct(LivingEntity casterEntity, BlockPos targetPos, EntityType<? extends ConstructEntity> wallEntityType, double xshift, double zshift, Direction pillarFacing) {
        targetPos = targetPos.offset(xshift, 0, zshift);
        boolean flag = false;
        double yShift = 0.0D;

        do {
           BlockPos blockpos1 = targetPos.below();
           BlockState blockstate = casterEntity.level.getBlockState(blockpos1);
           if (blockstate.isFaceSturdy(casterEntity.level, blockpos1, Direction.UP)) {
              if (!casterEntity.level.isEmptyBlock(targetPos)) {
                 BlockState blockstate1 = casterEntity.level.getBlockState(targetPos);
                 VoxelShape voxelshape = blockstate1.getCollisionShape(casterEntity.level, targetPos);
                 if (!voxelshape.isEmpty()) {
                	 yShift = voxelshape.max(Direction.Axis.Y);
                 }
              }

              flag = true;
              break;
           }

           targetPos = targetPos.below();
        } while(targetPos.getY() >= Mth.floor(targetPos.getY()) - 1);
        
        // verify that the construct will be summoned on valid ground
        if(flag && canAllowBlockEntitySpawn(casterEntity, targetPos)){
            ConstructEntity constructEntity = wallEntityType.create(casterEntity.level);
            if (constructEntity != null) {
                constructEntity.setCaster(casterEntity);
                constructEntity.setPos(targetPos.getX(), targetPos.getY() + yShift, targetPos.getZ());
                constructEntity.setLifeTicks(100 + casterEntity.getRandom().nextInt(10));
                constructEntity.directionToFace = pillarFacing;
                constructEntity.playSound(ModSoundEvents.GEOMANCER_BOMB_SPAWN.get(), 2.0F, 1.0F);
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

    public static void summonAreaDenialVineTrap(LivingEntity casterEntity, LivingEntity targetEntity, EntityType<? extends AbstractVineEntity> entityType, int[] rowToRemove) {

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
    
    public static void summonWallTrap(LivingEntity casterEntity, BlockPos targetPos, EntityType<? extends ConstructEntity> entityType) {
    	
		if (casterEntity.getRandom().nextBoolean()) {
    		if (casterEntity.getRandom().nextBoolean()) {
    	    	for (int length = -7; length < 7; length++) {
        		summonAreaDenialConstruct(casterEntity, targetPos, entityType, -3, length, Direction.NORTH);
    	    	}
    		} else {
    	    	for (int length = -7; length < 7; length++) {
        		summonAreaDenialConstruct(casterEntity, targetPos, entityType, 3, length, Direction.NORTH);
    	    	}
    		}	
		} else {
    		if (casterEntity.getRandom().nextBoolean()) {
    	    	for (int length = -7; length < 7; length++) {
        		summonAreaDenialConstruct(casterEntity, targetPos, entityType, length, -3, Direction.NORTH);
    	    	}
    		} else {
    	    	for (int length = -7; length < 7; length++) {
        		summonAreaDenialConstruct(casterEntity, targetPos, entityType, length, 3, Direction.NORTH);
    	    	}
    		}
		}   	
}
    
    public static void summonWallTrap(LivingEntity casterEntity, Entity targetEntity, EntityType<? extends ConstructEntity> entityType) {
        BlockPos targetPos = createCenteredBlockPosOnTarget(targetEntity);
        

		if (casterEntity.getRandom().nextBoolean()) {
    		if (casterEntity.getRandom().nextBoolean()) {
    	    	for (int length = -7; length < 7; length++) {
        		summonAreaDenialConstruct(casterEntity, targetPos, entityType, -3, length, Direction.NORTH);
    	    	}
    		} else {
    	    	for (int length = -7; length < 7; length++) {
        		summonAreaDenialConstruct(casterEntity, targetPos, entityType, 3, length, Direction.NORTH);
    	    	}
    		}	
		} else {
    		if (casterEntity.getRandom().nextBoolean()) {
    	    	for (int length = -7; length < 7; length++) {
        		summonAreaDenialConstruct(casterEntity, targetPos, entityType, length, -3, Direction.NORTH);
    	    	}
    		} else {
    	    	for (int length = -7; length < 7; length++) {
        		summonAreaDenialConstruct(casterEntity, targetPos, entityType, length, 3, Direction.NORTH);
    	    	}
    		}
		}   	
}
    
    public static void summonRandomPillarsTrap(LivingEntity casterEntity, BlockPos targetPos, EntityType<? extends ConstructEntity> entityType) {
    	
    	for (int length = 0; length < 25; length++) {
    		summonAreaDenialConstruct(casterEntity, targetPos, entityType, -8 + casterEntity.getRandom().nextInt(16), -8 + casterEntity.getRandom().nextInt(16), Direction.NORTH);
    	}
    	
    }
    
    public static void summonRandomPillarsTrap(LivingEntity casterEntity, Entity targetEntity, EntityType<? extends ConstructEntity> entityType) {
        BlockPos targetPos = createCenteredBlockPosOnTarget(targetEntity);
        
    	for (int length = 0; length < 25; length++) {
    		summonAreaDenialConstruct(casterEntity, targetPos, entityType, -8 + casterEntity.getRandom().nextInt(16), -8 + casterEntity.getRandom().nextInt(16), Direction.NORTH);
    	}
    	
    }
    
    public static void summonQuadOffensiveTrap(LivingEntity casterEntity, BlockPos targetPos, EntityType<? extends ConstructEntity> entityType) {
    	summonOffensiveConstruct(casterEntity, targetPos, entityType, -2, 0, Direction.NORTH);
    	summonOffensiveConstruct(casterEntity, targetPos, entityType, 2, 0, Direction.NORTH);
    	summonOffensiveConstruct(casterEntity, targetPos, entityType, 0, -2, Direction.NORTH);
    	summonOffensiveConstruct(casterEntity, targetPos, entityType, 0, 2, Direction.NORTH);
    }
    
    public static void summonQuadOffensiveTrap(LivingEntity casterEntity, Entity targetEntity, EntityType<? extends ConstructEntity> entityType) {
        BlockPos targetPos = createCenteredBlockPosOnTarget(targetEntity);
    	summonOffensiveConstruct(casterEntity, targetPos, entityType, -2, 0, Direction.NORTH);
    	summonOffensiveConstruct(casterEntity, targetPos, entityType, 2, 0, Direction.NORTH);
    	summonOffensiveConstruct(casterEntity, targetPos, entityType, 0, -2, Direction.NORTH);
    	summonOffensiveConstruct(casterEntity, targetPos, entityType, 0, 2, Direction.NORTH);
    }

    public static boolean canAllowBlockEntitySpawn(Entity entity, BlockPos blockPos){   	
        return entity.level.getBlockState(blockPos).canBeReplaced(Fluids.EMPTY);
    }
}
