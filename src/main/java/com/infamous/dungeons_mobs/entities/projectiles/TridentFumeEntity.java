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

public class TridentFumeEntity extends AbstractOrbEntity {

    public TridentFumeEntity(World worldIn){
        super(ModEntityTypes.TRIDENT_FUME.get(), worldIn);
    }

    public TridentFumeEntity(EntityType<? extends TridentFumeEntity> entityType, World world) {
        super(entityType, world);
    }

    public TridentFumeEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntityTypes.TRIDENT_FUME.get(), worldIn, shooter, accelX, accelY, accelZ);
    }

    public TridentFumeEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntityTypes.TRIDENT_FUME.get(), worldIn, x, y, z, accelX, accelY, accelZ);
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityRayTraceResult entityRTR) {
        super.onHitEntity(entityRTR);
        if (!this.level.isClientSide) {
            Entity target = entityRTR.getEntity();
            Entity owner = this.getOwner();
            boolean didHurt = target.hurt(DamageSource.fireball(this, owner), 10.0F); // twice as much damage as normal fire
            if (didHurt && owner instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)owner, target);
            }

        }
    }
}
