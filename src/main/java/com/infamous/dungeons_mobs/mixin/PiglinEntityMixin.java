package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.interfaces.ISmartCrossbowUser;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityMixin extends AbstractPiglinEntity implements ISmartCrossbowUser {

    @Shadow protected abstract boolean isChargingCrossbow();

    private static final DataParameter<Boolean> DATA_IS_CROSSBOW_USER = EntityDataManager.defineId(PiglinEntity.class, DataSerializers.BOOLEAN);

    public PiglinEntityMixin(EntityType<? extends AbstractPiglinEntity> p_i241915_1_, World p_i241915_2_) {
        super(p_i241915_1_, p_i241915_2_);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void readAdditional(CompoundNBT compoundNBT, CallbackInfo ci){
        this.readCrossbowUserNBT(compoundNBT);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void writeAdditional(CompoundNBT compoundNBT, CallbackInfo ci){
        this.writeCrossbowUserNBT(compoundNBT);
    }


    @Inject(at = @At("TAIL"), method = "finalizeSpawn")
    private void finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, ILivingEntityData p_213386_4_, CompoundNBT p_213386_5_, CallbackInfoReturnable<ILivingEntityData> cir){
        this.setCrossbowUser(this.isHolding(item -> item instanceof CrossbowItem));
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    private void registerCustomData(CallbackInfo ci){
        this.entityData.define(DATA_IS_CROSSBOW_USER, false);
    }

    @Override
    public boolean isCrossbowUser() {
        return this.entityData.get(DATA_IS_CROSSBOW_USER);
    }

    @Override
    public void setCrossbowUser(boolean crossbowUser) {
        this.entityData.set(DATA_IS_CROSSBOW_USER, crossbowUser);
    }

    @Override
    public boolean _isChargingCrossbow() {
        return this.isChargingCrossbow();
    }
}
