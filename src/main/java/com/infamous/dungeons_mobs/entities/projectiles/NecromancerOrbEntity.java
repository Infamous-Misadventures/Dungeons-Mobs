package com.infamous.dungeons_mobs.entities.projectiles;

import java.util.List;

import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
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

public class NecromancerOrbEntity extends StraightMovingProjectileEntity implements IAnimatable {

	private static final DataParameter<Boolean> DELAYED_FORM = EntityDataManager.defineId(NecromancerOrbEntity.class,
			DataSerializers.BOOLEAN);
	
	public int formAnimationTick;
	public int formAnimationLength = 20;

	public int vanishAnimationTick;
	public int vanishAnimationLength = 40;

	public int lifeTime;
	
	public int textureChange = 0;

	AnimationFactory factory = new AnimationFactory(this);

	public NecromancerOrbEntity(World worldIn) {
		super(ModEntityTypes.NECROMANCER_ORB.get(), worldIn);
	}

	public NecromancerOrbEntity(EntityType<? extends NecromancerOrbEntity> p_i50147_1_, World p_i50147_2_) {
		super(p_i50147_1_, p_i50147_2_);
	}

	public NecromancerOrbEntity(World p_i1794_1_, LivingEntity p_i1794_2_, double p_i1794_3_, double p_i1794_5_,
			double p_i1794_7_) {
		super(ModEntityTypes.NECROMANCER_ORB.get(), p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_, p_i1794_1_);
	}

	@OnlyIn(Dist.CLIENT)
	public NecromancerOrbEntity(World p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_,
			double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
		super(ModEntityTypes.NECROMANCER_ORB.get(), p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_,
				p_i1795_12_, p_i1795_1_);
	}
	
	@Override
	protected boolean canHitEntity(Entity p_230298_1_) {
		return !(p_230298_1_ instanceof ProjectileEntity);
	}
	
	public void handleEntityEvent(byte p_28844_) {
		if (p_28844_ == 1) {
			this.vanishAnimationTick = vanishAnimationLength;
		} else if (p_28844_ == 2) {
			this.formAnimationTick = formAnimationLength;
		} else {
			super.handleEntityEvent(p_28844_);
		}
	}
	
	@Override
	protected IParticleData getTrailParticle() {
		return ModParticleTypes.NECROMANCY.get();
	}
	
	@Override
	public double getSpawnParticlesY() {
		return 0.2;
	}
	
	@Override
	public boolean shouldSpawnParticles() {
		return this.vanishAnimationTick <= 0;
	}
	
	@Override
	protected float getInertia() {
		return 0.8F;
	}

	public void startForming() {
		if (!this.level.isClientSide) {
			this.formAnimationTick = this.formAnimationLength;
			this.level.broadcastEntityEvent(this, (byte) 2);
		}
	}
	
	@Override
	protected boolean isMovementNoisy() {
		return false;
	}
	
	public void rotateToMatchMovement() {
		this.updateRotation();
	}

	@Override
	public void baseTick() {
		super.baseTick();
		this.tickDownAnimTimers();
		
    	List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), null);
		if (!list.isEmpty() && !this.level.isClientSide && this.vanishAnimationTick <= 0) {
			for (Entity entity : list) {
				if (this.canHitEntity(entity)) {
					this.onHitEntity(entity);
				}
			}
		}

		this.lifeTime++;

		this.updateRotation();
		
		if (this.tickCount % 5 == 0) {
			textureChange ++;
		}
		
		if (!this.level.isClientSide && this.lifeTime > 200 && this.vanishAnimationTick <= 0) {
			this.vanishAnimationTick = this.vanishAnimationLength;
			this.level.broadcastEntityEvent(this, (byte) 1);
		}
		
		if (!this.level.isClientSide && this.hasDelayedForm()) {
			this.startForming();
			this.setDelayedForm(false);
		}
		
		if (!this.level.isClientSide && this.vanishAnimationTick > 0) {
			this.setDeltaMovement(0, 0, 0);
		}

		if (!this.level.isClientSide && this.vanishAnimationTick == 2) {
			this.remove();
		}
	}

	public void tickDownAnimTimers() {
		if (this.formAnimationTick > 0) {
			this.formAnimationTick--;
		}

		if (this.vanishAnimationTick > 0) {
			this.vanishAnimationTick--;
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (this.vanishAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_orb_vanish", true));
		} else if (this.formAnimationTick > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_orb_form", true));
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("necromancer_orb_idle", true));
		}
		return PlayState.CONTINUE;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DELAYED_FORM, false);
	}
	
	public boolean hasDelayedForm() {
		return this.entityData.get(DELAYED_FORM);
	}

	public void setDelayedForm(boolean attached) {
		this.entityData.set(DELAYED_FORM, attached);
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
		if (entity instanceof MobEntity && ((MobEntity)entity).getMobType() == CreatureAttribute.UNDEAD) {
			
		} else if (!this.level.isClientSide) {
			Entity entity1 = this.getOwner();
			boolean flag;
			if (entity1 instanceof LivingEntity) {
				LivingEntity livingentity = (LivingEntity) entity1;
				flag = entity.hurt(DamageSource.indirectMagic(this, livingentity), 6.0F);
				if (flag) {
					if (entity.isAlive()) {
						this.doEnchantDamageEffects(livingentity, entity);
					}
				}
			} else {
				flag = entity.hurt(DamageSource.MAGIC, 6.0F);
			}
			entity.setDeltaMovement(entity.getDeltaMovement().add(this.getDeltaMovement().scale(2)));
			
			this.playSound(ModSoundEvents.NECROMANCER_ORB_IMPACT.get(), 1.0F, 1.0F);
			this.vanishAnimationTick = this.vanishAnimationLength;
			this.level.broadcastEntityEvent(this, (byte) 1);
		}
	}
	
	@Override
	protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
		super.onHitBlock(p_230299_1_);	
		if (!this.level.isClientSide) {
			this.playSound(ModSoundEvents.NECROMANCER_ORB_IMPACT.get(), 1.0F, 1.0F);
			this.vanishAnimationTick = this.vanishAnimationLength;
			this.level.broadcastEntityEvent(this, (byte) 1);
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
}
