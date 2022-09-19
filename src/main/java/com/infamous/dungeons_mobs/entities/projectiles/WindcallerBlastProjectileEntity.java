package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class WindcallerBlastProjectileEntity extends DamagingProjectileEntity {

	public int lifeTime;
	
    public WindcallerBlastProjectileEntity(World world){
        super(ModEntityTypes.WINDCALLER_BLAST_PROJECTILE.get(), world);
    }

    public WindcallerBlastProjectileEntity(EntityType<? extends WindcallerBlastProjectileEntity> type, World world) {
        super(type, world);
    }
    
    public WindcallerBlastProjectileEntity(World p_i1771_1_, LivingEntity p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_) {
        super(ModEntityTypes.WINDCALLER_BLAST_PROJECTILE.get(), p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_, p_i1771_1_);
     }

     public WindcallerBlastProjectileEntity(World p_i1772_1_, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_) {
        super(ModEntityTypes.WINDCALLER_BLAST_PROJECTILE.get(), p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_, p_i1772_1_);
     }
    
     @Override
    public void baseTick() {
    	super.baseTick();
    	
    	this.lifeTime ++;
    	
    	if (!this.level.isClientSide && this.lifeTime > 10) {
    		this.remove();
    	}
    }
     
     @Override
    public void tick() {
    	super.tick();    
    	for (int i = 0; i < 3; i++) {
    		this.level.addParticle(this.getTrailParticle(), this.getRandomX(1), this.getRandomY(), this.getRandomZ(1), 0.0D, 0.0D, 0.0D);
    	}
    }
     
     @Override
    protected float getInertia() {
    	return 1.1F;
    }
     
     @Override
     protected boolean canHitEntity(Entity p_230298_1_) {
         if (!p_230298_1_.isSpectator() && p_230298_1_.isAlive() && p_230298_1_.isPickable()) {
            Entity entity = this.getOwner();
            if (entity != null && entity == p_230298_1_) {
            	return false;
            } else {
            	return entity == null || !entity.isPassengerOfSameVehicle(p_230298_1_);
            }
         } else {
            return false;
         }
      }
     
     @Override
    protected IParticleData getTrailParticle() {
    	return ModParticleTypes.WIND.get();
    }
     
     @Override
    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
    	super.onHitEntity(p_213868_1_);
    	
    	if (!this.level.isClientSide) {
    		p_213868_1_.getEntity().getRootVehicle().ejectPassengers();
    		
	    	p_213868_1_.getEntity().setDeltaMovement(p_213868_1_.getEntity().getDeltaMovement().add(this.getDeltaMovement().scale(1.5D).add(0, 0.5, 0)));
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
		 
		   public boolean isOnFire() {
			      return false;
			   }
		   
		   @Override
		public IPacket<?> getAddEntityPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}
}
