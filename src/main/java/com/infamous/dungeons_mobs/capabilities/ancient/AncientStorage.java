package com.infamous.dungeons_mobs.capabilities.ancient;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class AncientStorage implements Capability.IStorage<IAncient> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IAncient> capability, IAncient instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("ancient", instance.isAncient());
        if(instance.getBossInfo() != null) {
            tag.putString("displayName", instance.getBossInfo().getName().getString());
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<IAncient> capability, IAncient instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setAncient(tag.getBoolean("ancient"));
        if(tag.contains("displayName")) {
            instance.initiateBossBar(new StringTextComponent(tag.getString("displayName")));
        }
    }

}
