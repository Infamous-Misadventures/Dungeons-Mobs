package com.infamous.dungeons_mobs.goals;

import com.infamous.dungeons_mobs.interfaces.IAquaticMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;

public class AquaticMoveHelperController<T extends MobEntity & IAquaticMob> extends MovementController {
    private final T aquaticMob;

    public AquaticMoveHelperController(T p_i48909_1_) {
        super(p_i48909_1_);
        this.aquaticMob = p_i48909_1_;
    }

    public void tick() {
        LivingEntity target = this.aquaticMob.getTarget();
        if (this.aquaticMob.wantsToSwim(this.aquaticMob) && this.aquaticMob.isInWater()) {
            if (target != null && target.getY() > this.aquaticMob.getY() || this.aquaticMob.isSearchingForLand()) {
                this.aquaticMob.setDeltaMovement(this.aquaticMob.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
            }

            if (this.operation != Action.MOVE_TO || this.aquaticMob.getNavigation().isDone()) {
                this.aquaticMob.setSpeed(0.0F);
                return;
            }

            double d0 = this.wantedX - this.aquaticMob.getX();
            double d1 = this.wantedY - this.aquaticMob.getY();
            double d2 = this.wantedZ - this.aquaticMob.getZ();
            double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            d1 = d1 / d3;
            float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
            this.aquaticMob.yRot = this.rotlerp(this.aquaticMob.yRot, f, 90.0F);
            this.aquaticMob.yBodyRot = this.aquaticMob.yRot;
            float f1 = (float) (this.speedModifier * this.aquaticMob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float f2 = MathHelper.lerp(0.125F, this.aquaticMob.getSpeed(), f1);
            this.aquaticMob.setSpeed(f2);
            this.aquaticMob.setDeltaMovement(this.aquaticMob.getDeltaMovement().add((double) f2 * d0 * 0.005D, (double) f2 * d1 * 0.1D, (double) f2 * d2 * 0.005D));
        } else {
            if (!this.aquaticMob.isOnGround()) {
                this.aquaticMob.setDeltaMovement(this.aquaticMob.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
            }

            super.tick();
        }

    }
}
