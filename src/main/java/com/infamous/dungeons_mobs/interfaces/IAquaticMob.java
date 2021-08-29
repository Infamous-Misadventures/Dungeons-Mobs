package com.infamous.dungeons_mobs.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public interface IAquaticMob {

    static boolean isDeepEnoughToSpawn(IWorld world, BlockPos blockPos) {
        return blockPos.getY() < world.getSeaLevel() - 5;
    }

    default <T extends LivingEntity & IAquaticMob> boolean okTarget(T aquaticMob, @Nullable LivingEntity target){
        if(aquaticMob != this) throw new IllegalArgumentException("Supplied aquaticMob is not this instance!");

        if (target != null) {
            return !aquaticMob.level.isDay() || target.isInWater();
        } else {
            return false;
        }
    }

    default <T extends MobEntity & IAquaticMob> boolean wantsToSwim(T aquaticMob){
        if(aquaticMob != this) throw new IllegalArgumentException("Supplied aquaticMob is not this instance!");

        if (this.isSearchingForLand()) {
            return true;
        } else {
            LivingEntity target = aquaticMob.getTarget();
            return target != null && target.isInWater();
        }
    }

    void setSearchingForLand(boolean searchingForLand);

    default <T extends MobEntity & IAquaticMob> boolean closeToNextPos(T aquaticMob) {
        if(aquaticMob != this) throw new IllegalArgumentException("Supplied aquaticMob is not this instance!");

        Path path = aquaticMob.getNavigation().getPath();
        if (path != null) {
            BlockPos blockpos = path.getTarget();
            double distanceToSqr = aquaticMob.distanceToSqr((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
            return distanceToSqr < 4.0D;
        }
        return false;
    }

    default <T extends MobEntity & IAquaticMob> void updateNavigation(T aquaticMob) {
        if(aquaticMob != this) throw new IllegalArgumentException("Supplied aquaticMob is not this instance!");

        if (!aquaticMob.level.isClientSide) {
            if (aquaticMob.isEffectiveAi() && aquaticMob.isInWater() && this.wantsToSwim(aquaticMob)) {
                aquaticMob.setNavigation(aquaticMob.getWaterNavigation());
                aquaticMob.setSwimming(true);
            } else {
                aquaticMob.setNavigation(aquaticMob.getGroundNavigation());
                aquaticMob.setSwimming(false);
            }
        }
    }

    default <T extends MobEntity & IAquaticMob> void checkAquaticTravel(T aquaticMob, Vector3d travelVec) {
        if(aquaticMob != this) throw new IllegalArgumentException("Supplied aquaticMob is not this instance!");

        if (aquaticMob.isEffectiveAi() && aquaticMob.isInWater() && this.wantsToSwim(aquaticMob)) {
            aquaticMob.moveRelative(0.01F, travelVec);
            aquaticMob.move(MoverType.SELF, aquaticMob.getDeltaMovement());
            aquaticMob.setDeltaMovement(aquaticMob.getDeltaMovement().scale(0.9D));
        } else {
            this.normalTravel(travelVec);
        }
    }

    void normalTravel(Vector3d travelVec);

    boolean isSearchingForLand();

    void setNavigation(PathNavigator navigation);

    GroundPathNavigator getGroundNavigation();

    SwimmerPathNavigator getWaterNavigation();
}
