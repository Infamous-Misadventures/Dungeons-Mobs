package com.infamous.dungeons_mobs.goals;

import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ProjectileWeaponItem;

public class SmartZombieAttackGoal extends ZombieAttackGoal {

    public SmartZombieAttackGoal(Zombie zombieIn, double speedModIn, boolean shouldCommit) {
        super(zombieIn, speedModIn, shouldCommit);
    }

    @Override
    public boolean canUse() {
        return !this.isHoldingUsableProjectileWeapon() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.isHoldingUsableProjectileWeapon() && super.canContinueToUse();
    }

    private boolean isHoldingUsableProjectileWeapon() {
        return this.mob.isHolding((itemStack) -> itemStack.getItem() instanceof ProjectileWeaponItem
                && this.mob.canFireProjectileWeapon((ProjectileWeaponItem) itemStack.getItem()));
    }
}
