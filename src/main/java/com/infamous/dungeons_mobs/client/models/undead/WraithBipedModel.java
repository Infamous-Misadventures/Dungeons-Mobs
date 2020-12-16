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
         this.bipedRightArm = new ModelRenderer(this, 40, 16);
         this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
         this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
         this.bipedLeftArm = new ModelRenderer(this, 40, 16);
         this.bipedLeftArm.mirror = true;
         this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
         this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
         this.bipedRightLeg = new ModelRenderer(this, 0, 16);
         this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
         this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
         this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
         this.bipedLeftLeg.mirror = true;
         this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
         this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
      }

   }

   /**
    * Sets this entity's model rotation angles
    */
   public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      if (!entityIn.isUsingMagic()) {
         float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
         float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
         this.bipedRightArm.rotateAngleZ = 0.0F;
         this.bipedLeftArm.rotateAngleZ = 0.0F;
         this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
         this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
         this.bipedRightArm.rotateAngleX = (-(float)Math.PI / 2F);
         this.bipedLeftArm.rotateAngleX = (-(float)Math.PI / 2F);
         this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
         this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
         ModelHelper.func_239101_a_(this.bipedRightArm, this.bipedLeftArm, ageInTicks);
      }
      else{
         this.bipedRightArm.rotationPointZ = 0.0F;
         this.bipedRightArm.rotationPointX = -5.0F;
         this.bipedLeftArm.rotationPointZ = 0.0F;
         this.bipedLeftArm.rotationPointX = 5.0F;
         this.bipedRightArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
         this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
         this.bipedRightArm.rotateAngleZ = 2.3561945F;
         this.bipedLeftArm.rotateAngleZ = -2.3561945F;
         this.bipedRightArm.rotateAngleY = 0.0F;
         this.bipedLeftArm.rotateAngleY = 0.0F;
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