package com.infamous.dungeons_mobs.capabilities.ancient;

import net.minecraft.world.entity.Entity;

import static com.infamous.dungeons_mobs.capabilities.ModCapabilities.ANCIENT_CAPABILITY;


public class AncientHelper {

    public static Ancient getAncientCapability(Entity entity) {
        return entity.getCapability(ANCIENT_CAPABILITY).orElse(new Ancient());
    }
}
