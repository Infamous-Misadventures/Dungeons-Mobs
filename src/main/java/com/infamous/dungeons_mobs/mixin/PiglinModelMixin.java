package com.infamous.dungeons_mobs.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.infamous.dungeons_mobs.interfaces.ISmartCrossbowUser;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.CrossbowItem;

@Mixin(PiglinModel.class)
public abstract class PiglinModelMixin<T extends Mob> extends PlayerModel<T> {

    public PiglinModelMixin(ModelPart p_170821_, boolean p_170822_) {
        super(p_170821_, p_170822_);
    }

    @Inject(at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/EntityType;ZOMBIFIED_PIGLIN:Lnet/minecraft/world/entity/EntityType;",
            opcode = Opcodes.GETSTATIC,
            ordinal = 0
    ),
            method = "setupAnim",
            cancellable = true)
    private void animateZombifiedPiglin(T piglinLike, float p_225597_2_, float p_225597_3_, float ageInTicks, float p_225597_5_, float p_225597_6_, CallbackInfo ci) {
        if(piglinLike instanceof ZombifiedPiglin){
            ci.cancel();
            if(piglinLike.isHolding(itemStack -> itemStack.getItem() instanceof CrossbowItem)){
                if (piglinLike instanceof ISmartCrossbowUser && ((ISmartCrossbowUser) piglinLike)._isChargingCrossbow()) {
                    AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, piglinLike, !piglinLike.isLeftHanded());
                } else {
                    AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, !piglinLike.isLeftHanded());
                }
            } else {
                AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, piglinLike.isAggressive(), this.attackTime, ageInTicks);
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
