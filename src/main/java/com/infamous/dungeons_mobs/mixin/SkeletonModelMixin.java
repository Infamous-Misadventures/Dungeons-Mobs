package com.infamous.dungeons_mobs.mixin;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkeletonModel.class)
public class SkeletonModelMixin {

    @Redirect(method = "prepareMobModel(Lnet/minecraft/world/entity/Mob;FFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean handleIsBowPrepareMobModel(ItemStack instance, Item pItem){
        return instance.getItem() instanceof BowItem;
    }

    @Redirect(method = "setupAnim(Lnet/minecraft/world/entity/Mob;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean handleIsBowSetupAnim(ItemStack instance, Item pItem){
        return instance.getItem() instanceof BowItem;
    }
}
