package com.infamous.dungeons_mobs.interfaces;

public interface ITrapsTarget {

    void setTargetTrapped(boolean trapped, boolean notifyOthers);

    boolean isTargetTrapped();
}
