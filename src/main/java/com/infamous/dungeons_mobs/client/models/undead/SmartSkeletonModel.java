package com.infamous.dungeons_mobs.client.models.undead;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class SmartSkeletonModel<T extends AbstractSkeleton> extends SkeletonModel<T> {

    public SmartSkeletonModel(ModelPart p_170941_) {
        super(p_170941_);
    }

    @Override
    public void prepareMobModel(T skeleton, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        super.prepareMobModel(skeleton, p_212843_2_, p_212843_3_, p_212843_4_);
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        if (skeleton.getMainArm() == HumanoidArm.RIGHT) {
            this.giveModelRightArmPoses(InteractionHand.MAIN_HAND, skeleton);
            this.giveModelLeftArmPoses(InteractionHand.OFF_HAND, skeleton);
        } else {
            this.giveModelRightArmPoses(InteractionHand.OFF_HAND, skeleton);
            this.giveModelLeftArmPoses(InteractionHand.MAIN_HAND, skeleton);
        }
    }

    @Override
    public void setupAnim(T skeleton, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
        super.setupAnim(skeleton, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
        if (this.swimAmount > 0.0F) {
            this.rightArm.xRot = this.rotlerpRad(this.swimAmount, this.rightArm.xRot, -2.5132742F) + this.swimAmount * 0.35F * Mth.sin(0.1F * p_225597_4_);
            this.leftArm.xRot = this.rotlerpRad(this.swimAmount, this.leftArm.xRot, -2.5132742F) - this.swimAmount * 0.35F * Mth.sin(0.1F * p_225597_4_);
            this.rightArm.zRot = this.rotlerpRad(this.swimAmount, this.rightArm.zRot, -0.15F);
            this.leftArm.zRot = this.rotlerpRad(this.swimAmount, this.leftArm.zRot, 0.15F);
            this.leftLeg.xRot -= this.swimAmount * 0.55F * Mth.sin(0.1F * p_225597_4_);
            this.rightLeg.xRot += this.swimAmount * 0.55F * Mth.sin(0.1F * p_225597_4_);
            this.head.xRot = 0.0F;
        }
    }

    private void giveModelRightArmPoses(InteractionHand hand, T skeleton) {
        ItemStack itemstack = skeleton.getItemInHand(hand);
        UseAnim useaction = itemstack.getUseAnimation();
        switch (useaction) {
            case BLOCK:
                if (skeleton.isBlocking()) {
                    this.rightArmPose = ArmPose.BLOCK;
                } else {
                    this.rightArmPose = ArmPose.ITEM;
                }
                break;
            case CROSSBOW:
                this.rightArmPose = ArmPose.CROSSBOW_HOLD;
                if (skeleton.isUsingItem()) {
                    this.rightArmPose = ArmPose.CROSSBOW_CHARGE;
                }
                break;
            case BOW:
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
                break;
            case SPEAR:
                this.leftArmPose = ArmPose.THROW_SPEAR;
                break;
            default:
                this.rightArmPose = ArmPose.EMPTY;
                if (!itemstack.isEmpty()) {
                    this.rightArmPose = ArmPose.ITEM;
                }
                break;
        }
    }

    private void giveModelLeftArmPoses(InteractionHand hand, T entityIn) {
        ItemStack itemstack = entityIn.getItemInHand(hand);
        UseAnim useaction = itemstack.getUseAnimation();
        switch (useaction) {
            case BLOCK:
                if (entityIn.isBlocking()) {
                    this.leftArmPose = ArmPose.BLOCK;
                } else {
                    this.leftArmPose = ArmPose.ITEM;
                }
                break;
            case CROSSBOW:
                this.leftArmPose = ArmPose.CROSSBOW_HOLD;
                if (entityIn.isUsingItem()) {
                    this.leftArmPose = ArmPose.CROSSBOW_CHARGE;
                }
                break;
            case BOW:
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
                break;
            case SPEAR:
                this.leftArmPose = ArmPose.THROW_SPEAR;
                break;
            default:
                this.leftArmPose = ArmPose.EMPTY;
                if (!itemstack.isEmpty()) {
                    this.leftArmPose = ArmPose.ITEM;
                }
                break;
        }
    }
}
