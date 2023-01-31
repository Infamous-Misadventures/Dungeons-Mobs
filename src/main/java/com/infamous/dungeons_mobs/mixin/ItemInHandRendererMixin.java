package com.infamous.dungeons_mobs.mixin;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Redirect(method = "evaluateWhichHandsToRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private static boolean handleIsBowEvaluate(ItemStack instance, Item pItem){
        if(pItem == Items.BOW){
            return instance.getItem() instanceof BowItem;
        } else if(pItem == Items.CROSSBOW){
            return instance.getItem() instanceof CrossbowItem;
        }
        return false;
    }

    @Redirect(method = "selectionUsingItemWhileHoldingBowLike", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private static boolean handleIsBowSelection(ItemStack instance, Item pItem){
        if(pItem == Items.BOW){
            return instance.getItem() instanceof BowItem;
        } else if(pItem == Items.CROSSBOW){
            return instance.getItem() instanceof CrossbowItem;
        }
        return false;
    }

    @Redirect(method = "isChargedCrossbow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private static boolean handleIsBow(ItemStack instance, Item pItem){
        return instance.getItem() instanceof CrossbowItem;
    }
}
