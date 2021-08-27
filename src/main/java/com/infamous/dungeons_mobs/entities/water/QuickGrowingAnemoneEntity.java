package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class QuickGrowingAnemoneEntity extends QuickGrowingVineEntity implements IAquaticMob {

    public QuickGrowingAnemoneEntity(EntityType<? extends QuickGrowingVineEntity> entityType, World world) {
        super(entityType, world);
    }

    public QuickGrowingAnemoneEntity(World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicks) {
        super(ModEntityTypes.QUICK_GROWING_ANEMONE.get(), worldIn, x, y, z, casterIn, lifeTicks);
    }

    @Override
    public boolean checkSpawnObstruction(IWorldReader worldReader) {
        return worldReader.isUnobstructed(this);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void setSearchingForLand(boolean searchingForLand) {
        // NO-OP
    }

    @Override
    public void normalTravel(Vector3d travelVec) {
        // NO-OP
    }

    @Override
    public boolean isSearchingForLand() {
        return false;
    }

    @Override
    public void setNavigation(PathNavigator navigation) {
        // NO-OP
    }

    @Override
    public GroundPathNavigator getGroundNavigation() {
        return null;
    }

    @Override
    public SwimmerPathNavigator getWaterNavigation() {
        return null;
    }
}
