package com.infamous.dungeons_mobs.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface ISmartCrossbowUser {

	boolean isCrossbowUser();

	void setCrossbowUser(boolean crossbowUser);

	default void writeCrossbowUserNBT(CompoundTag compoundNBT) {
		compoundNBT.putBoolean("CrossbowUser", this.isCrossbowUser());
	}

	default void readCrossbowUserNBT(CompoundTag compoundNBT) {
		this.setCrossbowUser(compoundNBT.getBoolean("CrossbowUser"));
	}

	boolean _isChargingCrossbow();
}
