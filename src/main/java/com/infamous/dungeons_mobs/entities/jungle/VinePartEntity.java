package com.infamous.dungeons_mobs.entities.jungle;

import com.infamous.dungeons_mobs.tags.CustomTags;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
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

	protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
	}

	protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
	}

	public boolean isPickable() {
		return parentMob.isOut();
	}

	public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
		if (p_70097_1_.getEntity() != null && p_70097_1_.getEntity().getType().is(CustomTags.PLANT_MOBS)) {
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

	@Override
	public boolean canBeCollidedWith() {
		return parentMob.isOut();
	}

	public EntitySize getSizeForSegment() {
		EntitySize size = EntitySize.scalable(0, 0);

		if(this.segmentNumber < 27 - parentMob.getLengthInSegments()) {
			return size;
		}

		if (this.segmentNumber == 26) {
			size = EntitySize.scalable(0.25F, 1.375F);
		} else if (this.segmentNumber == 25) {
			size = EntitySize.scalable(0.5F, 1.375F);
		} else if (this.segmentNumber >= 23 && this.segmentNumber <= 24) {
			size = EntitySize.scalable(0.75F, 1.375F);
		} else if (this.segmentNumber >= 20 && this.segmentNumber <= 22) {
			size = EntitySize.scalable(1.0F, 1.375F);
		} else if (this.segmentNumber >= 16 && this.segmentNumber <= 19) {
			size = EntitySize.scalable(1.25F, 1.375F);
		} else if (this.segmentNumber >= 11 && this.segmentNumber <= 15) {
			size = EntitySize.scalable(1.5F, 1.375F);
		} else if (this.segmentNumber >= 5 && this.segmentNumber <= 10) {
			size = EntitySize.scalable(1.75F, 1.375F);
		} else if (this.segmentNumber >= 1 && this.segmentNumber <= 4) {
			size = EntitySize.scalable(2.0F, 1.375F);
		}
		return size;
	}

	public float getYOffsetForSegment() {
		return (1.375F * (this.segmentNumber - 1)) - (1.375F * 26) + this.parentMob.getLengthInBlocks();
	}

	public EntitySize getDimensions(Pose p_213305_1_) {
		return this.getSizeForSegment();
	}

	public IPacket<?> getAddEntityPacket() {
		throw new UnsupportedOperationException();
	}
}