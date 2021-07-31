package com.infamous.dungeons_mobs.entities.piglin;

import com.infamous.dungeons_mobs.goals.SmartZombieAttackGoal;
import com.infamous.dungeons_mobs.items.BlueNethershroomItem;
import com.infamous.dungeons_mobs.mixin.GoalSelectorAccessor;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class ZombifiedFungusThrowerEntity extends ZombifiedPiglinEntity {
    public ZombifiedFungusThrowerEntity(EntityType<? extends ZombifiedFungusThrowerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        ((GoalSelectorAccessor)this.goalSelector)
                .getAvailableGoals()
                .removeIf(pg -> pg.getPriority() == 2 && pg.getGoal() instanceof SmartZombieAttackGoal);
        this.goalSelector.addGoal(2, new SmartZombieAttackGoal(this, 1.0D, false){
            @Override
            public boolean canUse() {
                return !isHoldingBlueNethershroom() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !isHoldingBlueNethershroom() && super.canContinueToUse();
            }

            private boolean isHoldingBlueNethershroom() {
                return this.mob.isHolding(item -> item instanceof BlueNethershroomItem);
            }
        });
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, ModItems.BLUE_NETHERSHROOM.get().getDefaultInstance());
    }
}
