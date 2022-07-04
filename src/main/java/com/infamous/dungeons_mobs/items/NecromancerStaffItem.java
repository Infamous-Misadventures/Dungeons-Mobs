package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.projectiles.AbstractOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.LaserOrbEntity;
import net.minecraft.entity.player.PlayerEntity;

public class NecromancerStaffItem extends AbstractNecromancerStaffItem{
    public NecromancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected AbstractOrbEntity createOrb(PlayerEntity playerIn, double xDifference, double yDifference, double zDifference) {
        return new LaserOrbEntity(playerIn.level, playerIn, xDifference, yDifference, zDifference);
    }
}
