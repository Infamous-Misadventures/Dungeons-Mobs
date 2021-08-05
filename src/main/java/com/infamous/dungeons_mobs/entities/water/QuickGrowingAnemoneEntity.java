package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class QuickGrowingAnemoneEntity extends QuickGrowingVineEntity {

    public QuickGrowingAnemoneEntity(EntityType<? extends QuickGrowingVineEntity> entityType, World world) {
        super(entityType, world);
    }

    public QuickGrowingAnemoneEntity(World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicks) {
        super(ModEntityTypes.QUICK_GROWING_ANEMONE.get(), worldIn, x, y, z, casterIn, lifeTicks);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
}
