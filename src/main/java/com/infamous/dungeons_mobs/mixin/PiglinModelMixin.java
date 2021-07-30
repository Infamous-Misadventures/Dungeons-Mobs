package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.interfaces.ISmartCrossbowUser;
import net.minecraft.client.renderer.entity.model.PiglinModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.item.CrossbowItem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinModel.class)
public abstract class PiglinModelMixin<T extends MobEntity> extends PlayerModel<T> {

    public PiglinModelMixin(float p_i46304_1_, boolean p_i46304_2_) {
        super(p_i46304_1_, p_i46304_2_);
    }

    @Inject(at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/entity/EntityType;ZOMBIFIED_PIGLIN:Lnet/minecraft/entity/EntityType;",
                opcode = Opcodes.GETSTATIC,
                ordinal = 0
                ),
            method = "setupAnim",
            cancellable = true)
    private void animateZombifiedPiglin(T piglinLike, float p_225597_2_, float p_225597_3_, float ageInTicks, float p_225597_5_, float p_225597_6_, CallbackInfo ci) {
        if(piglinLike instanceof ZombifiedPiglinEntity){
            ci.cancel();
            if(piglinLike.isHolding(item -> item instanceof CrossbowItem)){
                if (piglinLike instanceof ISmartCrossbowUser && ((ISmartCrossbowUser) piglinLike)._isChargingCrossbow()) {
                    ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, piglinLike, !piglinLike.isLeftHanded());
                } else {
                    ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, !piglinLike.isLeftHanded());
                }
            } else {
                ModelHelper.animateZombieArms(this.leftArm, this.rightArm, piglinLike.isAggressive(), this.attackTime, ageInTicks);
            }
            this.animateClothes();
        }
    }

    private void animateClothes() {
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
    }
}
