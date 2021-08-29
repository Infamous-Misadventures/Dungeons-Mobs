package com.infamous.dungeons_mobs.capabilities.teamable;

import net.minecraft.entity.MobEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class TeamableHelper {

    @Nullable
    public static ITeamable getTeamableCapability(MobEntity mob)
    {
        LazyOptional<ITeamable> lazyCap = mob.getCapability(TeamableProvider.TEAMABLE_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the teamable capability from the Entity!"));
        }
        return null;
    }

    public static void makeTeammates(MobEntity mob, MobEntity teammate){
        ITeamable entityTeamableCap = getTeamableCapability(mob);
        ITeamable teammateTeamableCap = getTeamableCapability(teammate);
        if(entityTeamableCap != null && teammateTeamableCap != null){
            entityTeamableCap.addTeammate(teammate.getUUID());
            teammateTeamableCap.addTeammate(mob.getUUID());
        }
    }



    public static boolean areTeammates(MobEntity mob, MobEntity teammate){
        ITeamable entityTeamableCap = getTeamableCapability(mob);
        ITeamable teammateTeamableCap = getTeamableCapability(teammate);
        if(entityTeamableCap != null && teammateTeamableCap != null){
            return entityTeamableCap.getTeammates().contains(teammate.getUUID())
                    || teammateTeamableCap.getTeammates().contains(mob.getUUID());
        }
        return false;
    }
}
