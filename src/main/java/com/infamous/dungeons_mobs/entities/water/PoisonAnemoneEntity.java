package com.infamous.dungeons_mobs.entities.water;

import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class PoisonAnemoneEntity extends PoisonQuillVineEntity {

    public PoisonAnemoneEntity(EntityType<? extends PoisonQuillVineEntity> entityType, World world) {
        super(entityType, world);
    }

    public PoisonAnemoneEntity(World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicks) {
        super(ModEntityTypes.POISON_ANEMONE.get(), worldIn, x, y, z, casterIn, lifeTicks);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
}
