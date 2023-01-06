package com.infamous.dungeons_mobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.infamous.dungeons_mobs.interfaces.IHasItemStackData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@Mixin(ThrownTrident.class)
public abstract class TridentEntityMixin extends AbstractArrow implements IHasItemStackData {
	private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ThrownTrident.class,
			EntityDataSerializers.ITEM_STACK);

	protected TridentEntityMixin(EntityType<? extends AbstractArrow> p_i48546_1_, Level p_i48546_2_) {
		super(p_i48546_1_, p_i48546_2_);
	}

	@Inject(at = @At("RETURN"), method = "Lnet/minecraft/world/entity/projectile/ThrownTrident;<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V")
	private void constructWithItemStack(Level p_i48790_1_, LivingEntity p_i48790_2_, ItemStack stack, CallbackInfo ci) {
		this.setDataItem(stack.copy());
	}

	@Inject(at = @At("RETURN"), method = "getPickupItem", cancellable = true)
	private void pickupDataItem(CallbackInfoReturnable<ItemStack> cir) {
		cir.setReturnValue(this.getDataItem());
	}

	@Inject(at = @At("TAIL"), method = "defineSynchedData")
	private void defineItemStackData(CallbackInfo ci) {
		this.entityData.define(DATA_ITEM_STACK, new ItemStack(Items.TRIDENT));
	}

	@Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
	private void writeItemStackDataToTag(CompoundTag tag, CallbackInfo ci) {
		this.writeDataItem(tag, "Trident");
	}

	@Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
	private void readItemStackDataFromTag(CompoundTag tag, CallbackInfo ci) {
		this.readDataItem(tag, "Trident");
	}

	@Override
	public ItemStack getDataItem() {
		return this.entityData.get(DATA_ITEM_STACK);
	}

	@Override
	public void setDataItem(ItemStack dataItem) {
		this.entityData.set(DATA_ITEM_STACK, dataItem);
	}

	@Accessor
	public abstract ItemStack getTridentItem();
}
