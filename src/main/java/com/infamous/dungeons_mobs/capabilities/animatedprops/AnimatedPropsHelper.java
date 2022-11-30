package com.infamous.dungeons_mobs.capabilities.animatedprops;

import net.minecraft.world.entity.Mob;

public class AnimatedPropsHelper {

    public static AnimatedProps getAnimatedPropsCapability(Mob mob)
    {
        return mob.getCapability(AnimatedPropsProvider.ANIMATED_PROPS_CAPABILITY).orElse(new AnimatedProps());
    }
}
