package com.infamous.dungeons_mobs.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class ParticleGenerator {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    private static final Random RANDOM = new Random();

    @Nullable
    public static Particle addParticleInternal(IParticleData particleData, boolean guarantee, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return addParticleInternal(particleData, guarantee, false, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Nullable
    private static Particle addParticleInternal(IParticleData particleData, boolean guarantee, boolean minimize, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        ActiveRenderInfo activerenderinfo = MINECRAFT.gameRenderer.getMainCamera();
        if (activerenderinfo.isInitialized() && MINECRAFT.particleEngine != null) {
            ParticleStatus particlestatus = calculateParticleLevel(minimize);
            if (guarantee) {
                return MINECRAFT.particleEngine.createParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
            } else if (activerenderinfo.getPosition().distanceToSqr(x, y, z) > 1024.0D) {
                return null;
            } else {
                return particlestatus == ParticleStatus.MINIMAL ? null : MINECRAFT.particleEngine.createParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
            }
        } else {
            return null;
        }
    }

    private static ParticleStatus calculateParticleLevel(boolean minimize) {
        ParticleStatus particlestatus = MINECRAFT.options.particles;
        if (minimize && particlestatus == ParticleStatus.MINIMAL && RANDOM.nextInt(10) == 0) {
            particlestatus = ParticleStatus.DECREASED;
        }

        if (particlestatus == ParticleStatus.DECREASED && RANDOM.nextInt(3) == 0) {
            particlestatus = ParticleStatus.MINIMAL;
        }

        return particlestatus;
    }

    public static void generatePotionImpact(World level, Potion potion, ItemStack itemStack, BlockPos blockPos, int color, SoundEvent soundEvent) {
       Vector3d bottomCenterOf = Vector3d.atBottomCenterOf(blockPos);

       for(int particleIndex = 0; particleIndex < 8; ++particleIndex) {
          level.addParticle(new ItemParticleData(ParticleTypes.ITEM, itemStack), bottomCenterOf.x, bottomCenterOf.y, bottomCenterOf.z, level.random.nextGaussian() * 0.15D, level.random.nextDouble() * 0.2D, level.random.nextGaussian() * 0.15D);
       }

       float red = (float)(color >> 16 & 255) / 255.0F;
       float green = (float)(color >> 8 & 255) / 255.0F;
       float blue = (float)(color >> 0 & 255) / 255.0F;
       IParticleData particleData = potion.hasInstantEffects() ? ParticleTypes.INSTANT_EFFECT : ParticleTypes.EFFECT;

       for(int internalParticleCount = 0; internalParticleCount < 100; ++internalParticleCount) {
          double power = level.random.nextDouble() * 4.0D;
          double d27 = level.random.nextDouble() * Math.PI * 2.0D;
          double d29 = Math.cos(d27) * power;
          double d5 = 0.01D + level.random.nextDouble() * 0.5D;
          double d7 = Math.sin(d27) * power;
          Particle particleInternal = addParticleInternal(particleData, particleData.getType().getOverrideLimiter(), bottomCenterOf.x + d29 * 0.1D, bottomCenterOf.y + 0.3D, bottomCenterOf.z + d7 * 0.1D, d29, d5, d7);
          if (particleInternal != null) {
             float colorStrength = 0.75F + level.random.nextFloat() * 0.25F;
             particleInternal.setColor(red * colorStrength, green * colorStrength, blue * colorStrength);
             particleInternal.setPower((float)power);
          }
       }

       level.playLocalSound((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, soundEvent, SoundCategory.NEUTRAL, 1.0F, level.random.nextFloat() * 0.1F + 0.9F, false);
    }

}
