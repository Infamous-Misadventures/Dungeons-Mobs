package com.infamous.dungeons_mobs.capabilities.ancient.properties;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class AncientStorage implements Capability.IStorage<IAncient> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IAncient> capability, IAncient instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("ancient", instance.isAncient());

        return tag;
    }

    @Override
    public void readNBT(Capability<IAncient> capability, IAncient instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setAncient(tag.getBoolean("ancient"));
    }

}
