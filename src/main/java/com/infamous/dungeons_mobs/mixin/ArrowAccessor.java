package com.infamous.dungeons_mobs.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.Arrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(Arrow.class)
public interface ArrowAccessor {

    @Accessor
    Set<MobEffectInstance> getEffects();
}
