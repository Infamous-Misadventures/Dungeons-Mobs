package com.infamous.dungeons_mobs.capabilities.cloneable;

import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CloneableProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(ICloneable.class)
    public static final Capability<ICloneable> CLONEABLE_CAPABILITY = null;

    private LazyOptional<ICloneable> instance = LazyOptional.of(CLONEABLE_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CLONEABLE_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return CLONEABLE_CAPABILITY.getStorage().writeNBT(CLONEABLE_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CLONEABLE_CAPABILITY.getStorage().readNBT(CLONEABLE_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}