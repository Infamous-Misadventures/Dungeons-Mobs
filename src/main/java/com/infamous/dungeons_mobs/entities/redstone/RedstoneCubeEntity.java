package com.infamous.dungeons_mobs.entities.redstone;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class RedstoneCubeEntity extends Monster {
    protected int rollingDuration;

    private static final EntityDataAccessor<Boolean> IS_ROLLING = SynchedEntityData.defineId(RedstoneCubeEntity.class, EntityDataSerializers.BOOLEAN);

    public RedstoneCubeEntity(Level worldIn) {
        super(ModEntityTypes.REDSTONE_CUBE.get(), worldIn);
    }

    public RedstoneCubeEntity(EntityType<? extends RedstoneCubeEntity> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new RedstoneCubeEntity.MoveHelperController(this);
        this.maxUpStep = 1.0F;
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D * 4.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)(0.2F + 0.1F * (float)2.0D * 0.5D))
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ROLLING, false);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RedstoneCubeEntity.AttackGoal(this));
        this.goalSelector.addGoal(3, new RedstoneCubeEntity.FaceRandomGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity) -> Math.abs(entity.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void push(Entity entityIn) {
        super.push(entityIn);
        if (entityIn instanceof IronGolem && this.canDamagePlayer()) {
            this.dealDamage((IronGolem)entityIn);
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void playerTouch(Player entityIn) {
        if (this.canDamagePlayer()) {
            this.dealDamage(entityIn);
        }

    }

    protected void dealDamage(LivingEntity entityIn) {
        if (this.isAlive()) {
            int i = 2; // Using biggest slime size
            if (this.distanceToSqr(entityIn) < 0.6D * (double)i * 0.6D * (double)i && this.hasLineOfSight(entityIn) && entityIn.hurt(DamageSource.mobAttack(this), this.getAttackDamageAmount())) {
                this.playSound(SoundEvents.STONE_HIT, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.doEnchantDamageEffects(this, entityIn);
            }
        }

    }

    protected float getAttackDamageAmount() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.625F * sizeIn.height;
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean canDamagePlayer() {
        return this.isEffectiveAi();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.STONE_HIT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.STONE_BREAK;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getMaxHeadXRot() {
        return 0;
    }


    static class AttackGoal extends Goal {
        private final RedstoneCubeEntity redstoneCubeEntity;
        private int growTieredTimer;

        AttackGoal(RedstoneCubeEntity cubeIn) {
            this.redstoneCubeEntity = cubeIn;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = this.redstoneCubeEntity.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                return (!(livingentity instanceof Player) || !((Player) livingentity).getAbilities().invulnerable);
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.growTieredTimer = 300;
            super.start();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = this.redstoneCubeEntity.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (livingentity instanceof Player && ((Player)livingentity).getAbilities().invulnerable) {
                return false;
            } else {
                return --this.growTieredTimer > 0;
            }
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.redstoneCubeEntity.lookAt(this.redstoneCubeEntity.getTarget(), 10.0F, 10.0F);
            ((RedstoneCubeEntity.MoveHelperController)this.redstoneCubeEntity.getMoveControl()).setDirection(this.redstoneCubeEntity.getYRot(), this.redstoneCubeEntity.canDamagePlayer());

        }
    }

    static class FaceRandomGoal extends Goal {
        private final RedstoneCubeEntity redstoneCubeEntity;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public FaceRandomGoal(RedstoneCubeEntity cubeIn) {
            this.redstoneCubeEntity = cubeIn;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return this.redstoneCubeEntity.getTarget() == null && (this.redstoneCubeEntity.isOnGround() || this.redstoneCubeEntity.isInWater() || this.redstoneCubeEntity.isInLava() || this.redstoneCubeEntity.hasEffect(MobEffects.LEVITATION));
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = 40 + this.redstoneCubeEntity.getRandom().nextInt(60);
                this.chosenDegrees = (float)this.redstoneCubeEntity.getRandom().nextInt(360);
            }

            ((RedstoneCubeEntity.MoveHelperController)this.redstoneCubeEntity.getMoveControl())
                    .setDirection(this.chosenDegrees, false);
        }
    }

    static class MoveHelperController extends MoveControl {
        private float yRot;
        private final RedstoneCubeEntity redstoneCubeEntity;
        private boolean isAggressive;

        public MoveHelperController(RedstoneCubeEntity cubeIn) {
            super(cubeIn);
            this.redstoneCubeEntity = cubeIn;
            this.yRot = 180.0F * cubeIn.getYRot() / (float)Math.PI;
        }

        public void setDirection(float yRotIn, boolean aggressive) {
            this.yRot = yRotIn;
            this.isAggressive = aggressive;
        }

        public void setSpeed(double speedIn) {
            this.speedModifier = speedIn;
            this.operation = MoveControl.Operation.MOVE_TO;
        }

        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();
            if (this.operation == Operation.WAIT) {
                this.mob.setZza(0.0F);
            } else if(this.operation == Operation.MOVE_TO) {
                this.operation = MoveControl.Operation.WAIT;
                if (this.mob.isOnGround() || this.mob.isInWater()) {
                    this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if(!this.redstoneCubeEntity.isRolling() && this.redstoneCubeEntity.shouldRoll()){
                        this.redstoneCubeEntity.startRolling(20);
                    }
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(this.isRolling()){
            if(this.level.isClientSide){
                this.level.addParticle(DustParticleOptions.REDSTONE,
                        this.getRandomX(0.5D) + 1.0D,
                        this.getRandomY() - 0.25D + 1.0D,
                        this.getRandomZ(0.5D) + 1.0D,
                        (this.random.nextDouble() - 0.5D) * 2.0D,
                        -this.random.nextDouble(),
                        (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        }
        if (this.rollingDuration > 0) {
            --this.rollingDuration;
            this.updateRoll();
        }
        else{
            this.stopRolling();
        }
    }

    public void startRolling(int timeIn) {
        this.rollingDuration = timeIn;
        if (!this.level.isClientSide) {
            this.setIsRolling(true);
        }
    }

    public void stopRolling() {
        this.rollingDuration = 0;
        if (!this.level.isClientSide) {
            this.setIsRolling(false);
        }
    }

    public boolean shouldRoll(){
        return (this.isOnGround() || this.isInWater());
    }

    protected void updateRoll() {
        if (!this.shouldRoll()) {
            this.rollingDuration = 0;
        }

        if (!this.level.isClientSide && this.rollingDuration <= 0) {
            this.setIsRolling(false);
        }
    }

    public void setIsRolling(boolean isRolling){
        this.entityData.set(IS_ROLLING, isRolling);
    }

    public boolean isRolling() {
        return this.entityData.get(IS_ROLLING);
    }

}
