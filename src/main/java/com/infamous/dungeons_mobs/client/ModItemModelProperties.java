package com.infamous.dungeons_mobs.client;

import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;

public class ModItemModelProperties {

    public ModItemModelProperties(){
        ItemModelsProperties.register(ModItems.ROYAL_GUARD_SHIELD.get(),
                new ResourceLocation("blocking"),
                (stack, clientWorld, livingEntity) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F;
        });
        ItemModelsProperties.register(ModItems.SKELETON_VANGUARD_SHIELD.get(),
                new ResourceLocation("blocking"),
                (stack, clientWorld, livingEntity) -> {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F;
                });
        ItemModelsProperties.register(ModItems.YELLOW_TRIDENT.get(),
                new ResourceLocation("throwing"),
                (stack, clientWorld, livingEntity) -> {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F;
                });
        ItemModelsProperties.register(ModItems.PURPLE_TRIDENT.get(),
                new ResourceLocation("throwing"),
                (stack, clientWorld, livingEntity) -> {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == stack ? 1.0F : 0.0F;
                });
    }
}
