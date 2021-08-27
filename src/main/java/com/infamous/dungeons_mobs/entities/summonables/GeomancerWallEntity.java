package com.infamous.dungeons_mobs.entities.summonables;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class GeomancerWallEntity extends ConstructEntity {

    public GeomancerWallEntity(World world) {
        super(ModEntityTypes.GEOMANCER_WALL.get(), world);
    }

    public GeomancerWallEntity(EntityType<? extends GeomancerWallEntity> entityType, World world) {
        super(entityType, world);
    }

    public GeomancerWallEntity(World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicksIn) {
        super(ModEntityTypes.GEOMANCER_WALL.get(), worldIn, x, y, z, casterIn, lifeTicksIn);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
