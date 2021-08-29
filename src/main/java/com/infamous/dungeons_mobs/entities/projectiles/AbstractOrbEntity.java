package com.infamous.dungeons_mobs.entities.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class AbstractOrbEntity extends AbstractFireballEntity {

    protected AbstractOrbEntity(EntityType<? extends AbstractOrbEntity> entityType, World world) {
        super(entityType, world);
    }

    protected AbstractOrbEntity(EntityType<? extends AbstractOrbEntity> entityType, World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(entityType, shooter, accelX, accelY, accelZ, worldIn);
    }

    protected AbstractOrbEntity(EntityType<? extends AbstractOrbEntity> entityType, World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(entityType, x, y, z, accelX, accelY, accelZ, worldIn);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.remove();
        }

    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean isPickable() {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }
}
