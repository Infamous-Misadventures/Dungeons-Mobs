package com.infamous.dungeons_mobs.goals;

import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.item.ShootableItem;

public class SmartZombieAttackGoal extends ZombieAttackGoal {

    public SmartZombieAttackGoal(ZombieEntity zombieIn, double speedModIn, boolean shouldCommit) {
        super(zombieIn, speedModIn, shouldCommit);
    }

    @Override
    public boolean canUse() {
        return !this.isHoldingUsableProjectileWeapon() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.isHoldingUsableProjectileWeapon() &&super.canContinueToUse();
    }

    private boolean isHoldingUsableProjectileWeapon() {
        return this.mob.isHolding((item) -> item instanceof ShootableItem
                && this.mob.canFireProjectileWeapon((ShootableItem)item));
    }
}
