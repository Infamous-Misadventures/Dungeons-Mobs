package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class LaserBeamEntity extends AbstractFireballEntity {

    public LaserBeamEntity(World worldIn){
        super(ModEntityTypes.WRAITH_FIREBALL.get(), worldIn);
    }

    public LaserBeamEntity(EntityType<? extends LaserBeamEntity> entityType, World world) {
        super(entityType, world);
    }

    public LaserBeamEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntityTypes.WRAITH_FIREBALL.get(), shooter, accelX, accelY, accelZ, worldIn);
    }

    public LaserBeamEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntityTypes.WRAITH_FIREBALL.get(), x, y, z, accelX, accelY, accelZ, worldIn);
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
