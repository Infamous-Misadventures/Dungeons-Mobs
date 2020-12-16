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
            return this.hostMob.getHeldItemMainhand().getItem() instanceof ShootableItem
                    || this.hostMob.getHeldItemMainhand().getItem() instanceof SnowballItem
                    || this.hostMob.getHeldItemMainhand().getItem() instanceof EggItem;
        }

        private boolean hasRangedItemInOffhand()
        {
            return this.hostMob.getHeldItemOffhand().getItem() instanceof ShootableItem
                    || this.hostMob.getHeldItemOffhand().getItem() instanceof SnowballItem
                    || this.hostMob.getHeldItemMainhand().getItem() instanceof EggItem;
        }

        private void swapWeapons(){
            ItemStack mainhand = this.hostMob.getHeldItemMainhand();
            ItemStack offhand = this.hostMob.getHeldItemOffhand();
            this.hostMob.setItemStackToSlot(EquipmentSlotType.OFFHAND, mainhand);
            this.hostMob.setItemStackToSlot(EquipmentSlotType.MAINHAND, offhand);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            this.target = this.hostMob.getAttackTarget();

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
                        (this.hostMob.getDistance(this.target) < minDistance && hasRangedItemInMainhand() && !hasRangedItemInOffhand())
                            || (this.hostMob.getDistance(this.target) > maxDistance && !hasRangedItemInMainhand()) && hasRangedItemInOffhand())
                        && this.hostMob.canEntityBeSeen(this.target))
                {
                    return true;
                }

                return false;
            }
        }

        /**
         * Resets the task
         */
        public void resetTask()
        {
            target = null;
        }

        /**
         * Updates the task
         */
        public void tick()
        {
            if(this.hostMob.getDistance(this.target) < minDistance && hasRangedItemInMainhand() && !hasRangedItemInOffhand())
            {
                swapWeapons();
            }
            else if(this.hostMob.getDistance(this.target) > maxDistance && !hasRangedItemInMainhand() && hasRangedItemInOffhand())
            {
                swapWeapons();
            }
        }
    }