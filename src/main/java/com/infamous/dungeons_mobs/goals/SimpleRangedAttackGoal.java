package com.infamous.dungeons_mobs.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class SimpleRangedAttackGoal<T extends MobEntity> extends Goal {
    private final T mob;
    private final BiConsumer<T, LivingEntity> performRangedAttack;
    private final Predicate<Item> weaponPredicate;
    private LivingEntity target;
    private int attackTime = -1;
    private final double speedModifier;
    private int seeTime;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;

    public SimpleRangedAttackGoal(T p_i1649_1_, Predicate<Item> weaponPredicate, BiConsumer<T, LivingEntity> performRangedAttack, double p_i1649_2_, int p_i1649_4_, float p_i1649_5_) {
        this(p_i1649_1_, weaponPredicate, performRangedAttack, p_i1649_2_, p_i1649_4_, p_i1649_4_, p_i1649_5_);
    }

    public SimpleRangedAttackGoal(T p_i1650_1_, Predicate<Item> weaponPredicate, BiConsumer<T, LivingEntity> performRangedAttack, double p_i1650_2_, int p_i1650_4_, int p_i1650_5_, float p_i1650_6_) {
        this.mob = p_i1650_1_;
        this.weaponPredicate = weaponPredicate;
        this.performRangedAttack = performRangedAttack;
        this.speedModifier = p_i1650_2_;
        this.attackIntervalMin = p_i1650_4_;
        this.attackIntervalMax = p_i1650_5_;
        this.attackRadius = p_i1650_6_;
        this.attackRadiusSqr = p_i1650_6_ * p_i1650_6_;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if(!this.mob.isHolding(this.weaponPredicate)){
            return false;
        }
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            return true;
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        if(!this.mob.isHolding(this.weaponPredicate)){
            return false;
        }
        return this.canUse() || !this.mob.getNavigation().isDone();
    }

    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public void tick() {
        double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean flag = this.mob.getSensing().canSee(this.target);
        if (flag) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!flag) {
                return;
            }

            float f = MathHelper.sqrt(d0) / this.attackRadius;
            float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
            this.performRangedAttack.accept(this.mob, this.target);
            this.attackTime = MathHelper.floor(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            float f2 = MathHelper.sqrt(d0) / this.attackRadius;
            this.attackTime = MathHelper.floor(f2 * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
        }

    }
}
