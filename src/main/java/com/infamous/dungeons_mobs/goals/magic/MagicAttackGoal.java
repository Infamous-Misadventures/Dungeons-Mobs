package com.infamous.dungeons_mobs.goals.magic;

import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.MonsterEntity;

import java.util.EnumSet;

public class MagicAttackGoal<T extends MonsterEntity & IMagicUser> extends Goal {
   private final T magicUserMob;
   private final double moveSpeedAmp;
   private final float maxAttackDistanceSquared;
   private int seeTime;

   public MagicAttackGoal(T mob, double moveSpeedAmpIn, float maxAttackDistanceIn) {
      this.magicUserMob = mob;
      this.moveSpeedAmp = moveSpeedAmpIn;
      this.maxAttackDistanceSquared = maxAttackDistanceIn * maxAttackDistanceIn;
      this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean shouldExecute() {
      return this.magicUserMob.getAttackTarget() != null;
   }

   /**
    * Returns whether an in-progress EntityAIBase should continue executing
    */
   public boolean shouldContinueExecuting() {
      return ((this.shouldExecute()
              || !this.magicUserMob.getNavigator().noPath())
              && !this.magicUserMob.isUsingMagic());
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void startExecuting() {
      super.startExecuting();
      this.magicUserMob.setAggroed(true);
   }

   /**
    * Reset the task's internal state. Called when this task is interrupted by another one
    */
   public void resetTask() {
      super.resetTask();
      this.magicUserMob.setAggroed(false);
      this.seeTime = 0;
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      LivingEntity livingentity = this.magicUserMob.getAttackTarget();
      if (livingentity != null) {
         double squareDistanceToTarget = this.magicUserMob.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
         boolean flag = this.magicUserMob.getEntitySenses().canSee(livingentity);
         boolean flag1 = this.seeTime > 0;
         if (flag != flag1) {
            this.seeTime = 0;
         }

         if (flag) {
            ++this.seeTime;
         } else {
            --this.seeTime;
         }

         if (!(squareDistanceToTarget > (double)this.maxAttackDistanceSquared) && this.seeTime >= 20) {
            this.magicUserMob.getNavigator().clearPath();
         } else {
            this.magicUserMob.getNavigator().tryMoveToEntityLiving(livingentity, this.moveSpeedAmp);
         }
            this.magicUserMob.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);

      }
   }
}