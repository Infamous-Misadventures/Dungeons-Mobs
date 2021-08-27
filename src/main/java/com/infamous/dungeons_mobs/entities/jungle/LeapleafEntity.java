package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;

public class LeapleafEntity extends MonsterEntity {
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


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D) // 1x Golem Health
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D) // 1x Golem Attack
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D); // 1x Ravager knockback
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(2, new LeapleafEntity.StalkGoal());
        this.goalSelector.addGoal(3, new LeapleafEntity.LeapGoal());
        this.goalSelector.addGoal(4, new LeapleafEntity.AttackGoal());
        this.goalSelector.addGoal(5, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(6, new LeapleafEntity.WatchGoal(this, PlayerEntity.class, 24.0F));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, LeapleafEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_STALKING, false);
        this.entityData.define(IS_CROUCHING, false);
        this.entityData.define(IS_LEAPING, false);
    }

    @Override
    public void tick() {
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

    public boolean canLeap() {
        return this.primaryCrouchAmount == 3.0F;
    }

    public void setStalking(boolean isStalking) {
        this.entityData.set(IS_STALKING, isStalking);
    }

    public boolean isStalking() {
        return this.entityData.get(IS_STALKING);
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

    class LeapGoal extends net.minecraft.entity.ai.goal.JumpGoal {
        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (!LeapleafEntity.this.canLeap()) {
                return false;
            } else {
                LivingEntity livingentity = LeapleafEntity.this.getTarget();
                if (livingentity != null && livingentity.isAlive()) {
                    if (livingentity.getMotionDirection() != livingentity.getDirection()) {
                        return false;
                    } else {
                        boolean canLeapTowardsTarget = LeapleafEntity.canLeapTowardsTarget(LeapleafEntity.this, livingentity);
                        if (!canLeapTowardsTarget) {
                            LeapleafEntity.this.getNavigation().createPath(livingentity, 0);
                            LeapleafEntity.this.setCrouching(false);
                            LeapleafEntity.this.setStalking(false);
                        }

                        return canLeapTowardsTarget;
                    }
                } else {
                    return false;
                }
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = LeapleafEntity.this.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                double d0 = LeapleafEntity.this.getDeltaMovement().y;
                return (!(d0 * d0 < (double)0.05F) || !(Math.abs(LeapleafEntity.this.xRot) < 15.0F) || !LeapleafEntity.this.onGround);
            } else {
                return false;
            }
        }

        public boolean isInterruptable() {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            LeapleafEntity.this.setJumping(true);
            LeapleafEntity.this.setLeaping(true);
            LeapleafEntity.this.setStalking(false);
            LivingEntity attackTarget = LeapleafEntity.this.getTarget();
            if (attackTarget != null) {
                LeapleafEntity.this.getLookControl().setLookAt(attackTarget, 60.0F, 30.0F);
                Vector3d vector3d = (new Vector3d(attackTarget.getX() - LeapleafEntity.this.getX(), attackTarget.getY() - LeapleafEntity.this.getY(), attackTarget.getZ() - LeapleafEntity.this.getZ())).normalize();
                LeapleafEntity.this.setDeltaMovement(LeapleafEntity.this.getDeltaMovement().add(vector3d.x * 0.8D, 0.9D, vector3d.z * 0.8D));
                LeapleafEntity.this.getNavigation().stop();
            }
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            LeapleafEntity.this.setCrouching(false);
            LeapleafEntity.this.primaryCrouchAmount = 0.0F;
            LeapleafEntity.this.secondaryCrouchAmount = 0.0F;
            LeapleafEntity.this.setStalking(false);
            LeapleafEntity.this.setLeaping(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity attackTarget = LeapleafEntity.this.getTarget();
            if (attackTarget != null) {
                LeapleafEntity.this.getLookControl().setLookAt(attackTarget, 60.0F, 30.0F);
            }

            /*
            Vector3d vector3d = LeapleafEntity.this.getMotion();
            if (vector3d.y * vector3d.y < (double)0.03F && LeapleafEntity.this.rotationPitch != 0.0F) {
                LeapleafEntity.this.rotationPitch = MathHelper.rotLerp(LeapleafEntity.this.rotationPitch, 0.0F, 0.2F);
            } else {
                double d0 = Math.sqrt(Entity.horizontalMag(vector3d));
                double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * (double)(180F / (float)Math.PI);
                LeapleafEntity.this.rotationPitch = (float)d1;
            }
             */

            if (attackTarget != null && LeapleafEntity.this.distanceToSqr(attackTarget) <= 4.0F) {
                LeapleafEntity.this.doHurtTarget(attackTarget);
            }
        }
    }

    class AttackGoal extends MeleeAttackGoal{

        public AttackGoal() {
            super(LeapleafEntity.this, 1.2F, false);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            float adjustedAttackerWidth = LeapleafEntity.this.getBbWidth() - 0.8F;
            float attackerWidthSquaredTimes4 = adjustedAttackerWidth * 2.0F * adjustedAttackerWidth * 2.0F;
            return (double)(attackerWidthSquaredTimes4 + attackTarget.getBbWidth());
        }



        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            LeapleafEntity.this.setStalking(false);
            super.start();
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return !LeapleafEntity.this.isSleeping() && !LeapleafEntity.this.isCrouching() && super.canUse();
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
