package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.entities.summonables.SimpleTrapEntity;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class CobwebProjectileEntity extends Projectile implements IAnimatable {
	
	AnimationFactory factory = GeckoLibUtil.createFactory(this);
	
	public boolean delayedSpawnParticles;
	
	   public CobwebProjectileEntity(EntityType<? extends CobwebProjectileEntity> p_i50162_1_, Level p_i50162_2_) {
	      super(p_i50162_1_, p_i50162_2_);
	   }

	   public CobwebProjectileEntity(Level p_i47273_1_, LivingEntity p_i47273_2_) {
	      this(ModEntityTypes.COBWEB_PROJECTILE.get(), p_i47273_1_);
	      super.setOwner(p_i47273_2_);
	      this.setPos(p_i47273_2_.getX() - (double)(p_i47273_2_.getBbWidth() + 1.0F) * 0.5D * (double)Mth.sin(p_i47273_2_.yBodyRot * ((float)Math.PI / 180F)), p_i47273_2_.getEyeY() - (double)0.1F, p_i47273_2_.getZ() + (double)(p_i47273_2_.getBbWidth() + 1.0F) * 0.5D * (double)Mth.cos(p_i47273_2_.yBodyRot * ((float)Math.PI / 180F)));
	   }

	   @OnlyIn(Dist.CLIENT)
	   public CobwebProjectileEntity(Level p_i47274_1_, double p_i47274_2_, double p_i47274_4_, double p_i47274_6_, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
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
	      
	      Vec3 vector3d = this.getDeltaMovement();
	      HitResult raytraceresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
	      if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
	         this.onHit(raytraceresult);
	      }

	      double d0 = this.getX() + vector3d.x;
	      double d1 = this.getY() + vector3d.y;
	      double d2 = this.getZ() + vector3d.z;
	      this.updateRotation();
	      float f = 0.99F;
	      float f1 = 0.06F;
	      if (this.level.getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
	         this.remove(RemovalReason.DISCARDED);
	      } else if (this.isInWaterOrBubble()) {
	         this.remove(RemovalReason.DISCARDED);
	      } else {
	         this.setDeltaMovement(vector3d.scale((double)f));
	         if (!this.isNoGravity()) {
	            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double)-f1, 0.0D));
	         }

	         this.setPos(d0, d1, d2);
	      }
	   }

	   protected void onHitEntity(EntityHitResult p_213868_1_) {
	      super.onHitEntity(p_213868_1_);
	      Entity entity = this.getOwner();
	      if (p_213868_1_.getEntity() instanceof LivingEntity && ((LivingEntity)p_213868_1_.getEntity()).getMobType() == MobType.ARTHROPOD) {
	    	  
	      } else {
		      if (entity instanceof LivingEntity) {
		         p_213868_1_.getEntity().hurt(DamageSource.indirectMobAttack(this, (LivingEntity)entity).setProjectile(), 1.0F);
		      }
	
		      if (!this.level.isClientSide) {
		    	 this.spawnTrap(p_213868_1_.getEntity().getX(), p_213868_1_.getEntity().getY(), p_213868_1_.getEntity().getZ());
		    	 
		         this.remove(RemovalReason.DISCARDED);
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

	   protected void onHitBlock(BlockHitResult p_230299_1_) {
	      super.onHitBlock(p_230299_1_);
	      if (!this.level.isClientSide) {
	    	 this.spawnTrap(this.getX(), this.getY(), this.getZ());
	    	 
	         this.remove(RemovalReason.DISCARDED);
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
			event.getController().setAnimation(new AnimationBuilder().addAnimation("web_projectile_idle", LOOP));
			return PlayState.CONTINUE;
		}

		@Override
		public AnimationFactory getFactory() {
			return factory;
		}

	   public Packet<?> getAddEntityPacket() {
	      return NetworkHooks.getEntitySpawningPacket(this);
	   }
	}
