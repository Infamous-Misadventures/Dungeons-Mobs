package com.infamous.dungeons_mobs.goals;

import com.infamous.dungeons_mobs.interfaces.IWebShooter;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.RangedAttackGoal;

public class RangedWebAttackGoal<T extends MobEntity & IWebShooter> extends RangedAttackGoal {
    private T webShooter;

    public RangedWebAttackGoal(T attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, maxAttackTime, maxAttackDistanceIn);
        this.webShooter = attacker;
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && this.webShooter.shouldShootWeb() && !this.webShooter.isTargetSlowedDown();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return super.shouldContinueExecuting() && !this.webShooter.isTargetSlowedDown();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.webShooter.setWebShooting(true);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.webShooter.setWebShooting(false);
    }
}
