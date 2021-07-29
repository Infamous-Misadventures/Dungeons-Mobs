package com.infamous.dungeons_mobs.client.models.undead;

import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WraithBipedModel<T extends WraithEntity> extends BipedModel<T> {
   public WraithBipedModel() {
      this(0.0F, false);
   }

   public WraithBipedModel(float modelSize, boolean p_i46303_2_) {
      super(modelSize);
      if (!p_i46303_2_) {
         this.rightArm = new ModelRenderer(this, 40, 16);
         this.rightArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
         this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
         this.leftArm = new ModelRenderer(this, 40, 16);
         this.leftArm.mirror = true;
         this.leftArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
         this.leftArm.setPos(5.0F, 2.0F, 0.0F);
         this.rightLeg = new ModelRenderer(this, 0, 16);
         this.rightLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
         this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
         this.leftLeg = new ModelRenderer(this, 0, 16);
         this.leftLeg.mirror = true;
         this.leftLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
         this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
      }

   }

   /**
    * Sets this entity's model rotation angles
    */
   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      if (!entityIn.isUsingMagic()) {
         float f = MathHelper.sin(this.attackTime * (float)Math.PI);
         float f1 = MathHelper.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
         this.rightArm.zRot = 0.0F;
         this.leftArm.zRot = 0.0F;
         this.rightArm.yRot = -(0.1F - f * 0.6F);
         this.leftArm.yRot = 0.1F - f * 0.6F;
         this.rightArm.xRot = (-(float)Math.PI / 2F);
         this.leftArm.xRot = (-(float)Math.PI / 2F);
         this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
         this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
         ModelHelper.bobArms(this.rightArm, this.leftArm, ageInTicks);
      }
      else{
         this.rightArm.z = 0.0F;
         this.rightArm.x = -5.0F;
         this.leftArm.z = 0.0F;
         this.leftArm.x = 5.0F;
         this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
         this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
         this.rightArm.zRot = 2.3561945F;
         this.leftArm.zRot = -2.3561945F;
         this.rightArm.yRot = 0.0F;
         this.leftArm.yRot = 0.0F;
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