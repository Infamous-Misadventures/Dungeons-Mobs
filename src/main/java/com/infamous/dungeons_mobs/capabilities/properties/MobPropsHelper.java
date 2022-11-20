package com.infamous.dungeons_mobs.capabilities.properties;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_mobs.capabilities.ModCapabilities.MOB_PROPS_CAPABILITY;

public class MobPropsHelper {

    public static LazyOptional<MobProps> getMobPropsCapabilityLazy(Entity entity)
    {
        if(MOB_PROPS_CAPABILITY == null) {
            return LazyOptional.empty();
        }
        LazyOptional<MobProps> lazyCap = entity.getCapability(MOB_PROPS_CAPABILITY);
        return lazyCap;
    }

    public static MobProps getMobPropsCapability(Entity entity)
    {
        LazyOptional<MobProps> lazyCap = entity.getCapability(MOB_PROPS_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the MobProps capability from the Entity!"));
        }
        return null;
    }
}
