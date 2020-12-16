package com.infamous.dungeons_mobs.client.models.jungle;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class WhispererModel<T extends WhispererEntity> extends BipedModel<T> {

	public WhispererModel() {
		this(0.0F);
	}

	public WhispererModel(float modelSize) {
		super(modelSize);
		textureWidth = 128;
		textureHeight = 128;

		this.bipedHead = new ModelRenderer(this);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.setTextureOffset(0, 107).addBox(-5.0F, -14.0F, -5.0F, 11.0F, 7.0F, 10.0F, modelSize, false);
		this.bipedHead.setTextureOffset(0, 90).addBox(-5.0F, -10.0F, -5.0F, 11.0F, 5.0F, 10.0F, modelSize, false);
		this.bipedHead.setTextureOffset(0, 75).addBox(-5.0F, -12.0F, -5.0F, 11.0F, 6.0F, 9.0F, modelSize, false);

		this.bipedHeadwear = new ModelRenderer(this);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.setTextureOffset(0, 107).addBox(-5.0F, -14.0F, -5.0F, 11.0F, 7.0F, 10.0F, 0.5F + modelSize, false);
		this.bipedHeadwear.showModel = false;

		this.bipedBody = new ModelRenderer(this);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.setTextureOffset(42, 71).addBox(-4.0F, -5.0F, -2.0F, 9.0F, 14.0F, 4.0F, modelSize, false);
		this.bipedBody.setTextureOffset(52, 109).addBox(-9.0F, -5.0F, -10.0F, 19.0F, 0.0F - 0.001F, 19.0F, modelSize, false);
		this.bipedBody.setTextureOffset(42, 89).addBox(-4.0F, -5.0F, -2.0F, 9.0F, 14.0F, 4.0F, 0.25F + modelSize, false);

		this.bipedRightArm = new ModelRenderer(this);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedRightArm.setTextureOffset(84, 80).addBox(-3.0F, -5.0F, -2.0F, 4.0F, 23.0F, 4.0F, modelSize, false);

		this.bipedLeftArm = new ModelRenderer(this);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedLeftArm.setTextureOffset(68, 80).addBox(0.0F, -5.0F, -2.0F, 4.0F, 23.0F, 4.0F, modelSize, false);

		this.bipedRightLeg = new ModelRenderer(this);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		this.bipedRightLeg.setTextureOffset(114, 89).addBox(-1.1F, -3.0F, -2.0F, 3.0F, 15.0F, 4.0F, modelSize, false);

		this.bipedLeftLeg = new ModelRenderer(this);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		this.bipedLeftLeg.setTextureOffset(100, 89).addBox(-0.9F, -3.0F, -2.0F, 3.0F, 15.0F, 4.0F, modelSize, false);
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

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}