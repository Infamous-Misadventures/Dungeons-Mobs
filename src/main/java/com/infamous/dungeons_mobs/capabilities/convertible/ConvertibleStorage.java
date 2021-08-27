package com.infamous.dungeons_mobs.capabilities.convertible;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ConvertibleStorage implements Capability.IStorage<IConvertible> {

    @Override
    public INBT writeNBT(Capability<IConvertible> capability, IConvertible instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("conversionTime", instance.isConverting() ? instance.getConversionTime() : -1);
        tag.putInt("prepareConversionTime", instance.canConvert() ? instance.getPrepareConversionTime() : -1);
        return tag;

    }

    @Override
    public void readNBT(Capability<IConvertible> capability, IConvertible instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setPrepareConversionTime(tag.getInt("prepareConversionTime"));
        if(tag.contains("DrownedConversionTime", 99) && tag.getInt("conversionTime") > -1){
            instance.startConversion(tag.getInt("conversionTime"));
        }

    }
}