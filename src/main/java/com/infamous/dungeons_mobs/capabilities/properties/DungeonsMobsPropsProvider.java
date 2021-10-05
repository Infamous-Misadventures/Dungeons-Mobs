package com.infamous.dungeons_mobs.capabilities.properties;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DungeonsMobsPropsProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IDungeonsMobProps.class)
    public static final Capability<IDungeonsMobProps> COMBO_CAPABILITY = null;

    private LazyOptional<IDungeonsMobProps> instance = LazyOptional.of(COMBO_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == COMBO_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return COMBO_CAPABILITY.getStorage().writeNBT(COMBO_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        COMBO_CAPABILITY.getStorage().readNBT(COMBO_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}