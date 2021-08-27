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
        this.moveControl = new ConjuredSlimeEntity.MoveHelperController(this);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes();
    }

    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new ConjuredSlimeEntity.FloatGoal(this));
        this.goalSelector.addGoal(2, new ImmobileRangedAttackGoal( 40, 10.0F));
        this.goalSelector.addGoal(3, new ConjuredSlimeEntity.FaceRandomGoal(this));
        //this.goalSelector.addGoal(5, new ConjuredSlimeEntity.HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::canSlimeReach));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    private boolean canSlimeReach(LivingEntity target){
        return Math.abs(target.getY() - this.getY()) <= 4.0D;
    }

    @Override
    public EntityType<? extends ConjuredSlimeEntity> getType() {
        return ModEntityTypes.CONJURED_SLIME.get();
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void push(Entity entityIn) {
        if(!this.isSleeping()){
            if (!this.isPassengerOfSameVehicle(entityIn)) {
                if (!entityIn.noPhysics && !this.noPhysics) {
                    double d0 = entityIn.getX() - this.getX();
                    double d1 = entityIn.getZ() - this.getZ();
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
                        d0 = d0 * (double)(1.0F - this.pushthrough);
                        d1 = d1 * (double)(1.0F - this.pushthrough);
                        if (!this.isVehicle()) {
                            this.push(-d0, 0.0D, -d1);
                        }

                        if (!entityIn.isVehicle()) {
                            entityIn.push(d0, 0.0D, d1);
                        }
                    }

                }
            }
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void playerTouch(PlayerEntity entityIn) {
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
    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jumpFromGround() {
        // NO-OP
    }


    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        double xDifference = target.getX() - this.getX();
        double yDifference = target.getY(0.5D) - this.getY(0.5D);
        double zDifference = target.getZ() - this.getZ();
        float euclidDist = MathHelper.sqrt(xDifference * xDifference + yDifference * yDifference + zDifference * zDifference);

        SlimeballEntity slimeballEntity = new SlimeballEntity(this.level,
                this,
                0,
                0,
                0);
        slimeballEntity.setPos(slimeballEntity.getX(),
                this.getY(0.5D),
                slimeballEntity.getZ());
        slimeballEntity.shoot(xDifference, yDifference, zDifference, euclidDist, 0.0F);
        this.level.addFreshEntity(slimeballEntity);
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
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = ConjuredSlimeEntity.this.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                return (!(livingentity instanceof PlayerEntity)
                        || !((PlayerEntity) livingentity).abilities.invulnerable)
                        && ConjuredSlimeEntity.this.getMoveControl() instanceof MoveHelperController;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.growTieredTimer = 300;
            super.start();
        }

        @Override
        public void stop() {
            this.seeTime = 0;
            this.rangedAttackTime = -1;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = ConjuredSlimeEntity.this.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (livingentity instanceof PlayerEntity && ((PlayerEntity)livingentity).abilities.invulnerable) {
                return false;
            } else {
                return --this.growTieredTimer > 0;
            }
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity attackTarget = ConjuredSlimeEntity.this.getTarget();
            if(attackTarget != null){

                // HANDLE SEE TIME
                double distanceSqToTarget = ConjuredSlimeEntity.this.distanceToSqr(attackTarget.getX(), attackTarget.getY(), attackTarget.getZ());
                boolean canSeeTarget = ConjuredSlimeEntity.this.getSensing().canSee(attackTarget);
                if (canSeeTarget) {
                    ++this.seeTime;
                } else {
                    this.seeTime = 0;
                }

                // FACE THE TARGET
                ConjuredSlimeEntity.this.lookAt(ConjuredSlimeEntity.this.getTarget(), 10.0F, 10.0F);
                ((ConjuredSlimeEntity.MoveHelperController)ConjuredSlimeEntity.this.getMoveControl())
                        .setDirection(ConjuredSlimeEntity.this.yRot);

                // ATTACK WITH RANGED ATTACK OR TICK DOWN RANGED ATTACK TIME
                if (--this.rangedAttackTime == 0) {
                    if (!canSeeTarget) {
                        return;
                    }

                    float f = MathHelper.sqrt(distanceSqToTarget) / this.attackRadius;
                    float clamp = MathHelper.clamp(f, 0.1F, 1.0F);
                    ConjuredSlimeEntity.this.performRangedAttack(attackTarget, clamp);
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
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return this.slime.getTarget() == null
                    && (this.slime.onGround
                    || this.slime.isInWater()
                    || this.slime.isInLava()
                    || this.slime.hasEffect(Effects.LEVITATION))
                    && this.slime.getMoveControl() instanceof ConjuredSlimeEntity.MoveHelperController;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = 40 + this.slime.getRandom().nextInt(60);
                this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
            }

            ((ConjuredSlimeEntity.MoveHelperController)this.slime.getMoveControl())
                    .setDirection(this.chosenDegrees);
        }
    }

    static class MoveHelperController extends MovementController {
        private float yRot;
        private final ConjuredSlimeEntity slime;

        public MoveHelperController(ConjuredSlimeEntity slimeIn) {
            super(slimeIn);
            this.slime = slimeIn;
            this.yRot = 180.0F * slimeIn.yRot / (float)Math.PI;
        }

        public void setDirection(float yRotIn) {
            this.yRot = yRotIn;
        }

        public void tick() {
            this.mob.yRot = this.rotlerp(this.mob.yRot, this.yRot, 90.0F);
            this.mob.yHeadRot = this.mob.yRot;
            this.mob.yBodyRot = this.mob.yRot;
        }
    }
}
