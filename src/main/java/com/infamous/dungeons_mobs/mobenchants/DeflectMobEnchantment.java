package com.infamous.dungeons_mobs.mobenchants;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_mobs.mobenchants.MobEnchantmentHelper.executeIfPresent;
import static com.infamous.dungeons_mobs.mod.ModMobEnchantments.DEFLECT;
import static com.sun.javafx.font.FontResource.ZERO;

@Mod.EventBusSubscriber(modid = DungeonsMobs.MODID)
public class DeflectMobEnchantment extends MobEnchantment {

    public DeflectMobEnchantment(Rarity rarity) {
        super(rarity);
    }

    @SubscribeEvent
    public static void onDeflectImpact(ProjectileImpactEvent event){
        RayTraceResult rayTraceResult = event.getRayTraceResult();
        if(!projectileHitLivingEntity(rayTraceResult)) return;
        Entity entity = event.getEntity();
        if(entity instanceof ProjectileEntity) {
            ProjectileEntity projectile = (ProjectileEntity) entity;
            if (!shooterIsLiving(projectile)) return;
            LivingEntity victim = (LivingEntity) ((EntityRayTraceResult) rayTraceResult).getEntity();
            if (victimIsOwner(projectile, victim)) {
                event.setCanceled(true);
                return;
            }
            executeIfPresent(victim, DEFLECT.get(), () -> {
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

    private static void deflectProjectile(ProjectileEntity projectile, LivingEntity victim) {
        if(projectile instanceof ShulkerBulletEntity){
            ((ShulkerBulletEntity) projectile).finalTarget = projectile.getOwner();
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
            Vector3d deltaMovement = projectile.getDeltaMovement();
            double speed = (double) MathHelper.sqrt(deltaMovement.x * deltaMovement.x + deltaMovement.y * deltaMovement.y + deltaMovement.z * deltaMovement.z);
            speed = speed < 1.0E-4D ? ZERO : speed;
            double d0 = projectile.getOwner().getX() - victim.getX();
            double d1 = projectile.getOwner().getY(0.3333333333333333D) - projectile.getY();
            double d2 = projectile.getOwner().getZ() - victim.getZ();
            double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
            projectile.shoot(d0, d1 + d3 * (double) 0.2F, d2, (float) speed, (float) (14 - victim.level.getDifficulty().getId() * 4));
        }
    }

    private static boolean victimIsOwner(ProjectileEntity projectile, LivingEntity victim) {
        return victim.equals(projectile.getOwner());
    }

    public static boolean projectileHitLivingEntity(RayTraceResult rayTraceResult) {
        if(rayTraceResult instanceof EntityRayTraceResult){
            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)rayTraceResult;
            if(entityRayTraceResult.getEntity() instanceof LivingEntity){
                return true;
            } else{
                return false;
            }
        } else{
            return false;
        }
    }

    public static boolean shooterIsLiving(ProjectileEntity projectile) {
        return projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity;
    }
}