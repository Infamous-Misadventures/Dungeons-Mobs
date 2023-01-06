package com.infamous.dungeons_mobs.goals;

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class SimpleRangedAttackGoal<T extends Mob> extends Goal {
	protected final T mob;
	protected final BiConsumer<T, LivingEntity> performRangedAttack;
	protected final Predicate<ItemStack> weaponPredicate;
	protected LivingEntity target;
	protected int attackTime = -1;
	protected final double speedModifier;
	protected int seeTime;
	protected final int attackIntervalMin;
	protected final int attackIntervalMax;
	protected final float attackRadius;
	protected final float attackRadiusSqr;

	public SimpleRangedAttackGoal(T p_i1649_1_, Predicate<ItemStack> weaponPredicate,
			BiConsumer<T, LivingEntity> performRangedAttack, double speedModifier, int attackInterval,
			float attackRadius) {
		this(p_i1649_1_, weaponPredicate, performRangedAttack, speedModifier, attackInterval, attackInterval,
				attackRadius);
	}

	public SimpleRangedAttackGoal(T p_i1650_1_, Predicate<ItemStack> weaponPredicate,
			BiConsumer<T, LivingEntity> performRangedAttack, double speedModifier, int attackIntervalMin,
			int attackIntervalMax, float attackRadius) {
		this.mob = p_i1650_1_;
		this.weaponPredicate = weaponPredicate;
		this.performRangedAttack = performRangedAttack;
		this.speedModifier = speedModifier;
		this.attackIntervalMin = attackIntervalMin;
		this.attackIntervalMax = attackIntervalMax;
		this.attackRadius = attackRadius;
		this.attackRadiusSqr = attackRadius * attackRadius;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	public boolean canUse() {
		if (!this.mob.isHolding(this.weaponPredicate)) {
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
		if (!this.mob.isHolding(this.weaponPredicate)) {
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
		boolean flag = this.mob.getSensing().hasLineOfSight(this.target);
		if (flag) {
			++this.seeTime;
		} else {
			this.seeTime = 0;
		}

		if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 5) {
			this.mob.getNavigation().stop();
		} else {
			this.mob.getNavigation().moveTo(this.target, this.speedModifier);
		}

		this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
		if (--this.attackTime == 0) {
			if (!flag) {
				return;
			}

			float f = Mth.sqrt((float) d0) / this.attackRadius;
			float lvt_5_1_ = Mth.clamp(f, 0.1F, 1.0F);
			this.performRangedAttack.accept(this.mob, this.target);
			this.attackTime = Mth.floor(
					f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
		} else if (this.attackTime < 0) {
			float f2 = Mth.sqrt((float) d0) / this.attackRadius;
			this.attackTime = Mth.floor(
					f2 * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
		}

	}
}
