package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.goals.AquaticMoveHelperController;
import com.infamous.dungeons_mobs.goals.GoToBeachGoal;
import com.infamous.dungeons_mobs.goals.GoToWaterGoal;
import com.infamous.dungeons_mobs.goals.SwimUpGoal;
import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.mojang.math.Vector3f;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class SunkenSkeletonEntity extends AbstractSkeleton implements CrossbowAttackMob, IAquaticMob {
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

    private static final EntityDataAccessor<Boolean> CHARGING_CROSSBOW = SynchedEntityData.defineId(SunkenSkeletonEntity.class, EntityDataSerializers.BOOLEAN);

    private boolean searchingForLand;
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation groundNavigation;
    private final boolean isConstructed;

    public SunkenSkeletonEntity(EntityType<? extends SunkenSkeletonEntity> entityType, Level world) {
        super(entityType, world);
        this.isConstructed = true;
        this.maxUpStep = 1.0F;
        this.moveControl = new AquaticMoveHelperController<>(this);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.waterNavigation = new WaterBoundPathNavigation(this, world);
        this.groundNavigation = new GroundPathNavigation(this, world);
    }



    public static AttributeSupplier.Builder setCustomAttributes() {
        return AbstractSkeleton.createAttributes();
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldReader) {
        return worldReader.isUnobstructed(this);
    }

    @Override
    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }

    @Override
    public void travel(Vec3 travelVec) {
        this.checkAquaticTravel(this, travelVec);
    }

    @Override
    public void normalTravel(Vec3 travelVec) {
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
    public void setNavigation(PathNavigation navigation) {
        this.navigation = navigation;
    }

    @Override
    public GroundPathNavigation getGroundNavigation() {
        return this.groundNavigation;
    }

    @Override
    public WaterBoundPathNavigation getWaterNavigation() {
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
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, SunkenSkeletonEntity.class)).setAlertOthers(SunkenSkeletonEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, target -> this.okTarget(this, target)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    @Override
    public void reassessWeaponGoal() {
        if (this.isConstructed && this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            this.goalSelector.removeGoal(this.crossbowGoal);
            ItemStack bowStack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem));
            ItemStack crossbowStack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem));
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
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance p_180481_1_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
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
    public void shootCrossbowProjectile(LivingEntity target, ItemStack crossbow, Projectile projectile, float inaccuracy) {
        this.shootCrossbowProjectile(this, target, projectile, inaccuracy, 1.6F);
    }
    
    @Override
    public void shootCrossbowProjectile(LivingEntity p_234279_1_, LivingEntity p_234279_2_,
    		Projectile p_234279_3_, float p_234279_4_, float p_234279_5_) {
        double d0 = p_234279_2_.getX() - p_234279_1_.getX();
        double d1 = p_234279_2_.getZ() - p_234279_1_.getZ();
        double d2 = (double)Mth.sqrt((float) (d0 * d0 + d1 * d1));
        double d3 = p_234279_2_.getY(0.3333333333333333D) - p_234279_3_.getY() + d2 * (double)0.2F;
        Vector3f vector3f = this.getProjectileShotVector(p_234279_1_, new Vec3(d0, d3, d1), p_234279_4_);
        p_234279_3_.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), p_234279_5_, (float)(14 - p_234279_1_.level.getDifficulty().getId() * 4));
        p_234279_1_.playSound(this.isInWater() ? ModSoundEvents.SUNKEN_SKELETON_SHOOT.get() : SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (p_234279_1_.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float p_82196_2_) {
        if(this.isHolding(itemStack -> itemStack.getItem() instanceof BowItem)){
            super.performRangedAttack(target, p_82196_2_);
        } else{
            this.performCrossbowAttack(this, 1.6F);
        }
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem shootable) {
        return shootable instanceof BowItem || shootable instanceof CrossbowItem;
    }
}
