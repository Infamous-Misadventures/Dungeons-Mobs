package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.entities.projectiles.AbstractOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.DrownedNecromancerOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.TridentFumeEntity;
import com.infamous.dungeons_mobs.interfaces.IHasInventorySprite;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;

public class NecromancerTridentItem extends AbstractNecromancerStaffItem implements IHasInventorySprite {
    public NecromancerTridentItem(Properties properties) {
        super(properties);
    }

    @Override
    protected ProjectileEntity createOrb(PlayerEntity playerIn, double xDifference, double yDifference, double zDifference) {
        DrownedNecromancerOrbEntity necromancerOrb = new DrownedNecromancerOrbEntity(playerIn.level, playerIn, xDifference + (playerIn.getRandom().nextGaussian() * 1.25), yDifference + (playerIn.getRandom().nextGaussian() * 0.5), zDifference + (playerIn.getRandom().nextGaussian() * 1.25));
        necromancerOrb.rotateToMatchMovement();
        return necromancerOrb;
    }
}
