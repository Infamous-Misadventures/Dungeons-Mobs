package com.infamous.dungeons_mobs.entities.piglin;

import com.infamous.dungeons_mobs.entities.piglin.ai.FungusThrowerAi;
import com.infamous.dungeons_mobs.goals.SimpleRangedAttackGoal;
import com.infamous.dungeons_mobs.items.BlueNethershroomItem;
import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ShootableItem;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class ZombifiedFungusThrowerEntity extends ZombifiedPiglinEntity {
    public ZombifiedFungusThrowerEntity(EntityType<? extends ZombifiedFungusThrowerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean canFireProjectileWeapon(ShootableItem shootableItem) {
        return super.canFireProjectileWeapon(shootableItem) || shootableItem instanceof BlueNethershroomItem;
    }

    @Override
    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.goalSelector.addGoal(2, new SimpleRangedAttackGoal<>(this, FungusThrowerAi.FUNGUS_ITEM_PREDICATE, FungusThrowerAi::performFungusThrow, 0.75D, 60, 6.0F));
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, ModItems.BLUE_NETHERSHROOM.get().getDefaultInstance());
    }
}
