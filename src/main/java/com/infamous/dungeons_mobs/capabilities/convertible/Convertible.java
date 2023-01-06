package com.infamous.dungeons_mobs.capabilities.convertible;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.BiConsumer;

import static com.infamous.dungeons_mobs.capabilities.ModCapabilities.CONVERTIBLE_CAPABILITY;

public class Convertible implements INBTSerializable<CompoundTag> {

    boolean isConverting;
    private boolean canConvert;
    int conversionTime;
    int prepareConversionTime;

    public void tickConversionTime() {
        this.setConversionTime(this.getConversionTime() - 1);
    }

    public <T extends Mob> T doConversion(Mob original, EntityType<T> convertToType, BiConsumer<Mob, T> onConversion) {
        T convertedTo = original.convertTo(convertToType, true);
        onConversion.accept(original, convertedTo);
        return convertedTo;

    }

    public void startConversion(int conversionLength) {
        this.setConverting(true);
        this.setConversionTime(conversionLength);
    }

    public void tickPrepareConversionTime() {
        this.setPrepareConversionTime(this.getPrepareConversionTime() + 1);
    }

    public boolean isConverting() {
        return this.isConverting;
    }

    public void setConverting(boolean converting) {
        this.isConverting = converting;
    }

    public void setConversionTime(int conversionTime) {
        this.conversionTime = conversionTime;
    }

    public int getConversionTime() {
        return this.conversionTime;
    }

    public void setPrepareConversionTime(int prepareConversionTime) {
        this.prepareConversionTime = prepareConversionTime;
    }

    public int getPrepareConversionTime() {
        return this.prepareConversionTime;
    }

    public boolean canConvert() {
        return this.canConvert;
    }

    public void setCanConvert(boolean canConvert) {
        this.canConvert = canConvert;
    }

    @Override
    public CompoundTag serializeNBT() {
        if (CONVERTIBLE_CAPABILITY == null) {
            return new CompoundTag();
        }
        CompoundTag tag = new CompoundTag();
        tag.putInt("conversionTime", this.isConverting() ? this.getConversionTime() : -1);
        tag.putInt("prepareConversionTime", this.canConvert() ? this.getPrepareConversionTime() : -1);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.setPrepareConversionTime(tag.getInt("prepareConversionTime"));
        if (tag.contains("DrownedConversionTime", 99) && tag.getInt("conversionTime") > -1) {
            this.startConversion(tag.getInt("conversionTime"));
        }
    }
}
