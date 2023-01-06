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


public class CorruptedMagicParticle extends TextureSheetParticle {
	
    protected CorruptedMagicParticle(ClientLevel level, double xCoord, double yCoord, double zCoord,
    		SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.quadSize *= 1.25F;
        this.lifetime = 15;
        this.hasPhysics = false;
        
        this.pickSprite(spriteSet);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
           this.remove();
        }
        fadeOut();
    }
    
    @Override
    protected int getLightColor(float p_189214_1_) {
    	return 240;
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)lifetime) * age + 1);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new CorruptedMagicParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
