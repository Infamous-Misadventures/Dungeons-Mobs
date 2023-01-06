package com.infamous.dungeons_mobs.capabilities.ancient;

import static com.infamous.dungeons_mobs.capabilities.ModCapabilities.ANCIENT_CAPABILITY;

import net.minecraft.world.entity.Entity;


public class AncientHelper {

    public static Ancient getAncientCapability(Entity entity)
    {
        return entity.getCapability(ANCIENT_CAPABILITY).orElse(new Ancient());
    }
}
