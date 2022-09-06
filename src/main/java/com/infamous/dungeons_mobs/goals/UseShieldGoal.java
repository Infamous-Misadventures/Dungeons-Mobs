package com.infamous.dungeons_mobs.goals;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.interfaces.IShieldUser;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Hand;

public class UseShieldGoal extends Goal {

	public int blockingFor;
	public int blockDuration;
	public int maxBlockDuration;
	public int blockChance;
	public int stopChanceAfterDurationEnds;
	public double blockDistance;
	public boolean guaranteedBlockIfTargetNotVisible;
	
	public CreatureEntity mob;
	@Nullable
	public LivingEntity target;
	
    public UseShieldGoal(CreatureEntity attackingMob, double blockDistance, int blockDuration, int maxBlockDuration, int stopChanceAfterDurationEnds, int blockChance, boolean guaranteedBlockIfTargetNotVisible) {
       this.blockDuration = maxBlockDuration;
		this.mob = attackingMob;
		this.target = attackingMob.getTarget();
		this.blockChance = blockChance;
		this.maxBlockDuration = maxBlockDuration;
		this.stopChanceAfterDurationEnds = stopChanceAfterDurationEnds;
		this.blockDistance = blockDistance;
		this.guaranteedBlockIfTargetNotVisible = guaranteedBlockIfTargetNotVisible;
    }
    
	@Override
	public boolean isInterruptable() {
		return false;
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}
	
	public boolean shouldBlockForTarget(LivingEntity target) {
		if (target instanceof MobEntity && ((MobEntity)target).getTarget() != null && ((MobEntity)target).getTarget() != mob) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isShieldDisabled(CreatureEntity shieldUser) {
		if (shieldUser instanceof IShieldUser && ((IShieldUser)shieldUser).isShieldDisabled()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean canUse() {
		target = mob.getTarget();
		return target != null && !isShieldDisabled(mob) && shouldBlockForTarget(target) && (((mob.getRandom().nextInt(this.blockChance) == 0 && mob.distanceTo(target) <= blockDistance && mob.canSee(target) && mob.getOffhandItem().getItem().isShield(mob.getOffhandItem(), mob)) || mob.isBlocking()) || (guaranteedBlockIfTargetNotVisible && !mob.canSee(target)));
	}

	@Override
	public boolean canContinueToUse() {
		return target != null && mob.invulnerableTime <= 0 && !isShieldDisabled(mob) && mob.getOffhandItem().getItem().isShield(mob.getOffhandItem(), mob);
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
