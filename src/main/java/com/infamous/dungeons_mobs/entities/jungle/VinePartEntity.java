package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.tags.EntityTags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.entity.PartEntity;

public class VinePartEntity extends PartEntity<AbstractVineEntity> {
	public final AbstractVineEntity parentMob;

	public int segmentNumber;

	public VinePartEntity(AbstractVineEntity p_i50232_1_, int segmentNumber) {
		super(p_i50232_1_);
		this.parentMob = p_i50232_1_;
		this.segmentNumber = segmentNumber;
		this.refreshDimensions();
	}

	protected void defineSynchedData() {
	}

	protected void readAdditionalSaveData(CompoundTag p_70037_1_) {
	}

	protected void addAdditionalSaveData(CompoundTag p_213281_1_) {
	}

	public boolean isPickable() {
		return parentMob.isOut();
	}

	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		if (p_70097_1_.getEntity() != null && p_70097_1_.getEntity().getType().is(EntityTags.PLANT_MOBS)) {
			return false;
		} else {
			return this.isInvulnerableTo(p_70097_1_) ? false : this.parentMob.hurt(p_70097_1_, p_70097_2_);
		}
	}

	public void refreshDimensions() {
		double d0 = this.getX();
		double d1 = this.getY();
		double d2 = this.getZ();
		super.refreshDimensions();
		this.setPos(d0, d1, d2);
	}

	public boolean isPoisonQuill() {
		return parentMob instanceof PoisonQuillVineEntity;
	}

	@Override
	public boolean canBeCollidedWith() {
		return parentMob.isOut();
	}

	public EntityDimensions getSizeForSegment() {
		EntityDimensions size = EntityDimensions.scalable(0, 0);

		if (this.segmentNumber < 27 - parentMob.getLengthInSegments()) {
			return size;
		}

		if (this.segmentNumber == 26) {
			size = EntityDimensions.scalable(0.25F, this.isPoisonQuill() ? 1.625F : 1.375F);
		} else if (this.segmentNumber == 25) {
			size = EntityDimensions.scalable(0.5F, this.isPoisonQuill() ? 2.0F : 1.375F);
		} else if (this.segmentNumber >= 23 && this.segmentNumber <= 24) {
			size = EntityDimensions.scalable(0.75F, this.isPoisonQuill() && this.segmentNumber == 24 ? 2.0F : 1.375F);
		} else if (this.segmentNumber >= 20 && this.segmentNumber <= 22) {
			size = EntityDimensions.scalable(1.0F, 1.375F);
		} else if (this.segmentNumber >= 16 && this.segmentNumber <= 19) {
			size = EntityDimensions.scalable(1.25F, 1.375F);
		} else if (this.segmentNumber >= 11 && this.segmentNumber <= 15) {
			size = EntityDimensions.scalable(1.5F, 1.375F);
		} else if (this.segmentNumber >= 5 && this.segmentNumber <= 10) {
			size = EntityDimensions.scalable(1.75F, 1.375F);
		} else if (this.segmentNumber >= 1 && this.segmentNumber <= 4) {
			size = EntityDimensions.scalable(2.0F, 1.375F);
		}
		return size;
	}

	public float getYOffsetForSegment() {
		float extraHeight = this.isPoisonQuill() && this.segmentNumber > 24 ? (0.625F * (-24 + this.segmentNumber))
				: 0.0F;
		return ((1.375F * (this.segmentNumber - 1)) - (1.375F * 26) + this.parentMob.getLengthInBlocks()) + extraHeight;
	}

	public EntityDimensions getDimensions(Pose p_213305_1_) {
		return this.getSizeForSegment();
	}

	public Packet<?> getAddEntityPacket() {
		throw new UnsupportedOperationException();
	}
}