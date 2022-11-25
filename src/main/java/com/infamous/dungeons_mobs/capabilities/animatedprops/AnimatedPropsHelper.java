package com.infamous.dungeons_mobs.capabilities.animatedprops;

import net.minecraft.entity.MobEntity;
import net.minecraftforge.common.util.LazyOptional;

public class AnimatedPropsHelper {

    public static AnimatedProps getAnimatedPropsCapability(MobEntity mob)
    {
        LazyOptional<AnimatedProps> lazyCap = mob.getCapability(AnimatedPropsProvider.ANIMATED_PROPS_CAPABILITY);
        return lazyCap.orElse(new AnimatedProps());
    }
}
