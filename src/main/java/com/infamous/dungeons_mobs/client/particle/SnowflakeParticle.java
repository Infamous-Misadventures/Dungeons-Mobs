package com.infamous.dungeons_mobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SnowflakeParticle extends TextureSheetParticle {

	protected SnowflakeParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet,
			double xd, double yd, double zd) {
		super(level, xCoord, yCoord, zCoord, xd, yd, zd);

		this.quadSize *= 1.25F;
		this.lifetime = 5 + this.random.nextInt(10);
		this.hasPhysics = false;

		this.pickSprite(spriteSet);
	}

	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.yd += 0.004D;
			this.move(this.xd, this.yd, this.zd);
			if (this.y == this.yo) {
				this.xd *= 1.1D;
				this.zd *= 1.1D;
			}

			this.xd *= 0.75F;
			this.yd *= 0.75F;
			this.zd *= 0.75F;
			if (this.onGround) {
				this.xd *= 0.6F;
				this.zd *= 0.6F;
			}

		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Factory(SpriteSet spriteSet) {
			this.sprites = spriteSet;
		}

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z,
				double dx, double dy, double dz) {
			return new SnowflakeParticle(level, x, y, z, this.sprites, dx, dy, dz);
		}
	}
}
