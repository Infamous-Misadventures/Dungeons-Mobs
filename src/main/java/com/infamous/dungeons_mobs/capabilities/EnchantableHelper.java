package com.infamous.dungeons_mobs.capabilities;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

public class EnchantableHelper {

    public static LazyOptional<IEnchantable> getEnchantableCapability(Entity entity)
    {
        LazyOptional<IEnchantable> lazyCap = entity.getCapability(EnchantableProvider.ENCHANTABLE_CAPABILITY);
        /*if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the Enchantable capability from the Entity!"));
        }
        return null;*/
        return lazyCap;
    }
}
