package com.infamous.dungeons_mobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;

@Mixin(ThrownTrident.class)
public interface TridentEntityAccessor {
    @Accessor
    ItemStack getTridentItem();
}
