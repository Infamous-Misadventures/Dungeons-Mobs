package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;

import net.minecraft.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class SlimeballEntity extends AbstractHurtingProjectile implements ItemSupplier {
	private static final EntityDataAccessor<ItemStack> STACK = SynchedEntityData.defineId(SlimeballEntity.class,
			EntityDataSerializers.ITEM_STACK);

	public SlimeballEntity(Level worldIn) {
		super(ModEntityTypes.SLIMEBALL.get(), worldIn);
	}

	public SlimeballEntity(EntityType<? extends SlimeballEntity> entityType, Level world) {
		super(entityType, world);
	}

	public SlimeballEntity(Level world, double x, double y, double z, double accelX, double accelY, double accelZ) {
		super(ModEntityTypes.SLIMEBALL.get(), x, y, z, accelX, accelY, accelZ, world);
	}

	public SlimeballEntity(Level world, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(ModEntityTypes.SLIMEBALL.get(), shooter, accelX, accelY, accelZ, world);
	}

	public void setStack(ItemStack stack) {
		if (stack.getItem() != Items.SLIME_BALL || stack.hasTag()) {
			this.getEntityData().set(STACK, Util.make(stack.copy(), (itemStack) -> {
				itemStack.setCount(1);
			}));
		}
	}

	protected ItemStack getStack() {
		return this.getEntityData().get(STACK);
	}

	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		ItemStack itemstack = this.getStack();
		return itemstack.isEmpty() ? new ItemStack(Items.SLIME_BALL) : itemstack;
	}

	protected void defineSynchedData() {
		this.getEntityData().define(STACK, ItemStack.EMPTY);
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		ItemStack itemstack = this.getStack();
		if (!itemstack.isEmpty()) {
			compound.put("Item", itemstack.save(new CompoundTag()));
		}

	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		ItemStack itemstack = ItemStack.of(compound.getCompound("Item"));
		this.setStack(itemstack);
	}

	protected boolean shouldBurn() {
		return false;
	}

	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.ITEM_SLIME;
	}

	@Override
	protected void onHitEntity(EntityHitResult rayTraceResult) {
		super.onHitEntity(rayTraceResult);
		Entity entity = rayTraceResult.getEntity();
		int attackDamage = 3;
		if (!(entity instanceof Slime)) {
			entity.hurt(DamageSource.thrown(this, this.getOwner()), (float) attackDamage);
		}
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected void onHit(HitResult result) {
		super.onHit(result);
		if (result instanceof EntityHitResult) {
			EntityHitResult entityRayTraceResult = (EntityHitResult) result;
			if (!(entityRayTraceResult.getEntity() instanceof Slime)) {
				if (!this.level.isClientSide) {
					this.remove(RemovalReason.DISCARDED);
				}
			}
		} else {
			removeIfWorldNotRemote();
		}
	}

	private void removeIfWorldNotRemote() {
		if (!this.level.isClientSide) {
			this.remove(RemovalReason.DISCARDED);
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this
	 * Entity.
	 */
	public boolean isPickable() {
		return false;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}