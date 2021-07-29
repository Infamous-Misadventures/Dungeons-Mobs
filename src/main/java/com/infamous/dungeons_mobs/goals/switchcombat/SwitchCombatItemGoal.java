package com.infamous.dungeons_mobs.goals.switchcombat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Hand;

public class SwitchCombatItemGoal extends Goal
    {
        private MobEntity hostMob;
        private LivingEntity target;
        private double minDistance;
        private double maxDistance;

        public SwitchCombatItemGoal(MobEntity mobEntity, double minDistance, double maxDistance) {
            this.hostMob = mobEntity;
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
        }


        private boolean hasRangedItemInMainhand()
        {
            return this.hostMob.getMainHandItem().getItem() instanceof ShootableItem
                    || this.hostMob.getMainHandItem().getItem() instanceof SnowballItem
                    || this.hostMob.getMainHandItem().getItem() instanceof EggItem;
        }

        private boolean hasRangedItemInOffhand()
        {
            return this.hostMob.getOffhandItem().getItem() instanceof ShootableItem
                    || this.hostMob.getOffhandItem().getItem() instanceof SnowballItem
                    || this.hostMob.getMainHandItem().getItem() instanceof EggItem;
        }

        private void swapWeapons(){
            ItemStack mainhand = this.hostMob.getMainHandItem();
            ItemStack offhand = this.hostMob.getOffhandItem();
            this.hostMob.setItemSlot(EquipmentSlotType.OFFHAND, mainhand);
            this.hostMob.setItemSlot(EquipmentSlotType.MAINHAND, offhand);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean canUse()
        {
            this.target = this.hostMob.getTarget();

            if (target == null)
            {
                return false;
            }
            else if (!target.isAlive())
            {
                return false;
            }
            else
            {
                // check if we are close to the target and have a ranged item in mainhand and do not have one in offhand,
                // or if we are far from the target and we do not have a ranged item in our mainhand but do have one in our offhand
                if((
                        (this.hostMob.distanceTo(this.target) < minDistance && hasRangedItemInMainhand() && !hasRangedItemInOffhand())
                            || (this.hostMob.distanceTo(this.target) > maxDistance && !hasRangedItemInMainhand()) && hasRangedItemInOffhand())
                        && this.hostMob.canSee(this.target))
                {
                    return true;
                }

                return false;
            }
        }

        /**
         * Resets the task
         */
        public void stop()
        {
            target = null;
        }

        /**
         * Updates the task
         */
        public void tick()
        {
            if(this.hostMob.distanceTo(this.target) < minDistance && hasRangedItemInMainhand() && !hasRangedItemInOffhand())
            {
                swapWeapons();
            }
            else if(this.hostMob.distanceTo(this.target) > maxDistance && !hasRangedItemInMainhand() && hasRangedItemInOffhand())
            {
                swapWeapons();
            }
        }
    }