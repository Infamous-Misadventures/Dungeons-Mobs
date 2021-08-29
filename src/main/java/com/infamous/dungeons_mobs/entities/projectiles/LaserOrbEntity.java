package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class LaserOrbEntity extends AbstractOrbEntity {

    public LaserOrbEntity(World worldIn){
        super(ModEntityTypes.LASER_ORB.get(), worldIn);
    }

    public LaserOrbEntity(EntityType<? extends LaserOrbEntity> entityType, World world) {
        super(entityType, world);
    }

    public LaserOrbEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntityTypes.LASER_ORB.get(), worldIn, shooter, accelX, accelY, accelZ);
    }

    public LaserOrbEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntityTypes.LASER_ORB.get(), worldIn, x, y, z, accelX, accelY, accelZ);
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityRayTraceResult entityRTR) {
        super.onHitEntity(entityRTR);
        if (!this.level.isClientSide) {
            Entity target = entityRTR.getEntity();
            Entity owner = this.getOwner();
            boolean didHurt = target.hurt(DamageSource.indirectMagic(this, owner), 10.0F); // twice as much damage as normal fire
            if (didHurt && owner instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)owner, target);
            }

        }
    }
}
