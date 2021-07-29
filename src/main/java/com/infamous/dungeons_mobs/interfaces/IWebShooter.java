package com.infamous.dungeons_mobs.interfaces;

import com.infamous.dungeons_mobs.entities.projectiles.CobwebProjectileEntity;
import net.minecraft.block.WebBlock;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public interface IWebShooter extends IRangedAttackMob {

    boolean shouldShootWeb();

    boolean isTargetSlowedDown();

    boolean isWebShooting();

    void setWebShooting(boolean webShooting);

    default void shootWeb(MobEntity webShooter, LivingEntity target){
        double squareDistanceToTarget = webShooter.distanceToSqr(target);
        double xDifference = target.getX() - webShooter.getX();
        double yDifference = target.getY(0.5D) - webShooter.getY(0.5D);
        double zDifference = target.getZ() - webShooter.getZ();
        float f = MathHelper.sqrt(MathHelper.sqrt(squareDistanceToTarget)) * 0.5F;

        CobwebProjectileEntity cobwebProjectileEntity = new CobwebProjectileEntity(webShooter.level,
                webShooter,
                xDifference * (double)f,
                yDifference,
                zDifference * (double)f);
        cobwebProjectileEntity.setPos(cobwebProjectileEntity.getX(),
                webShooter.getY(0.25D),
                cobwebProjectileEntity.getZ());
        webShooter.level.addFreshEntity(cobwebProjectileEntity);
    }
}
