package com.infamous.dungeons_mobs.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntity {

    @Shadow @Nullable public abstract Entity getControllingPassenger();

    protected AbstractHorseEntityMixin(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }
    @Inject(at = @At("HEAD"), method = "travel", cancellable = true)
    private void travel(Vector3d travelVector, CallbackInfo callbackInfo){
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider() && this.isSaddled() && this.getControllingPassenger() instanceof MobEntity) {
                //DungeonsMobs.LOGGER.info("Forcing saddled horse to follow rider's AI!");
                this.flyingSpeed = 0.02F;
                super.travel(travelVector);
                callbackInfo.cancel();
            }
        }
    }

    @Shadow
    public abstract boolean isSaddled();
}
