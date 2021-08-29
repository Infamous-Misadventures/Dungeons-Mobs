package com.infamous.dungeons_mobs.capabilities.cloneable;

import com.infamous.dungeons_mobs.capabilities.cloneable.ICloneable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.UUID;

public class CloneableStorage implements Capability.IStorage<ICloneable> {

    @Override
    public INBT writeNBT(Capability<ICloneable> capability, ICloneable instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        for(int i = 0; i < instance.getClones().length; i++){
            UUID clone = instance.getClones()[i];
            if(clone != null){
                tag.putUUID("clone" + i, clone);
            }
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<ICloneable> capability, ICloneable instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        for(int i = 0; i < instance.getClones().length; i++){
            String currentClone = "clone" + i;
            if(tag.hasUUID(currentClone)){
                instance.addClone(tag.getUUID(currentClone));
            }
        }
    }
}