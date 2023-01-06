package com.infamous.dungeons_mobs.utils;

import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;

public class EchoDamageSource extends EntityDamageSource {
	public EchoDamageSource(Entity entity) {
		super("echo", entity);
	}
}
