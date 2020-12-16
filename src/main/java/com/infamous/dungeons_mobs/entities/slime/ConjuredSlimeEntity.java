package com.infamous.dungeons_mobs.entities.slime;

import com.infamous.dungeons_mobs.entities.projectiles.SlimeballEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class ConjuredSlimeEntity extends SlimeEntity implements IRangedAttackMob {

    public ConjuredSlimeEntity(World worldIn) {
        super(ModEntityTypes.CONJURED_SLIME.get(), worldIn);
    }

    public ConjuredSlimeEntity(EntityType<? extends ConjuredSlimeEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new ConjuredSlimeEntity.MoveHelperController(this);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.func_234295_eP_();
    }

    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new ConjuredSlimeEntity.FloatGoal(this));
        this.goalSelector.addGoal(2, new ImmobileRangedAttackGoal( 40, 10.0F));
        this.goalSelector.addGoal(3, new ConjuredSlimeEntity.FaceRandomGoal(this));
        //this.goalSelector.addGoal(5, new ConjuredSlimeEntity.HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (entity) -> Math.abs(entity.getPosY() - this.getPosY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public EntityType<? extends ConjuredSlimeEntity> getType() {
        return ModEntityTypes.CONJURED_SLIME.get();
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void applyEntityCollision(Entity entityIn) {
        if(!this.isSleeping()){
            if (!this.isRidingSameEntity(entityIn)) {
                if (!entityIn.noClip && !this.noClip) {
                    double d0 = entityIn.getPosX() - this.getPosX();
                    double d1 = entityIn.getPosZ() - this.getPosZ();
                    double d2 = MathHelper.absMax(d0, d1);
                    if (d2 >= (double)0.01F) {
                        d2 = (double)MathHelper.sqrt(d2);
                        d0 = d0 / d2;
                        d1 = d1 / d2;
                        double d3 = 1.0D / d2;
                        if (d3 > 1.0D) {
                            d3 = 1.0D;
                        }

                        d0 = d0 * d3;
                        d1 = d1 * d3;
                        d0 = d0 * (double)0.05F;
                        d1 = d1 * (double)0.05F;
                        d0 = d0 * (double)(1.0F - this.entityCollisionReduction);
                        d1 = d1 * (double)(1.0F - this.entityCollisionReduction);
                        if (!this.isBeingRidden()) {
                            this.addVelocity(-d0, 0.0D, -d1);
                        }

                        if (!entityIn.isBeingRidden()) {
                            entityIn.addVelocity(d0, 0.0D, d1);
                        }
                    }

                }
            }
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(PlayerEntity entityIn) {
        // NO-OP
    }

    protected void dealDamage(LivingEntity entityIn) {
        // NO-OP
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 0.625F * sizeIn.height;
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean canDamagePlayer() {
        return this.isServerWorld();
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump() {
        // NO-OP
    }


    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        double squareDistanceToTarget = this.getDistanceSq(target);
        double xDifference = target.getPosX() - this.getPosX();
        double yDifference = target.getPosYHeight(0.5D) - this.getPosYHeight(0.5D);
        double zDifference = target.getPosZ() - this.getPosZ();
        float f = MathHelper.sqrt(MathHelper.sqrt(squareDistanceToTarget)) * 0.5F;

        SlimeballEntity slimeballEntity = new SlimeballEntity(this.world,
                this,
                xDifference * (double)f,
                yDifference,
                zDifference * (double)f);
        slimeballEntity.setPosition(slimeballEntity.getPosX(),
                this.getPosYHeight(0.25D),
                slimeballEntity.getPosZ());
        this.world.addEntity(slimeballEntity);
    }


    class ImmobileRangedAttackGoal extends Goal {
        private final int minRangedAttackInterval;
        private final int maxRangedAttackTime;
        private final float attackRadius;
        private final float maxAttackDistance;
        private int rangedAttackTime = -1;
        private int seeTime;
        private int growTieredTimer;

        public ImmobileRangedAttackGoal(int maxAttackTimeIn, float maxAttackDistanceIn) {
            this.minRangedAttackInterval = maxAttackTimeIn;
            this.maxRangedAttackTime = maxAttackTimeIn;
            this.attackRadius = maxAttackDistanceIn;
            this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = ConjuredSlimeEntity.this.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                return (!(livingentity instanceof PlayerEntity)
                        || !((PlayerEntity) livingentity).abilities.disableDamage)
                        && ConjuredSlimeEntity.this.getMoveHelper() instanceof MoveHelperController;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.growTieredTimer = 300;
            super.startExecuting();
        }

        @Override
        public void resetTask() {
            this.seeTime = 0;
            this.rangedAttackTime = -1;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = ConjuredSlimeEntity.this.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (livingentity instanceof PlayerEntity && ((PlayerEntity)livingentity).abilities.disableDamage) {
                return false;
            } else {
                return --this.growTieredTimer > 0;
            }
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity attackTarget = ConjuredSlimeEntity.this.getAttackTarget();
            if(attackTarget != null){

                // HANDLE SEE TIME
                double distanceSqToTarget = ConjuredSlimeEntity.this.getDistanceSq(attackTarget.getPosX(), attackTarget.getPosY(), attackTarget.getPosZ());
                boolean canSeeTarget = ConjuredSlimeEntity.this.getEntitySenses().canSee(attackTarget);
                if (canSeeTarget) {
                    ++this.seeTime;
                } else {
                    this.seeTime = 0;
                }

                // FACE THE TARGET
                ConjuredSlimeEntity.this.faceEntity(ConjuredSlimeEntity.this.getAttackTarget(), 10.0F, 10.0F);
                ((ConjuredSlimeEntity.MoveHelperController)ConjuredSlimeEntity.this.getMoveHelper())
                        .setDirection(ConjuredSlimeEntity.this.rotationYaw);

                // ATTACK WITH RANGED ATTACK OR TICK DOWN RANGED ATTACK TIME
                if (--this.rangedAttackTime == 0) {
                    if (!canSeeTarget) {
                        return;
                    }

                    float f = MathHelper.sqrt(distanceSqToTarget) / this.attackRadius;
                    float clamp = MathHelper.clamp(f, 0.1F, 1.0F);
                    ConjuredSlimeEntity.this.attackEntityWithRangedAttack(attackTarget, clamp);
                    this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.minRangedAttackInterval) + (float)this.minRangedAttackInterval);
                } else if (this.rangedAttackTime < 0) {
                    float f2 = MathHelper.sqrt(distanceSqToTarget) / this.attackRadius;
                    this.rangedAttackTime = MathHelper.floor(f2 * (float)(this.maxRangedAttackTime - this.minRangedAttackInterval) + (float)this.minRangedAttackInterval);
                }

            }}
    }

    static class FaceRandomGoal extends Goal {
        private final ConjuredSlimeEntity slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public FaceRandomGoal(ConjuredSlimeEntity slimeIn) {
            this.slime = slimeIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return this.slime.getAttackTarget() == null
                    && (this.slime.onGround
                    || this.slime.isInWater()
                    || this.slime.isInLava()
                    || this.slime.isPotionActive(Effects.LEVITATION))
                    && this.slime.getMoveHelper() instanceof ConjuredSlimeEntity.MoveHelperController;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = 40 + this.slime.getRNG().nextInt(60);
                this.chosenDegrees = (float)this.slime.getRNG().nextInt(360);
            }

            ((ConjuredSlimeEntity.MoveHelperController)this.slime.getMoveHelper())
                    .setDirection(this.chosenDegrees);
        }
    }

    static class MoveHelperController extends MovementController {
        private float yRot;
        private final ConjuredSlimeEntity slime;

        public MoveHelperController(ConjuredSlimeEntity slimeIn) {
            super(slimeIn);
            this.slime = slimeIn;
            this.yRot = 180.0F * slimeIn.rotationYaw / (float)Math.PI;
        }

        public void setDirection(float yRotIn) {
            this.yRot = yRotIn;
        }

        public void tick() {
            this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, this.yRot, 90.0F);
            this.mob.rotationYawHead = this.mob.rotationYaw;
            this.mob.renderYawOffset = this.mob.rotationYaw;
        }
    }
}
