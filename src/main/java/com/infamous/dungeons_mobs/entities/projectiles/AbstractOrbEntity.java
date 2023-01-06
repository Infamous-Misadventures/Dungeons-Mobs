package com.infamous.dungeons_mobs.entities.projectiles;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public abstract class AbstractOrbEntity extends Fireball {

    protected AbstractOrbEntity(EntityType<? extends AbstractOrbEntity> entityType, Level world) {
        super(entityType, world);
    }

    protected AbstractOrbEntity(EntityType<? extends AbstractOrbEntity> entityType, Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(entityType, shooter, accelX, accelY, accelZ, worldIn);
    }

    protected AbstractOrbEntity(EntityType<? extends AbstractOrbEntity> entityType, Level worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(entityType, x, y, z, accelX, accelY, accelZ, worldIn);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.remove(RemovalReason.DISCARDED);
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
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }
}
