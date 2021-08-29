package com.infamous.dungeons_mobs.capabilities.convertible;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;

import java.util.function.BiConsumer;

public interface IConvertible {

    default void tickConversionTime(){
        this.setConversionTime(this.getConversionTime() -1);
    }

    boolean isConverting();

    void setConverting(boolean converting);

    default <T extends MobEntity> T doConversion(MobEntity original, EntityType<T> convertToType, BiConsumer<MobEntity, T> onConversion){
        T convertedTo = original.convertTo(convertToType, true);
        onConversion.accept(original, convertedTo);
        return convertedTo;

    }

    default void startConversion(int conversionLength){
        this.setConverting(true);
        this.setConversionTime(conversionLength);
    }

    void setConversionTime(int conversionTime);

    int getConversionTime();

    default void tickPrepareConversionTime(){
        this.setPrepareConversionTime(this.getPrepareConversionTime() + 1);
    }

    void setPrepareConversionTime(int prepareConversionTime);

    int getPrepareConversionTime();

    boolean canConvert();

    void setCanConvert(boolean canConvert);
}
