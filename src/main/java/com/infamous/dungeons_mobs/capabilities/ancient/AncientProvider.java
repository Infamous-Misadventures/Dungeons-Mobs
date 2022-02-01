package com.infamous.dungeons_mobs.capabilities.ancient;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AncientProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IAncient.class)
    public static final Capability<IAncient> MOB_PROPS_CAPABILITY = null;

    private LazyOptional<IAncient> instance = LazyOptional.of(MOB_PROPS_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == MOB_PROPS_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return MOB_PROPS_CAPABILITY.getStorage().writeNBT(MOB_PROPS_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        MOB_PROPS_CAPABILITY.getStorage().readNBT(MOB_PROPS_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}