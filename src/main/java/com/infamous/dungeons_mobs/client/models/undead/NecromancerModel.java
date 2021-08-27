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
        this.texWidth = 64;
        this.texHeight = 64;

        this.head = new ModelRenderer(this, 0, 0);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSize);

        this.hat = new ModelRenderer(this, 32, 24);
        this.hat.setPos(0.0F, -1.0F, 0.0F);
        // have to shift the headwear up by 1 pixel on the y-axis
        this.hat.addBox(-4.0F, -8.0F - 1.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.4F + modelSize, 0.2F + modelSize, 0.4F + modelSize);
        /*
        this.bipedHeadwear = new ModelRenderer(this, 0, 0);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F + modelSize);

         */

        this.body = new ModelRenderer(this, 16, 16);
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, modelSize);

        this.rightArm = new ModelRenderer(this, 0, 16);
        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.rightArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
        this.setRotateAngle(rightArm, 0.0F, 0.0F, 0.10000736647217022F); // the z-value here doesn't really affect anything
        this.leftArm = new ModelRenderer(this, 0, 16);
        this.leftArm.mirror = true;
        this.leftArm.setPos(5.0F, 2.0F, 0.0F);
        this.leftArm.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
        this.setRotateAngle(leftArm, 0.0F, 0.0F, -0.10000736647217022F); // the z-value here doesn't really affect anything

        this.leftLeg = new ModelRenderer(this, 32, 2);
        this.leftLeg.mirror = true;
        this.leftLeg.setPos(2.0F, 12.0F, 0.1F);
        this.leftLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);
        this.rightLeg = new ModelRenderer(this, 32, 2);
        this.rightLeg.setPos(-2.0F, 12.0F, 0.1F);
        this.rightLeg.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, modelSize);

        /*
        ModelRenderer crown = new ModelRenderer(this, 32, 24);
        crown.setRotationPoint(0.0F, -1.0F, 0.0F);
        crown.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.4F + modelSize, 0.2F + modelSize, 0.4F + modelSize);
        this.bipedHead.addChild(crown);

         */

        ModelRenderer cape = new ModelRenderer(this, 24, 37);
        cape.setPos(0.0F, 0.0F, 0.0F);
        cape.addBox(-8.0F, 0.0F, -1.0F, 16.0F, 23.0F, 4.0F, modelSize);
        ModelRenderer skirt = new ModelRenderer(this, 40, 0);
        skirt.setPos(0.0F, 12.0F, 0.0F);
        skirt.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 10.0F, 4.0F, modelSize);
        this.body.addChild(cape);
        this.body.addChild(skirt);

        ModelRenderer leftShoulderPad = new ModelRenderer(this, 0, 53);
        leftShoulderPad.setPos(0.0F, 0.0F, 0.0F);
        leftShoulderPad.addBox(-1.0F, -2.4F, -2.5F, 5.0F, 5.0F, 6.0F, modelSize);
        this.leftArm.addChild(leftShoulderPad);

        ModelRenderer rightShoulderPad = new ModelRenderer(this, 0, 53);
        rightShoulderPad.mirror = true;
        rightShoulderPad.setPos(0.0F, 0.0F, 0.0F);
        rightShoulderPad.addBox(-4.0F, -2.4F, -2.5F, 5.0F, 5.0F, 6.0F, modelSize);
        this.rightArm.addChild(rightShoulderPad);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if(entityIn.isUsingMagic()){
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

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
