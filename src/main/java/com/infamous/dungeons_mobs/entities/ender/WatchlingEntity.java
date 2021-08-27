package com.infamous.dungeons_mobs.entities.ender;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.world.World;

// ENDERMAN-LIKE
public class WatchlingEntity extends EndermanEntity {
    public WatchlingEntity(EntityType<? extends WatchlingEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return EndermanEntity.createAttributes();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }
}
