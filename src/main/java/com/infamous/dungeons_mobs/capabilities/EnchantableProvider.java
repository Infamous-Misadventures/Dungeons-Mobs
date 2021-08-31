package com.infamous.dungeons_mobs.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnchantableProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IEnchantable.class)
    public static final Capability<IEnchantable> ENCHANTABLE_CAPABILITY = null;

    private LazyOptional<IEnchantable> instance = LazyOptional.of(ENCHANTABLE_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ENCHANTABLE_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return ENCHANTABLE_CAPABILITY.getStorage().writeNBT(ENCHANTABLE_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ENCHANTABLE_CAPABILITY.getStorage().readNBT(ENCHANTABLE_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}