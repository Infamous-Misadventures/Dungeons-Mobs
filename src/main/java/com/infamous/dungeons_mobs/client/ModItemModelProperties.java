package com.infamous.dungeons_mobs.client;

import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ModItemModelProperties {

    public ModItemModelProperties(){
        ItemProperties.register(ModItems.ROYAL_GUARD_SHIELD.get(),
                new ResourceLocation("blocking"),
                (stack, clientWorld, livingEntity, i) -> {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F;
                });
        ItemProperties.register(ModItems.VANGUARD_SHIELD.get(),
                new ResourceLocation("blocking"),
                (stack, clientWorld, livingEntity, i) -> {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F;
                });
        ItemProperties.register(ModItems.YELLOW_TRIDENT.get(),
                new ResourceLocation("throwing"),
                (stack, clientWorld, livingEntity, i) -> {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F;
                });
        ItemProperties.register(ModItems.PURPLE_TRIDENT.get(),
                new ResourceLocation("throwing"),
                (stack, clientWorld, livingEntity, i) -> {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F;
                });
    }
}
