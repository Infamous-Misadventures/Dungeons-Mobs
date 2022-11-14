package com.infamous.dungeons_mobs.capabilities.properties;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class MobPropsHelper {

    public static IMobProps getMobPropsCapability(Entity entity)
    {
        LazyOptional<IMobProps> lazyCap = entity.getCapability(MobPropsProvider.MOB_PROPS_CAPABILITY);
        return lazyCap.orElse(new MobProps());
    }
}
