package com.infamous.dungeons_mobs.entities.summonables;

import com.google.common.collect.Lists;
import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

import net.minecraft.world.entity.Entity.RemovalReason;

public class AreaDamageEntity extends Entity {

	private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(AreaDamageEntity.class,
			EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> SIZE_TO_REACH = SynchedEntityData.defineId(AreaDamageEntity.class,
			EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> GROW_SPEED = SynchedEntityData.defineId(AreaDamageEntity.class,
			EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> Y_SIZE = SynchedEntityData.defineId(AreaDamageEntity.class,
			EntityDataSerializers.FLOAT);
	
	private static final EntityDataAccessor<Integer> PARTICLE_TYPE = SynchedEntityData.defineId(AreaDamageEntity.class,
			EntityDataSerializers.INT);
	
	private static final EntityDataAccessor<Integer> EXTRA_TIME = SynchedEntityData.defineId(AreaDamageEntity.class,
			EntityDataSerializers.INT);
	
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
	
	public int extraTimeTick;
	
	public List<AreaDamageEntity> connectedAreaDamages = Lists.newArrayList();

    public AreaDamageEntity(Level world){
        super(ModEntityTypes.AREA_DAMAGE.get(), world);
    }
    
    public AreaDamageEntity(EntityType<? extends AreaDamageEntity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }
    
    @Override
    public EntityDimensions getDimensions(Pose p_213305_1_) {
    	return EntityDimensions.scalable(this.getSize(), this.getYSize());
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
	            int i = Mth.floor(this.getX());
	            int j = Mth.floor(this.getY() - (double)0.2F);
	            int k = Mth.floor(this.getZ());
	            BlockPos pos = new BlockPos(i, j, k);
	            BlockState blockstate = this.level.getBlockState(pos);
	            if (!blockstate.isAir()) {
	               this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX(), this.getY() + 0.1D, this.getZ(), 0, 0, 0);
	            }
			}
			
			for(int i = 0; i < 60; ++i) {
				Vec3 vector3d = this.position();
	            double d0 = this.random.nextGaussian() * 15.0D;
	            double d1 = this.random.nextFloat() * 1.75D;
	            double d2 = this.random.nextGaussian() * 15.0D;
	            this.level.addParticle(ModParticleTypes.DUST.get(), vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
	         }
		} else if (p_70103_1_ == 2) {
			for (int particleAmount = 0; particleAmount < 25; particleAmount++) {
	            int i = Mth.floor(this.getX());
	            int j = Mth.floor(this.getY() - (double)0.2F);
	            int k = Mth.floor(this.getZ());
	            BlockPos pos = new BlockPos(i, j, k);
	            BlockState blockstate = this.level.getBlockState(pos);
	            if (!blockstate.isAir()) {
	               this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX(), this.getY() + 0.1D, this.getZ(), 0, 0, 0);
	            }
			}
			
			for(int i = 0; i < 50; ++i) {
				Vec3 vector3d = this.position();
	            double d0 = this.random.nextGaussian() * 0.5D;
	            double d1 = this.random.nextFloat() * 2.0D;
	            double d2 = this.random.nextGaussian() * 0.5D;
	            this.level.addParticle(ParticleTypes.BUBBLE, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
	         }
		} else {
			super.handleEntityEvent(p_70103_1_);
		}
	}
	
	public static AreaDamageEntity spawnAreaDamage(Level level, Vec3 pos, LivingEntity owner, float damage, DamageSource damageSource, float size, float sizeToReach, float growSpeed, float ySize, int extraTime, boolean constantDamage, boolean friendlyFire, double knockbackAmount, double knockbackAmountY, boolean disableShields, int disableShieldTime, int particleVariant) {
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
			areaDamage.setExtraTime(extraTime);
		return areaDamage;
	}

    @Override
    public void baseTick() {
    	super.baseTick();
    	
    	this.refreshDimensions();   	
    	
    	List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), Entity::isAlive);
		if (!list.isEmpty()) {
			for (Entity entity : list) {
				if (!this.level.isClientSide && this.canEntityBeDamaged(entity)) {
					entity.hurt(this.damageSource, damage);
					if (this.distanceTo(entity) >= 0.5) {
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
    	
    	if (this.getSize() >= this.getSizeToReach()) {
    		this.extraTimeTick ++;
    	}
    	
    	if (!this.level.isClientSide && ((this.getExtraTime() > 0 && this.extraTimeTick >= this.getExtraTime()) || (this.getExtraTime() <= 0 && this.getSize() >= this.getSizeToReach()))) {
    		this.remove(RemovalReason.DISCARDED);
    	}   	
    }
    
	public void disableShield(LivingEntity livingEntity, int ticks) {
		if (livingEntity instanceof Player && livingEntity.isBlocking()) {
			((Player) livingEntity).getCooldowns()
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
		this.entityData.define(EXTRA_TIME, 0);
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
	
	public int getExtraTime() {
		return this.entityData.get(EXTRA_TIME);
	}

	public void setExtraTime(int attached) {
		this.entityData.set(EXTRA_TIME, attached);
	}
	

	@Override
	protected void readAdditionalSaveData(CompoundTag p_70037_1_) {
		this.setSize(p_70037_1_.getFloat("Size"));
		this.setSizeToReach(p_70037_1_.getFloat("SizeToReach"));
		this.setYSize(p_70037_1_.getFloat("YSize"));
		this.setGrowSpeed(p_70037_1_.getFloat("GrowSpeed"));
		this.setParticleType(p_70037_1_.getInt("ParticleType"));
		this.setExtraTime(p_70037_1_.getInt("ExtraTime"));
		this.damage = p_70037_1_.getFloat("Damage");
		this.constantDamage = p_70037_1_.getBoolean("ConstantDamage");
		this.knockbackAmount = p_70037_1_.getDouble("KnockbackAmount");
		this.knockbackAmountY = p_70037_1_.getDouble("KnockbackAmountY");
		this.disableShieldTime = p_70037_1_.getInt("DisableShieldTime");
		this.disableShields = p_70037_1_.getBoolean("DisableShields");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_213281_1_) {
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
		p_213281_1_.putInt("ExtraTime", this.getExtraTime());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
    
}
