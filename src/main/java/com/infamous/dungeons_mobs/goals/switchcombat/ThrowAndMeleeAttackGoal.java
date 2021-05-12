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
			this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if(hasThrowableItemInMainhand()){
			LivingEntity attackTarget = this.hostCreature.getAttackTarget();
			return attackTarget != null && attackTarget.isAlive();
		}
		else{
			return super.shouldExecute();
		}
	}

	private boolean hasThrowableItemInMainhand(){
		return this.hostCreature.getHeldItemMainhand().getItem() instanceof SnowballItem
				| this.hostCreature.getHeldItemMainhand().getItem() instanceof EggItem;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		if(hasThrowableItemInMainhand()){
			return this.shouldExecute() || !this.hostCreature.getNavigator().noPath();
		}
		else{
			return super.shouldContinueExecuting();
		}
	}

	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		if(hasThrowableItemInMainhand()){
			this.seeTime = 0;
			this.rangedAttackTime = -1;
		}
		else{
			super.resetTask();
		}
	}

	/**
	 * Updates the task
	 */
	public void tick()
	{
		LivingEntity attackTarget = this.hostCreature.getAttackTarget();
		if (hasThrowableItemInMainhand() && attackTarget != null) {
			double hostDistanceSq = this.hostCreature.getDistanceSq(attackTarget.getPosX(), attackTarget.getPosY(), attackTarget.getPosZ());
			boolean canSee = this.hostCreature.getEntitySenses().canSee(attackTarget);
			if (canSee) {
				++this.seeTime;
			} else {
				this.seeTime = 0;
			}

			if (hostDistanceSq <= (double)this.maxAttackDistance && this.seeTime >= 5) {
				this.hostCreature.getNavigator().clearPath();
			} else {
				this.hostCreature.getNavigator().tryMoveToEntityLiving(attackTarget, this.entityMoveSpeed);
			}

			this.hostCreature.getLookController().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
			float distanceOverAttackRadius;
			if (--this.rangedAttackTime == 0) {
				if (!canSee) {
					return;
				}

				distanceOverAttackRadius = MathHelper.sqrt(hostDistanceSq) / this.attackRadius;
				float clampedDistanceOverAttackRadius = MathHelper.clamp(distanceOverAttackRadius, 0.1F, 1.0F);

				// Used to animate snowball or egg throwing
				if(this.hasThrowableItemInMainhand()) this.hostCreature.swingArm(Hand.MAIN_HAND);

				this.hostCreature.attackEntityWithRangedAttack(attackTarget, clampedDistanceOverAttackRadius);
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