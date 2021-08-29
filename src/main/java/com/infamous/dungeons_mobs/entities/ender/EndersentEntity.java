package com.infamous.dungeons_mobs.entities.ender;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.world.World;

// Enderman-like Miniboss
public class EndersentEntity extends EndermanEntity {

    public EndersentEntity(EntityType<? extends EndersentEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return EndermanEntity.createAttributes().add(Attributes.MAX_HEALTH, 80.0D).add(Attributes.ATTACK_DAMAGE, 11.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }
}
