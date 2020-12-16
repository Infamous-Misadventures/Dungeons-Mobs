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
    private static final DataParameter<Boolean> IS_STALKING = EntityDataManager.createKey(LeapleafEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_CROUCHING = EntityDataManager.createKey(LeapleafEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_LEAPING = EntityDataManager.createKey(LeapleafEntity.class, DataSerializers.BOOLEAN);

    private float primaryCrouchAmount;
    private float secondaryCrouchAmount;

    public LeapleafEntity(World world){
        super(ModEntityTypes.LEAPLEAF.get(), world);
    }

    public LeapleafEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.lookController = new LeapleafEntity.LookHelperController();
        this.stepHeight = 1.0F;
        this.experienceValue = 20;
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.func_234295_eP_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 100.0D) // 1x Golem Health
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 15.0D) // 1x Golem Attack
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.5D); // 1x Ravager knockback
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

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, LeapleafEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(IS_STALKING, false);
        this.dataManager.register(IS_CROUCHING, false);
        this.dataManager.register(IS_LEAPING, false);
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
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        compound.putBoolean("Crouching", this.isCrouching());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);

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
        this.dataManager.set(IS_STALKING, isStalking);
    }

    public boolean isStalking() {
        return this.dataManager.get(IS_STALKING);
    }

    public void setCrouching(boolean isCrouching) {
        this.dataManager.set(IS_CROUCHING, isCrouching);
    }

    public boolean isLeaping() {
        return this.dataManager.get(IS_LEAPING);
    }

    public void setLeaping(boolean isLeaping) {
        this.dataManager.set(IS_LEAPING, isLeaping);
    }

    public static boolean canLeapTowardsTarget(LeapleafEntity leapleafEntity, LivingEntity targetEntity) {
        double zDifference = targetEntity.getPosZ() - leapleafEntity.getPosZ();
        double xDifference = targetEntity.getPosX() - leapleafEntity.getPosX();
        double zToXRatio = zDifference / xDifference;
        int leapDistance = 6;
        int leapHeight = 4;

        for(int horizontalAddition = 0; horizontalAddition < leapDistance; ++horizontalAddition) {
            double zAddition = zToXRatio == 0.0D ? 0.0D : zDifference * (double)((float)horizontalAddition / leapDistance);
            double xAddition = zToXRatio == 0.0D ? xDifference * (double)((float)horizontalAddition / leapDistance) : zAddition / zToXRatio;

            for(int yAddition = 1; yAddition < leapHeight; ++yAddition) {
                if (!leapleafEntity.world.getBlockState(new BlockPos(leapleafEntity.getPosX() + xAddition, leapleafEntity.getPosY() + (double)yAddition, leapleafEntity.getPosZ() + zAddition)).getMaterial().isReplaceable()) {
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

        protected boolean shouldResetPitch() {
            return !LeapleafEntity.this.isStalking() && !LeapleafEntity.this.isCrouching() && !LeapleafEntity.this.isLeaping();
        }
    }

    class StalkGoal extends Goal {
        public StalkGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (LeapleafEntity.this.isSleeping()) {
                return false;
            } else {
                LivingEntity livingentity = LeapleafEntity.this.getAttackTarget();
                return livingentity != null
                        && livingentity.isAlive()
                        && LeapleafEntity.this.getDistanceSq(livingentity) > 36.0D
                        && !LeapleafEntity.this.isCrouching()
                        && !LeapleafEntity.this.isStalking()
                        && !LeapleafEntity.this.isJumping;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            //LeapleafEntity.this.setSitting(false);
            //.this.setStuck(false);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            LivingEntity livingentity = LeapleafEntity.this.getAttackTarget();
            if (livingentity != null && LeapleafEntity.canLeapTowardsTarget(LeapleafEntity.this, livingentity)) {
                LeapleafEntity.this.setStalking(true);
                LeapleafEntity.this.setCrouching(true);
                LeapleafEntity.this.getNavigator().clearPath();
                LeapleafEntity.this.getLookController().setLookPositionWithEntity(livingentity, (float)LeapleafEntity.this.getHorizontalFaceSpeed(), (float)LeapleafEntity.this.getVerticalFaceSpeed());
            } else {
                LeapleafEntity.this.setStalking(false);
                LeapleafEntity.this.setCrouching(false);
            }

        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = LeapleafEntity.this.getAttackTarget();
            if(livingentity != null){
                LeapleafEntity.this.getLookController().setLookPositionWithEntity(livingentity, (float)LeapleafEntity.this.getHorizontalFaceSpeed(), (float)LeapleafEntity.this.getVerticalFaceSpeed());
                if (LeapleafEntity.this.getDistanceSq(livingentity) <= 36.0D) {
                    LeapleafEntity.this.setStalking(true);
                    LeapleafEntity.this.setCrouching(true);
                    LeapleafEntity.this.getNavigator().clearPath();
                } else {
                    LeapleafEntity.this.getNavigator().tryMoveToEntityLiving(livingentity, 1.5D);
                }
            }
        }
    }

    class LeapGoal extends net.minecraft.entity.ai.goal.JumpGoal {
        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (!LeapleafEntity.this.canLeap()) {
                return false;
            } else {
                LivingEntity livingentity = LeapleafEntity.this.getAttackTarget();
                if (livingentity != null && livingentity.isAlive()) {
                    if (livingentity.getAdjustedHorizontalFacing() != livingentity.getHorizontalFacing()) {
                        return false;
                    } else {
                        boolean canLeapTowardsTarget = LeapleafEntity.canLeapTowardsTarget(LeapleafEntity.this, livingentity);
                        if (!canLeapTowardsTarget) {
                            LeapleafEntity.this.getNavigator().getPathToEntity(livingentity, 0);
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
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = LeapleafEntity.this.getAttackTarget();
            if (livingentity != null && livingentity.isAlive()) {
                double d0 = LeapleafEntity.this.getMotion().y;
                return (!(d0 * d0 < (double)0.05F) || !(Math.abs(LeapleafEntity.this.rotationPitch) < 15.0F) || !LeapleafEntity.this.onGround);
            } else {
                return false;
            }
        }

        public boolean isPreemptible() {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            LeapleafEntity.this.setJumping(true);
            LeapleafEntity.this.setLeaping(true);
            LeapleafEntity.this.setStalking(false);
            LivingEntity attackTarget = LeapleafEntity.this.getAttackTarget();
            if (attackTarget != null) {
                LeapleafEntity.this.getLookController().setLookPositionWithEntity(attackTarget, 60.0F, 30.0F);
                Vector3d vector3d = (new Vector3d(attackTarget.getPosX() - LeapleafEntity.this.getPosX(), attackTarget.getPosY() - LeapleafEntity.this.getPosY(), attackTarget.getPosZ() - LeapleafEntity.this.getPosZ())).normalize();
                LeapleafEntity.this.setMotion(LeapleafEntity.this.getMotion().add(vector3d.x * 0.8D, 0.9D, vector3d.z * 0.8D));
                LeapleafEntity.this.getNavigator().clearPath();
            }
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
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
            LivingEntity attackTarget = LeapleafEntity.this.getAttackTarget();
            if (attackTarget != null) {
                LeapleafEntity.this.getLookController().setLookPositionWithEntity(attackTarget, 60.0F, 30.0F);
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

            if (attackTarget != null && LeapleafEntity.this.getDistanceSq(attackTarget) <= 4.0F) {
                LeapleafEntity.this.attackEntityAsMob(attackTarget);
            }
        }
    }

    class AttackGoal extends MeleeAttackGoal{

        public AttackGoal() {
            super(LeapleafEntity.this, 1.2F, false);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            float adjustedAttackerWidth = LeapleafEntity.this.getWidth() - 0.8F;
            float attackerWidthSquaredTimes4 = adjustedAttackerWidth * 2.0F * adjustedAttackerWidth * 2.0F;
            return (double)(attackerWidthSquaredTimes4 + attackTarget.getWidth());
        }



        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            LeapleafEntity.this.setStalking(false);
            super.startExecuting();
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return !LeapleafEntity.this.isSleeping() && !LeapleafEntity.this.isCrouching() && super.shouldExecute();
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
        public boolean shouldExecute() {
            return super.shouldExecute() && !LeapleafEntity.this.isStalking();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && !LeapleafEntity.this.isStalking();
        }
    }
}
