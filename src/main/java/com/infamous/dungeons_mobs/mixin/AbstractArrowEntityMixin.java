package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowEntityMixin extends Projectile {

    protected AbstractArrowEntityMixin(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @Inject(at = @At("RETURN"), method = "getWaterInertia", cancellable = true)
    private void checkShotByAquaticMob(CallbackInfoReturnable<Float> cir) {
        Entity owner = this.getOwner();
        if (owner instanceof IAquaticMob) {
            cir.setReturnValue(0.99F);
        }
    }

}
