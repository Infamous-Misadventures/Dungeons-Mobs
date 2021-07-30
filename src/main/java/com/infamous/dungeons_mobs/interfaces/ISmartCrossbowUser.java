package com.infamous.dungeons_mobs.interfaces;

import net.minecraft.entity.ICrossbowUser;
import net.minecraft.nbt.CompoundNBT;

public interface ISmartCrossbowUser extends ICrossbowUser {

    boolean isCrossbowUser();

    void setCrossbowUser(boolean crossbowUser);

    default void writeCrossbowUserNBT(CompoundNBT compoundNBT){
        compoundNBT.putBoolean("CrossbowUser", this.isCrossbowUser());
    }

    default void readCrossbowUserNBT(CompoundNBT compoundNBT){
        this.setCrossbowUser(compoundNBT.getBoolean("CrossbowUser"));
    }

    boolean _isChargingCrossbow();
}
