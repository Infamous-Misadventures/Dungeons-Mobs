package com.infamous.dungeons_mobs.entities.illagers;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

@SuppressWarnings("EntityConstructor")
public class ArchIllagerEntity extends AbstractIllagerEntity {

    protected ArchIllagerEntity(EntityType<? extends ArchIllagerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    public void applyRaidBuffs(int p_213660_1_, boolean p_213660_2_) {

    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }
}
