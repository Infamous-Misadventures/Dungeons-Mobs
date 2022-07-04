package com.infamous.dungeons_mobs.entities.jungle;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.List;

public class LeapleafEntity extends MonsterEntity implements IAnimatable {
    AnimationFactory factory = new AnimationFactory(this);

    private static final DataParameter<Boolean> IS_MELEE = EntityDataManager.defineId(LeapleafEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TIMER = EntityDataManager.defineId(LeapleafEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> LEAP_COOLDOWN = EntityDataManager.defineId(LeapleafEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> ATTACK_ID = EntityDataManager.defineId(LeapleafEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> IS_READYING = EntityDataManager.defineId(LeapleafEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_READY = EntityDataManager.defineId(LeapleafEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_REST = EntityDataManager.defineId(LeapleafEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_STALKING = EntityDataManager.defineId(LeapleafEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_CROUCHING = EntityDataManager.defineId(LeapleafEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_LEAPING = EntityDataManager.defineId(LeapleafEntity.class, DataSerializers.BOOLEAN);

    private float primaryCrouchAmount;
    private float secondaryCrouchAmount;

    public LeapleafEntity(World world){
        super(ModEntityTypes.LEAPLEAF.get(), world);
    }

    public LeapleafEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.lookControl = new LeapleafEntity.LookHelperController();
        this.maxUpStep = 1.0F;
        this.xpReward = 20;
    }


    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        int distanceFallen = MathHelper.ceil(p_225503_2_ - 1.0F);
        if (distanceFallen > 0) {
            List<Entity> list = Lists.newArrayList(this.level.getEntities(this, this.getBoundingBox().inflate(1.5, 0.5, 1.5)));
            for(Entity entity : list) {
                if(entity instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity)entity;
                    livingEntity.hurt(DamageSource.mobAttack(this),distanceFallen);
                }
            }
        }
        return false;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D) // 1x Golem Health
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 21.5D) // >= Golem Attack
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D); // 1x Ravager knockback
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.isMelee()) {
            if (this.entityData.get(ATTACK_ID) == 1) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.leaper.basic_attack", false));
            }else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.leaper.heavy_attack_down", false));
            }
        } else if (this.entityData.get(IS_READYING)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.leaper.charge", false));
        } else if (this.entityData.get(IS_REST)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.leaper.rest", true));
        } else if (this.isLeaping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.leaper.heavy_attack_up", false));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.leaper.walk", true));
        } else {
            event.getController().animationSpeed = 1;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.leaper.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 0.76D));
        this.goalSelector.addGoal(1, new LeapleafEntity.LeapGoal());
        this.goalSelector.addGoal(2, new LeapleafEntity.AttackGoal(this,1.1));
        this.goalSelector.addGoal(0, new LeapleafEntity.LeapMeleeGoal());
        this.goalSelector.addGoal(0, new LeapleafEntity.MeleeGoal());
        this.goalSelector.addGoal(0, new LeapleafEntity.RestGoal());
        this.goalSelector.addGoal(1, new LeapleafEntity.ChargeGoal());
        //this.goalSelector.addGoal(5, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(8, new LeapleafEntity.WatchGoal(this, PlayerEntity.class, 24.0F));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, LeapleafEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEAP_COOLDOWN, 0);
        this.entityData.define(ATTACK_ID, 0);
        this.entityData.define(TIMER, 0);
        this.entityData.define(IS_MELEE, false);
        this.entityData.define(IS_REST, false);
        this.entityData.define(IS_READY, false);
        this.entityData.define(IS_READYING, false);
        this.entityData.define(IS_STALKING, false);
        this.entityData.define(IS_CROUCHING, false);
        this.entityData.define(IS_LEAPING, false);
    }

    @Override
    public void tick() {
        if (this.entityData.get(LEAP_COOLDOWN) > 0 && !this.entityData.get(IS_REST)) {
            this.entityData.set(LEAP_COOLDOWN, this.entityData.get(LEAP_COOLDOWN) - 1);
        }
        super.tick();
        this.tickCrouch();
    }

    private void tickCrouch() {
        this.secondaryCrouchAmount = this.primaryCrouchAmount;
        if (this.isCrouching()) {
            this.primaryCrouchAmount += 0.2F;
            if (this.primaryCrouchAmount > 3.0F) {
                this.primaryCrouchAmount = 3.0F;
            }
        } else {
            this.primaryCrouchAmount = 0.0F;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);

        compound.putBoolean("Crouching", this.isCrouching());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);

        this.setCrouching(compound.getBoolean("Crouching"));
    }

    @OnlyIn(Dist.CLIENT)
    public float getCrouchRotationPointYAddition(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.secondaryCrouchAmount, this.primaryCrouchAmount);
    }

    @Override
    public void aiStep() {
        if (this.isMelee() || this.isLeaping()) {
            this.setTimer(this.getTimer() + 1);
        }
        super.aiStep();
    }

    public boolean canLeap() {
        return this.primaryCrouchAmount == 3.0F;
    }

    public void setStalking(boolean isStalking) {
        this.entityData.set(IS_STALKING, isStalking);
    }

    public boolean isStalking() {
        return this.entityData.get(IS_STALKING);
    }

    public void setIsReadying(boolean isStalking) {
        this.entityData.set(IS_READY, isStalking);
    }

    public boolean isReadying() {
        return this.entityData.get(IS_READY);
    }

    public void setIsMelee(boolean isStalking) {
        this.entityData.set(IS_MELEE, isStalking);
    }

    public boolean isMelee() {
        return this.entityData.get(IS_MELEE);
    }

    public int getTimer() {
        return this.entityData.get(TIMER);
    }

    public void setTimer(int e45ew) {
        this.entityData.set(TIMER, e45ew);
    }

    public void setCrouching(boolean isCrouching) {
        this.entityData.set(IS_CROUCHING, isCrouching);
    }

    public boolean isLeaping() {
        return this.entityData.get(IS_LEAPING);
    }

    public void setLeaping(boolean isLeaping) {
        this.entityData.set(IS_LEAPING, isLeaping);
    }

    public static boolean canLeapTowardsTarget(LeapleafEntity leapleafEntity, LivingEntity targetEntity) {
        double zDifference = targetEntity.getZ() - leapleafEntity.getZ();
        double xDifference = targetEntity.getX() - leapleafEntity.getX();
        double zToXRatio = zDifference / xDifference;
        int leapDistance = 6;
        int leapHeight = 4;

        for(int horizontalAddition = 0; horizontalAddition < leapDistance; ++horizontalAddition) {
            double zAddition = zToXRatio == 0.0D ? 0.0D : zDifference * (double)((float)horizontalAddition / leapDistance);
            double xAddition = zToXRatio == 0.0D ? xDifference * (double)((float)horizontalAddition / leapDistance) : zAddition / zToXRatio;

            for(int yAddition = 1; yAddition < leapHeight; ++yAddition) {
                if (!leapleafEntity.level.getBlockState(new BlockPos(leapleafEntity.getX() + xAddition, leapleafEntity.getY() + (double)yAddition, leapleafEntity.getZ() + zAddition)).getMaterial().isReplaceable()) {
                    return false;
                }
            }
        }

        return true;
    }

    public class LookHelperController extends LookController {
        private LookHelperController() {
            super(LeapleafEntity.this);
        }

        protected boolean resetXRotOnTick() {
            return !LeapleafEntity.this.isStalking() && !LeapleafEntity.this.isCrouching() && !LeapleafEntity.this.isLeaping();
        }
    }

    @Override
    public boolean doHurtTarget(Entity p_70652_1_) {
        if (this.getTimer() == 0 && this.level.isClientSide) {
            this.setIsMelee(true);
        }
        return true;
    }

    class StalkGoal extends Goal {
        public StalkGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (LeapleafEntity.this.isSleeping()) {
                return false;
            } else {
                LivingEntity livingentity = LeapleafEntity.this.getTarget();
                return livingentity != null
                        && livingentity.isAlive()
                        && LeapleafEntity.this.distanceToSqr(livingentity) > 36.0D
                        && !LeapleafEntity.this.isCrouching()
                        && !LeapleafEntity.this.isStalking()
                        && !LeapleafEntity.this.jumping;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            //LeapleafEntity.this.setSitting(false);
            //.this.setStuck(false);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            LivingEntity livingentity = LeapleafEntity.this.getTarget();
            if (livingentity != null && LeapleafEntity.canLeapTowardsTarget(LeapleafEntity.this, livingentity)) {
                LeapleafEntity.this.setStalking(true);
                LeapleafEntity.this.setCrouching(true);
                LeapleafEntity.this.getNavigation().stop();
                LeapleafEntity.this.getLookControl().setLookAt(livingentity, (float)LeapleafEntity.this.getMaxHeadYRot(), (float)LeapleafEntity.this.getMaxHeadXRot());
            } else {
                LeapleafEntity.this.setStalking(false);
                LeapleafEntity.this.setCrouching(false);
            }

        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = LeapleafEntity.this.getTarget();
            if(livingentity != null){
                LeapleafEntity.this.getLookControl().setLookAt(livingentity, (float)LeapleafEntity.this.getMaxHeadYRot(), (float)LeapleafEntity.this.getMaxHeadXRot());
                if (LeapleafEntity.this.distanceToSqr(livingentity) <= 36.0D) {
                    LeapleafEntity.this.setStalking(true);
                    LeapleafEntity.this.setCrouching(true);
                    LeapleafEntity.this.getNavigation().stop();
                } else {
                    LeapleafEntity.this.getNavigation().moveTo(livingentity, 1.5D);
                }
            }
        }
    }

    class LeapMeleeGoal extends Goal {

        public LeapMeleeGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return LeapleafEntity.this.getTarget() != null &&
                    LeapleafEntity.this.entityData.get(ATTACK_ID) == 2;
        }

        @Override
        public void start() {
            LeapleafEntity.this.entityData.set(ATTACK_ID,2);
            LeapleafEntity.this.setTimer(0);
            LeapleafEntity.this.setIsMelee(true);
        }

        @Override
        public void stop() {
            LeapleafEntity.this.entityData.set(ATTACK_ID,0);
            LeapleafEntity.this.setTimer(0);
            LeapleafEntity.this.entityData.set(ATTACK_ID,0);
            LeapleafEntity.this.setIsMelee(false);
            LeapleafEntity.this.entityData.set(IS_REST,true);
        }

        @Override
        public boolean canContinueToUse() {
            return LeapleafEntity.this.getTimer() <= 15;
        }

        @Override
        public void tick() {
            if(LeapleafEntity.this.getTarget() != null && LeapleafEntity.this.getTarget().isAlive()) {
                LeapleafEntity.this.getLookControl().setLookAt(LeapleafEntity.this.getTarget(), 30.0F, 30.0F);
                if (LeapleafEntity.this.getTimer() == 2 && LeapleafEntity.this.distanceToSqr(LeapleafEntity.this.getTarget()) <= 25.6D) {
                    float attackKnockback = (float) LeapleafEntity.this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                    LivingEntity attackTarget = LeapleafEntity.this.getTarget();
                    double ratioX = (double) MathHelper.sin(LeapleafEntity.this.yRot * ((float) Math.PI / 180F));
                    double ratioZ = (double) (-MathHelper.cos(LeapleafEntity.this.yRot * ((float) Math.PI / 180F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(LeapleafEntity.this), (float) (LeapleafEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5F));
                    this.forceKnockback(attackTarget,attackKnockback * 1.25F, ratioX, ratioZ, knockbackReduction);
                    LeapleafEntity.this.setDeltaMovement(LeapleafEntity.this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }
        }

        private void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
            LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
            if(event.isCanceled()) return;
            strength = event.getStrength();
            ratioX = event.getRatioX();
            ratioZ = event.getRatioZ();
            strength = (float)((double)strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
            if (!(strength <= 0.0F)) {
                attackTarget.hasImpulse = true;
                Vector3d vector3d = attackTarget.getDeltaMovement();
                Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale((double)strength);
                attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double)strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
            }
        }
    }

    class MeleeGoal extends Goal {

        public MeleeGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        }


        @Override
        public boolean canUse() {
            return LeapleafEntity.this.getTarget() != null &&
                    LeapleafEntity.this.entityData.get(ATTACK_ID) == 1;
        }

        @Override
        public void start() {
            LeapleafEntity.this.entityData.set(ATTACK_ID,1);
            LeapleafEntity.this.setTimer(0);
            LeapleafEntity.this.setIsMelee(true);
        }

        @Override
        public void stop() {
            LeapleafEntity.this.entityData.set(ATTACK_ID,0);
            LeapleafEntity.this.setTimer(0);
            LeapleafEntity.this.setIsMelee(false);
        }

        @Override
        public boolean canContinueToUse() {
            return LeapleafEntity.this.getTimer() <= 24;
        }

        @Override
        public void tick() {
            if(LeapleafEntity.this.getTarget() != null && LeapleafEntity.this.getTarget().isAlive()) {
                LeapleafEntity.this.getLookControl().setLookAt(LeapleafEntity.this.getTarget(), 30.0F, 30.0F);
                if (LeapleafEntity.this.getTimer() == 15 && LeapleafEntity.this.distanceToSqr(LeapleafEntity.this.getTarget()) <= 18.6D + LeapleafEntity.this.getBbWidth()) {
                    float attackKnockback = (float) LeapleafEntity.this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                    LivingEntity attackTarget = LeapleafEntity.this.getTarget();
                    double ratioX = (double) MathHelper.sin(LeapleafEntity.this.yRot * ((float) Math.PI / 180F));
                    double ratioZ = (double) (-MathHelper.cos(LeapleafEntity.this.yRot * ((float) Math.PI / 180F)));
                    double knockbackReduction = 0.5D;
                    attackTarget.hurt(DamageSource.mobAttack(LeapleafEntity.this), (float) LeapleafEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.forceKnockback(attackTarget,attackKnockback * 0.5F, ratioX, ratioZ, knockbackReduction);
                    LeapleafEntity.this.setDeltaMovement(LeapleafEntity.this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }
        }

        private void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
            LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
            if(event.isCanceled()) return;
            strength = event.getStrength();
            ratioX = event.getRatioX();
            ratioZ = event.getRatioZ();
            strength = (float)((double)strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
            if (!(strength <= 0.0F)) {
                attackTarget.hasImpulse = true;
                Vector3d vector3d = attackTarget.getDeltaMovement();
                Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale((double)strength);
                attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double)strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
            }
        }
    }

    class ChargeGoal extends Goal {

        public ChargeGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        }


        @Override
        public boolean canUse() {
            return !LeapleafEntity.this.entityData.get(IS_READY) &&
                    LeapleafEntity.this.getTarget() != null &&
                    LeapleafEntity.this.entityData.get(LEAP_COOLDOWN) <= 0;
        }

        @Override
        public void start() {
            LeapleafEntity.this.setTimer(0);
            LeapleafEntity.this.entityData.set(IS_READYING,true);
        }

        @Override
        public void stop() {
            LeapleafEntity.this.setTimer(0);
            LeapleafEntity.this.entityData.set(IS_READYING,false);
        }

        @Override
        public boolean canContinueToUse() {
            return LeapleafEntity.this.getTimer() <= 20;
        }

        @Override
        public void tick() {
            if (LeapleafEntity.this.getTimer() == 20) {
                LeapleafEntity.this.entityData.set(IS_READY,true);
            }
            LeapleafEntity.this.setTimer(LeapleafEntity.this.getTimer() + 1);
        }
    }

    class RestGoal extends Goal {

        public RestGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        }


        @Override
        public boolean canUse() {
            return LeapleafEntity.this.entityData.get(IS_REST);
        }

        @Override
        public void start() {
            LeapleafEntity.this.setTimer(0);
            LeapleafEntity.this.entityData.set(IS_REST,true);
        }

        @Override
        public void stop() {
            LeapleafEntity.this.entityData.set(IS_REST,false);
            LeapleafEntity.this.setTimer(0);
        }

        @Override
        public boolean canContinueToUse() {
            return LeapleafEntity.this.getTimer() <= 100;
        }

        @Override
        public void tick() {
            if (LeapleafEntity.this.getTimer() == 20) {
                LeapleafEntity.this.entityData.set(IS_REST,false);
                LeapleafEntity.this.entityData.set(LEAP_COOLDOWN,180);
            }
            LeapleafEntity.this.entityData.set(IS_REST,true);
            LeapleafEntity.this.setTimer(LeapleafEntity.this.getTimer() + 1);
        }

    }

    //net.minecraft.entity.ai.goal.JumpGoal
    class LeapGoal extends Goal {

        public boolean i;

        @Override
        public boolean canUse() {
            return LeapleafEntity.this.entityData.get(LEAP_COOLDOWN) <= 0 &&
                    LeapleafEntity.this.isReadying() &&
                    !LeapleafEntity.this.isMelee()&&
                    !LeapleafEntity.this.entityData.get(IS_REST);
        }

        @Override
        public boolean canContinueToUse() {
            return LeapleafEntity.this.getTimer() <= 15 ||
                    (LeapleafEntity.this.getTarget() != null &&
                            !(LeapleafEntity.this.getY() <= LeapleafEntity.this.getTarget().getY() + (LeapleafEntity.this.getTarget().getBbHeight() * 1.25)));
        }

        @Override
        public void tick() {
            LeapleafEntity v = LeapleafEntity.this;
            LivingEntity c = LeapleafEntity.this.getTarget();
            if (c != null && c.isAlive()) {
                LeapleafEntity.this.getLookControl().setLookAt(c, 60.0F, 30.0F);
            }
            if (v.getTimer() == 8 && c != null && c.isAlive()) {
                this.i = true;
                Vector3d vector3d = (new Vector3d(c.getX() - LeapleafEntity.this.getX(), c.getY() - LeapleafEntity.this.getY() + (c.getBbHeight() / 0.5), c.getZ() - LeapleafEntity.this.getZ())).normalize();
                LeapleafEntity.this.setDeltaMovement(LeapleafEntity.this.getDeltaMovement().add(vector3d.x * 0.8D, 0.9D, vector3d.z * 0.8D));
            }
            if (this.i && c != null && c.isAlive()) {
                Vector3d vector3d = (new Vector3d(c.getX() - LeapleafEntity.this.getX(), c.getY() - LeapleafEntity.this.getY(), c.getZ() - LeapleafEntity.this.getZ())).normalize();
                LeapleafEntity.this.setDeltaMovement(LeapleafEntity.this.getDeltaMovement().add(vector3d.x * 0.21D, 0D, vector3d.z * 0.21D));
            }
        }

        @Override
        public void start() {
            this.i = false;
            LeapleafEntity.this.setTimer(0);
            LeapleafEntity.this.setJumping(true);
            LeapleafEntity.this.setLeaping(true);
            LeapleafEntity.this.setStalking(false);
        }

        @Override
        public void stop() {
            if (LeapleafEntity.this.getTarget() != null) {
                LeapleafEntity.this.entityData.set(ATTACK_ID,2);
                LeapleafEntity.this.doHurtTarget(LeapleafEntity.this.getTarget());
            }
            this.i = false;
            LeapleafEntity.this.setJumping(false);
            LeapleafEntity.this.entityData.set(LEAP_COOLDOWN,180);
            LeapleafEntity.this.setLeaping(false);
            LeapleafEntity.this.setStalking(false);
            LeapleafEntity.this.setIsReadying(false);
        }

    }

    class AttackGoal extends MeleeAttackGoal{
        private int maxAttackTimer = 20;
        private final double moveSpeed;
        private int delayCounter;
        private int attackTimer;

        public AttackGoal(CreatureEntity creatureEntity, double moveSpeed) {
            super(creatureEntity, moveSpeed, true);
            this.moveSpeed = moveSpeed;
        }

        @Override
        public boolean canUse() {
            return LeapleafEntity.this.getTarget() != null &&
                    LeapleafEntity.this.getTarget().isAlive() &&
                    !LeapleafEntity.this.isLeaping();
        }

        @Override
        public void start() {
            this.delayCounter = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = LeapleafEntity.this.getTarget();
            if (livingentity == null) {
                return;
            }

            LeapleafEntity.this.lookControl.setLookAt(livingentity, 30.0F, 30.0F);

            if (--this.delayCounter <= 0) {
                this.delayCounter = 4 + LeapleafEntity.this.getRandom().nextInt(3);
                LeapleafEntity.this.getNavigation().moveTo(livingentity, (double)this.moveSpeed);
            }

            this.attackTimer = Math.max(this.attackTimer - 1, 0);
            this.checkAndPerformAttack(livingentity, LeapleafEntity.this.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ()));
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            if ((distToEnemySqr <= this.getAttackReachSqr(enemy) || LeapleafEntity.this.getBoundingBox().intersects(enemy.getBoundingBox())) && this.attackTimer <= 0) {
                this.attackTimer = this.maxAttackTimer;
                LeapleafEntity.this.entityData.set(ATTACK_ID, 1);
                LeapleafEntity.this.doHurtTarget(enemy);
            }
        }

        @Override
        public void stop() {
            LeapleafEntity.this.getNavigation().stop();
            if (LeapleafEntity.this.getTarget() == null) {
                LeapleafEntity.this.setAggressive(false);
            }
        }

        public LeapleafEntity.AttackGoal setMaxAttackTick(int max) {
            this.maxAttackTimer = max;
            return this;
        }
    }

    class WatchGoal extends LookAtGoal {
        public WatchGoal(MobEntity p_i50733_2_, Class<? extends LivingEntity> p_i50733_3_, float p_i50733_4_) {
            super(p_i50733_2_, p_i50733_3_, p_i50733_4_);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return super.canUse() && !LeapleafEntity.this.isStalking();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !LeapleafEntity.this.isStalking();
        }
    }
}