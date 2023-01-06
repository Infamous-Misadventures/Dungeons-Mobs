package com.infamous.dungeons_mobs.entities.piglin;

import com.infamous.dungeons_mobs.entities.piglin.ai.FungusThrowerAi;
import com.infamous.dungeons_mobs.goals.SimpleRangedAttackGoal;
import com.infamous.dungeons_mobs.items.BlueNethershroomItem;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;

public class ZombifiedFungusThrowerEntity extends ZombifiedPiglin {
    public ZombifiedFungusThrowerEntity(EntityType<? extends ZombifiedFungusThrowerEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem shootableItem) {
        return super.canFireProjectileWeapon(shootableItem) || shootableItem instanceof BlueNethershroomItem;
    }

    @Override
    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.goalSelector.addGoal(2, new SimpleRangedAttackGoal<>(this, FungusThrowerAi.FUNGUS_ITEM_STACK_PREDICATE, FungusThrowerAi::performFungusThrow, 0.75D, 60, 6.0F));
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficultyInstance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, ModItems.BLUE_NETHERSHROOM.get().getDefaultInstance());
    }
}
