package com.infamous.dungeons_mobs.entities.illagers;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.level.Level;

@SuppressWarnings("EntityConstructor")
public class ArchIllagerEntity extends AbstractIllager {

    protected ArchIllagerEntity(EntityType<? extends ArchIllagerEntity> type, Level worldIn) {
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
