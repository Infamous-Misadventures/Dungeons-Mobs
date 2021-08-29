package com.infamous.dungeons_mobs.client.models.jungle;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
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

public class LeapleafModel2<T extends LeapleafEntity> extends SegmentedModel<T> {


	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer rightArm;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightLeg;
	private final ModelRenderer leftLeg;

	public LeapleafModel2() {
		texWidth = 256;
		texHeight = 256;

		body = new ModelRenderer(this);
		body.setPos(0.0F, -5.0F, 0.0F);
		body.texOffs(44, 44).addBox(-10.0F, -9.0F, -6.0F, 20.0F, 18.0F, 14.0F, 0.0F, false);
		body.texOffs(0, 0).addBox(-9.0F, -8.0F, -3.0F, 18.0F, 16.0F, 10.0F, 0.0F, false);
		body.texOffs(98, 27).addBox(-7.0F, 8.0F, -4.5F, 14.0F, 13.0F, 10.0F, 0.0F, false);
		body.texOffs(110, 0).addBox(-6.0F, 9.0F, -3.0F, 12.0F, 11.0F, 8.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -14.0F, 0.0F);
		head.texOffs(0, 55).addBox(-1.0F, -9.0F, -0.5045F, 2.0F, 9.0F, 2.0F, 0.0F, false);

		this.buildHead();

		rightArm = new ModelRenderer(this);
		rightArm.setPos(-10.0F, -7.0F, 2.0F);
		rightArm.texOffs(33, 76).addBox(-11.0F, -4.0F, -6.0F, 11.0F, 18.0F, 11.0F, 0.0F, false);
		rightArm.texOffs(77, 77).addBox(-16.0F, 14.0F, -8.0F, 12.0F, 12.0F, 12.0F, 0.0F, true);
		rightArm.texOffs(104, 104).addBox(-15.0F, 15.0F, -7.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
		rightArm.texOffs(0, 26).addBox(-16.0F, 26.0F, -11.0F, 12.0F, 0.0F, 3.0F, 0.0F, true);
		rightArm.texOffs(68, 101).addBox(-10.0F, -3.0F, -5.0F, 9.0F, 16.0F, 9.0F, 0.0F, false);

		this.buildLeftArm();

		leftArm = new ModelRenderer(this);
		leftArm.setPos(10.0F, -8.0F, 2.0F);
		leftArm.texOffs(0, 55).addBox(0.0F, -3.0F, -6.0F, 11.0F, 18.0F, 11.0F, 0.0F, false);
		leftArm.texOffs(0, 96).addBox(1.0F, -2.0F, -5.0F, 9.0F, 16.0F, 9.0F, 0.0F, false);
		leftArm.texOffs(77, 77).addBox(4.0F, 15.0F, -8.0F, 12.0F, 12.0F, 12.0F, 0.0F, false);
		leftArm.texOffs(104, 104).addBox(5.0F, 16.0F, -7.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
		leftArm.texOffs(0, 26).addBox(4.0F, 27.0F, -11.0F, 12.0F, 0.0F, 3.0F, 0.0F, false);

		this.buildRightArm();

		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(-5.0F, 15.0F, 0.0F);
		rightLeg.texOffs(0, 122).addBox(-7.0F, 1.0F, -4.0F, 8.0F, 7.0F, 8.0F, 0.0F, false);
		rightLeg.texOffs(112, 50).addBox(-8.0F, 0.0F, -5.0F, 10.0F, 9.0F, 10.0F, 0.0F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(5.0F, 15.0F, 0.0F);
		leftLeg.texOffs(26, 111).addBox(-2.0F, 0.0F, -5.0F, 10.0F, 9.0F, 10.0F, 0.0F, false);
		leftLeg.texOffs(113, 69).addBox(-1.0F, 1.0F, -4.0F, 8.0F, 7.0F, 8.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		// reset body and arms
		this.body.yRot = 0.0F;
		this.leftArm.z = 2.0F; // original rotationPointZ of rightArm
		this.leftArm.x = 10.0F; // original rotationPointX of rightArm
		this.rightArm.z = 2.0F; // original rotationPointZ of leftArm
		this.rightArm.x = -10.0F; // original rotationPointX of leftArm

		// animate arms on X-axis
		this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		// reset rotateAngleZ for arms
		this.leftArm.zRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		// animate legs on X-axis
		this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;// since the Leapleaf's legs are relatively small, need to reduce the angle change by 50%
		this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;// since the Leapleaf's legs are relatively small, need to reduce the angle change by 50%
		// reset rotateAngleY and rotateAngleZ for legs
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.zRot = 0.0F;
		this.rightLeg.zRot = 0.0F;

		// reset rightArm and leftArm rotateAngleY before animating swing progress
		this.leftArm.yRot = 0.0F;
		this.rightArm.yRot = 0.0F;
		this.animateSwingProgress(entity, ageInTicks);

		// reset rotateAngleX for body
		this.body.xRot = 0.0F; // original rotateAngleX of body

		// reset rotationPointZ and rotationPointY for legs
		this.leftLeg.z = 0.0F; // original rotationPointZ of rightLeg
		this.rightLeg.z = 0.0F; // original rotationPointZ of leftLeg
		this.leftLeg.y = 15.0F; // original rotationPointY for rightLeg
		this.rightLeg.y = 15.0F; // original rotationPointY for leftLeg

		// reset rotationPointY for body
		this.body.y = -5.0F; // original rotationPointY for body

		// reset rotationPointY for arms
		this.rightArm.y = -7.0F; // original rotationPointY for leftArm
		this.leftArm.y = -8.0F; // original rotationPointY for rightArm

		// animate arms on X-axis and Z-axis
		ModelHelper.bobArms(this.leftArm, this.rightArm, ageInTicks);
		setRotationAngle(head, body.xRot, body.yRot, body.zRot);
	}

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
	}


	private HandSide getMainHand(T entityIn) {
		HandSide handside = entityIn.getMainArm();
		return entityIn.swingingArm == Hand.MAIN_HAND ? handside : handside.getOpposite();
	}

	private ModelRenderer getArmForSide(HandSide side) {
		return side == HandSide.LEFT ? this.rightArm : this.leftArm;
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

			this.leftArm.z = MathHelper.sin(this.body.yRot) * 5.0F;
			this.leftArm.x = -MathHelper.cos(this.body.yRot) * 5.0F;
			this.rightArm.z = -MathHelper.sin(this.body.yRot) * 5.0F;
			this.rightArm.x = MathHelper.cos(this.body.yRot) * 5.0F;
			this.leftArm.yRot += this.body.yRot;
			this.rightArm.yRot += this.body.yRot;
			//noinspection SuspiciousNameCombination
			this.rightArm.xRot += this.body.yRot;
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

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	private void buildRightArm() {
		ModelRenderer hand2_r1 = new ModelRenderer(this);
		hand2_r1.setPos(10.0F, 27.0F, 5.5F);
		leftArm.addChild(hand2_r1);
		setRotationAngle(hand2_r1, 3.1416F, 0.0F, 0.0F);
		hand2_r1.texOffs(0, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 0.0F, 3.0F, 0.0F, true);

		ModelRenderer hand2_r2 = new ModelRenderer(this);
		hand2_r2.setPos(2.5F, 27.0F, -2.0F);
		leftArm.addChild(hand2_r2);
		setRotationAngle(hand2_r2, 0.0F, 1.5708F, 0.0F);
		hand2_r2.texOffs(24, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 0.0F, 3.0F, 0.0F, false);

		ModelRenderer hand2_r3 = new ModelRenderer(this);
		hand2_r3.setPos(17.0F, 28.0F, -4.0F);
		leftArm.addChild(hand2_r3);
		setRotationAngle(hand2_r3, 0.0F, -1.5708F, 0.0F);
		hand2_r3.texOffs(24, 26).addBox(-4.0F, -1.0F, -2.0F, 12.0F, 0.0F, 3.0F, 0.0F, false);
	}

	private void buildLeftArm() {
		ModelRenderer hand1_r1 = new ModelRenderer(this);
		hand1_r1.setPos(-10.0F, 26.0F, 5.5F);
		rightArm.addChild(hand1_r1);
		setRotationAngle(hand1_r1, 3.1416F, 0.0F, 0.0F);
		hand1_r1.texOffs(0, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 0.0F, 3.0F, 0.0F, false);

		ModelRenderer hand1_r2 = new ModelRenderer(this);
		hand1_r2.setPos(-2.5F, 26.0F, -2.0F);
		rightArm.addChild(hand1_r2);
		setRotationAngle(hand1_r2, 0.0F, -1.5708F, 0.0F);
		hand1_r2.texOffs(24, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 0.0F, 3.0F, 0.0F, true);

		ModelRenderer hand1_r3 = new ModelRenderer(this);
		hand1_r3.setPos(-17.5F, 26.0F, -2.0F);
		rightArm.addChild(hand1_r3);
		setRotationAngle(hand1_r3, 0.0F, 1.5708F, 0.0F);
		hand1_r3.texOffs(24, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 0.0F, 3.0F, 0.0F, true);
	}

	private void buildHead() {
		ModelRenderer head_r1 = new ModelRenderer(this);
		head_r1.setPos(0.0F, -0.656F, 0.9955F);
		head.addChild(head_r1);
		setRotationAngle(head_r1, 1.5708F, 0.0F, 1.5708F);
		head_r1.texOffs(0, 0).addBox(0.0F, -13.0F, -14.75F, 0.0F, 26.0F, 29.0F, 0.0F, false);

		ModelRenderer head_r2 = new ModelRenderer(this);
		head_r2.setPos(0.0F, -0.906F, 0.4955F);
		head.addChild(head_r2);
		setRotationAngle(head_r2, 0.0F, 1.5708F, 1.5708F);
		head_r2.texOffs(56, 0).addBox(-13.5F, -13.5F, 0.0F, 27.0F, 27.0F, 0.0F, 0.0F, false);

		ModelRenderer petal1 = new ModelRenderer(this);
		petal1.setPos(0.0F, -1.0F, -5.5F);
		head.addChild(petal1);
		setRotationAngle(petal1, -2.0071F, 0.0F, 0.0F);


		ModelRenderer petal1_r1 = new ModelRenderer(this);
		petal1_r1.setPos(0.0F, 0.1675F, -11.7016F);
		petal1.addChild(petal1_r1);
		setRotationAngle(petal1_r1, 1.5708F, 0.0F, -1.5708F);
		petal1_r1.texOffs(58, 23).addBox(0.0F, -3.0F, -6.75F, 0.0F, 6.0F, 13.0F, 0.0F, true);

		ModelRenderer petal1_r2 = new ModelRenderer(this);
		petal1_r2.setPos(0.0F, 0.1675F, -4.2016F);
		petal1.addChild(petal1_r2);
		setRotationAngle(petal1_r2, 1.5708F, 0.0F, -1.5708F);
		petal1_r2.texOffs(58, 14).addBox(0.0F, -4.5F, -6.75F, 0.0F, 9.0F, 13.0F, 0.0F, true);

		ModelRenderer petal2 = new ModelRenderer(this);
		petal2.setPos(-6.5F, -1.0F, 0.0F);
		head.addChild(petal2);
		setRotationAngle(petal2, 0.0F, 0.0F, 2.0071F);


		ModelRenderer petal2_r1 = new ModelRenderer(this);
		petal2_r1.setPos(-11.9132F, -0.286F, 0.4955F);
		petal2.addChild(petal2_r1);
		setRotationAngle(petal2_r1, 0.0F, 0.0F, -1.5708F);
		petal2_r1.texOffs(58, 23).addBox(0.0F, -3.0F, -6.75F, 0.0F, 6.0F, 13.0F, 0.0F, true);

		ModelRenderer petal2_r2 = new ModelRenderer(this);
		petal2_r2.setPos(-4.4132F, -0.286F, 0.4955F);
		petal2.addChild(petal2_r2);
		setRotationAngle(petal2_r2, 0.0F, 0.0F, -1.5708F);
		petal2_r2.texOffs(58, 14).addBox(0.0F, -4.5F, -6.75F, 0.0F, 9.0F, 13.0F, 0.0F, true);

		ModelRenderer petal3 = new ModelRenderer(this);
		petal3.setPos(6.5F, -1.0F, 1.0F);
		head.addChild(petal3);
		setRotationAngle(petal3, 0.0F, 0.0F, -2.0071F);


		ModelRenderer petal3_r1 = new ModelRenderer(this);
		petal3_r1.setPos(11.9132F, -0.286F, -0.5045F);
		petal3.addChild(petal3_r1);
		setRotationAngle(petal3_r1, 0.0F, 0.0F, 1.5708F);
		petal3_r1.texOffs(58, 23).addBox(0.0F, -3.0F, -6.75F, 0.0F, 6.0F, 13.0F, 0.0F, false);

		ModelRenderer petal3_r2 = new ModelRenderer(this);
		petal3_r2.setPos(4.4132F, -0.286F, -0.5045F);
		petal3.addChild(petal3_r2);
		setRotationAngle(petal3_r2, 0.0F, 0.0F, 1.5708F);
		petal3_r2.texOffs(58, 14).addBox(0.0F, -4.5F, -6.75F, 0.0F, 9.0F, 13.0F, 0.0F, false);

		ModelRenderer petal4 = new ModelRenderer(this);
		petal4.setPos(0.0F, -1.0F, 6.5F);
		head.addChild(petal4);
		setRotationAngle(petal4, 2.0071F, 0.0F, 0.0F);


		ModelRenderer petal4_r1 = new ModelRenderer(this);
		petal4_r1.setPos(0.0F, 0.1594F, 11.7054F);
		petal4.addChild(petal4_r1);
		setRotationAngle(petal4_r1, -1.5708F, 0.0F, -1.5708F);
		petal4_r1.texOffs(58, 23).addBox(0.0F, -3.0F, -6.75F, 0.0F, 6.0F, 13.0F, 0.0F, true);

		ModelRenderer petal4_r2 = new ModelRenderer(this);
		petal4_r2.setPos(0.0F, 0.1594F, 4.2054F);
		petal4.addChild(petal4_r2);
		setRotationAngle(petal4_r2, -1.5708F, 0.0F, -1.5708F);
		petal4_r2.texOffs(58, 14).addBox(0.0F, -4.5F, -6.75F, 0.0F, 9.0F, 13.0F, 0.0F, true);
	}
}