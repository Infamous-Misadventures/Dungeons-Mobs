package com.infamous.dungeons_mobs.mixin;

import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractSkeleton.class)
public class AbstractSkeletonMixin {

    @Redirect(method = "reassessWeaponGoal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean handleIsBow(ItemStack instance, Item pItem){
        return instance.getItem() instanceof BowItem;
    }
}
