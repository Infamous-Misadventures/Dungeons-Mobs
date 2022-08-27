package com.infamous.dungeons_mobs.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class AvoidAndApproachTargetGoal  extends MeleeAttackGoal {
    private final double moveSpeed;
    private int delayCounter;
    public double maxDistanceToTarget;
    public double minDistanceToTarget;
    public double setAvoidToTargetDist;

    public CreatureEntity v;

    public AvoidAndApproachTargetGoal(CreatureEntity creatureEntity,
                                      double moveSpeed,
                                      double maxDistanceToTarget,
                                      double minDistanceToTarget,
                                      double AvoidToTargetDist) {
        super(creatureEntity, moveSpeed, true);
        this.v = creatureEntity;
        this.moveSpeed = moveSpeed;
        this.maxDistanceToTarget = maxDistanceToTarget;
        this.minDistanceToTarget = minDistanceToTarget;
        this.setAvoidToTargetDist = AvoidToTargetDist;
    }

    @Override
    public boolean canUse() {
        return v.getTarget() != null && v.getTarget().isAlive();
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.v.getTarget();
        if (livingentity != null) {
            this.v.getLookControl().setLookAt(livingentity,30,30);
            if (--this.delayCounter <= 0) {
                this.delayCounter = 8 + this.v.getRandom().nextInt(5);
                if (this.v.distanceToSqr(livingentity) <= this.minDistanceToTarget) {
                    this.v.getNavigation().moveTo(
                            livingentity.getX() + (livingentity.getRandom().nextInt((int) (this.setAvoidToTargetDist / 2)) + (this.setAvoidToTargetDist / 2)) * (livingentity.getRandom().nextBoolean() ? 1.14 : -1.14),
                            livingentity.getY(),
                            livingentity.getZ() + (livingentity.getRandom().nextInt((int) (this.setAvoidToTargetDist / 2)) + (this.setAvoidToTargetDist / 2)) * (livingentity.getRandom().nextBoolean() ? 1.14 : -1.14),
                            (double) this.moveSpeed + 0.05
                    );

                    this.delayCounter = 18 + this.v.getRandom().nextInt(12);

                }else {
                    if (this.v.distanceToSqr(livingentity) >= this.maxDistanceToTarget) {
                        this.v.getNavigation().moveTo(livingentity,
                                (double) this.moveSpeed
                        );
                    }
                }
                if (this.v.distanceToSqr(livingentity) >= this.minDistanceToTarget + 10 && v.distanceToSqr(livingentity) <= this.maxDistanceToTarget - 10) {
                    this.v.getNavigation().stop();
                }
            }
        }
    }

}
