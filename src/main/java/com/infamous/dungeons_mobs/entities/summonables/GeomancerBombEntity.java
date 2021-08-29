package com.infamous.dungeons_mobs.entities.summonables;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.network.IPacket;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class GeomancerBombEntity extends ConstructEntity {
    private float explosionRadius = 3.0F;

    public GeomancerBombEntity(World worldIn){
        super(ModEntityTypes.GEOMANCER_BOMB.get(), worldIn);
    }

    public GeomancerBombEntity(World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicksIn) {
        super(ModEntityTypes.GEOMANCER_BOMB.get(), worldIn, x, y, z, casterIn, lifeTicksIn);
    }

    public GeomancerBombEntity(EntityType<? extends GeomancerBombEntity> explodingPillarEntityEntityType, World world) {
        super(explodingPillarEntityEntityType, world);
    }

    @Override
    public void handleExpiration() {
        super.handleExpiration();
        if (!this.level.isClientSide) {
            this.explode();
        }
    }

    private void explode() {
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), this.explosionRadius, Explosion.Mode.NONE);
    }


    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
