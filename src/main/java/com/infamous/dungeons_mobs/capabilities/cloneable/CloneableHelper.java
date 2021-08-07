package com.infamous.dungeons_mobs.capabilities.cloneable;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class CloneableHelper {

    @Nullable
    public static ICloneable getCloneableCapability(Entity entity)
    {
        LazyOptional<ICloneable> lazyCap = entity.getCapability(CloneableProvider.CLONEABLE_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the cloneable capability from the Entity!"));
        }
        return null;
    }
}
