package com.infamous.dungeons_mobs.capabilities.convertible;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConvertibleProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IConvertible.class)
    public static final Capability<IConvertible> CONVERTIBLE_CAPABILITY = null;

    private LazyOptional<IConvertible> instance = LazyOptional.of(CONVERTIBLE_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CONVERTIBLE_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return CONVERTIBLE_CAPABILITY.getStorage().writeNBT(CONVERTIBLE_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CONVERTIBLE_CAPABILITY.getStorage().readNBT(CONVERTIBLE_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}