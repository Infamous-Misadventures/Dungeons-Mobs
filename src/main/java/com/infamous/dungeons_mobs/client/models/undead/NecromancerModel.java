package com.infamous.dungeons_mobs.client.models.undead;

import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Necromancer - BklynKian
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class NecromancerModel<T extends NecromancerEntity> extends BipedModel<T> {

    public NecromancerModel() {
        this(0.0F);
    }

    public NecromancerModel(float modelSize) {
        super(modelSize);
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize);

        this.bipedHeadwear = new ModelRenderer(this, 32, 24);
        this.bipedHeadwear.setRotationPoint(0.0F, -1.0F, 0.0F);
        // have to shift the headwear up by 1 pixel on the y-axis
        this.bipedHeadwear.addBox(-4.0F, -8.0F - 1.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.4F + modelSize, 0.2F + modelSize, 0.4F + modelSize);
        /*
        this.bipedHeadwear = new ModelRenderer(this, 0, 0);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F + modelSize);

         */

        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, modelSize);

        this.bipedRightArm = new ModelRenderer(this, 0, 16);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
        this.setRotateAngle(bipedRightArm, 0.0F, 0.0F, 0.10000736647217022F); // the z-value here doesn't really affect anything
        this.bipedLeftArm = new ModelRenderer(this, 0, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
        this.setRotateAngle(bipedLeftArm, 0.0F, 0.0F, -0.10000736647217022F); // the z-value here doesn't really affect anything

        this.bipedLeftLeg = new ModelRenderer(this, 32, 2);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.1F);
        this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
        this.bipedRightLeg = new ModelRenderer(this, 32, 2);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.1F);
        this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);

        /*
        ModelRenderer crown = new ModelRenderer(this, 32, 24);
        crown.setRotationPoint(0.0F, -1.0F, 0.0F);
        crown.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.4F + modelSize, 0.2F + modelSize, 0.4F + modelSize);
        this.bipedHead.addChild(crown);

         */

        ModelRenderer cape = new ModelRenderer(this, 24, 37);
        cape.setRotationPoint(0.0F, 0.0F, 0.0F);
        cape.addBox(-8.0F, 0.0F, -1.0F, 16.0F, 23.0F, 4.0F, modelSize);
        ModelRenderer skirt = new ModelRenderer(this, 40, 0);
        skirt.setRotationPoint(0.0F, 12.0F, 0.0F);
        skirt.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 10.0F, 4.0F, modelSize);
        this.bipedBody.addChild(cape);
        this.bipedBody.addChild(skirt);

        ModelRenderer leftShoulderPad = new ModelRenderer(this, 0, 53);
        leftShoulderPad.setRotationPoint(0.0F, 0.0F, 0.0F);
        leftShoulderPad.addBox(-1.0F, -2.4F, -2.5F, 5.0F, 5.0F, 6.0F, modelSize);
        this.bipedLeftArm.addChild(leftShoulderPad);

        ModelRenderer rightShoulderPad = new ModelRenderer(this, 0, 53);
        rightShoulderPad.mirror = true;
        rightShoulderPad.setRotationPoint(0.0F, 0.0F, 0.0F);
        rightShoulderPad.addBox(-4.0F, -2.4F, -2.5F, 5.0F, 5.0F, 6.0F, modelSize);
        this.bipedRightArm.addChild(rightShoulderPad);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if(entityIn.isUsingMagic()){
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

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
