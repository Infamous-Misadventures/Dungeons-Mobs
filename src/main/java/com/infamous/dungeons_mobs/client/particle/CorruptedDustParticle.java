package com.infamous.dungeons_mobs.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;


public class CorruptedDustParticle extends SpriteTexturedParticle {
	
    protected CorruptedDustParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
    		IAnimatedSprite spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.quadSize *= 2.0F;
        this.lifetime = 10;
        this.hasPhysics = true;
        
        this.pickSprite(spriteSet);
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.xd = this.xd * 0.95F;
        this.yd = this.yd * 0.75F;
        this.zd = this.zd * 0.95F;
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
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprites;

        public Factory(IAnimatedSprite spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(BasicParticleType particleType, ClientWorld level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new CorruptedDustParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
