package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.interfaces.IHasItemStackData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends AbstractArrowEntity  implements IHasItemStackData {
    private static final DataParameter<ItemStack> DATA_ITEM_STACK = EntityDataManager.defineId(TridentEntity.class, DataSerializers.ITEM_STACK);

    protected TridentEntityMixin(EntityType<? extends AbstractArrowEntity> p_i48546_1_, World p_i48546_2_) {
        super(p_i48546_1_, p_i48546_2_);
    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V")
    private void constructWithItemStack(World p_i48790_1_, LivingEntity p_i48790_2_, ItemStack stack, CallbackInfo ci){
        this.setDataItem(stack.copy());
    }

    @Inject(at = @At("RETURN"), method = "getPickupItem", cancellable = true)
    private void pickupDataItem(CallbackInfoReturnable<ItemStack> cir){
        cir.setReturnValue(this.getDataItem());
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    private void defineItemStackData(CallbackInfo ci){
        this.entityData.define(DATA_ITEM_STACK, new ItemStack(Items.TRIDENT));
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void writeItemStackDataToTag(CompoundNBT tag, CallbackInfo ci){
        this.writeDataItem(tag, "Trident");
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void readItemStackDataFromTag(CompoundNBT tag, CallbackInfo ci){
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
}
