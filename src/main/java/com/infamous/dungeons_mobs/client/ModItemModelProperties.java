package com.infamous.dungeons_mobs.client;

import com.infamous.dungeons_mobs.mod.ModItems;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;

public class ModItemModelProperties {

    public ModItemModelProperties(){
        ItemModelsProperties.func_239418_a_(ModItems.ROYAL_GUARD_SHIELD.get(),
                new ResourceLocation("blocking"),
                (stack, clientWorld, livingEntity) -> {
            return livingEntity != null && livingEntity.isHandActive() && livingEntity.getActiveItemStack() == stack ? 1.0F : 0.0F;
        });
        ItemModelsProperties.func_239418_a_(ModItems.SKELETON_VANGUARD_SHIELD.get(),
                new ResourceLocation("blocking"),
                (stack, clientWorld, livingEntity) -> {
                    return livingEntity != null && livingEntity.isHandActive() && livingEntity.getActiveItemStack() == stack ? 1.0F : 0.0F;
                });
    }
}
