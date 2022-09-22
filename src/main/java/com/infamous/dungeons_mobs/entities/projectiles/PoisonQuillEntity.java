package com.infamous.dungeons_mobs.entities.projectiles;

import com.google.common.base.MoreObjects;
import com.infamous.dungeons_mobs.mod.ModDamageSources;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PoisonQuillEntity extends StraightMovingProjectileEntity implements IAnimatable {

	private static final DataParameter<Boolean> KELP = EntityDataManager.defineId(PoisonQuillEntity.class, DataSerializers.BOOLEAN);

	AnimationFactory factory = new AnimationFactory(this);
		   
	public PoisonQuillEntity(World worldIn) {
		super(ModEntityTypes.POISON_QUILL.get(), worldIn);
	}

	public PoisonQuillEntity(EntityType<? extends PoisonQuillEntity> p_i50147_1_, World p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);
	}

	public PoisonQuillEntity(World p_i1794_1_, LivingEntity p_i1794_2_, double p_i1794_3_, double p_i1794_5_,
			double p_i1794_7_) {
		super(ModEntityTypes.POISON_QUILL.get(), p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_, p_i1794_1_);
	}

	@OnlyIn(Dist.CLIENT)
	public PoisonQuillEntity(World p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_,
			double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
		super(ModEntityTypes.POISON_QUILL.get(), p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_,
				p_i1795_12_, p_i1795_1_);
	}
	
	@Override
	protected IParticleData getTrailParticle() {
		return null;
	}
	
	@Override
	public double getSpawnParticlesY() {
		return 0.2;
	}
	
	@Override
	public boolean slowedDownInWater() {
		return !this.isKelp();
	}
	
	@Override
	protected float getInertia() {
		return 1.0F;
	}
	
	@Override
	protected boolean isMovementNoisy() {
		return false;
	}	

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public boolean isOnFire() {
		return false;
	}

	protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
		super.onHitEntity(p_213868_1_);
	}
	
	public void onHitEntity(Entity entity) {
		if (!this.level.isClientSide) {
			this.playSound(ModSoundEvents.JUNGLE_ZOMBIE_STEP.get(), 0.75F, 1.0F + (this.random.nextFloat() * 0.5F));
			boolean flag;
				flag = entity.hurt(ModDamageSources.poisonQuill(this, MoreObjects.firstNonNull(this.getOwner(), this)), 5.0F);
	            if (entity instanceof LivingEntity) {
	                int i = 0;
	                if (this.level.getDifficulty() == Difficulty.NORMAL) {
	                    i = 8;
	                } else if (this.level.getDifficulty() == Difficulty.HARD) {
	                    i = 16;
	                }

	                if (i > 0) {
	                    ((LivingEntity)entity).addEffect(new EffectInstance(Effects.POISON, i * 20, 0));
	                }
	            }
				if (flag) {
					if (entity.isAlive() && this.getOwner() != null && this.getOwner() instanceof LivingEntity) {
						this.doEnchantDamageEffects((LivingEntity)this.getOwner(), entity);
					}
				}
			
			this.remove();
		}
	}
	
	   protected void defineSynchedData() {
		      this.entityData.define(KELP, false);
		   }

		   public boolean isKelp() {
		      return this.entityData.get(KELP);
		   }

		   public void setKelp(boolean p_82343_1_) {
		      this.entityData.set(KELP, p_82343_1_);
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
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public boolean getsStuckInBlocks() {
		return true;
	}
	
	@Override
	public SoundEvent getImpactSound() {
		return null;
	}
}
