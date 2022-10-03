package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.entities.summonables.SimpleTrapEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CobwebProjectileEntity extends ProjectileEntity implements IAnimatable {
	
	AnimationFactory factory = new AnimationFactory(this);
	
	public boolean delayedSpawnParticles;
	
	   public CobwebProjectileEntity(EntityType<? extends CobwebProjectileEntity> p_i50162_1_, World p_i50162_2_) {
	      super(p_i50162_1_, p_i50162_2_);
	   }

	   public CobwebProjectileEntity(World p_i47273_1_, LivingEntity p_i47273_2_) {
	      this(ModEntityTypes.COBWEB_PROJECTILE.get(), p_i47273_1_);
	      super.setOwner(p_i47273_2_);
	      this.setPos(p_i47273_2_.getX() - (double)(p_i47273_2_.getBbWidth() + 1.0F) * 0.5D * (double)MathHelper.sin(p_i47273_2_.yBodyRot * ((float)Math.PI / 180F)), p_i47273_2_.getEyeY() - (double)0.1F, p_i47273_2_.getZ() + (double)(p_i47273_2_.getBbWidth() + 1.0F) * 0.5D * (double)MathHelper.cos(p_i47273_2_.yBodyRot * ((float)Math.PI / 180F)));
	   }

	   @OnlyIn(Dist.CLIENT)
	   public CobwebProjectileEntity(World p_i47274_1_, double p_i47274_2_, double p_i47274_4_, double p_i47274_6_, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
	      this(ModEntityTypes.COBWEB_PROJECTILE.get(), p_i47274_1_);
	      this.setPos(p_i47274_2_, p_i47274_4_, p_i47274_6_);

	      for(int i = 0; i < 7; ++i) {
	         double d0 = 0.4D + 0.1D * (double)i;
	         p_i47274_1_.addParticle(ParticleTypes.SPIT, p_i47274_2_, p_i47274_4_, p_i47274_6_, p_i47274_8_ * d0, p_i47274_10_, p_i47274_12_ * d0);
	      }

	      this.setDeltaMovement(p_i47274_8_, p_i47274_10_, p_i47274_12_);
	   }

	   public void tick() {
	      super.tick();
	      
	      if (this.delayedSpawnParticles) {
	    	  this.delayedSpawnParticles = false;
	    	  this.createSpawnParticles();	    	 
	      }
	      
	      Vector3d vector3d = this.getDeltaMovement();
	      RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
	      if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
	         this.onHit(raytraceresult);
	      }

	      double d0 = this.getX() + vector3d.x;
	      double d1 = this.getY() + vector3d.y;
	      double d2 = this.getZ() + vector3d.z;
	      this.updateRotation();
	      float f = 0.99F;
	      float f1 = 0.06F;
	      if (this.level.getBlockStates(this.getBoundingBox()).noneMatch(AbstractBlock.AbstractBlockState::isAir)) {
	         this.remove();
	      } else if (this.isInWaterOrBubble()) {
	         this.remove();
	      } else {
	         this.setDeltaMovement(vector3d.scale((double)f));
	         if (!this.isNoGravity()) {
	            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double)-f1, 0.0D));
	         }

	         this.setPos(d0, d1, d2);
	      }
	   }

	   protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
	      super.onHitEntity(p_213868_1_);
	      Entity entity = this.getOwner();
	      if (p_213868_1_.getEntity() instanceof LivingEntity && ((LivingEntity)p_213868_1_.getEntity()).getMobType() == CreatureAttribute.ARTHROPOD) {
	    	  
	      } else {
		      if (entity instanceof LivingEntity) {
		         p_213868_1_.getEntity().hurt(DamageSource.indirectMobAttack(this, (LivingEntity)entity).setProjectile(), 1.0F);
		      }
	
		      if (!this.level.isClientSide) {
		    	 this.spawnTrap(p_213868_1_.getEntity().getX(), p_213868_1_.getEntity().getY(), p_213868_1_.getEntity().getZ());
		    	 
		         this.remove();
		      }
	      }
	   }
	   
	   public void createSpawnParticles() {
		   if (!this.level.isClientSide) {
			   this.level.broadcastEntityEvent(this, (byte) 1);
		   } else {
			   for (int i = 0; i < 5; i++) {
				   this.level.addParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			   }
		   }
	   }
	   
	   @Override
	public void handleEntityEvent(byte p_70103_1_) {
		if (p_70103_1_ == 1) {
			for (int i = 0; i < 5; i++) {
				   this.level.addParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleEntityEvent(p_70103_1_);
		}
	}

	   protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
	      super.onHitBlock(p_230299_1_);
	      if (!this.level.isClientSide) {
	    	 this.spawnTrap(this.getX(), this.getY(), this.getZ());
	    	 
	         this.remove();
	      }

	   }
	   
	   public void spawnTrap(double x, double y, double z) {
	    	 SimpleTrapEntity trap = ModEntityTypes.SIMPLE_TRAP.get().create(this.level);
	    	 trap.moveTo(x, y, z);
	    	 trap.owner = this.getOwner();
	    	 
	    	 this.level.addFreshEntity(trap);
	    	 
	    	 this.playSound(ModSoundEvents.SPIDER_WEB_IMPACT.get(), 1.0F, 1.0F);
	   }

	   protected void defineSynchedData() {
		   
	   }
	   
		@Override
		public void registerControllers(AnimationData data) {
			data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
		}

		private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("web_projectile_idle", true));
			return PlayState.CONTINUE;
		}

		@Override
		public AnimationFactory getFactory() {
			return factory;
		}

	   public IPacket<?> getAddEntityPacket() {
	      return NetworkHooks.getEntitySpawningPacket(this);
	   }
	}
