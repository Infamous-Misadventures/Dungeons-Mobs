package com.infamous.dungeons_mobs.capabilities.teamable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;
import java.util.UUID;

public class TeamableStorage implements Capability.IStorage<ITeamable> {

    @Override
    public INBT writeNBT(Capability<ITeamable> capability, ITeamable instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        List<UUID> teammates = instance.getTeammates();
        ListNBT teammatesNBT = new ListNBT();

        for(UUID uuid : teammates) {
            if (uuid != null) {
                teammatesNBT.add(NBTUtil.createUUID(uuid));
            }
        }
        tag.put("Teammates", teammatesNBT);

        return tag;
    }

    @Override
    public void readNBT(Capability<ITeamable> capability, ITeamable instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        ListNBT teammatesNBT = tag.getList("Teammates", 11);

        for (INBT inbt : teammatesNBT) {
            instance.addTeammate(NBTUtil.loadUUID(inbt));
        }
    }
}