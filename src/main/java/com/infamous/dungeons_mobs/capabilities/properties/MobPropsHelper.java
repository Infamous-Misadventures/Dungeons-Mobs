package com.infamous.dungeons_mobs.capabilities.properties;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class MobPropsHelper {

    @Nullable
    public static IMobProps getMobPropsCapability(Entity entity)
    {
        LazyOptional<IMobProps> lazyCap = entity.getCapability(MobPropsProvider.MOB_PROPS_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the summoner capability from the Entity!"));
        }
        return null;
    }
}
