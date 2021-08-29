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
    this.texHeight = 128;
    this.texWidth = 128;
    this.body = new ModelRenderer(this);
    this.body.setPos(0.0F, 0.0F, 0.0F);
    this.body.texOffs(0, 38).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F);
    this.body.texOffs(32, 0).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 6.0F, 6.0F, 0.0F); // pants

    this.head = new ModelRenderer(this);
    this.head.setPos(0.0F, 0.0F, 0.0F);
    this.head.texOffs(32, 32).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F);

    this.rightArm = new ModelRenderer(this);
    this.rightArm.setPos(5.0F, 2.0F, 0.0F);
    this.rightArm.texOffs(22, 63).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);

    this.leftArm = new ModelRenderer(this);
    this.leftArm.setPos(-5.0F, 2.0F, 0.0F);
    this.leftArm.texOffs(56, 59).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);

    this.rightLeg = new ModelRenderer(this);
    this.rightLeg.setPos(2.0F, 12.0F, 0.0F);
    this.rightLeg.texOffs(48, 59).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
    this.rightLeg.texOffs(18, 48).addBox(-2.0F, -2.0F, -3.0F, 5.0F, 9.0F, 6.0F, modelSize); // right legwear

    this.leftLeg = new ModelRenderer(this);
    this.leftLeg.setPos(-2.0F, 12.0F, 0.0F);
    this.leftLeg.texOffs(40, 59).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
    this.leftLeg.texOffs(46, 15).addBox(-3.0F, -2.0F, -3.0F, 5.0F, 9.0F, 6.0F, modelSize); // left legwear
  }

  @Override
  public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
    this.rightArmPose = BipedModel.ArmPose.EMPTY;
    this.leftArmPose = BipedModel.ArmPose.EMPTY;
    if (entityIn.getMainArm() == HandSide.RIGHT) {
      this.giveModelRightArmPoses(Hand.MAIN_HAND, entityIn);
      this.giveModelLeftArmPoses(Hand.OFF_HAND, entityIn);
    } else {
      this.giveModelRightArmPoses(Hand.OFF_HAND, entityIn);
      this.giveModelLeftArmPoses(Hand.MAIN_HAND, entityIn);
    }
    super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
  }

  private void giveModelRightArmPoses(Hand hand, T entityIn) {
    ItemStack itemstack = entityIn.getItemInHand(hand);
    UseAction useaction = itemstack.getUseAnimation();
    switch (useaction) {
      case BLOCK:
        if(entityIn.isBlocking()){
          this.rightArmPose = ArmPose.BLOCK;
        }
        else{
          this.rightArmPose = ArmPose.ITEM;
        }
        break;
      case CROSSBOW:
        this.rightArmPose = ArmPose.CROSSBOW_HOLD;
        if (entityIn.isUsingItem()) {
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
    ItemStack itemstack = entityIn.getItemInHand(hand);
    UseAction useaction = itemstack.getUseAnimation();
    switch (useaction) {
      case BLOCK:
        if(entityIn.isBlocking()){
          this.leftArmPose = ArmPose.BLOCK;
        }
        else{
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

  public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
    float f = sideIn == HandSide.RIGHT ? 1.0F : -1.0F;
    ModelRenderer modelrenderer = this.getArm(sideIn);
    modelrenderer.x += f;
    modelrenderer.translateAndRotate(matrixStackIn);
    modelrenderer.x -= f;
  }
}