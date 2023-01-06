package com.infamous.dungeons_mobs.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

@Mixin(GoalSelector.class)
public interface GoalSelectorAccessor {

	@Accessor
	Set<WrappedGoal> getAvailableGoals();
}
