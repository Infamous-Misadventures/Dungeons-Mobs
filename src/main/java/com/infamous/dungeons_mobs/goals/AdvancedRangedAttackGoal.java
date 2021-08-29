package com.infamous.dungeons_mobs.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.Item;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class AdvancedRangedAttackGoal<T extends MobEntity> extends SimpleRangedAttackGoal<T>{

    private final BiPredicate<T, LivingEntity> additionalStartConditions;

    public AdvancedRangedAttackGoal(T mob, Predicate<Item> weaponPredicate, BiConsumer<T, LivingEntity> performRangedAttack, double speedModifier, int attackInterval, float attackRadius, BiPredicate<T, LivingEntity> additionalStartConditions) {
        super(mob, weaponPredicate, performRangedAttack, speedModifier, attackInterval, attackInterval, attackRadius);
        this.additionalStartConditions = additionalStartConditions;

    }
    public AdvancedRangedAttackGoal(T mob, Predicate<Item> weaponPredicate, BiConsumer<T, LivingEntity> performRangedAttack, double speedModifier, int attackIntervalMin, int attackIntervalMax, float attackRadius, BiPredicate<T, LivingEntity> additionalStartConditions) {
        super(mob, weaponPredicate, performRangedAttack, speedModifier, attackIntervalMin, attackIntervalMax, attackRadius);
        this.additionalStartConditions = additionalStartConditions;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.checkAdditionalStartConditions();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.checkAdditionalStartConditions();
    }

    protected boolean checkAdditionalStartConditions() {
        return this.mob.getTarget() != null && this.additionalStartConditions.test(this.mob, this.mob.getTarget());
    }
}
