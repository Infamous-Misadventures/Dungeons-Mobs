package com.infamous.dungeons_mobs.client.models.undead;

import com.infamous.dungeons_mobs.entities.undead.SkeletonVanguardEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;

public class SkeletonVanguardModel<T extends SkeletonVanguardEntity> extends BipedModel<T> {

  public SkeletonVanguardModel() {
  this(0.0F);
}

  public SkeletonVanguardModel(float modelSize) {
    super(modelSize);
    this.textureHeight = 128;
    this.textureWidth = 128;
    this.bipedBody = new ModelRenderer(this);
    this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.bipedBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F);
    this.bipedBody.setTextureOffset(32, 0).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 6.0F, 6.0F, 0.0F); // pants

    this.bipedHead = new ModelRenderer(this);
    this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.bipedHead.setTextureOffset(32, 32).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F);

    this.bipedRightArm = new ModelRenderer(this);
    this.bipedRightArm.setRotationPoint(5.0F, 2.0F, 0.0F);
    this.bipedRightArm.setTextureOffset(22, 63).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);

    this.bipedLeftArm = new ModelRenderer(this);
    this.bipedLeftArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
    this.bipedLeftArm.setTextureOffset(56, 59).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);

    this.bipedRightLeg = new ModelRenderer(this);
    this.bipedRightLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
    this.bipedRightLeg.setTextureOffset(48, 59).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
    this.bipedRightLeg.setTextureOffset(18, 48).addBox(-2.0F, -2.0F, -3.0F, 5.0F, 9.0F, 6.0F, modelSize); // right legwear

    this.bipedLeftLeg = new ModelRenderer(this);
    this.bipedLeftLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
    this.bipedLeftLeg.setTextureOffset(40, 59).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
    this.bipedLeftLeg.setTextureOffset(46, 15).addBox(-3.0F, -2.0F, -3.0F, 5.0F, 9.0F, 6.0F, modelSize); // left legwear
  }

  @Override
  public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
    this.rightArmPose = BipedModel.ArmPose.EMPTY;
    this.leftArmPose = BipedModel.ArmPose.EMPTY;
    if (entityIn.getPrimaryHand() == HandSide.RIGHT) {
      this.giveModelRightArmPoses(Hand.MAIN_HAND, entityIn);
      this.giveModelLeftArmPoses(Hand.OFF_HAND, entityIn);
    } else {
      this.giveModelRightArmPoses(Hand.OFF_HAND, entityIn);
      this.giveModelLeftArmPoses(Hand.MAIN_HAND, entityIn);
    }
    super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
  }

  private void giveModelRightArmPoses(Hand hand, T entityIn) {
    ItemStack itemstack = entityIn.getHeldItem(hand);
    UseAction useaction = itemstack.getUseAction();
    switch (useaction) {
      case BLOCK:
        if(entityIn.isActiveItemStackBlocking()){
          this.rightArmPose = ArmPose.BLOCK;
        }
        else{
          this.rightArmPose = ArmPose.ITEM;
        }
        break;
      case CROSSBOW:
        this.rightArmPose = ArmPose.CROSSBOW_HOLD;
        if (entityIn.isHandActive()) {
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

  private void giveModelLeftArmPoses(Hand hand, T entityIn) {
    ItemStack itemstack = entityIn.getHeldItem(hand);
    UseAction useaction = itemstack.getUseAction();
    switch (useaction) {
      case BLOCK:
        if(entityIn.isActiveItemStackBlocking()){
          this.leftArmPose = ArmPose.BLOCK;
        }
        else{
          this.leftArmPose = ArmPose.ITEM;
        }
        break;
      case CROSSBOW:
        this.leftArmPose = ArmPose.CROSSBOW_HOLD;
        if (entityIn.isHandActive()) {
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

  public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
    float f = sideIn == HandSide.RIGHT ? 1.0F : -1.0F;
    ModelRenderer modelrenderer = this.getArmForSide(sideIn);
    modelrenderer.rotationPointX += f;
    modelrenderer.translateRotate(matrixStackIn);
    modelrenderer.rotationPointX -= f;
  }
}