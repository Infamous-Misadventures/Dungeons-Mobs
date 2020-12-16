package com.infamous.dungeons_mobs.client.models.redstone;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RedstoneGolemModel<T extends RedstoneGolemEntity> extends SegmentedModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer rightArm;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightLeg;
	private final ModelRenderer leftLeg;

	public RedstoneGolemModel() {
		textureWidth = 256;
		textureHeight = 256;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 0.5F, 2.0F);
		setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
		body.setTextureOffset(0, 28).addBox(-20.0F, -13.0F, 5.0F, 40.0F, 20.0F, 32.0F, 0.0F, false);
		body.setTextureOffset(72, 6).addBox(-11.0F, -9.0F, -3.0F, 22.0F, 14.0F, 8.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addBox(-8.0F, -25.0F, 21.0F, 16.0F, 12.0F, 16.0F, 0.0F, false);
		body.setTextureOffset(132, 16).addBox(-8.0F, -5.0F, 9.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(8.0F, -14.0F, 15.0F);
		leftLeg.setTextureOffset(192, 48).addBox(1.0F, 18.0F, -19.5F, 12.0F, 20.0F, 12.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(20.0F, -13.0F, 20.0F);
		rightLeg.setTextureOffset(144, 48).addBox(-40.0F, 17.0F, -24.5F, 12.0F, 20.0F, 12.0F, 0.0F, false);

		leftArm = new ModelRenderer(this);
		leftArm.setRotationPoint(8.0F, -13.0F - 13.0F, -3.5F);
		leftArm.setTextureOffset(31, 116).addBox(12.0F, -22.0F + 13.0F, -2.0F, 14.0F, 24.0F, 12.0F, 0.0F, false);
		leftArm.setTextureOffset(83, 118).addBox(17.0F, 2.0F + 13.0F, -2.0F, 11.0F, 22.0F, 12.0F, 0.0F, false);
		leftArm.setTextureOffset(145, 80).addBox(23.0F, 24.0F + 13.0F, -1.0F, 3.0F, 10.0F, 5.0F, 0.0F, false);
		leftArm.setTextureOffset(145, 80).addBox(23.0F, 24.0F + 13.0F, 5.0F, 3.0F, 10.0F, 5.0F, 0.0F, false);
		leftArm.setTextureOffset(129, 82).addBox(17.0F, 24.0F + 13.0F, -1.0F, 3.0F, 8.0F, 5.0F, 0.0F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setRotationPoint(-8.0F, -13.0F - 13.0F, -3.5F);
		rightArm.setTextureOffset(129, 82).addBox(-20.0F, 24.0F + 13.0F, -1.0F, 3.0F, 8.0F, 5.0F, 0.0F, false);
		rightArm.setTextureOffset(83, 82).addBox(-28.0F, 2.0F + 13.0F, -2.0F, 11.0F, 22.0F, 12.0F, 0.0F, false);
		rightArm.setTextureOffset(145, 80).addBox(-26.0F, 24.0F + 13.0F, -1.0F, 3.0F, 10.0F, 5.0F, 0.0F, false);
		rightArm.setTextureOffset(145, 80).addBox(-26.0F, 24.0F + 13.0F, 5.0F, 3.0F, 10.0F, 5.0F, 0.0F, false);
		rightArm.setTextureOffset(31, 80).addBox(-26.0F, -22.0F + 13.0F, -2.0F, 14.0F, 24.0F, 12.0F, 0.0F, false);
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
	}

	@Override
	public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
		//this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
		/*
		this.leftLeg.rotateAngleX = -0.5F * MathHelper.func_233021_e_(limbSwing, 13.0F) * limbSwingAmount;
		this.rightLeg.rotateAngleX = 0.5F * MathHelper.func_233021_e_(limbSwing, 13.0F) * limbSwingAmount;
		this.leftLeg.rotateAngleY = 0.0F;
		this.rightLeg.rotateAngleY = 0.0F;

		 */


		// reset body and arms
		this.body.rotateAngleY = 0.0F;
		this.rightArm.rotationPointZ = -3.5F; // original rotationPointZ of rightArm
		this.rightArm.rotationPointX = -8.0F; // original rotationPointX of rightArm
		this.leftArm.rotationPointZ = -3.5F; // original rotationPointZ of leftArm
		this.leftArm.rotationPointX = 8.0F; // original rotationPointX of leftArm

		// animate arms on X-axis
		this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		// reset rotateAngleZ for arms
		this.rightArm.rotateAngleZ = 0.0F;
		this.leftArm.rotateAngleZ = 0.0F;
		// animate legs on X-axis
		this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F; // since the Redstone Golem's legs are small, need to reduce the angle change by 50%
		this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F; // since the Redstone Golem's legs are small, need to reduce the angle change by 50%
		// reset rotateAngleY and rotateAngleZ for legs
		this.rightLeg.rotateAngleY = 0.0F;
		this.leftLeg.rotateAngleY = 0.0F;
		this.rightLeg.rotateAngleZ = 0.0F;
		this.leftLeg.rotateAngleZ = 0.0F;

		// reset rightArm and leftArm rotateAngleY before animating swing progress
		this.rightArm.rotateAngleY = 0.0F;
		this.leftArm.rotateAngleY = 0.0F;
		this.animateSwingProgress(entity, ageInTicks);

		// reset rotateAngleX for body
		this.body.rotateAngleX = 1.5708F; // original rotateAngleX of body

		// reset rotationPointZ and rotationPointY for legs
		this.rightLeg.rotationPointZ = 20.01F; // original rotationPointZ of rightLeg
		this.leftLeg.rotationPointZ = 15.01F; // original rotationPointZ of leftLeg
		this.rightLeg.rotationPointY = -13.0F; // original rotationPointY for rightLeg
		this.leftLeg.rotationPointY = -14.0F; // original rotationPointY for leftLeg

		// reset rotationPointY for body
		this.body.rotationPointY = 0.5F; // original rotationPointY for body

		// reset rotationPointY for arms
		this.leftArm.rotationPointY = -26.0F; // original rotationPointY for leftArm
		this.rightArm.rotationPointY = -26.0F; // original rotationPointY for rightArm

		// animate arms on X-axis and Z-axis
		ModelHelper.func_239101_a_(this.rightArm, this.leftArm, ageInTicks);
	}


	private HandSide getMainHand(T entityIn) {
		HandSide handside = entityIn.getPrimaryHand();
		return entityIn.swingingHand == Hand.MAIN_HAND ? handside : handside.opposite();
	}

	private ModelRenderer getArmForSide(HandSide side) {
		return side == HandSide.LEFT ? this.leftArm : this.rightArm;
	}

	private void animateSwingProgress(T entity, float ageInTicks) {
		if (!(this.swingProgress <= 0.0F)) {
			HandSide handside = this.getMainHand(entity);
			ModelRenderer modelrenderer = this.getArmForSide(handside);
			float f = this.swingProgress;
			this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f) * ((float)Math.PI * 2F)) * 0.2F;
			if (handside == HandSide.LEFT) {
				this.body.rotateAngleY *= -1.0F;
			}

			this.rightArm.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 5.0F;
			this.rightArm.rotationPointX = -MathHelper.cos(this.body.rotateAngleY) * 5.0F;
			this.leftArm.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 5.0F;
			this.leftArm.rotationPointX = MathHelper.cos(this.body.rotateAngleY) * 5.0F;
			this.rightArm.rotateAngleY += this.body.rotateAngleY;
			this.leftArm.rotateAngleY += this.body.rotateAngleY;
			this.leftArm.rotateAngleX += this.body.rotateAngleY;
			f = 1.0F - this.swingProgress;
			f = f * f;
			f = f * f;
			f = 1.0F - f;
			float f1 = MathHelper.sin(f * (float)Math.PI);
			float f2 = MathHelper.sin(this.swingProgress * (float)Math.PI) * -(this.body.rotateAngleX - 0.7F) * 0.75F; // originally head
			modelrenderer.rotateAngleX = (float)((double)modelrenderer.rotateAngleX - ((double)f1 * 1.2D + (double)f2));
			modelrenderer.rotateAngleY += this.body.rotateAngleY * 2.0F;
			modelrenderer.rotateAngleZ += MathHelper.sin(this.swingProgress * (float)Math.PI) * -0.4F;
		}
	}


	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){

		this.getParts().forEach((part) -> part.render(matrixStack, buffer, packedLight, packedOverlay));
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}