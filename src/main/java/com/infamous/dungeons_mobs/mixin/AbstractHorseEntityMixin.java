package com.infamous.dungeons_mobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseEntityMixin extends Animal {

//    @Shadow @Nullable public abstract Entity getControllingPassenger();

    protected AbstractHorseEntityMixin(EntityType<? extends Animal> type, Level worldIn) {
        super(type, worldIn);
    }
    @Inject(at = @At("HEAD"), method = "travel", cancellable = true)
    private void travel(Vec3 travelVector, CallbackInfo callbackInfo){
        if (this.isAlive()) {
            if (this.isVehicle() && this.hasControllingPassenger() && this.isSaddled() && this.getControllingPassenger() instanceof Mob) {
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
