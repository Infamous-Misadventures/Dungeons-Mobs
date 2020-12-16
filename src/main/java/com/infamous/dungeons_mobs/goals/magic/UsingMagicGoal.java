package com.infamous.dungeons_mobs.goals.magic;

import com.infamous.dungeons_mobs.interfaces.IMagicUser;
import com.infamous.dungeons_mobs.entities.magic.MagicType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class UsingMagicGoal<T extends MobEntity & IMagicUser> extends Goal {
    private T hostMobEntity;

      public UsingMagicGoal(T magicUserMob) {
          this.hostMobEntity = magicUserMob;
         this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }

      /**
       * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
       * method as well.
       */
      public boolean shouldExecute() {
         return this.hostMobEntity.getMagicUseTicks() > 0;
      }

      /**
       * Execute a one shot task or start executing a continuous task
       */
      public void startExecuting() {
         super.startExecuting();
         this.hostMobEntity.getNavigator().clearPath();
      }

      /**
       * Reset the task's internal state. Called when this task is interrupted by another one
       */
      public void resetTask() {
         super.resetTask();
         this.hostMobEntity.setMagicType(MagicType.NONE);
      }

      /**
       * Keep ticking a continuous task that has already been started
       */
      public void tick() {
         if (this.hostMobEntity.getAttackTarget() != null) {
            this.hostMobEntity.getLookController()
                    .setLookPositionWithEntity(this.hostMobEntity.getAttackTarget(),
                            (float)this.hostMobEntity.getHorizontalFaceSpeed(),
                            (float)this.hostMobEntity.getVerticalFaceSpeed());
         }

      }
   }