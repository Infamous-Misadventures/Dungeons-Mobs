package com.infamous.dungeons_mobs.capabilities.teamable;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TeamableProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(ITeamable.class)
    public static final Capability<ITeamable> TEAMABLE_CAPABILITY = null;

    private LazyOptional<ITeamable> instance = LazyOptional.of(TEAMABLE_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == TEAMABLE_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return TEAMABLE_CAPABILITY.getStorage().writeNBT(TEAMABLE_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        TEAMABLE_CAPABILITY.getStorage().readNBT(TEAMABLE_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}