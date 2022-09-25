package com.infamous.dungeons_mobs.goals;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.interfaces.IShieldUser;

import com.infamous.dungeons_mobs.tags.CustomTags;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.Hand;

import java.util.List;

public class UseShieldGoal extends Goal {

	public int blockingFor;
	public int blockDuration;
	public int maxBlockDuration;
	public int blockChance;
	public int stopChanceAfterDurationEnds;
	public double MaxBlockDistance;
	public double MinBlockDistance;
	public boolean guaranteedBlockIfTargetNotVisible;
	
	public CreatureEntity mob;
	@Nullable
	public LivingEntity target;
	
    public UseShieldGoal(CreatureEntity attackingMob, double maxBlockDistance, double minBlockDistance, int maxBlockDuration, int stopChanceAfterDurationEnds, int blockChance, boolean guaranteedBlockIfTargetNotVisible) {
       this.blockDuration = maxBlockDuration;
		this.mob = attackingMob;
		this.target = attackingMob.getTarget();
		this.blockChance = blockChance;
		this.maxBlockDuration = maxBlockDuration;
		this.MinBlockDistance = minBlockDistance;
		this.stopChanceAfterDurationEnds = stopChanceAfterDurationEnds;
		this.MaxBlockDistance = maxBlockDistance;
		this.guaranteedBlockIfTargetNotVisible = guaranteedBlockIfTargetNotVisible;
    }
    
	@Override
	public boolean isInterruptable() {
		return false;
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}
	
	public boolean isShieldDisabled(CreatureEntity shieldUser) {
		return shieldUser instanceof IShieldUser && ((IShieldUser) shieldUser).isShieldDisabled();
	}

	@Override
	public boolean canUse() {
		target = mob.getTarget();

		List<? extends CreatureEntity> list = mob.level.getEntitiesOfClass(mob.getClass(), mob.getBoundingBox().inflate(3));
		list.removeIf(pg -> !pg.isBlocking());

		List<? extends ProjectileEntity> projectileList = mob.level.getEntitiesOfClass(ProjectileEntity.class, mob.getBoundingBox().inflate(8));

		return target != null && ((!isShieldDisabled(mob) && (((mob.getRandom().nextInt(this.blockChance) == 0 && (mob.distanceTo(target) <= MaxBlockDistance || target instanceof IRangedAttackMob || !projectileList.isEmpty()) && mob.canSee(target) && mob.getOffhandItem().getItem().isShield(mob.getOffhandItem(), mob)) || mob.isBlocking()) || (guaranteedBlockIfTargetNotVisible && !mob.canSee(target)))) || !list.isEmpty() && !target.getType().is(CustomTags.DONT_SHIELD_AGAINST));
	}

	@Override
	public boolean canContinueToUse() {
		return target != null && !isShieldDisabled(mob) && mob.getOffhandItem().getItem().isShield(mob.getOffhandItem(), mob);
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
		if (target != null && mob.distanceTo(target) <= this.MinBlockDistance && mob.getRandom().nextInt(20) == 0) {
			this.stop();
		}
	}
	
	@Override
	public void stop() {
		mob.stopUsingItem();
		this.blockingFor = 0;
	}

}
