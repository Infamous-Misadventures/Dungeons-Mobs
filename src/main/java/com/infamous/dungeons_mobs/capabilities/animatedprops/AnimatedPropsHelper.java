package com.infamous.dungeons_mobs.capabilities.animatedprops;

import net.minecraft.world.entity.Mob;

import static com.infamous.dungeons_mobs.capabilities.ModCapabilities.ANIMATED_PROPS_CAPABILITY;

public class AnimatedPropsHelper {

    public static AnimatedProps getAnimatedPropsCapability(Mob mob)
    {
        return mob.getCapability(ANIMATED_PROPS_CAPABILITY).orElse(new AnimatedProps());
    }
}
