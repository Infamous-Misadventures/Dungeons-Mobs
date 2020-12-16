package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntity {

    @Shadow protected abstract boolean getHorseWatchableBoolean(int i);

    @Shadow @Nullable public abstract Entity getControllingPassenger();

    protected AbstractHorseEntityMixin(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }
    @Inject(at = @At("HEAD"), method = "travel", cancellable = true)
    private void travel(Vector3d travelVector, CallbackInfo callbackInfo){
        if (this.isAlive()) {
            if (this.isBeingRidden() && this.canBeSteered() && this.isHorseSaddled() && this.getControllingPassenger() instanceof MobEntity) {
                //DungeonsMobs.LOGGER.info("Forcing saddled horse to follow rider's AI!");
                this.jumpMovementFactor = 0.02F;
                super.travel(travelVector);
                callbackInfo.cancel();
            }
        }
    }

    @Shadow
    public boolean isHorseSaddled() {
        return this.getHorseWatchableBoolean(4);
    }
}
