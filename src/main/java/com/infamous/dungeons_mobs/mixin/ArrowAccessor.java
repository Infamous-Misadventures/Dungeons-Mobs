package com.infamous.dungeons_mobs.mixin;

import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.potion.EffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ArrowEntity.class)
public interface ArrowAccessor {

    @Accessor
    Set<EffectInstance> getEffects();
}
