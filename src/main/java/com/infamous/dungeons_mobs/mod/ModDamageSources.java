package com.infamous.dungeons_mobs.mod;

import java.util.Random;

import com.infamous.dungeons_mobs.entities.projectiles.BlastlingBulletEntity;
import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.entities.summonables.TridentStormEntity;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

public class ModDamageSources {

	static Random rand = new Random();

	public static DamageSource blastlingBullet(BlastlingBulletEntity p_233549_0_, Entity p_233549_1_) {
		return (new IndirectEntityDamageSource("blastling", p_233549_0_, p_233549_1_)).setProjectile().setExplosion();
	}
	
	public static DamageSource iceChunk(IceCloudEntity p_233549_0_, Entity p_233549_1_) {
		return (new IndirectEntityDamageSource("ice_chunk", p_233549_0_, p_233549_1_)).setExplosion();
	}
	
	public static DamageSource summonedTridentStorm(TridentStormEntity p_233549_0_, Entity p_233549_1_) {
		return (new IndirectEntityDamageSource("summoned_trident_storm", p_233549_0_, p_233549_1_)).setProjectile();
	}
	
	public static DamageSource tridentStorm(TridentStormEntity p_233549_0_) {
		return (new IndirectEntityDamageSource("trident_storm", p_233549_0_, null)).setProjectile();
	}
}
