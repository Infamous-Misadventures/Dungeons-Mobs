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
      this.navigation = entityIn.getNavigation();
      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean canUse() {
      this.avoidTarget = this.getNearestEntity(this.classToAvoid, this.hostCreature, this.hostCreature.getX(), this.hostCreature.getY(), this.hostCreature.getZ(), this.hostCreature.getBoundingBox().inflate((double)this.avoidDistance, 3.0D, (double)this.avoidDistance));
      if (this.avoidTarget == null) {
         return false;
      } else {
         Vector3d vector3d = RandomPositionGenerator.getPosAvoid(this.hostCreature, 16, 7, this.avoidTarget.position());
         if (vector3d == null) {
            return false;
         } else if (this.avoidTarget.distanceToSqr(vector3d.x, vector3d.y, vector3d.z) < this.avoidTarget.distanceToSqr(this.hostCreature)) {
            return false;
         } else {
            this.path = this.navigation.createPath(vector3d.x, vector3d.y, vector3d.z, 0);
            return this.path != null;
         }
      }
   }

   /**
    * Returns whether an in-progress EntityAIBase should continue executing
    */
   public boolean canContinueToUse() {
      return !this.navigation.isDone();
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void start() {
      this.navigation.moveTo(this.path, this.farSpeed);
   }

   /**
    * Reset the task's internal state. Called when this task is interrupted by another one
    */
   public void stop() {
      this.avoidTarget = null;
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      if (this.hostCreature.distanceToSqr(this.avoidTarget) < 49.0D) {
         this.hostCreature.getNavigation().setSpeedModifier(this.nearSpeed);
      } else {
         this.hostCreature.getNavigation().setSpeedModifier(this.farSpeed);
      }

   }



   @Nullable
   private <T extends Entity> T getNearestEntity(Class<? extends T> entityClass, LivingEntity livingEntity, double xPos, double yPos, double zPos, AxisAlignedBB axisAlignedBB) {
      return this.getClosestEntity(livingEntity.level.getLoadedEntitiesOfClass(entityClass, axisAlignedBB, (Predicate<? super T>)null), livingEntity, xPos, yPos, zPos);
   }



   @Nullable
   private <T extends Entity> T getClosestEntity(List<? extends T> nearbyEntities, LivingEntity livingEntity, double xPos, double yPos, double zPos) {
      double closestDistanceSq = -1.0D;
      T closestEntity = null;

      for(T nearbyEntity : nearbyEntities) {
         if (canDetect(livingEntity, nearbyEntity)) {
            double distanceSq = nearbyEntity.distanceToSqr(xPos, yPos, zPos);
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
            if (detector.isAlliedTo(target)) {
               return false;
            }

            return !(detector instanceof MobEntity) || ((MobEntity) detector).getSensing().canSee(target);
         }

         return true;
      }
   }
}