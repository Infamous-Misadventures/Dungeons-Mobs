package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.projectiles.NecromancerOrbEntity;
import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;

public class NecromancerStaffItem extends AbstractNecromancerStaffItem implements IHasInventorySprite {
    public NecromancerStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected ProjectileEntity createOrb(PlayerEntity playerIn, double xDifference, double yDifference, double zDifference) {
        NecromancerOrbEntity necromancerOrb = new NecromancerOrbEntity(playerIn.level, playerIn, xDifference, yDifference, zDifference);
        necromancerOrb.setDelayedForm(true);
        necromancerOrb.rotateToMatchMovement();
        return necromancerOrb;
    }
}
