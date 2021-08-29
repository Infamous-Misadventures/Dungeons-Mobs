package com.infamous.dungeons_mobs.goals;

import com.infamous.dungeons_mobs.utils.ModProjectileHelper;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.BowItem;

import java.util.EnumSet;

public class ModdedRangedBowAttackGoal<T extends MonsterEntity & IRangedAttackMob> extends Goal {
   private final T entity;
   private final double moveSpeedAmp;
   private int attackCooldown;
   private final float maxAttackDistanceSq;
   private int attackTime = -1;
   private int seeTime;
   private boolean strafingClockwise;
   private boolean strafingBackwards;
   private int strafingTime = -1;

   public ModdedRangedBowAttackGoal(T mob, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
      this.entity = mob;
      this.moveSpeedAmp = moveSpeedAmpIn;
      this.attackCooldown = attackCooldownIn;
      this.maxAttackDistanceSq = maxAttackDistanceIn * maxAttackDistanceIn;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   public void setAttackCooldown(int attackCooldownIn) {
      this.attackCooldown = attackCooldownIn;
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean canUse() {
      return this.entity.getTarget() != null && this.isBowInMainhand();
   }


    private boolean isBowInMainhand() {
        return this.entity.isHolding(item -> item instanceof BowItem);
    }

   /**
    * Returns whether an in-progress EntityAIBase should continue executing
    */
   public boolean canContinueToUse() {
      return (this.canUse() || !this.entity.getNavigation().isDone()) && this.isBowInMainhand();
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void start() {
      super.start();
      this.entity.setAggressive(true);
   }

   /**
    * Reset the task's internal state. Called when this task is interrupted by another one
    */
   public void stop() {
      super.stop();
      this.entity.setAggressive(false);
      this.seeTime = 0;
      this.attackTime = -1;
      this.entity.stopUsingItem();
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      LivingEntity livingentity = this.entity.getTarget();
      if (livingentity != null) {
         double d0 = this.entity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
         boolean flag = this.entity.getSensing().canSee(livingentity);
         boolean flag1 = this.seeTime > 0;
         if (flag != flag1) {
            this.seeTime = 0;
         }

         if (flag) {
            ++this.seeTime;
         } else {
            --this.seeTime;
         }

         if (!(d0 > (double)this.maxAttackDistanceSq) && this.seeTime >= 20) {
            this.entity.getNavigation().stop();
            ++this.strafingTime;
         } else {
            this.entity.getNavigation().moveTo(livingentity, this.moveSpeedAmp);
            this.strafingTime = -1;
         }

         if (this.strafingTime >= 20) {
            if ((double)this.entity.getRandom().nextFloat() < 0.3D) {
               this.strafingClockwise = !this.strafingClockwise;
            }

            if ((double)this.entity.getRandom().nextFloat() < 0.3D) {
               this.strafingBackwards = !this.strafingBackwards;
            }

            this.strafingTime = 0;
         }

         if (this.strafingTime > -1) {
            if (d0 > (double)(this.maxAttackDistanceSq * 0.75F)) {
               this.strafingBackwards = false;
            } else if (d0 < (double)(this.maxAttackDistanceSq * 0.25F)) {
               this.strafingBackwards = true;
            }

            this.entity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
            this.entity.lookAt(livingentity, 30.0F, 30.0F);
         } else {
            this.entity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
         }

         if (this.entity.isUsingItem()) {
            if (!flag && this.seeTime < -60) {
               this.entity.stopUsingItem();
            } else if (flag) {
               int i = this.entity.getTicksUsingItem();
               if (i >= 20) {
                  this.entity.stopUsingItem();
                  this.entity.performRangedAttack(livingentity, BowItem.getPowerForTime(i));
                  this.attackTime = this.attackCooldown;
               }
            }
         } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
             // Silly Mojang, checking for the hand holding Items.BOW
             this.entity.startUsingItem(ModProjectileHelper.getHandWith(this.entity, item -> item instanceof BowItem));
         }

      }
   }
}