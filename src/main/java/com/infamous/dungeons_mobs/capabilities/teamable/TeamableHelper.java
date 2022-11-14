package com.infamous.dungeons_mobs.capabilities.teamable;

import net.minecraft.entity.MobEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class TeamableHelper {

    public static ITeamable getTeamableCapability(MobEntity mob)
    {
        LazyOptional<ITeamable> lazyCap = mob.getCapability(TeamableProvider.TEAMABLE_CAPABILITY);
        return lazyCap.orElse(new Teamable());
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
