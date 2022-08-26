package com.infamous.dungeons_mobs.goals;

import javax.annotation.Nullable;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Hand;

public class UseShieldGoal extends Goal {

	public int blockingFor;
	public int blockDuration;
	public int maxBlockDuration;
	public int blockChance;
	public int stopChanceAfterDurationEnds;
	public double blockDistance;
	
	public CreatureEntity mob;
	@Nullable
	public LivingEntity target;
	
    public UseShieldGoal(CreatureEntity attackingMob, double blockDistance, int blockDuration, int maxBlockDuration, int stopChanceAfterDurationEnds, int blockChance) {
       this.blockDuration = maxBlockDuration;
		this.mob = attackingMob;
		this.target = attackingMob.getTarget();
		this.blockChance = blockChance;
		this.maxBlockDuration = maxBlockDuration;
		this.stopChanceAfterDurationEnds = stopChanceAfterDurationEnds;
		this.blockDistance = blockDistance;
    }
    
	@Override
	public boolean isInterruptable() {
		return false;
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public boolean canUse() {
		target = mob.getTarget();
		return target != null && mob.getRandom().nextInt(this.blockChance) == 0 && mob.distanceTo(target) <= blockDistance && mob.canSee(target) && mob.getOffhandItem().getItem().isShield(mob.getOffhandItem(), mob);
	}

	@Override
	public boolean canContinueToUse() {
		return target != null && mob.invulnerableTime <= 0 && mob.getOffhandItem().getItem().isShield(mob.getOffhandItem(), mob);
	}

	@Override
	public void start() {
		mob.startUsingItem(Hand.OFF_HAND);
	}

	@Override
	public void tick() {
		target = mob.getTarget();
		this.blockingFor ++;
		
		if ((this.blockingFor >= this.blockDuration && mob.getRandom().nextInt(this.stopChanceAfterDurationEnds) == 0) || this.blockingFor >= this.maxBlockDuration) {
			this.stop();
		}
	}
	
	@Override
	public void stop() {
		mob.stopUsingItem();
		this.blockingFor = 0;
	}

}
