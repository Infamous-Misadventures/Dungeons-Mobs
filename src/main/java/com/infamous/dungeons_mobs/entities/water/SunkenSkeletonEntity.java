package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.goals.AquaticMoveHelperController;
import com.infamous.dungeons_mobs.goals.GoToBeachGoal;
import com.infamous.dungeons_mobs.goals.GoToWaterGoal;
import com.infamous.dungeons_mobs.goals.SwimUpGoal;
import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class SunkenSkeletonEntity extends AbstractSkeletonEntity implements ICrossbowUser, IAquaticMob {
    private final RangedBowAttackGoal<SunkenSkeletonEntity> bowGoal = new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false) {

        @Override
        public boolean canUse() {
            return super.canUse() && SunkenSkeletonEntity.this.okTarget(SunkenSkeletonEntity.this, SunkenSkeletonEntity.this.getTarget());
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && SunkenSkeletonEntity.this.okTarget(SunkenSkeletonEntity.this, SunkenSkeletonEntity.this.getTarget());
        }
    };
    private final RangedCrossbowAttackGoal<SunkenSkeletonEntity> crossbowGoal = new RangedCrossbowAttackGoal<>(this, 1.2D, 10.0F);

    private static final DataParameter<Boolean> CHARGING_CROSSBOW = EntityDataManager.defineId(SunkenSkeletonEntity.class, DataSerializers.BOOLEAN);

    private boolean searchingForLand;
    protected final SwimmerPathNavigator waterNavigation;
    protected final GroundPathNavigator groundNavigation;
    private final boolean isConstructed;

    public SunkenSkeletonEntity(EntityType<? extends SunkenSkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.isConstructed = true;
        this.maxUpStep = 1.0F;
        this.moveControl = new AquaticMoveHelperController<>(this);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        this.waterNavigation = new SwimmerPathNavigator(this, world);
        this.groundNavigation = new GroundPathNavigator(this, world);
    }



    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return AbstractSkeletonEntity.createAttributes();
    }

    public static boolean checkSunkenSkeletonSpawnRules(EntityType<? extends SunkenSkeletonEntity> p_223332_0_, IServerWorld serverWorld, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        Optional<RegistryKey<Biome>> biomeName = serverWorld.getBiomeName(blockPos);
        boolean canSpawn = serverWorld.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(serverWorld, blockPos, random) && (spawnReason == SpawnReason.SPAWNER || serverWorld.getFluidState(blockPos).is(FluidTags.WATER));
        if (!Objects.equals(biomeName, Optional.of(Biomes.RIVER)) && !Objects.equals(biomeName, Optional.of(Biomes.FROZEN_RIVER))) {
            return random.nextInt(40) == 0 && IAquaticMob.isDeepEnoughToSpawn(serverWorld, blockPos) && canSpawn;
        } else {
            return random.nextInt(15) == 0 && canSpawn;
        }
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
    public void travel(Vector3d travelVec) {
        this.checkAquaticTravel(this, travelVec);
    }

    @Override
    public void normalTravel(Vector3d travelVec) {
        super.travel(travelVec);
    }

    @Override
    public void updateSwimming() {
        this.updateNavigation(this);
    }

    @Override
    public boolean isSearchingForLand() {
        return searchingForLand;
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

    @Override
    public void setSearchingForLand(boolean searchingForLand) {
        this.searchingForLand = searchingForLand;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GoToWaterGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new GoToBeachGoal<>(this, 1.0D));
        this.goalSelector.addGoal(6, new SwimUpGoal<>(this, 1.2D, this.level.getSeaLevel()));
        this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, SunkenSkeletonEntity.class)).setAlertOthers(SunkenSkeletonEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, target -> this.okTarget(this, target)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_ON_LAND_SELECTOR));
    }

    @Override
    public void reassessWeaponGoal() {
        if (this.isConstructed && this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            this.goalSelector.removeGoal(this.crossbowGoal);
            ItemStack bowStack = this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof BowItem));
            ItemStack crossbowStack = this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem));
            if (bowStack.getItem() instanceof BowItem) {
                int i = 20;
                if (this.level.getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(2, this.bowGoal);
            } else if(crossbowStack.getItem() instanceof CrossbowItem){
                this.goalSelector.addGoal(2, this.crossbowGoal);
            } else {
                this.goalSelector.addGoal(2, this.meleeGoal);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHARGING_CROSSBOW, false);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
    }
    
    @Override
    public void playAmbientSound() {
    	if (this.isInWater()) {
	        SoundEvent soundevent = this.getAmbientSound();
	        if (soundevent != null) {
	           this.playSound(soundevent, 0.5F, this.getVoicePitch());
	        }
    	} else {
    		super.playAmbientSound();
    	}
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? ModSoundEvents.SUNKEN_SKELETON_IDLE.get() : SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isInWater() ? ModSoundEvents.SUNKEN_SKELETON_HURT.get() : SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isInWater() ? ModSoundEvents.SUNKEN_SKELETON_DEATH.get() : SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }
    
    @Override
    protected SoundEvent getSwimSound() {
        return ModSoundEvents.SUNKEN_SKELETON_STEP.get();
    }

    @Override
    public void setChargingCrossbow(boolean chargingCrossbow) {
        this.entityData.set(CHARGING_CROSSBOW, chargingCrossbow);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float inaccuracy) {
        this.shootCrossbowProjectile(this, target, projectile, inaccuracy, 1.6F);
    }
    
    @Override
    public void shootCrossbowProjectile(LivingEntity p_234279_1_, LivingEntity p_234279_2_,
    		ProjectileEntity p_234279_3_, float p_234279_4_, float p_234279_5_) {
        double d0 = p_234279_2_.getX() - p_234279_1_.getX();
        double d1 = p_234279_2_.getZ() - p_234279_1_.getZ();
        double d2 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1);
        double d3 = p_234279_2_.getY(0.3333333333333333D) - p_234279_3_.getY() + d2 * (double)0.2F;
        Vector3f vector3f = this.getProjectileShotVector(p_234279_1_, new Vector3d(d0, d3, d1), p_234279_4_);
        p_234279_3_.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), p_234279_5_, (float)(14 - p_234279_1_.level.getDifficulty().getId() * 4));
        p_234279_1_.playSound(this.isInWater() ? ModSoundEvents.SUNKEN_SKELETON_SHOOT.get() : SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (p_234279_1_.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float p_82196_2_) {
        if(this.isHolding(item -> item instanceof BowItem)){
            super.performRangedAttack(target, p_82196_2_);
        } else{
            this.performCrossbowAttack(this, 1.6F);
        }
    }

    @Override
    public boolean canFireProjectileWeapon(ShootableItem shootable) {
        return shootable instanceof BowItem || shootable instanceof CrossbowItem;
    }
}
