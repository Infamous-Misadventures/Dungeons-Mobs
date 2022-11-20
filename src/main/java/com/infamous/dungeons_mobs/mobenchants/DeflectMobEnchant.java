package com.infamous.dungeons_mobs.mobenchants;

import com.baguchan.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.infamous.dungeons_mobs.mobenchants.NewMobEnchantUtils.executeIfPresentWithLevel;
import static com.infamous.dungeons_mobs.mod.ModMobEnchants.DEFLECT;

public class DeflectMobEnchant extends MobEnchant {

    public DeflectMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onDeflectImpact(ProjectileImpactEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;
            HitResult rayTraceResult = event.getRayTraceResult();
            if(!projectileHitLivingEntity(rayTraceResult)) return;
            if (!shooterIsLiving(projectile)) return;
            LivingEntity victim = (LivingEntity) ((EntityHitResult) rayTraceResult).getEntity();
            if (victimIsOwner(projectile, victim)) {
                event.setCanceled(true);
                return;
            }
            executeIfPresentWithLevel(victim, DEFLECT.get(), (level) -> {
                if (projectile.level.isClientSide) {
                    deflectProjectile(projectile, victim);
                    projectile.setOwner(victim);
                    event.setCanceled(true);
                } else {
                    deflectProjectile(projectile, victim);
                    projectile.setOwner(victim);
                    event.setCanceled(true);
                }
            });
        }
    }

    private static void deflectProjectile(Projectile projectile, LivingEntity victim) {
        if(projectile instanceof ShulkerBullet){
            ((ShulkerBullet) projectile).finalTarget = projectile.getOwner();
    /*    } if(projectile instanceof DamagingProjectileEntity){
            DamagingProjectileEntity damagingProjectileEntity = (DamagingProjectileEntity) projectile;
            double x0 = projectile.getOwner().getX() - victim.getX();
            double y0 = projectile.getOwner().getY(0.5) - projectile.getY();
            double z0 = projectile.getOwner().getZ() - victim.getZ();
            float f = MathHelper.sqrt(MathHelper.sqrt(x0)) * 0.5F;
            double x1 = x0 + victim.getRandom().nextGaussian() * f;
            double y1 = y0;
            double z1 = z0 + victim.getRandom().nextGaussian() * f;
            double speed = (double)MathHelper.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
            if (speed != 0.0D) {
                damagingProjectileEntity.xPower = x1 / speed * 0.1D;
                damagingProjectileEntity.yPower = y1 / speed * 0.1D;
                damagingProjectileEntity.zPower = z1 / speed * 0.1D;
            }*/
        } else {
            Vec3 deltaMovement = projectile.getDeltaMovement();
            double speed = (double) Mth.sqrt((float) (deltaMovement.x * deltaMovement.x + deltaMovement.y * deltaMovement.y + deltaMovement.z * deltaMovement.z));
            speed = speed < 1.0E-4D ? 0.0D : speed;
            double d0 = projectile.getOwner().getX() - victim.getX();
            double d1 = projectile.getOwner().getY(0.3333333333333333D) - projectile.getY();
            double d2 = projectile.getOwner().getZ() - victim.getZ();
            double d3 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
            projectile.shoot(d0, d1 + d3 * (double) 0.2F, d2, (float) speed, (float) (14 - victim.level.getDifficulty().getId() * 4));
        }
    }

    private static boolean victimIsOwner(Projectile projectile, LivingEntity victim) {
        return victim.equals(projectile.getOwner());
    }

    public static boolean projectileHitLivingEntity(HitResult rayTraceResult) {
        if(rayTraceResult instanceof EntityHitResult){
            EntityHitResult entityRayTraceResult = (EntityHitResult)rayTraceResult;
            if(entityRayTraceResult.getEntity() instanceof LivingEntity){
                return true;
            } else{
                return false;
            }
        } else{
            return false;
        }
    }

    public static boolean shooterIsLiving(Projectile projectile) {
        return projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity;
    }
}