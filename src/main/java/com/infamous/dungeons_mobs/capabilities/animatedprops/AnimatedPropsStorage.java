package com.infamous.dungeons_mobs.capabilities.animatedprops;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class AnimatedPropsStorage implements Capability.IStorage<AnimatedProps> {

    @Override
    public INBT writeNBT(Capability<AnimatedProps> capability, AnimatedProps instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        return instance.save(tag, side);
    }

    @Override
    public void readNBT(Capability<AnimatedProps> capability, AnimatedProps instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.load(tag, side);
    }
}