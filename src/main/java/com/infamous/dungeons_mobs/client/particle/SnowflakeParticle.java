package com.infamous.dungeons_mobs.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;


@OnlyIn(Dist.CLIENT)
public class SnowflakeParticle extends SpriteTexturedParticle {
    private static final Random RANDOM = new Random();

    private SnowflakeParticle(ClientWorld clientWorld, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeed, double ySpeed, double zSpeed) {
        super(clientWorld, xCoordIn, yCoordIn, zCoordIn, 0.5D - RANDOM.nextDouble(), ySpeed, 0.5D - RANDOM.nextDouble());
        this.yd *= (double)0.2F;
        if (xSpeed == 0.0D && zSpeed == 0.0D) {
            this.xd *= (double)0.1F;
            this.zd *= (double)0.1F;
        }

        this.quadSize *= 0.75F;
        this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
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

            this.xd *= (double)0.96F;
            this.yd *= (double)0.96F;
            this.zd *= (double)0.96F;
            if (this.onGround) {
                this.xd *= (double)0.7F;
                this.zd *= (double)0.7F;
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite sprite){
            this.spriteSet = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SnowflakeParticle snowflakeParticle = new SnowflakeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            snowflakeParticle.pickSprite(this.spriteSet);
            return snowflakeParticle;
        }
    }
}
