package com.infamous.dungeons_mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

import java.util.UUID;

public class CombatEvent {

    public static void forceKnockback(LivingEntity attackTarget, float strength, double ratioX, double ratioZ, double knockbackResistanceReduction) {
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(attackTarget, strength, ratioX, ratioZ);
        if(event.isCanceled()) return;
        strength = event.getStrength();
        ratioX = event.getRatioX();
        ratioZ = event.getRatioZ();
        strength = (float)((double)strength * (1.0D - attackTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * knockbackResistanceReduction));
        if (!(strength <= 0.0F)) {
            attackTarget.hasImpulse = true;
            Vector3d vector3d = attackTarget.getDeltaMovement();
            Vector3d vector3d1 = (new Vector3d(ratioX, 0.0D, ratioZ)).normalize().scale((double)strength);
            attackTarget.setDeltaMovement(vector3d.x / 2.0D - vector3d1.x, attackTarget.isOnGround() ? Math.min(0.4D, vector3d.y / 2.0D + (double)strength) : vector3d.y, vector3d.z / 2.0D - vector3d1.z);
        }
    }

    public static void addNewProjectile(ProjectileEntity projectile, CompoundNBT compoundNBT, World world, float rotation) {
        ProjectileEntity newProjectile = (ProjectileEntity) projectile.getType().create(world);
        UUID uuid = newProjectile.getUUID();
        newProjectile.load(compoundNBT);
        newProjectile.setUUID(uuid);
        Vector3d vector3d = newProjectile.getDeltaMovement().yRot((float) (Math.PI / rotation));
        newProjectile.setDeltaMovement(vector3d);
        float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        newProjectile.yRot = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI));
        newProjectile.xRot = (float) (MathHelper.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI));
        newProjectile.yRotO = newProjectile.yRot;
        newProjectile.xRotO = newProjectile.xRot;
        if (newProjectile instanceof DamagingProjectileEntity) {
            DamagingProjectileEntity newDamagingProjectile = (DamagingProjectileEntity) newProjectile;
            Vector3d newPower = new Vector3d(newDamagingProjectile.xPower, newDamagingProjectile.yPower, newDamagingProjectile.zPower).yRot((float) (Math.PI / rotation));
            newDamagingProjectile.xPower = newPower.x;
            newDamagingProjectile.yPower = newPower.y;
            newDamagingProjectile.zPower = newPower.z;
        }

        world.addFreshEntity(newProjectile);
    }

    public static void AreaAttack(LivingEntity v, float range, float X, float Y, float Z, float arc, float damage, double DeltaY) {
        for (LivingEntity entityHit : v.level.getEntitiesOfClass(LivingEntity.class, v.getBoundingBox().inflate(X, Y, Z))) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - v.getZ(), entityHit.getX() - v.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = v.yBodyRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - v.getZ()) * (entityHit.getZ() - v.getZ()) + (entityHit.getX() - v.getX()) * (entityHit.getX() - v.getX()));
            if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                if (!v.isAlliedTo(entityHit) && !(entityHit == v)) {
                    entityHit.hurt(DamageSource.mobAttack(v), (float) v.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage);

                    float attackKnockback = (float) v.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                    double ratioX = (double) MathHelper.sin(v.yRot * ((float) Math.PI / 180F));
                    double ratioZ = (double) (-MathHelper.cos(v.yRot * ((float) Math.PI / 180F)));
                    double knockbackReduction = 0.35D;
                    entityHit.hurt(DamageSource.mobAttack(v), damage);
                    forceKnockback(entityHit, attackKnockback * 0.8F, ratioX, ratioZ, knockbackReduction);
                    entityHit.setDeltaMovement(entityHit.getDeltaMovement().add(0,DeltaY,0));
                }
            }
        }
    }
}
