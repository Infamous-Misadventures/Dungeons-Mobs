package com.infamous.dungeons_mobs.entities.creepers;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Collection;
import java.util.Random;


public class IcyCreeperEntity extends Creeper {

	   private int oldSwell;
	   private int swell;
	   private int maxSwell = 30;
	   private int explosionRadius = 3;
	   
    public IcyCreeperEntity(Level worldIn) {
        super(ModEntityTypes.ICY_CREEPER.get(), worldIn);
    }

    public static boolean canIcyCreeperSpawn(EntityType<IcyCreeperEntity> entityType, ServerLevelAccessor iWorld, MobSpawnType spawnReason, BlockPos blockPos, Random rand) {
        return checkMonsterSpawnRules(entityType, iWorld, spawnReason, blockPos, rand) && (spawnReason == MobSpawnType.SPAWNER || iWorld.canSeeSky(blockPos));
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide) {
            this.level.addParticle(ModParticleTypes.SNOWFLAKE.get(), this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
        }
        super.aiStep();
    }

    public IcyCreeperEntity(EntityType<? extends Creeper> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder setCustomAttributes(){
        return Creeper.createAttributes();
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
           Explosion.BlockInteraction explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
           float f = this.isPowered() ? 2.0F : 1.0F;
           this.dead = true;
           this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, explosion$mode);
           this.playSound(ModSoundEvents.ICY_CREEPER_EXPLODE.get(), 2.0F, 1.0F);
           this.remove(RemovalReason.DISCARDED);
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
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
           AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
           areaeffectcloudentity.setRadius(2.5F);
           areaeffectcloudentity.setRadiusOnUse(-0.5F);
           areaeffectcloudentity.setWaitTime(10);
           areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
           areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());

           for(MobEffectInstance effectinstance : collection) {
              areaeffectcloudentity.addEffect(new MobEffectInstance(effectinstance));
           }

           this.level.addFreshEntity(areaeffectcloudentity);
        }

     }
}
