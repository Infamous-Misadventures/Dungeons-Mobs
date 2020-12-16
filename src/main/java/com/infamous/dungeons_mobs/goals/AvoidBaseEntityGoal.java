package com.infamous.dungeons_mobs.goals;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class AvoidBaseEntityGoal<T extends Entity> extends Goal {
   private final CreatureEntity hostCreature;
   private final double farSpeed;
   private final double nearSpeed;
   private T avoidTarget;
   private final float avoidDistance;
   private Path path;
   private final PathNavigator navigation;
   /** Class of entity this behavior seeks to avoid */
   private final Class<T> classToAvoid;

   public AvoidBaseEntityGoal(CreatureEntity entityIn, Class<T> avoidClass, float distance, double farSpeedIn, double nearSpeedIn) {
      this.hostCreature = entityIn;
      this.classToAvoid = avoidClass;
      this.avoidDistance = distance;
      this.farSpeed = farSpeedIn;
      this.nearSpeed = nearSpeedIn;
      this.navigation = entityIn.getNavigator();
      this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean shouldExecute() {
      this.avoidTarget = this.getNearestEntity(this.classToAvoid, this.hostCreature, this.hostCreature.getPosX(), this.hostCreature.getPosY(), this.hostCreature.getPosZ(), this.hostCreature.getBoundingBox().grow((double)this.avoidDistance, 3.0D, (double)this.avoidDistance));
      if (this.avoidTarget == null) {
         return false;
      } else {
         Vector3d vector3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.hostCreature, 16, 7, this.avoidTarget.getPositionVec());
         if (vector3d == null) {
            return false;
         } else if (this.avoidTarget.getDistanceSq(vector3d.x, vector3d.y, vector3d.z) < this.avoidTarget.getDistanceSq(this.hostCreature)) {
            return false;
         } else {
            this.path = this.navigation.getPathToPos(vector3d.x, vector3d.y, vector3d.z, 0);
            return this.path != null;
         }
      }
   }

   /**
    * Returns whether an in-progress EntityAIBase should continue executing
    */
   public boolean shouldContinueExecuting() {
      return !this.navigation.noPath();
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void startExecuting() {
      this.navigation.setPath(this.path, this.farSpeed);
   }

   /**
    * Reset the task's internal state. Called when this task is interrupted by another one
    */
   public void resetTask() {
      this.avoidTarget = null;
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      if (this.hostCreature.getDistanceSq(this.avoidTarget) < 49.0D) {
         this.hostCreature.getNavigator().setSpeed(this.nearSpeed);
      } else {
         this.hostCreature.getNavigator().setSpeed(this.farSpeed);
      }

   }



   @Nullable
   private <T extends Entity> T getNearestEntity(Class<? extends T> entityClass, LivingEntity livingEntity, double xPos, double yPos, double zPos, AxisAlignedBB axisAlignedBB) {
      return this.getClosestEntity(livingEntity.world.getLoadedEntitiesWithinAABB(entityClass, axisAlignedBB, (Predicate<? super T>)null), livingEntity, xPos, yPos, zPos);
   }



   @Nullable
   private <T extends Entity> T getClosestEntity(List<? extends T> nearbyEntities, LivingEntity livingEntity, double xPos, double yPos, double zPos) {
      double closestDistanceSq = -1.0D;
      T closestEntity = null;

      for(T nearbyEntity : nearbyEntities) {
         if (canDetect(livingEntity, nearbyEntity)) {
            double distanceSq = nearbyEntity.getDistanceSq(xPos, yPos, zPos);
            if (closestDistanceSq == -1.0D || distanceSq < closestDistanceSq) {
               closestDistanceSq = distanceSq;
               closestEntity = nearbyEntity;
            }
         }
      }

      return closestEntity;
   }

   private boolean canDetect(@Nullable LivingEntity detector, Entity target) {
      if (detector == target) {
         return false;
      } else if (target.isSpectator()) {
         return false;
      } else if (!target.isAlive()) {
         return false;
      } else {
         if (detector != null) {
            if (detector.isOnSameTeam(target)) {
               return false;
            }

            return !(detector instanceof MobEntity) || ((MobEntity) detector).getEntitySenses().canSee(target);
         }

         return true;
      }
   }
}