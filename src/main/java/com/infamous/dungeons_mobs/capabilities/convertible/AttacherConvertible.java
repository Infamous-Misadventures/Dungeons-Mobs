package com.infamous.dungeons_mobs.capabilities.convertible;

import com.infamous.dungeons_mobs.capabilities.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class AttacherConvertible {

    private static class ConvertibleProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(MODID, "convertible");
        private final Convertible backend = new Convertible();
        private final LazyOptional<Convertible> optionalData = LazyOptional.of(() -> backend);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return ModCapabilities.CONVERTIBLE_CAPABILITY.orEmpty(cap, this.optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            final ConvertibleProvider provider = new ConvertibleProvider();
            event.addCapability(ConvertibleProvider.IDENTIFIER, provider);
        }
    }
}
