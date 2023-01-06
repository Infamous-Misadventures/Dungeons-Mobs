package com.infamous.dungeons_mobs.interfaces;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public interface IAquaticMob {

    static boolean isDeepEnoughToSpawn(LevelAccessor world, BlockPos blockPos) {
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

    default <T extends Mob & IAquaticMob> boolean wantsToSwim(T aquaticMob){
        if(aquaticMob != this) throw new IllegalArgumentException("Supplied aquaticMob is not this instance!");

        if (this.isSearchingForLand()) {
            return true;
        } else {
            LivingEntity target = aquaticMob.getTarget();
            return target != null && target.isInWater();
        }
    }

    void setSearchingForLand(boolean searchingForLand);

    default <T extends Mob & IAquaticMob> boolean closeToNextPos(T aquaticMob) {
        if(aquaticMob != this) throw new IllegalArgumentException("Supplied aquaticMob is not this instance!");

        Path path = aquaticMob.getNavigation().getPath();
        if (path != null) {
            BlockPos blockpos = path.getTarget();
            double distanceToSqr = aquaticMob.distanceToSqr((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
            return distanceToSqr < 4.0D;
        }
        return false;
    }

    default <T extends Mob & IAquaticMob> void updateNavigation(T aquaticMob) {
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

    default <T extends Mob & IAquaticMob> void checkAquaticTravel(T aquaticMob, Vec3 travelVec) {
        if(aquaticMob != this) throw new IllegalArgumentException("Supplied aquaticMob is not this instance!");

        if (aquaticMob.isEffectiveAi() && aquaticMob.isInWater() && this.wantsToSwim(aquaticMob)) {
            aquaticMob.moveRelative(0.01F, travelVec);
            aquaticMob.move(MoverType.SELF, aquaticMob.getDeltaMovement());
            aquaticMob.setDeltaMovement(aquaticMob.getDeltaMovement().scale(0.9D));
        } else {
            this.normalTravel(travelVec);
        }
    }

    void normalTravel(Vec3 travelVec);

    boolean isSearchingForLand();

    void setNavigation(PathNavigation navigation);

    GroundPathNavigation getGroundNavigation();

    WaterBoundPathNavigation getWaterNavigation();
}
