package com.infamous.dungeons_mobs.mixin;

import com.infamous.dungeons_mobs.interfaces.ISmartCrossbowUser;
import net.minecraft.client.renderer.entity.model.PiglinModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.piglin.PiglinAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PiglinModel.class)
public abstract class PiglinModelMixin<T extends MobEntity> extends PlayerModel<T> {

    public PiglinModelMixin(float p_i46304_1_, boolean p_i46304_2_) {
        super(p_i46304_1_, p_i46304_2_);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/model/ModelHelper;animateZombieArms(Lnet/minecraft/client/renderer/model/ModelRenderer;Lnet/minecraft/client/renderer/model/ModelRenderer;ZFF)V"),
            method = "setupAnim")
    private void animateZombifiedPiglin(ModelRenderer p_239105_0_, ModelRenderer p_239105_1_, boolean p_239105_2_, float p_239105_3_, float p_239105_4_, T piglinLikeMob, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
        if (piglinLikeMob.isHolding(item -> item instanceof net.minecraft.item.CrossbowItem)) {
            ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, !piglinLikeMob.isLeftHanded());
        } else if (piglinLikeMob instanceof ISmartCrossbowUser && ((ISmartCrossbowUser) piglinLikeMob).isChargingCrossbow()) {
            ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, piglinLikeMob, !piglinLikeMob.isLeftHanded());
        } else{
            ModelHelper.animateZombieArms(this.leftArm, this.rightArm, piglinLikeMob.isAggressive(), this.attackTime, p_225597_4_);
        }
    }
}
