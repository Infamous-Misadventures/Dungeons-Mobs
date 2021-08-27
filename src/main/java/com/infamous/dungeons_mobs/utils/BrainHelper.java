package com.infamous.dungeons_mobs.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.infamous.dungeons_mobs.entities.piglin.FungusThrowerEntity;
import com.infamous.dungeons_mobs.mixin.BrainAccessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.Task;

import java.util.List;
import java.util.Set;

public class BrainHelper {

    /*
    public static <E extends LivingEntity> void addActivityAndRemoveMemoryWhenStopped(Brain<E> brain, Activity activity, int priorityStart, ImmutableList<? extends Task<? super E>> tasks, MemoryModuleType<?> memoryToRemove) {
        Set<Pair<MemoryModuleType<?>, MemoryModuleStatus>> memoryToStatusSet = ImmutableSet.of(Pair.of(memoryToRemove, MemoryModuleStatus.VALUE_PRESENT));
        Set<MemoryModuleType<?>> memorySet = ImmutableSet.of(memoryToRemove);
        addActivityAndRemoveMemoriesWhenStopped(brain, activity, createPriorityPairs(priorityStart, tasks), memoryToStatusSet, memorySet);
    }
    */

    public static <E extends LivingEntity> ImmutableList<? extends Pair<Integer, ? extends Task<? super E>>> createPriorityPairs(int priorityStart, ImmutableList<? extends Task<? super E>> tasks) {
        int priorityIndex = priorityStart;
        ImmutableList.Builder<Pair<Integer, ? extends Task<? super E>>> priorityPairs = ImmutableList.builder();

        for(Task<? super E> task : tasks) {
            priorityPairs.add(Pair.of(priorityIndex++, task));
        }

        return priorityPairs.build();
    }

    /*
    private static <E extends LivingEntity> void addActivityAndRemoveMemoriesWhenStopped(Brain<E> brain, Activity activity, ImmutableList<? extends Pair<Integer, ? extends Task<? super E>>> prioritizedTasks, Set<Pair<MemoryModuleType<?>, MemoryModuleStatus>> memoryToStatusSet, Set<MemoryModuleType<?>> memorySet){
        BrainAccessor<E> brainAccessor = castToAccessor(brain);
        brainAccessor.getActivityRequirements().put(activity, memoryToStatusSet);
        if (!memorySet.isEmpty()) {
            brainAccessor.getActivityMemoriesToEraseWhenStopped().put(activity, memorySet);
        }

        addPrioritizedBehaviors(activity, prioritizedTasks, brainAccessor);
    }

    public static <E extends LivingEntity> void addPrioritizedBehaviors(Activity activity, ImmutableList<? extends Pair<Integer, ? extends Task<? super E>>> prioritizedTasks, BrainAccessor<E> brainAccessor) {
        for(Pair<Integer, ? extends Task<? super E>> pair : prioritizedTasks) {
            brainAccessor.getAvailableBehaviorsByPriority()
                    .computeIfAbsent(pair.getFirst(), (p) -> Maps.newHashMap())
                    .computeIfAbsent(activity, (a) -> Sets.newLinkedHashSet())
                    .add(pair.getSecond());
        }
    }
     */

    public static <E extends LivingEntity> void addPrioritizedBehaviors(Activity activity, ImmutableList<? extends Pair<Integer, ? extends Task<? super E>>> prioritizedTasks, Brain<E> brain) {
        BrainAccessor<E> brainAccessor = castToAccessor(brain);

        for(Pair<Integer, ? extends Task<? super E>> pair : prioritizedTasks) {
            brainAccessor.getAvailableBehaviorsByPriority()
                    .computeIfAbsent(pair.getFirst(), (p) -> Maps.newHashMap())
                    .computeIfAbsent(activity, (a) -> Sets.newLinkedHashSet())
                    .add(pair.getSecond());
        }
    }

    public static <E extends LivingEntity> BrainAccessor<E> castToAccessor(Brain<E> brain) {
        //noinspection unchecked
        return (BrainAccessor<E>)brain;
    }
}