package com.infamous.dungeons_mobs.mod;

import java.util.Random;

import com.infamous.dungeons_mobs.entities.projectiles.BlastlingBulletEntity;
import com.infamous.dungeons_mobs.entities.projectiles.PoisonQuillEntity;
import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.infamous.dungeons_mobs.entities.summonables.TridentStormEntity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

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

	public static DamageSource poisonQuill(PoisonQuillEntity p_233549_0_, Entity p_233549_1_) {
		return (new IndirectEntityDamageSource("poison_quill", p_233549_0_, p_233549_1_)).setProjectile();
	}

}
