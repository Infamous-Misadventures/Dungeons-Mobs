package com.infamous.dungeons_mobs.tasks;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

public class ThrowAtTargetTask<E extends Mob> extends Behavior<E> {
   private int attackDelay = 0;
   private final Predicate<ItemStack> throwItemPredicate;
   private final BiConsumer<E, LivingEntity> performRangedAttack;

   public ThrowAtTargetTask(Predicate<ItemStack> throwItemPredicate, BiConsumer<E, LivingEntity> performRangedAttack) {
      super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
      this.throwItemPredicate = throwItemPredicate;
      this.performRangedAttack = performRangedAttack;
   }

   protected boolean checkExtraStartConditions(ServerLevel serverWorld, E thrower) {
      LivingEntity attackTarget = getAttackTarget(thrower);
      return thrower.isHolding(this.throwItemPredicate) && BehaviorUtils.canSee(thrower, attackTarget) && BehaviorUtils.isWithinAttackRange(thrower, attackTarget, 0);
   }

   protected boolean canStillUse(ServerLevel serverWorld, E thrower, long gameTime) {
      return thrower.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(serverWorld, thrower);
   }

   protected void tick(ServerLevel serverWorld, E thrower, long gameTime) {
      LivingEntity attackTarget = getAttackTarget(thrower);
      this.lookAtTarget(thrower, attackTarget);
      this.throwAttack(thrower, attackTarget);
   }

   private void throwAttack(E thrower, LivingEntity attackTarget) {
      if(this.attackDelay > 0){
         --this.attackDelay;
      }
      if (this.attackDelay <= 0) {
         this.performRangedAttack.accept(thrower, attackTarget);
         this.attackDelay = 50 + thrower.getRandom().nextInt(20); // average delay of 60 ticks
      }
   }

   private void lookAtTarget(Mob attacker, LivingEntity attackTarget) {
      attacker.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(attackTarget, true));
   }

   private static LivingEntity getAttackTarget(LivingEntity attacker) {
      return attacker.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
   }
}