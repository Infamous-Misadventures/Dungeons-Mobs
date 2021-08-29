package com.infamous.dungeons_mobs.tasks;

import com.google.common.collect.ImmutableMap;
import com.infamous.dungeons_mobs.entities.piglin.ai.FungusThrowerAi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.Item;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ThrowAtTargetTask<E extends MobEntity> extends Task<E> {
   private int attackDelay = 0;
   private final Predicate<Item> throwItemPredicate;
   private final BiConsumer<E, LivingEntity> performRangedAttack;

   public ThrowAtTargetTask(Predicate<Item> throwItemPredicate, BiConsumer<E, LivingEntity> performRangedAttack) {
      super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT), 1200);
      this.throwItemPredicate = throwItemPredicate;
      this.performRangedAttack = performRangedAttack;
   }

   protected boolean checkExtraStartConditions(ServerWorld serverWorld, E thrower) {
      LivingEntity attackTarget = getAttackTarget(thrower);
      return thrower.isHolding(this.throwItemPredicate) && BrainUtil.canSee(thrower, attackTarget) && BrainUtil.isWithinAttackRange(thrower, attackTarget, 0);
   }

   protected boolean canStillUse(ServerWorld serverWorld, E thrower, long gameTime) {
      return thrower.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(serverWorld, thrower);
   }

   protected void tick(ServerWorld serverWorld, E thrower, long gameTime) {
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

   private void lookAtTarget(MobEntity attacker, LivingEntity attackTarget) {
      attacker.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(attackTarget, true));
   }

   private static LivingEntity getAttackTarget(LivingEntity attacker) {
      return attacker.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
   }
}