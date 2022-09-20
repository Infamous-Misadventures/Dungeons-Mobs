package com.infamous.dungeons_mobs.entities.summonables;

import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class AreaDamageEntity extends Entity {

	private static final DataParameter<Float> SIZE = EntityDataManager.defineId(AreaDamageEntity.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> SIZE_TO_REACH = EntityDataManager.defineId(AreaDamageEntity.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> GROW_SPEED = EntityDataManager.defineId(AreaDamageEntity.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Float> Y_SIZE = EntityDataManager.defineId(AreaDamageEntity.class,
			DataSerializers.FLOAT);
	
	private static final DataParameter<Integer> PARTICLE_TYPE = EntityDataManager.defineId(AreaDamageEntity.class,
			DataSerializers.INT);
	
	public float damage;
	public DamageSource damageSource = DamageSource.GENERIC;
	public LivingEntity owner;
	
	public boolean constantDamage;
	public List<Entity> damagedEntities = Lists.newArrayList();
	
	public boolean friendlyFire;
	
	public double knockbackAmount;
	public double knockbackAmountY;
	
	public boolean disableShields;
	public int disableShieldTime;
	
	public List<AreaDamageEntity> connectedAreaDamages = Lists.newArrayList();

    public AreaDamageEntity(World world){
        super(ModEntityTypes.AREA_DAMAGE.get(), world);
    }
    
    public AreaDamageEntity(EntityType<? extends AreaDamageEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }
    
    @Override
    public EntitySize getDimensions(Pose p_213305_1_) {
    	return EntitySize.scalable(this.getSize(), this.getYSize());
    }
    
	public void refreshDimensions() {
		double d0 = this.getX();
		double d1 = this.getY();
		double d2 = this.getZ();
		super.refreshDimensions();
		this.setPos(d0, d1, d2);
	}
	
	public boolean canEntityBeDamaged(Entity entity) {		
		if (entity == null) {
			return false;
		} else if (this.owner != null && entity == this.owner) {
			return false;
		} else if (this.owner != null && this.owner.isAlliedTo(entity) && !this.friendlyFire) {
			return false;
		} else if (!this.damagedEntities.isEmpty() && !this.constantDamage && this.damagedEntities.contains(entity)) {
			return false;
		} else if (!this.connectedAreaDamages.isEmpty()) {
			boolean canConnectedAreaDamagesHarm = true;			
			for (AreaDamageEntity areaDamage : this.connectedAreaDamages) {
					if (!areaDamage.damagedEntities.isEmpty() && !areaDamage.constantDamage && areaDamage.damagedEntities.contains(entity)) {
						canConnectedAreaDamagesHarm = false;
					}
			}		
			return canConnectedAreaDamagesHarm;
		} else {
			return true;			
		}
	}
	
	@Override
	public void handleEntityEvent(byte p_70103_1_) {
		if (p_70103_1_ == 1) {
			for (int particleAmount = 0; particleAmount < 25; particleAmount++) {
	            int i = MathHelper.floor(this.getX());
	            int j = MathHelper.floor(this.getY() - (double)0.2F);
	            int k = MathHelper.floor(this.getZ());
	            BlockPos pos = new BlockPos(i, j, k);
	            BlockState blockstate = this.level.getBlockState(pos);
	            if (!blockstate.isAir(this.level, pos)) {
	               this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX(), this.getY() + 0.1D, this.getZ(), 0, 0, 0);
	            }
			}
			
			for(int i = 0; i < 60; ++i) {
				Vector3d vector3d = this.position();
	            double d0 = this.random.nextGaussian() * 15.0D;
	            double d1 = this.random.nextFloat() * 1.75D;
	            double d2 = this.random.nextGaussian() * 15.0D;
	            this.level.addParticle(ModParticleTypes.DUST.get(), vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
	         }
		} else if (p_70103_1_ == 2) {
			for (int particleAmount = 0; particleAmount < 25; particleAmount++) {
	            int i = MathHelper.floor(this.getX());
	            int j = MathHelper.floor(this.getY() - (double)0.2F);
	            int k = MathHelper.floor(this.getZ());
	            BlockPos pos = new BlockPos(i, j, k);
	            BlockState blockstate = this.level.getBlockState(pos);
	            if (!blockstate.isAir(this.level, pos)) {
	               this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX(), this.getY() + 0.1D, this.getZ(), 0, 0, 0);
	            }
			}
			
			for(int i = 0; i < 50; ++i) {
				Vector3d vector3d = this.position();
	            double d0 = this.random.nextGaussian() * 0.5D;
	            double d1 = this.random.nextFloat() * 2.0D;
	            double d2 = this.random.nextGaussian() * 0.5D;
	            this.level.addParticle(ParticleTypes.BUBBLE, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
	         }
		} else {
			super.handleEntityEvent(p_70103_1_);
		}
	}
	
	public static AreaDamageEntity spawnAreaDamage(World level, Vector3d pos, LivingEntity owner, float damage, DamageSource damageSource, float size, float sizeToReach, float growSpeed, float ySize, boolean constantDamage, boolean friendlyFire, double knockbackAmount, double knockbackAmountY, boolean disableShields, int disableShieldTime, int particleVariant) {
		AreaDamageEntity areaDamage = ModEntityTypes.AREA_DAMAGE.get().create(level);
			areaDamage.moveTo(pos.x, pos.y, pos.z);
			areaDamage.owner = owner;
			areaDamage.damage = damage;
			areaDamage.damageSource = damageSource;
			areaDamage.setSize(size);
			areaDamage.setSizeToReach(sizeToReach);
			areaDamage.setGrowSpeed(growSpeed);
			areaDamage.setYSize(ySize);
			areaDamage.constantDamage = constantDamage;
			areaDamage.friendlyFire = friendlyFire;
			areaDamage.knockbackAmount = knockbackAmount;
			areaDamage.knockbackAmountY = knockbackAmountY;
			areaDamage.disableShields = disableShields;
			areaDamage.disableShieldTime = disableShieldTime;
			areaDamage.setParticleType(particleVariant);
		return areaDamage;
	}

    @Override
    public void baseTick() {
    	super.baseTick();
    	
    	this.refreshDimensions();   	
    	
    	List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), null);
		if (!list.isEmpty()) {
			for (Entity entity : list) {
				if (!this.level.isClientSide && this.canEntityBeDamaged(entity)) {
					entity.hurt(this.damageSource, damage);
					if (this.distanceTo(entity) >= 0.25) {
						double d0 = entity.getX() - this.getX();
					    double d1 = entity.getZ() - this.getZ();
					    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
					    entity.push(d0 / d2 * this.knockbackAmount, this.knockbackAmountY, d1 / d2 * this.knockbackAmount);
					}
					if (entity instanceof LivingEntity && this.disableShields) {
						this.disableShield(((LivingEntity)entity), disableShieldTime);
					}
					this.damagedEntities.add(entity);
				}
			}
		}
		
		if (this.getParticleType() > 0) {
			this.level.broadcastEntityEvent(this, (byte) this.getParticleType());
			this.setParticleType(0);
		}
		
    	if (this.getSize() < this.getSizeToReach()) {
    		this.setSize(this.getSize() + this.getGrowSpeed());
    	}
    	
    	if (!this.level.isClientSide && this.getSize() >= this.getSizeToReach()) {
    		this.remove();
    	}   	
    }
    
	public void disableShield(LivingEntity livingEntity, int ticks) {
		if (livingEntity instanceof PlayerEntity && livingEntity.isBlocking()) {
			((PlayerEntity) livingEntity).getCooldowns()
					.addCooldown(livingEntity.getItemInHand(livingEntity.getUsedItemHand()).getItem(), ticks);
			livingEntity.stopUsingItem();
			livingEntity.level.broadcastEntityEvent(livingEntity, (byte) 30);
		}
	}
    
	@Override
	protected void defineSynchedData() {
		this.entityData.define(SIZE, 0.0F);
		this.entityData.define(SIZE_TO_REACH, 0.0F);
		this.entityData.define(Y_SIZE, 0.0F);
		this.entityData.define(GROW_SPEED, 0.0F);
		this.entityData.define(PARTICLE_TYPE, 0);
	}
	
	public float getSize() {
		return this.entityData.get(SIZE);
	}

	public void setSize(float attached) {
		this.entityData.set(SIZE, attached);
	}
	
	public float getSizeToReach() {
		return this.entityData.get(SIZE_TO_REACH);
	}

	public void setSizeToReach(float attached) {
		this.entityData.set(SIZE_TO_REACH, attached);
	}
	
	public float getYSize() {
		return this.entityData.get(Y_SIZE);
	}

	public void setYSize(float attached) {
		this.entityData.set(Y_SIZE, attached);
	}
	
	public float getGrowSpeed() {
		return this.entityData.get(GROW_SPEED);
	}

	public void setGrowSpeed(float attached) {
		this.entityData.set(GROW_SPEED, attached);
	}
	
	public int getParticleType() {
		return this.entityData.get(PARTICLE_TYPE);
	}

	public void setParticleType(int attached) {
		this.entityData.set(PARTICLE_TYPE, attached);
	}
	

	@Override
	protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
		this.setSize(p_70037_1_.getFloat("Size"));
		this.setSizeToReach(p_70037_1_.getFloat("SizeToReach"));
		this.setYSize(p_70037_1_.getFloat("YSize"));
		this.setGrowSpeed(p_70037_1_.getFloat("GrowSpeed"));
		this.setParticleType(p_70037_1_.getInt("ParticleType"));
		this.damage = p_70037_1_.getFloat("Damage");
		this.constantDamage = p_70037_1_.getBoolean("ConstantDamage");
		this.knockbackAmount = p_70037_1_.getDouble("KnockbackAmount");
		this.knockbackAmountY = p_70037_1_.getDouble("KnockbackAmountY");
		this.disableShieldTime = p_70037_1_.getInt("DisableShieldTime");
		this.disableShields = p_70037_1_.getBoolean("DisableShields");
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
		p_213281_1_.putFloat("Size", this.getSize());
		p_213281_1_.putFloat("SizeToReach", this.getSizeToReach());
		p_213281_1_.putFloat("YSize", this.getYSize());
		p_213281_1_.putFloat("GrowSpeed", this.getGrowSpeed());
		p_213281_1_.putInt("ParticleType", this.getParticleType());
		p_213281_1_.putFloat("Damage", this.damage);
		p_213281_1_.putBoolean("ConstantDamage", this.constantDamage);
		p_213281_1_.putDouble("KnockbackAmount", this.knockbackAmount);
		p_213281_1_.putDouble("KnockbackAmountY", this.knockbackAmountY);
		p_213281_1_.putInt("DisableShieldTime", this.disableShieldTime);
		p_213281_1_.putBoolean("DisableShields", this.disableShields);
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
    
}
