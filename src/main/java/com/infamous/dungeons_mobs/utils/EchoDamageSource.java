package com.infamous.dungeons_mobs.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class EchoDamageSource extends EntityDamageSource {
    public EchoDamageSource(Entity entity) {
        super("echo", entity);
    }
}
