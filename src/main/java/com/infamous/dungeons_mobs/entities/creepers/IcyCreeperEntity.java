package com.infamous.dungeons_mobs.entities.creepers;

import java.util.Collection;
import java.util.Random;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;


public class IcyCreeperEntity extends CreeperEntity {

	   private int oldSwell;
	   private int swell;
	   private int maxSwell = 30;
	   private int explosionRadius = 3;
	   
    public IcyCreeperEntity(World worldIn) {
        super(ModEntityTypes.ICY_CREEPER.get(), worldIn);
    }

    public static boolean canIcyCreeperSpawn(EntityType<IcyCreeperEntity> entityType, IServerWorld iWorld, SpawnReason spawnReason, BlockPos blockPos, Random rand) {
        return checkMonsterSpawnRules(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == SpawnReason.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            this.level.addParticle(ModParticleTypes.SNOWFLAKE.get(), this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
        }
        super.aiStep();
    }

    public IcyCreeperEntity(EntityType<? extends CreeperEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return CreeperEntity.createAttributes();
    }
    
    public void tick() {
        if (this.isAlive()) {
           this.oldSwell = this.swell;
           if (this.isIgnited()) {
              this.setSwellDir(1);
           }

           int i = this.getSwellDir();
           if (i > 0 && this.swell == 0) {
              this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.25F);
           }

           this.swell += i;
           if (this.swell < 0) {
              this.swell = 0;
           }

           if (this.swell >= this.maxSwell) {
              this.swell = this.maxSwell;
              this.explodeCreeper();
           }
        }

        super.tick();
     }
    
    private void explodeCreeper() {
        if (!this.level.isClientSide) {
           Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
           float f = this.isPowered() ? 2.0F : 1.0F;
           this.dead = true;
           this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, explosion$mode);
           this.playSound(ModSoundEvents.ICY_CREEPER_EXPLODE.get(), 2.0F, 1.0F);
           this.remove();
           this.spawnLingeringCloud();
        }
        
        for(int i = 0; i < 75; ++i) {
            double d0 = this.random.nextGaussian() * 0.3D;
            double d1 = this.random.nextGaussian() * 0.2D;
            double d2 = this.random.nextGaussian() * 0.3D;
            this.level.addParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), d0, d1, d2);
         }
        
        for(int i = 0; i < 50; ++i) {
            double d0 = this.random.nextGaussian() * 0.6D;
            double d1 = this.random.nextGaussian() * 0.3D;
            double d2 = this.random.nextGaussian() * 0.6D;
            this.level.addParticle(ModParticleTypes.SNOWFLAKE.get(), this.getX(), this.getY(), this.getZ(), d0, d1, d2);
         }

     }

     private void spawnLingeringCloud() {
        Collection<EffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
           AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
           areaeffectcloudentity.setRadius(2.5F);
           areaeffectcloudentity.setRadiusOnUse(-0.5F);
           areaeffectcloudentity.setWaitTime(10);
           areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
           areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());

           for(EffectInstance effectinstance : collection) {
              areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
           }

           this.level.addFreshEntity(areaeffectcloudentity);
        }

     }
}
