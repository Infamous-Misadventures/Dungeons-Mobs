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
		texWidth = 128;
		texHeight = 128;

		this.head = new ModelRenderer(this);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.texOffs(0, 107).addBox(-5.0F, -14.0F, -5.0F, 11.0F, 7.0F, 10.0F, modelSize, false);
		this.head.texOffs(0, 90).addBox(-5.0F, -10.0F, -5.0F, 11.0F, 5.0F, 10.0F, modelSize, false);
		this.head.texOffs(0, 75).addBox(-5.0F, -12.0F, -5.0F, 11.0F, 6.0F, 9.0F, modelSize, false);

		this.hat = new ModelRenderer(this);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.texOffs(0, 107).addBox(-5.0F, -14.0F, -5.0F, 11.0F, 7.0F, 10.0F, 0.5F + modelSize, false);
		this.hat.visible = false;

		this.body = new ModelRenderer(this);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.body.texOffs(42, 71).addBox(-4.0F, -5.0F, -2.0F, 9.0F, 14.0F, 4.0F, modelSize, false);
		this.body.texOffs(52, 109).addBox(-9.0F, -5.0F, -10.0F, 19.0F, 0.0F - 0.001F, 19.0F, modelSize, false);
		this.body.texOffs(42, 89).addBox(-4.0F, -5.0F, -2.0F, 9.0F, 14.0F, 4.0F, 0.25F + modelSize, false);

		this.rightArm = new ModelRenderer(this);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
		this.rightArm.texOffs(84, 80).addBox(-3.0F, -5.0F, -2.0F, 4.0F, 23.0F, 4.0F, modelSize, false);

		this.leftArm = new ModelRenderer(this);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
		this.leftArm.texOffs(68, 80).addBox(0.0F, -5.0F, -2.0F, 4.0F, 23.0F, 4.0F, modelSize, false);

		this.rightLeg = new ModelRenderer(this);
		this.rightLeg.setPos(-1.9F, 12.0F, 0.0F);
		this.rightLeg.texOffs(114, 89).addBox(-1.1F, -3.0F, -2.0F, 3.0F, 15.0F, 4.0F, modelSize, false);

		this.leftLeg = new ModelRenderer(this);
		this.leftLeg.setPos(1.9F, 12.0F, 0.0F);
		this.leftLeg.texOffs(100, 89).addBox(-0.9F, -3.0F, -2.0F, 3.0F, 15.0F, 4.0F, modelSize, false);
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