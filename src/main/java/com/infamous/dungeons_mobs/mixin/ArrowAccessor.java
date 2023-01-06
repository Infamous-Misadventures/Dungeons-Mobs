package com.infamous.dungeons_mobs.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.Arrow;

@Mixin(Arrow.class)
public interface ArrowAccessor {

    @Accessor
    Set<MobEffectInstance> getEffects();
}
