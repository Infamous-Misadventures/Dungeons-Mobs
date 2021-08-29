package com.infamous.dungeons_mobs.mixin;

import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PiglinEntity.class)
public interface PiglinAccessor {

    @Accessor
    Inventory getInventory();
}
