package com.infamous.dungeons_mobs.goals.geo_combat;

import com.infamous.dungeons_mobs.interfaces.IGeoEntityMeleeAttack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GeoMobsMeleeGoal<T extends MobEntity & IGeoEntityMeleeAttack> extends Goal {

    protected T entity;

    protected boolean areaAttack;

    protected boolean CanWalkWhenMelee;

    protected int WalkSpeedWhenMelee;

    protected int DamageTick;

    protected int interruptMeleeTick;

    protected int DamageValue;

    public GeoMobsMeleeGoal(T livingEntity,
                            boolean canWalk,
                            boolean isAreaAttack,
                            int MeleeAnimationTick,
                            int InterruptMeleeTick,
                            int WalkWhenMeleeSpeed,
                            int damageValue) {
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        this.entity = livingEntity;
        this.areaAttack = isAreaAttack;
        this.CanWalkWhenMelee = canWalk;
        this.WalkSpeedWhenMelee = WalkWhenMeleeSpeed;
        this.interruptMeleeTick = InterruptMeleeTick;
        this.DamageTick = MeleeAnimationTick;
        this.DamageValue = damageValue;
    }

    public GeoMobsMeleeGoal(T livingEntity,
                            boolean canWalk,
                            boolean isAreaAttack,
                            int MeleeAnimationTick,
                            int InterruptMeleeTick,
                            int WalkWhenMeleeSpeed) {
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        this.entity = livingEntity;
        this.areaAttack = isAreaAttack;
        this.CanWalkWhenMelee = canWalk;
        this.interruptMeleeTick = InterruptMeleeTick;
        this.WalkSpeedWhenMelee = WalkWhenMeleeSpeed;
        this.DamageTick = MeleeAnimationTick;
    }

    @Override
    public boolean canUse() {
        return this.entity.CanMelee() && this.entity.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
        this.entity.setTimer(0);
        this.entity.setMelee(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setTimer(0);
        this.entity.setCanMelee(false);
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.Timer() <= this.interruptMeleeTick;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }
}
