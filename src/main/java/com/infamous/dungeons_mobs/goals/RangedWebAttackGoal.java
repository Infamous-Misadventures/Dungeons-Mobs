package com.infamous.dungeons_mobs.goals;

import com.infamous.dungeons_mobs.interfaces.IWebShooter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.RangedAttackGoal;

public class RangedWebAttackGoal<T extends MobEntity & IWebShooter> extends RangedAttackGoal {
    private T webShooter;

    public RangedWebAttackGoal(T attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, maxAttackTime, maxAttackDistanceIn);
        this.webShooter = attacker;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.webShooter.getTarget();
        return super.canUse() && target != null && !this.webShooter.isTargetTrapped(target);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.webShooter.getTarget();
        return super.canContinueToUse() && target != null && !this.webShooter.isTargetTrapped(target);
    }

    @Override
    public void start() {
        super.start();
        this.webShooter.setWebShooting(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.webShooter.setWebShooting(false);
    }
}
