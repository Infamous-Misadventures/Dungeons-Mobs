package com.infamous.dungeons_mobs.capabilities.ancient;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class AncientHelper {

    public static IAncient getAncientCapability(Entity entity)
    {
        LazyOptional<IAncient> lazyCap = entity.getCapability(AncientProvider.MOB_PROPS_CAPABILITY);
        return lazyCap.orElse(new Ancient());
    }

}
