package com.infamous.dungeons_mobs.client.models.golem;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class SquallGolemModel<T extends SquallGolemEntity> extends SegmentedModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer rightArm;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightLeg;
	private final ModelRenderer leftLeg;

	public SquallGolemModel() {
		textureWidth = 128;
		textureHeight = 128;

		body = new ModelRenderer(this);
		body.setRotationPoint(-18.0F, 22.0F, 0.0F);
		body.setTextureOffset(0, 0).addBox(3.0F, -40.0F, -8.0F, 30.0F, 22.0F, 14.0F, 0.0F, true);
		body.setTextureOffset(0, 36).addBox(10.0F, -18.0F, -6.0F, 16.0F, 5.0F, 9.0F, 0.0F, true);

		// head
		body.setTextureOffset(76, 105).addBox(5.0F + 7.0F, -86.0F + 46.0F, -26.0F + 8.0F, 16.0F, 13.0F, 10.0F, 0.0F, true);
		body.setTextureOffset(54, 121).addBox(12.0F + 7.0F, -86.0F + 46.0F, -28.0F + 8.0F, 2.0F, 5.0F, 2.0F, 0.0F, true);
		body.setTextureOffset(62, 118).addBox(11.0F + 7.0F, -79.0F + 46.0F, -29.0F + 8.0F, 4.0F, 7.0F, 3.0F, 0.0F, false);

		ModelRenderer rightSpinner = new ModelRenderer(this);
		rightSpinner.setRotationPoint(8.0F, -43.0833F, -5.0F);
		body.addChild(rightSpinner);
		rightSpinner.setTextureOffset(112, 0).addBox(-2.0F, 1.0833F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, true);
		rightSpinner.setTextureOffset(120, 6).addBox(-1.0F, -8.9167F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, true);
		rightSpinner.setTextureOffset(108, 18).addBox(0.0F, -8.9167F, 1.0F, 0.0F, 8.0F, 5.0F, 0.0F, true);
		rightSpinner.setTextureOffset(118, 18).addBox(-6.0F, -8.9167F, 0.0F, 5.0F, 8.0F, 0.0F, 0.0F, true);
		rightSpinner.setTextureOffset(118, 18).addBox(1.0F, -8.9167F, 0.0F, 5.0F, 8.0F, 0.0F, 0.0F, true);
		rightSpinner.setTextureOffset(108, 18).addBox(0.0F, -8.9167F, -6.0F, 0.0F, 8.0F, 5.0F, 0.0F, true);

		ModelRenderer leftSpinner = new ModelRenderer(this);
		leftSpinner.setRotationPoint(29.0F, -43.0833F, -5.0F);
		body.addChild(leftSpinner);
		leftSpinner.setTextureOffset(112, 0).addBox(-2.0F, 1.0833F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, true);
		leftSpinner.setTextureOffset(120, 6).addBox(-1.0F, -8.9167F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, true);
		leftSpinner.setTextureOffset(108, 18).addBox(0.0F, -8.9167F, 1.0F, 0.0F, 8.0F, 5.0F, 0.0F, true);
		leftSpinner.setTextureOffset(118, 18).addBox(-6.0F, -8.9167F, 0.0F, 5.0F, 8.0F, 0.0F, 0.0F, true);
		leftSpinner.setTextureOffset(118, 18).addBox(1.0F, -8.9167F, 0.0F, 5.0F, 8.0F, 0.0F, 0.0F, true);
		leftSpinner.setTextureOffset(108, 18).addBox(0.0F, -8.9167F, -6.0F, 0.0F, 8.0F, 5.0F, 0.0F, true);

		rightArm = new ModelRenderer(this);
		rightArm.setRotationPoint(-17.0F, 13.75F - 13.75F - 7.0F, 2.5F);
		rightArm.setTextureOffset(0, 53).addBox(-4.0F, -25.75F + 13.75F + 7.0F, -8.5F, 6.0F, 35.0F, 9.0F, 0.0F, false);
		rightArm.setTextureOffset(0, 117).addBox(-4.0F, 5.25F + 13.75F + 7.0F, 0.5F, 6.0F, 4.0F, 7.0F, 0.0F, false);

		leftArm = new ModelRenderer(this);
		leftArm.setRotationPoint(18.0F, 13.75F - 13.75F - 7.0F, 2.5F);
		leftArm.setTextureOffset(0, 53).addBox(-3.0F, -25.75F + 13.75F + 7.0F, -8.5F, 6.0F, 35.0F, 9.0F, 0.0F, false);
		leftArm.setTextureOffset(0, 117).addBox(-3.0F, 5.25F + 13.75F + 7.0F, 0.5F, 6.0F, 4.0F, 7.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-18.0F, 24.0F, 0.0F);
		rightLeg.setTextureOffset(93, 39).addBox(7.0F, -15.0F, -5.0F, 8.0F, 15.0F, 7.0F, 0.0F, true);

		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(-18.0F, 24.0F, 0.0F);
		leftLeg.setTextureOffset(93, 39).addBox(21.0F, -15.0F, -5.0F, 8.0F, 15.0F, 7.0F, 0.0F, true);
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
	}

	@Override
	public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
		//this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);

		this.leftLeg.rotateAngleX = -0.5F * MathHelper.func_233021_e_(limbSwing, 13.0F) * limbSwingAmount;
		this.rightLeg.rotateAngleX = 0.5F * MathHelper.func_233021_e_(limbSwing, 13.0F) * limbSwingAmount;
		this.leftLeg.rotateAngleY = 0.0F;
		this.rightLeg.rotateAngleY = 0.0F;
	}

	public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		int i = entityIn.getAttackTimer();
		if (i > 0) {
			this.rightArm.rotateAngleX = -2.0F + 1.5F * MathHelper.func_233021_e_((float)i - partialTick, 10.0F);
			this.leftArm.rotateAngleX = -2.0F + 1.5F * MathHelper.func_233021_e_((float)i - partialTick, 10.0F);
		} else {
			this.rightArm.rotateAngleX = (-0.2F + 1.5F * MathHelper.func_233021_e_(limbSwing, 13.0F)) * limbSwingAmount;
			this.leftArm.rotateAngleX = (-0.2F - 1.5F * MathHelper.func_233021_e_(limbSwing, 13.0F)) * limbSwingAmount;
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