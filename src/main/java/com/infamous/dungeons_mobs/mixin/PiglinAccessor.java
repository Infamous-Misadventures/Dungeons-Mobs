package com.infamous.dungeons_mobs.mixin;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.monster.piglin.Piglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Piglin.class)
public interface PiglinAccessor {

    @Accessor
    SimpleContainer getInventory();
}
