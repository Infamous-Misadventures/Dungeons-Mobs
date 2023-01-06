package com.infamous.dungeons_mobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.monster.piglin.Piglin;

@Mixin(Piglin.class)
public interface PiglinAccessor {

	@Accessor
	SimpleContainer getInventory();
}
