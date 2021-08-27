package com.infamous.dungeons_mobs.capabilities.cloneable;

import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;

import java.util.UUID;

public class Cloneable implements ICloneable {
    private UUID[] clones = new UUID[7];

    public Cloneable(){
    }

    @Override
    public boolean addClone(UUID clone) {
        for(int i = 0; i < clones.length; i++){
            UUID currentClone = clones[i];
            if(currentClone == null){
                clones[i] = clone;
                return true;
            }
        }
        return false;
    }

    @Override
    public UUID[] getClones() {
        return this.clones;
    }
}
