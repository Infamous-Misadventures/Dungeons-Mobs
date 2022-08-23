package com.infamous.dungeons_mobs.mod;

import java.util.Random;

import com.infamous.dungeons_mobs.entities.projectiles.BlastlingBulletEntity;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

public class ModDamageSources {

	static Random rand = new Random();

	public static DamageSource blastlingBullet(BlastlingBulletEntity p_233549_0_, Entity p_233549_1_) {
		return (new IndirectEntityDamageSource("blastling", p_233549_0_, p_233549_1_)).setProjectile();
	}
}
