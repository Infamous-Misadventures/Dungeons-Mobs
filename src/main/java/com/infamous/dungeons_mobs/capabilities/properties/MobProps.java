package com.infamous.dungeons_mobs.capabilities.properties;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import static com.infamous.dungeons_mobs.capabilities.ModCapabilities.CONVERTIBLE_CAPABILITY;

public class MobProps implements INBTSerializable<CompoundTag> {
    private int burnNearbyTimer;
    private int freezeNearbyTimer;
    private int gravityPulseTimer;

    public MobProps() {
        this.burnNearbyTimer = 20;
        this.freezeNearbyTimer = 40;
        this.gravityPulseTimer = 100;
    }

    public int getBurnNearbyTimer() {
        return this.burnNearbyTimer;
    }

    public void setBurnNearbyTimer(int burnNearbyTimer) {
        this.burnNearbyTimer = burnNearbyTimer;
    }

    public int getFreezeNearbyTimer() {
        return this.freezeNearbyTimer;
    }

    public void setFreezeNearbyTimer(int freezeNearbyTimer) {
        this.freezeNearbyTimer = freezeNearbyTimer;
    }

    public int getGravityPulseTimer() {
        return gravityPulseTimer;
    }

    public void setGravityPulseTimer(int gravityPulseTimer) {
        this.gravityPulseTimer = gravityPulseTimer;
    }

    @Override
    public CompoundTag serializeNBT() {
        if (CONVERTIBLE_CAPABILITY == null) {
            return new CompoundTag();
        }
        CompoundTag tag = new CompoundTag();
        tag.putInt("burnNearbyTimer", this.getBurnNearbyTimer());
        tag.putInt("freezeNearbyTimer", this.getFreezeNearbyTimer());
        tag.putInt("gravityPulseTimer", this.getGravityPulseTimer());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.setBurnNearbyTimer(tag.getInt("burnNearbyTimer"));
        this.setFreezeNearbyTimer(tag.getInt("freezeNearbyTimer"));
        this.setGravityPulseTimer(tag.getInt("gravityPulseTimer"));
    }
}
