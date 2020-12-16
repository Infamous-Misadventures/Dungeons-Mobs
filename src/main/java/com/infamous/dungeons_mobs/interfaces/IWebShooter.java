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
        double squareDistanceToTarget = webShooter.getDistanceSq(target);
        double xDifference = target.getPosX() - webShooter.getPosX();
        double yDifference = target.getPosYHeight(0.5D) - webShooter.getPosYHeight(0.5D);
        double zDifference = target.getPosZ() - webShooter.getPosZ();
        float f = MathHelper.sqrt(MathHelper.sqrt(squareDistanceToTarget)) * 0.5F;

        CobwebProjectileEntity cobwebProjectileEntity = new CobwebProjectileEntity(webShooter.world,
                webShooter,
                xDifference * (double)f,
                yDifference,
                zDifference * (double)f);
        cobwebProjectileEntity.setPosition(cobwebProjectileEntity.getPosX(),
                webShooter.getPosYHeight(0.25D),
                cobwebProjectileEntity.getPosZ());
        webShooter.world.addEntity(cobwebProjectileEntity);
    }
}
