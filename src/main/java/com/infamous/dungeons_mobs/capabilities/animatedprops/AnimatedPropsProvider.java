package com.infamous.dungeons_mobs.capabilities.animatedprops;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AnimatedPropsProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(AnimatedProps.class)
    public static final Capability<AnimatedProps> ANIMATED_PROPS_CAPABILITY = null;

    private LazyOptional<AnimatedProps> instance = LazyOptional.of(ANIMATED_PROPS_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ANIMATED_PROPS_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return ANIMATED_PROPS_CAPABILITY.getStorage().writeNBT(ANIMATED_PROPS_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ANIMATED_PROPS_CAPABILITY.getStorage().readNBT(ANIMATED_PROPS_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}