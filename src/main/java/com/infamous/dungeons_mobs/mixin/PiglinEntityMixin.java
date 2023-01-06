package com.infamous.dungeons_mobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.infamous.dungeons_mobs.interfaces.ISmartCrossbowUser;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(Piglin.class)
public abstract class PiglinEntityMixin extends AbstractPiglin implements ISmartCrossbowUser {

    @Shadow protected abstract boolean isChargingCrossbow();

    private static final EntityDataAccessor<Boolean> DATA_IS_CROSSBOW_USER = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);

    public PiglinEntityMixin(EntityType<? extends AbstractPiglin> p_i241915_1_, Level p_i241915_2_) {
        super(p_i241915_1_, p_i241915_2_);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void readAdditional(CompoundTag compoundNBT, CallbackInfo ci){
        this.readCrossbowUserNBT(compoundNBT);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void writeAdditional(CompoundTag compoundNBT, CallbackInfo ci){
        this.writeCrossbowUserNBT(compoundNBT);
    }


    @Inject(at = @At("TAIL"), method = "finalizeSpawn")
    private void finalizeSpawn(ServerLevelAccessor p_213386_1_, DifficultyInstance p_213386_2_, MobSpawnType p_213386_3_, SpawnGroupData p_213386_4_, CompoundTag p_213386_5_, CallbackInfoReturnable<SpawnGroupData> cir){
        this.setCrossbowUser(this.isHolding(itemStack -> itemStack.getItem() instanceof CrossbowItem));
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
