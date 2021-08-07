package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.projectiles.AbstractOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.TridentFumeEntity;
import net.minecraft.entity.player.PlayerEntity;

public class NecromancerTridentItem extends NecromancerStaffItem{
    public NecromancerTridentItem(Properties properties) {
        super(properties);
    }

    @Override
    protected AbstractOrbEntity createOrb(PlayerEntity playerIn, double xDifference, double yDifference, double zDifference) {
        return new TridentFumeEntity(playerIn.level, playerIn, xDifference, yDifference, zDifference);
    }
}
