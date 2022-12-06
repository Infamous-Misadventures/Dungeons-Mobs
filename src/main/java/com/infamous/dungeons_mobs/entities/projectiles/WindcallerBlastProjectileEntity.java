package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.entity.Entity.RemovalReason;

public class WindcallerBlastProjectileEntity extends AbstractHurtingProjectile {

    public int lifeTime;

    public WindcallerBlastProjectileEntity(Level world) {
        super(ModEntityTypes.WINDCALLER_BLAST_PROJECTILE.get(), world);
        this.setNoGravity(true);
    }

    public WindcallerBlastProjectileEntity(EntityType<? extends WindcallerBlastProjectileEntity> type, Level world) {
        super(type, world);
        this.setNoGravity(true);
    }

    public WindcallerBlastProjectileEntity(Level p_i1771_1_, LivingEntity p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_) {
        super(ModEntityTypes.WINDCALLER_BLAST_PROJECTILE.get(), p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_, p_i1771_1_);
        this.setNoGravity(true);
    }

    public WindcallerBlastProjectileEntity(Level p_i1772_1_, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_) {
        super(ModEntityTypes.WINDCALLER_BLAST_PROJECTILE.get(), p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_, p_i1772_1_);
        this.setNoGravity(true);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.lifeTime++;

        if (!this.level.isClientSide && this.lifeTime > 10) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void tick() {
        super.tick();
        for (int i = 0; i < 3; i++) {
            this.level.addParticle(this.getTrailParticle(), this.getRandomX(1), this.getRandomY(), this.getRandomZ(1), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected float getInertia() {
        return 1.1F;
    }

    @Override
    protected boolean canHitEntity(Entity p_230298_1_) {
        if (!p_230298_1_.isSpectator() && p_230298_1_.isAlive() && p_230298_1_.isPickable()) {
            Entity entity = this.getOwner();
            if (entity != null && entity == p_230298_1_) {
                return false;
            } else {
                return entity == null || !entity.isPassengerOfSameVehicle(p_230298_1_);
            }
        } else {
            return false;
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ModParticleTypes.WIND.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        Entity entity = p_213868_1_.getEntity();

        if (!this.level.isClientSide) {
            entity.getRootVehicle().ejectPassengers();
            if (entity instanceof LivingEntity) {
                double d1 = this.getX() - entity.getX();

                double d0;
                for (d0 = this.getZ() - entity.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                    d1 = (Math.random() - Math.random()) * 0.01D;
                }

                ((LivingEntity) entity).knockback(2F, d1, d0);
            } else {
                entity.setDeltaMovement(entity.getDeltaMovement().add(this.getDeltaMovement().scale(1.5D).add(0, 0.5, 0)));
            }
            entity.hurtMarked = true;
        }
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    protected boolean shouldBurn() {
        return false;
    }

    public boolean isOnFire() {
        return false;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
