package com.infamous.dungeons_mobs.client.models.jungle;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class WhispererModel2<T extends WhispererEntity> extends BipedModel<T> {

	public WhispererModel2() {
		this(0.0F);
	}

	public WhispererModel2(float modelSize) {
		super(modelSize);
		texWidth = 128;
		texHeight = 128;

		body = new ModelRenderer(this);
		body.setPos(0.5F, -5.0F, 0.0F);
		body.texOffs(48, 24).addBox(-4.5F, 0.0F, -2.0F, 9.0F, 14.0F, 4.0F, 0.0F, false);
		body.texOffs(92, 9).addBox(-4.5F, 0.0F, -2.0F, 9.0F, 14.0F, 4.0F, 0.25F, false);

		hat = new ModelRenderer(this); // dummy hat that won't render
		hat.setPos(0.0F, 0.0F, 0.0F);
		hat.visible = false;

		head = new ModelRenderer(this);
		head.setPos(0.5F, -7.0F, 0.0F);
		head.texOffs(77, 37).addBox(-1.75F, -10.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, false);
		head.texOffs(81, 69).addBox(-5.5F, -5.75F, -5.0F, 11.0F, 6.0F, 10.0F, 0.0F, false);
		head.texOffs(77, 37).addBox(1.5F, -10.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, false);
		head.texOffs(78, 30).addBox(-2.0F, -10.0F, -1.75F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		head.texOffs(78, 30).addBox(-2.0F, -10.0F, 2.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		head.texOffs(50, 95).addBox(-5.5F, -2.0F, -5.0F, 11.0F, 0.0F, 10.0F, 0.0F, false);

		ModelRenderer jaw = new ModelRenderer(this);
		jaw.setPos(0.0F, 2.0F, 0.0F);
		head.addChild(jaw);
		jaw.texOffs(50, 95).addBox(-5.5F, -0.75F, -5.0F, 11.0F, 0.0F, 10.0F, 0.0F, false);
		jaw.texOffs(81, 93).addBox(-5.5F, -4.0F, -5.0F, 11.0F, 4.0F, 10.0F, 0.0F, false);

		ModelRenderer collar = new ModelRenderer(this);
		collar.setPos(0.0F, 2.0F, -1.0F);
		body.addChild(collar);
		collar.texOffs(18, 69).addBox(-9.5F, 0.0F, -9.5F, 19.0F, 0.0F, 19.0F, 0.0F, false);

		leftArm = new ModelRenderer(this);
		leftArm.setPos(5.0F, -3.5F, 0.0F);
		leftArm.texOffs(4, 0).addBox(0.0F, -1.5F, -2.5F, 0.0F, 12.0F, 5.0F, 0.0F, false);
		leftArm.texOffs(24, 98).addBox(-1.0F, -1.5F, 1.5F, 5.0F, 11.0F, 0.0F, 0.0F, false);
		leftArm.texOffs(82, 116).addBox(0.0F, -1.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		leftArm.texOffs(4, 98).addBox(-1.0F, -1.5F, -1.5F, 5.0F, 11.0F, 0.0F, 0.0F, false);
		leftArm.texOffs(102, 116).addBox(0.0F, 9.5F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		leftArm.texOffs(4, 109).addBox(-1.0F, 9.5F, -1.5F, 5.0F, 12.0F, 0.0F, 0.0F, false);
		leftArm.texOffs(102, 116).addBox(0.0F, 10.5F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		leftArm.texOffs(4, 39).addBox(5.0F, 10.5F, -3.0F, 0.0F, 11.0F, 6.0F, 0.0F, false);
		leftArm.texOffs(102, 116).addBox(0.0F, 20.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		leftArm.texOffs(24, 109).addBox(-1.0F, 9.5F, 1.5F, 5.0F, 12.0F, 0.0F, 0.0F, false);
		leftArm.texOffs(4, 12).addBox(0.0F, 10.5F, -2.5F, 0.0F, 11.0F, 5.0F, 0.0F, false);
		leftArm.texOffs(4, 27).addBox(5.0F, -1.5F, -3.0F, 0.0F, 12.0F, 6.0F, 0.0F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setPos(-4.0F, -3.5F, 0.0F);
		rightArm.texOffs(4, 66).addBox(-5.0F, -1.25F, 1.5F, 6.0F, 11.0F, 0.0F, 0.0F, false);
		rightArm.texOffs(82, 116).addBox(-3.5F, -1.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		rightArm.texOffs(26, -1).addBox(-3.5F, -1.75F, -3.0F, 0.0F, 12.0F, 6.0F, 0.0F, false);
		rightArm.texOffs(102, 116).addBox(-3.5F, 9.75F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		rightArm.texOffs(25, 29).addBox(-0.5F, -1.5F, -2.5F, 0.0F, 12.0F, 5.0F, 0.0F, false);
		rightArm.texOffs(26, 10).addBox(-3.5F, 10.25F, -3.0F, 0.0F, 11.0F, 6.0F, 0.0F, false);
		rightArm.texOffs(102, 116).addBox(-3.5F, 20.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		rightArm.texOffs(5, 77).addBox(-4.0F, 9.75F, 1.5F, 5.0F, 12.0F, 0.0F, 0.0F, false);
		rightArm.texOffs(25, 41).addBox(-0.5F, 10.5F, -2.5F, 0.0F, 11.0F, 5.0F, 0.0F, false);
		rightArm.texOffs(24, 76).addBox(-5.0F, 9.5F, -1.5F, 6.0F, 12.0F, 0.0F, 0.0F, false);
		rightArm.texOffs(102, 116).addBox(-3.5F, 10.5F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		rightArm.texOffs(24, 65).addBox(-5.0F, -1.5F, -1.5F, 6.0F, 11.0F, 0.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(-1.5F, 9.4109F, 0.0F);
		rightLeg.texOffs(102, 40).addBox(-1.5F, -0.1874F, -1.9729F, 3.0F, 15.0F, 4.0F, 0.0F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(2.5F, 9.2111F, 0.0F);
		leftLeg.texOffs(93, 40).addBox(-1.5F, -0.2111F, -2.0F, 3.0F, 15.0F, 4.0F, 0.0F, false);
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

		if (this.swimAmount > 0.0F) {
			this.rightArm.xRot = this.rotlerpRad(this.swimAmount, this.rightArm.xRot, -2.5132742F) + this.swimAmount * 0.35F * MathHelper.sin(0.1F * ageInTicks);
			this.leftArm.xRot = this.rotlerpRad(this.swimAmount, this.leftArm.xRot, -2.5132742F) - this.swimAmount * 0.35F * MathHelper.sin(0.1F * ageInTicks);
			this.rightArm.zRot = this.rotlerpRad(this.swimAmount, this.rightArm.zRot, -0.15F);
			this.leftArm.zRot = this.rotlerpRad(this.swimAmount, this.leftArm.zRot, 0.15F);
			this.leftLeg.xRot -= this.swimAmount * 0.55F * MathHelper.sin(0.1F * ageInTicks);
			this.rightLeg.xRot += this.swimAmount * 0.55F * MathHelper.sin(0.1F * ageInTicks);
			this.head.xRot = 0.0F;
		}
	}

	public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
		float f = sideIn == HandSide.RIGHT ? 1.0F : -1.0F;
		ModelRenderer modelrenderer = this.getArm(sideIn);
		modelrenderer.x += f;
		modelrenderer.translateAndRotate(matrixStackIn);
		modelrenderer.x -= f;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}