package com.infamous.dungeons_mobs.goals.switchcombat;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.item.EggItem;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class ThrowAndMeleeAttackGoal<T extends CreatureEntity & IRangedAttackMob> extends MeleeAttackGoal
{
	private final T hostCreature;
	private int rangedAttackTime;
	private final double entityMoveSpeed;
	private int seeTime;
	private final int attackIntervalMin;
	private final int maxRangedAttackTime;
	private final float attackRadius;
	private final float maxAttackDistance;

	public ThrowAndMeleeAttackGoal(T rangedAttackMob, double speedAmplifier, int attackInterval, float maxDistance, boolean useLongMemory)
	{
		super(rangedAttackMob, speedAmplifier, useLongMemory);
			this.rangedAttackTime = -1;
			this.hostCreature = rangedAttackMob;
			this.entityMoveSpeed = speedAmplifier;
			this.attackIntervalMin = attackInterval;
			this.maxRangedAttackTime = attackInterval;
			this.attackRadius = maxDistance;
			this.maxAttackDistance = maxDistance * maxDistance;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean canUse()
	{
		if(hasThrowableItemInMainhand()){
			LivingEntity attackTarget = this.hostCreature.getTarget();
			return attackTarget != null && attackTarget.isAlive();
		}
		else{
			return super.canUse();
		}
	}

	private boolean hasThrowableItemInMainhand(){
		return this.hostCreature.getMainHandItem().getItem() instanceof SnowballItem
				| this.hostCreature.getMainHandItem().getItem() instanceof EggItem;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse()
	{
		if(hasThrowableItemInMainhand()){
			return this.canUse() || !this.hostCreature.getNavigation().isDone();
		}
		else{
			return super.canContinueToUse();
		}
	}

	/**
	 * Resets the task
	 */
	public void stop()
	{
		if(hasThrowableItemInMainhand()){
			this.seeTime = 0;
			this.rangedAttackTime = -1;
		}
		else{
			super.stop();
		}
	}

	/**
	 * Updates the task
	 */
	public void tick()
	{
		LivingEntity attackTarget = this.hostCreature.getTarget();
		if (hasThrowableItemInMainhand() && attackTarget != null) {
			double hostDistanceSq = this.hostCreature.distanceToSqr(attackTarget.getX(), attackTarget.getY(), attackTarget.getZ());
			boolean canSee = this.hostCreature.getSensing().canSee(attackTarget);
			if (canSee) {
				++this.seeTime;
			} else {
				this.seeTime = 0;
			}

			if (hostDistanceSq <= (double)this.maxAttackDistance && this.seeTime >= 5) {
				this.hostCreature.getNavigation().stop();
			} else {
				this.hostCreature.getNavigation().moveTo(attackTarget, this.entityMoveSpeed);
			}

			this.hostCreature.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
			float distanceOverAttackRadius;
			if (--this.rangedAttackTime == 0) {
				if (!canSee) {
					return;
				}

				distanceOverAttackRadius = MathHelper.sqrt(hostDistanceSq) / this.attackRadius;
				float clampedDistanceOverAttackRadius = MathHelper.clamp(distanceOverAttackRadius, 0.1F, 1.0F);

				// Used to animate snowball or egg throwing
				if(this.hasThrowableItemInMainhand()) this.hostCreature.swing(Hand.MAIN_HAND);

				this.hostCreature.performRangedAttack(attackTarget, clampedDistanceOverAttackRadius);
				this.rangedAttackTime = MathHelper.floor(distanceOverAttackRadius * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
			} else if (this.rangedAttackTime < 0) {
				distanceOverAttackRadius = MathHelper.sqrt(hostDistanceSq) / this.attackRadius;
				this.rangedAttackTime = MathHelper.floor(distanceOverAttackRadius * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
			}
		}
		else if(attackTarget != null)
		{
			super.tick();
		}
	}
}