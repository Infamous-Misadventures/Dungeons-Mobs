package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.entities.ender.AbstractEnderlingEntity;
import com.infamous.dungeons_mobs.mod.ModDamageSources;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class BlastlingBulletEntity extends NecromancerOrbEntity {

	public BlastlingBulletEntity(EntityType<? extends BlastlingBulletEntity> p_i50147_1_, Level p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);
	}

	public BlastlingBulletEntity(Level p_i1794_1_, LivingEntity p_i1794_2_, double p_i1794_3_, double p_i1794_5_,
			double p_i1794_7_) {
		super(ModEntityTypes.BLASTLING_BULLET.get(), p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_, p_i1794_1_);
	}

	public BlastlingBulletEntity(Level p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_,
			double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
		super(ModEntityTypes.BLASTLING_BULLET.get(), p_i1795_1_, p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_,
				p_i1795_10_, p_i1795_12_);
	}

	protected float getInertia() {
		return super.getInertia() * 1.1F;
	}

	public boolean isOnFire() {
		return false;
	}

	protected void onHitEntity(EntityHitResult p_213868_1_) {
		super.onHitEntity(p_213868_1_);
		if (!this.level.isClientSide) {
			Entity entity = p_213868_1_.getEntity();
			Entity entity1 = this.getOwner();
			if (entity1 instanceof LivingEntity && !(entity instanceof AbstractEnderlingEntity)) {
				LivingEntity livingentity = (LivingEntity) entity1;
				entity.hurt(ModDamageSources.blastlingBullet(this, livingentity), 4.0F);
			}

		}
	}

	protected void onHit(HitResult p_70227_1_) {
		super.onHit(p_70227_1_);
		this.playSound(ModSoundEvents.BLASTLING_BULLET_LAND.get(), 1.0F, 1.0F);
		for (int i = 0; i < this.random.nextInt(35) + 20; ++i) {
			this.level.addParticle(ParticleTypes.WITCH, this.getX() + this.random.nextGaussian(),
					this.getY() + 0.5D + this.random.nextGaussian() * (double) 0.13F,
					this.getZ() + this.random.nextGaussian(), 0.0D, 0.0D, 0.0D);
		}
		if (!this.level.isClientSide) {
			// Explosion.Mode explosion$mode =
			// net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level,
			// this.getOwner()) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
			// this.level.explode(this, this.getX(), this.getY(), this.getZ(), 0.5F, false,
			// explosion$mode);
			this.remove(RemovalReason.DISCARDED);
		}

	}

	public boolean isPickable() {
		return false;
	}

	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		return false;
	}

	protected boolean shouldBurn() {
		return false;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}