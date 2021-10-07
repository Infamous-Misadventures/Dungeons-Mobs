package com.infamous.dungeons_mobs.capabilities.properties;


public interface IMobProps {
    int getBurnNearbyTimer();
    void setBurnNearbyTimer(int burnNearbyTimer);

    int getFreezeNearbyTimer();
    void setFreezeNearbyTimer(int freezeNearbyTimer);

    int getGravityPulseTimer();
    void setGravityPulseTimer(int gravityPulseTimer);
}
