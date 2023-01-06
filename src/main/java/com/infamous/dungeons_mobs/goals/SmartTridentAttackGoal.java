package com.infamous.dungeons_mobs.goals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.TridentItem;

public class SmartTridentAttackGoal extends RangedAttackGoal {
    private final Mob thrower;

    public SmartTridentAttackGoal(RangedAttackMob rangedAttackMob, double speedModifier, int attackInterval, float attackRadius) {
        super(rangedAttackMob, speedModifier, attackInterval, attackRadius);
        this.thrower = (Mob) rangedAttackMob;
    }

    public boolean canUse() {
        return super.canUse() && this.thrower.isHolding(itemStack -> itemStack.getItem() instanceof TridentItem);
    }

    public void start() {
        super.start();
        this.thrower.setAggressive(true);
        this.thrower.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.thrower, item -> item instanceof TridentItem));
    }

    public void stop() {
        super.stop();
        this.thrower.stopUsingItem();
        this.thrower.setAggressive(false);
    }
}