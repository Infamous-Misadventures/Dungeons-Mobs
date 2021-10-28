package com.infamous.dungeons_mobs.capabilities.ancient.properties;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class AncientHelper {

    @Nullable
    public static IAncient getAncientCapability(Entity entity)
    {
        LazyOptional<IAncient> lazyCap = entity.getCapability(AncientProvider.MOB_PROPS_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the summoner capability from the Entity!"));
        }
        return null;
    }
    @Nullable
    public static LazyOptional<IAncient> getAncientCapabilityLazy(Entity entity)
    {
        return entity.getCapability(AncientProvider.MOB_PROPS_CAPABILITY);
    }
}
