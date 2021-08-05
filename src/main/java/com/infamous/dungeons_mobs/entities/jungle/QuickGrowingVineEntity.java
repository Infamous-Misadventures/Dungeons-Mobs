package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class QuickGrowingVineEntity extends VineEntity {

    public QuickGrowingVineEntity(World world) {
        super(ModEntityTypes.QUICK_GROWING_VINE.get(), world);
    }

    public QuickGrowingVineEntity(EntityType<? extends QuickGrowingVineEntity> entityType, World world) {
        super(entityType, world);
    }

    public QuickGrowingVineEntity(EntityType<? extends QuickGrowingVineEntity> entityType, World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicks) {
        super(entityType, worldIn, x, y, z, casterIn, lifeTicks);
    }

    public QuickGrowingVineEntity(World worldIn, double x, double y, double z, LivingEntity casterIn, int lifeTicks) {
        super(ModEntityTypes.QUICK_GROWING_VINE.get(), worldIn, x, y, z, casterIn, lifeTicks);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return VineEntity.setCustomAttributes();
    }
}
