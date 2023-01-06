package com.infamous.dungeons_mobs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;

@Mixin(DrownedModel.class)
public abstract class DrownedModelMixin<T extends Zombie> extends ZombieModel<T> {

    public DrownedModelMixin(ModelPart p_171090_) {
        super(p_171090_);
    }

    @Inject(at = @At("TAIL"), method = "prepareMobModel")
    private void setPosesForAnyTrident(T drowned, float p_212843_2_, float p_212843_3_, float p_212843_4_, CallbackInfo ci){
        ItemStack mainhandStack = drowned.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offhandStack = drowned.getItemInHand(InteractionHand.OFF_HAND);

        if (mainhandStack.getItem() instanceof TridentItem && drowned.isAggressive()) {
            if (drowned.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = HumanoidModel.ArmPose.THROW_SPEAR;
            } else {
                this.leftArmPose = HumanoidModel.ArmPose.THROW_SPEAR;
            }
        } else if(offhandStack.getItem() instanceof TridentItem && drowned.isAggressive()) {
            if (drowned.getMainArm() == HumanoidArm.RIGHT) {
                this.leftArmPose = HumanoidModel.ArmPose.THROW_SPEAR;
            } else {
                this.rightArmPose = HumanoidModel.ArmPose.THROW_SPEAR;
            }
        }
    }
}
