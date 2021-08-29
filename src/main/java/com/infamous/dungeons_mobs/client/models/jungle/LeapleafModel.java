package com.infamous.dungeons_mobs.client.models.jungle;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.infamous.dungeons_mobs.entities.jungle.LeapleafEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class LeapleafModel<T extends LeapleafEntity> extends SegmentedModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer rightLeg;
	private final ModelRenderer leftLeg;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightArm;

	public LeapleafModel() {
		texWidth = 128;
		texHeight = 128;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 3.0F, 0.0F);
		body.texOffs(1, 53).addBox(-11.0F, -8.0F, -8.0F, 22.0F, 13.0F, 17.0F, 0.0F, false);
		body.texOffs(54, 26).addBox(-10.0F, -7.0F, -7.0F, 20.0F, 11.0F, 15.0F, 0.0F, false);

		ModelRenderer flower = new ModelRenderer(this);
		flower.setPos(1.0F, -7.0F + -3.0F, 0.0F);
		body.addChild(flower);
		setRotationAngle(flower, 0.0F, 0.0F, 0.0F);
		flower.texOffs(44, 3).addBox(-11.0F, 2.0F, -10.0F, 21.0F, 0.0F, 21.0F, 0.0F, false);
		flower.texOffs(30, 98).addBox(-8.0F, 1.0F, -7.0F, 14.0F, 1.0F, 15.0F, 0.0F, true);
		flower.texOffs(114, 116).addBox(-2.0F, -8.0F, -1.0F, 3.0F, 9.0F, 3.0F, 0.0F, false);

		ModelRenderer abdomen = new ModelRenderer(this);
		abdomen.setPos(0.0F, 10.0F, 0.0F);
		body.addChild(abdomen);
		abdomen.texOffs(0, 53).addBox(-6.0F, -5.0F, -4.0F, 12.0F, 8.0F, 9.0F, 0.0F, false);
		abdomen.texOffs(87, 41).addBox(-5.0F, -4.0F, -3.0F, 10.0F, 6.0F, 7.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(-6.0F, 18.0F, 0.0F);
		rightLeg.texOffs(0, 44).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 8.0F, 9.0F, 0.0F, false);
		rightLeg.texOffs(84, 74).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 6.0F, 7.0F, 0.0F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(6.0F, 18.0F, 0.0F);
		leftLeg.texOffs(0, 44).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 8.0F, 9.0F, 0.0F, false);
		leftLeg.texOffs(96, 74).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 6.0F, 7.0F, 0.0F, false);

		ModelRenderer backPetal = new ModelRenderer(this);
		backPetal.setPos(-42.0F, 27.0F, 7.0F);
		flower.addChild(backPetal);
		backPetal.texOffs(13, 6).addBox(34.0F, -26.0F, 1.0F, 14.0F, 0.0F, 19.0F, 0.0F, false);

		ModelRenderer frontPetal = new ModelRenderer(this);
		frontPetal.setPos(42.0F, 27.0F, -40.0F);
		flower.addChild(frontPetal);
		setRotationAngle(frontPetal, 0.0F, 3.1416F, 0.0F);
		frontPetal.texOffs(13, 6).addBox(36.0F, -26.0F, -33.0F, 14.0F, 0.0F, 19.0F, 0.0F, false);

		ModelRenderer rightPetal = new ModelRenderer(this);
		rightPetal.setPos(-31.0F, 27.0F, -25.0F);
		flower.addChild(rightPetal);
		setRotationAngle(rightPetal, 0.0F, -1.5708F, 0.0F);
		rightPetal.texOffs(12, 5).addBox(17.0F, -26.0F, -23.0F, 15.0F, 0.0F, 19.0F, 0.0F, false);

		ModelRenderer leftPetal = new ModelRenderer(this);
		leftPetal.setPos(22.0F, 27.0F, 64.0F);
		flower.addChild(leftPetal);
		setRotationAngle(leftPetal, 0.0F, 1.5708F, 0.0F);
		leftPetal.texOffs(13, 5).addBox(57.0F, -26.0F, -16.0F, 15.0F, 0.0F, 19.0F, 0.0F, false);

		leftArm = new ModelRenderer(this);
		leftArm.setPos(16.0F, -2.0F, 0.0F);
		leftArm.texOffs(34, 54).addBox(-5.0F, -3.0F, -6.0F, 10.0F, 17.0F, 13.0F, 0.0F, false);
		leftArm.texOffs(0, 57).addBox(-5.0F, 14.0F, -7.0F, 13.0F, 11.0F, 15.0F, 0.0F, false);
		leftArm.texOffs(88, 59).addBox(-4.0F, 15.0F, -6.0F, 11.0F, 9.0F, 13.0F, 0.0F, false);
		leftArm.texOffs(88, 59).addBox(-3.0F, -2.0F, -3.0F, 5.0F, 20.0F, 7.0F, 0.0F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setPos(-16.0F, -2.0F, 0.0F);
		//setRotationAngle(rightArm, 0.0F, 0.0F, 0.0F);
		rightArm.texOffs(3, 53).addBox(-5.0F, -3.0F, -7.0F, 10.0F, 17.0F, 13.0F, 0.0F, false);
		rightArm.texOffs(0, 57).addBox(-8.0F, 14.0F, -8.0F, 13.0F, 11.0F, 15.0F, 0.0F, false);
		rightArm.texOffs(88, 59).addBox(-7.0F, 15.0F, -7.0F, 11.0F, 9.0F, 13.0F, 0.0F, false);
		rightArm.texOffs(86, 59).addBox(-1.0F, -2.0F, -4.0F, 5.0F, 20.0F, 7.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
		//this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
		/*
		this.leftLeg.rotateAngleX = -0.5F * MathHelper.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.rightLeg.rotateAngleX = 0.5F * MathHelper.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.leftLeg.rotateAngleY = 0.0F;
		this.rightLeg.rotateAngleY = 0.0F;

		 */


		// reset body and arms
		this.body.yRot = 0.0F;
		this.rightArm.z = 0.0F; // original rotationPointZ of rightArm
		this.rightArm.x = -16.0F; // original rotationPointX of rightArm
		this.leftArm.z = 0.0F; // original rotationPointZ of leftArm
		this.leftArm.x = 16.0F; // original rotationPointX of leftArm

		// animate arms on X-axis
		this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		// reset rotateAngleZ for arms
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		// animate legs on X-axis
		this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;// since the Leapleaf's legs are relatively small, need to reduce the angle change by 50%
		this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;// since the Leapleaf's legs are relatively small, need to reduce the angle change by 50%
		// reset rotateAngleY and rotateAngleZ for legs
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.zRot = 0.0F;
		this.leftLeg.zRot = 0.0F;

		// reset rightArm and leftArm rotateAngleY before animating swing progress
		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.animateSwingProgress(entity, ageInTicks);

		// reset rotateAngleX for body
		this.body.xRot = 0.0F; // original rotateAngleX of body

		// reset rotationPointZ and rotationPointY for legs
		this.rightLeg.z = 0.01F; // original rotationPointZ of rightLeg
		this.leftLeg.z = 0.01F; // original rotationPointZ of leftLeg
		this.rightLeg.y = 18.0F; // original rotationPointY for rightLeg
		this.leftLeg.y = 18.0F; // original rotationPointY for leftLeg

		// reset rotationPointY for body
		this.body.y = 3.0F; // original rotationPointY for body

		// reset rotationPointY for arms
		this.leftArm.y = -2.0F; // original rotationPointY for leftArm
		this.rightArm.y = -2.0F; // original rotationPointY for rightArm

		// animate arms on X-axis and Z-axis
		ModelHelper.bobArms(this.rightArm, this.leftArm, ageInTicks);
	}

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
	}


	private HandSide getMainHand(T entityIn) {
		HandSide handside = entityIn.getMainArm();
		return entityIn.swingingArm == Hand.MAIN_HAND ? handside : handside.getOpposite();
	}

	private ModelRenderer getArmForSide(HandSide side) {
		return side == HandSide.LEFT ? this.leftArm : this.rightArm;
	}

	private void animateSwingProgress(T entity, float ageInTicks) {
		if (!(this.attackTime <= 0.0F)) {
			HandSide handside = this.getMainHand(entity);
			ModelRenderer modelrenderer = this.getArmForSide(handside);
			float f = this.attackTime;
			this.body.yRot = MathHelper.sin(MathHelper.sqrt(f) * ((float)Math.PI * 2F)) * 0.2F;
			if (handside == HandSide.LEFT) {
				this.body.yRot *= -1.0F;
			}

			this.rightArm.z = MathHelper.sin(this.body.yRot) * 5.0F;
			this.rightArm.x = -MathHelper.cos(this.body.yRot) * 5.0F;
			this.leftArm.z = -MathHelper.sin(this.body.yRot) * 5.0F;
			this.leftArm.x = MathHelper.cos(this.body.yRot) * 5.0F;
			this.rightArm.yRot += this.body.yRot;
			this.leftArm.yRot += this.body.yRot;
			this.leftArm.xRot += this.body.yRot;
			f = 1.0F - this.attackTime;
			f = f * f;
			f = f * f;
			f = 1.0F - f;
			float f1 = MathHelper.sin(f * (float)Math.PI);
			float f2 = MathHelper.sin(this.attackTime * (float)Math.PI) * -(this.body.xRot - 0.7F) * 0.75F; // originally head
			modelrenderer.xRot = (float)((double)modelrenderer.xRot - ((double)f1 * 1.2D + (double)f2));
			modelrenderer.yRot += this.body.yRot * 2.0F;
			modelrenderer.zRot += MathHelper.sin(this.attackTime * (float)Math.PI) * -0.4F;
		}
	}


	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		this.parts().forEach((part) -> part.render(matrixStack, buffer, packedLight, packedOverlay));
	}

	/*
	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		int i = entityIn.getAttackTimer();
		if (i > 0) {
			this.rightArm.rotateAngleX = -2.0F + 1.5F * MathHelper.triangleWave((float)i - partialTick, 10.0F);
			this.leftArm.rotateAngleX = -2.0F + 1.5F * MathHelper.triangleWave((float)i - partialTick, 10.0F);
		} else {
			this.rightArm.rotateAngleX = (-0.2F + 1.5F * MathHelper.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
			this.leftArm.rotateAngleX = (-0.2F - 1.5F * MathHelper.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
		}

	}

	 */

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}