package com.infamous.dungeons_mobs.capabilities.convertible;

public class Convertible implements IConvertible {

    boolean isConverting;
    private boolean canConvert;
    int conversionTime;
    int prepareConversionTime;

    @Override
    public boolean isConverting() {
        return this.isConverting;
    }

    @Override
    public void setConverting(boolean converting) {
        this.isConverting = converting;
    }

    @Override
    public void setConversionTime(int conversionTime) {
        this.conversionTime = conversionTime;
    }

    @Override
    public int getConversionTime() {
        return this.conversionTime;
    }

    @Override
    public void setPrepareConversionTime(int prepareConversionTime) {
        this.prepareConversionTime = prepareConversionTime;
    }

    @Override
    public int getPrepareConversionTime() {
        return this.prepareConversionTime;
    }

    @Override
    public boolean canConvert() {
        return this.canConvert;
    }

    @Override
    public void setCanConvert(boolean canConvert) {
        this.canConvert = canConvert;
    }
}
