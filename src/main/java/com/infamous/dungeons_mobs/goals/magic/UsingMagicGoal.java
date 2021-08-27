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
         this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }

      /**
       * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
       * method as well.
       */
      public boolean canUse() {
         return this.hostMobEntity.getMagicUseTicks() > 0;
      }

      /**
       * Execute a one shot task or start executing a continuous task
       */
      public void start() {
         super.start();
         this.hostMobEntity.getNavigation().stop();
      }

      /**
       * Reset the task's internal state. Called when this task is interrupted by another one
       */
      public void stop() {
         super.stop();
         this.hostMobEntity.setMagicType(MagicType.NONE);
      }

      /**
       * Keep ticking a continuous task that has already been started
       */
      public void tick() {
         if (this.hostMobEntity.getTarget() != null) {
            this.hostMobEntity.getLookControl()
                    .setLookAt(this.hostMobEntity.getTarget(),
                            (float)this.hostMobEntity.getMaxHeadYRot(),
                            (float)this.hostMobEntity.getMaxHeadXRot());
         }

      }
   }