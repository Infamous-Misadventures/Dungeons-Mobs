package com.infamous.dungeons_mobs.mixin;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.DrownedModel;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrownedModel.class)
public abstract class DrownedModelMixin<T extends ZombieEntity> extends ZombieModel<T> {
    public DrownedModelMixin(float p_i1168_1_, boolean p_i1168_2_) {
        super(p_i1168_1_, p_i1168_2_);
    }

    @Inject(at = @At("TAIL"), method = "prepareMobModel")
    private void setPosesForAnyTrident(T drowned, float p_212843_2_, float p_212843_3_, float p_212843_4_, CallbackInfo ci){
        ItemStack mainhandStack = drowned.getItemInHand(Hand.MAIN_HAND);
        ItemStack offhandStack = drowned.getItemInHand(Hand.OFF_HAND);

        if (mainhandStack.getItem() instanceof TridentItem && drowned.isAggressive()) {
            if (drowned.getMainArm() == HandSide.RIGHT) {
                this.rightArmPose = BipedModel.ArmPose.THROW_SPEAR;
            } else {
                this.leftArmPose = BipedModel.ArmPose.THROW_SPEAR;
            }
        } else if(offhandStack.getItem() instanceof TridentItem && drowned.isAggressive()) {
            if (drowned.getMainArm() == HandSide.RIGHT) {
                this.leftArmPose = BipedModel.ArmPose.THROW_SPEAR;
            } else {
                this.rightArmPose = BipedModel.ArmPose.THROW_SPEAR;
            }
        }
    }
}
