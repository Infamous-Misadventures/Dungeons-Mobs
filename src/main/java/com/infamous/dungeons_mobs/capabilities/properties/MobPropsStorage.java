package com.infamous.dungeons_mobs.capabilities.properties;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class MobPropsStorage implements Capability.IStorage<IMobProps> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IMobProps> capability, IMobProps instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("burnNearbyTimer", instance.getBurnNearbyTimer());
        tag.putInt("freezeNearbyTimer", instance.getFreezeNearbyTimer());
        tag.putInt("gravityPulseTimer", instance.getGravityPulseTimer());

        return tag;
    }

    @Override
    public void readNBT(Capability<IMobProps> capability, IMobProps instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setBurnNearbyTimer(tag.getInt("burnNearbyTimer"));
        instance.setFreezeNearbyTimer(tag.getInt("freezeNearbyTimer"));
        instance.setGravityPulseTimer(tag.getInt("gravityPulseTimer"));
    }

    private ListNBT newDoubleNBTList(double... numbers){
        ListNBT listnbt = new ListNBT();

        for(double d0 : numbers) {
            listnbt.add(DoubleNBT.valueOf(d0));
        }

        return listnbt;
    }
}
