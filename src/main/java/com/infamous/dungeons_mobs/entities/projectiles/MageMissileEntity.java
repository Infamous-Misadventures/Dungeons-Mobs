package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
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

public class MageMissileEntity extends StraightMovingProjectileEntity implements IAnimatable {

	AnimationFactory factory = new AnimationFactory(this);

	public MageMissileEntity(EntityType<? extends MageMissileEntity> p_i50147_1_, World p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);
	}

	public MageMissileEntity(World p_i1794_1_, LivingEntity p_i1794_2_, double p_i1794_3_, double p_i1794_5_,
			double p_i1794_7_) {
		super(ModEntityTypes.MAGE_MISSILE.get(), p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_, p_i1794_1_);
	}

	@OnlyIn(Dist.CLIENT)
	public MageMissileEntity(World p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_,
			double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
		super(ModEntityTypes.MAGE_MISSILE.get(), p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_,
				p_i1795_12_, p_i1795_1_);
	}
	
	@Override
	protected IParticleData getTrailParticle() {
		return ModParticleTypes.CORRUPTED_DUST.get();
	}
	
	@Override
	protected IParticleData getUnderWaterTrailParticle() {
		return ModParticleTypes.CORRUPTED_DUST.get();
	}
	
	@Override
	public double getSpawnParticlesY() {
		return 0.2;
	}
	
	@Override
	public boolean slowedDownInWater() {
		return false;
	}
	
	@Override
	protected float getInertia() {
		return 0.9F;
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
	
	@Override
	public void playImpactSound() {
		this.playSound(ModSoundEvents.NECROMANCER_ORB_IMPACT.get(), 0.75F, 2.0F);
	}
	
	@Override
	public void baseTick() {
		super.baseTick();
		if (this.lifeTime >= 10 && this.lifeTime <= 20 && this.getOwner() != null && this.getOwner() instanceof MobEntity && ((MobEntity)this.getOwner()).getTarget() != null) {
			MobEntity mob = ((MobEntity)this.getOwner());
			LivingEntity target = mob.getTarget();
	        double d1 = target.getX() - this.getX();
	        double d2 = (target.getY() - 2) - this.getY();
	        double d3 = target.getZ() - this.getZ();
	        double d0 = (double) MathHelper.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
	        if (d0 != 0.0D) {
	            this.xPower = d1 / d0 * 0.1D;
	            this.yPower = d2 / d0 * 0.1D;
	            this.zPower = d3 / d0 * 0.1D;
	        }
		}
	}
	
	public void onHitEntity(Entity entity) {
		if (!this.level.isClientSide) {
			super.onHitEntity(entity);
			boolean flag;
				flag = entity.hurt(DamageSource.indirectMagic(this, this.getOwner()), 2.5F);
	            if (entity instanceof LivingEntity) {
	                int i = 0;
	                if (this.level.getDifficulty() == Difficulty.NORMAL) {
	                    i = 8;
	                } else if (this.level.getDifficulty() == Difficulty.HARD) {
	                    i = 16;
	                }

	                if (i > 0) {
	                    ((LivingEntity)entity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, i * 20, 0));
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
		return false;
	}
	
	@Override
	public SoundEvent getImpactSound() {
		return null;
	}
}
