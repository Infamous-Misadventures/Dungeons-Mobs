package com.infamous.dungeons_mobs.interfaces;

import com.infamous.dungeons_mobs.entities.projectiles.CobwebProjectileEntity;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.utils.PositionUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public interface IWebShooter extends ITrapsTarget {

    boolean isWebShooting();

    void setWebShooting(boolean webShooting);

    static void shootWeb(Mob webShooter, LivingEntity target) {
        Vec3 pos = PositionUtils.getOffsetPos(webShooter, 0.0, 1.0, -0.75, webShooter.yBodyRot);

        CobwebProjectileEntity projectile = new CobwebProjectileEntity(webShooter.level, webShooter);
        projectile.setPos(pos.x, pos.y, pos.z);
        double d0 = target.getX() - pos.x;
        double d1 = target.getY(0.3333333333333333D) - pos.y;
        double d2 = target.getZ() - pos.z;
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.2F;
        projectile.shoot(d0, d1 + (double) f, d2, 1.0F, 5.0F);
        if (!webShooter.isSilent()) {
            webShooter.playSound(ModSoundEvents.SPIDER_SHOOT.get(), 1.0F, 1.0F);
        }

        webShooter.level.addFreshEntity(projectile);
        projectile.delayedSpawnParticles = true;
    }
}
