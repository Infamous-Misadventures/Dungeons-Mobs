package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.goals.AquaticMoveHelperController;
import com.infamous.dungeons_mobs.goals.GoToBeachGoal;
import com.infamous.dungeons_mobs.goals.GoToWaterGoal;
import com.infamous.dungeons_mobs.goals.SwimUpGoal;
import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class WavewhispererEntity extends WhispererEntity implements IAquaticMob {

    private boolean searchingForLand;
    protected final SwimmerPathNavigator waterNavigation;
    protected final GroundPathNavigator groundNavigation;

    public WavewhispererEntity(EntityType<? extends WavewhispererEntity> type, World worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1.0F;
        this.moveControl = new AquaticMoveHelperController<>(this);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        this.waterNavigation = new SwimmerPathNavigator(this, worldIn);
        this.groundNavigation = new GroundPathNavigator(this, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return WhispererEntity.setCustomAttributes();
    }

    @Override
    protected void addMovementBehaviors() {
        this.goalSelector.addGoal(1, new GoToWaterGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new GoToBeachGoal<>(this, 1.0D));
        this.goalSelector.addGoal(6, new SwimUpGoal<>(this, 1.0D, this.level.getSeaLevel()));
        this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1.0D));
    }

    @Override
    protected EntityType<? extends PoisonQuillVineEntity> getPoisonVineType() {
        return ModEntityTypes.POISON_ANEMONE.get();
    }

    @Override
    protected EntityType<? extends QuickGrowingVineEntity> getQuickGrowingVineType() {
        return ModEntityTypes.QUICK_GROWING_ANEMONE.get();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public static boolean checkWavewhispererSpawnRules(EntityType<? extends WavewhispererEntity> p_223332_0_, IServerWorld serverWorld, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        Optional<RegistryKey<Biome>> biomeName = serverWorld.getBiomeName(blockPos);
        boolean canSpawn = serverWorld.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(serverWorld, blockPos, random) && (spawnReason == SpawnReason.SPAWNER || serverWorld.getFluidState(blockPos).is(FluidTags.WATER));
        if (!Objects.equals(biomeName, Optional.of(Biomes.RIVER)) && !Objects.equals(biomeName, Optional.of(Biomes.FROZEN_RIVER))) {
            return random.nextInt(40) == 0 && IAquaticMob.isDeepEnoughToSpawn(serverWorld, blockPos) && canSpawn;
        } else {
            return random.nextInt(15) == 0 && canSpawn;
        }
    }

    @Override
    public void setSearchingForLand(boolean searchingForLand) {
        this.searchingForLand = searchingForLand;
    }

    @Override
    public boolean checkSpawnObstruction(IWorldReader worldReader) {
        return worldReader.isUnobstructed(this);
    }

    @Override
    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }

    @Override
    public void updateSwimming() {
        this.updateNavigation(this);
    }

    @Override
    public void travel(Vector3d travelVec) {
        this.checkAquaticTravel(this, travelVec);
    }

    @Override
    public void normalTravel(Vector3d travelVec) {
        super.travel(travelVec);
    }

    @Override
    public boolean isSearchingForLand() {
        return this.searchingForLand;
    }

    @Override
    public void setNavigation(PathNavigator navigation) {
        this.navigation = navigation;
    }

    @Override
    public GroundPathNavigator getGroundNavigation() {
        return this.groundNavigation;
    }

    @Override
    public SwimmerPathNavigator getWaterNavigation() {
        return this.waterNavigation;
    }
}
