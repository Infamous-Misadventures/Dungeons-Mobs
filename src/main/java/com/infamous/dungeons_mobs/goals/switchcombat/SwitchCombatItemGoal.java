package com.infamous.dungeons_mobs.goals.switchcombat;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SnowballItem;

public class SwitchCombatItemGoal extends Goal {
    private final Mob hostMob;
    private LivingEntity target;
    private final double minDistance;
    private final double maxDistance;

    public SwitchCombatItemGoal(Mob mobEntity, double minDistance, double maxDistance) {
        this.hostMob = mobEntity;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }


    private boolean hasRangedItemInMainhand() {
        return this.hostMob.getMainHandItem().getItem() instanceof ProjectileWeaponItem
                || this.hostMob.getMainHandItem().getItem() instanceof SnowballItem
                || this.hostMob.getMainHandItem().getItem() instanceof EggItem;
    }

    private boolean hasRangedItemInOffhand() {
        return this.hostMob.getOffhandItem().getItem() instanceof ProjectileWeaponItem
                || this.hostMob.getOffhandItem().getItem() instanceof SnowballItem
                || this.hostMob.getMainHandItem().getItem() instanceof EggItem;
    }

    private void swapWeapons() {
        ItemStack mainhand = this.hostMob.getMainHandItem();
        ItemStack offhand = this.hostMob.getOffhandItem();
        this.hostMob.setItemSlot(EquipmentSlot.OFFHAND, mainhand);
        this.hostMob.setItemSlot(EquipmentSlot.MAINHAND, offhand);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {
        this.target = this.hostMob.getTarget();

        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else {
            // check if we are close to the target and have a ranged item in mainhand and do not have one in offhand,
            // or if we are far from the target and we do not have a ranged item in our mainhand but do have one in our offhand
            return (
                    (this.hostMob.distanceTo(this.target) < minDistance && hasRangedItemInMainhand() && !hasRangedItemInOffhand())
                            || (this.hostMob.distanceTo(this.target) > maxDistance && !hasRangedItemInMainhand()) && hasRangedItemInOffhand())
                    && this.hostMob.hasLineOfSight(this.target);
        }
    }

    /**
     * Resets the task
     */
    public void stop() {
        target = null;
    }

    /**
     * Updates the task
     */
    public void tick() {
        if (this.hostMob.distanceTo(this.target) < minDistance && hasRangedItemInMainhand() && !hasRangedItemInOffhand()) {
            swapWeapons();
        } else if (this.hostMob.distanceTo(this.target) > maxDistance && !hasRangedItemInMainhand() && hasRangedItemInOffhand()) {
            swapWeapons();
        }
    }
}