package com.infamous.dungeons_mobs.client.models.jungle;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class WhispererModel1<T extends WhispererEntity> extends BipedModel<T> {
	private final ModelRenderer bone;
	private final ModelRenderer Body;
	private final ModelRenderer bodypart1;
	private final ModelRenderer arms;
	private final ModelRenderer RightArm;
	private final ModelRenderer RightArm_part1;
	private final ModelRenderer LeftArm;
	private final ModelRenderer LeftArm_r1;
	private final ModelRenderer LeftArm_part1;
	private final ModelRenderer LeftArm_r2;
	private final ModelRenderer Head;
	private final ModelRenderer part1;
	private final ModelRenderer InnerMouth_r1;
	private final ModelRenderer Head_r1;
	private final ModelRenderer Head_r2;
	private final ModelRenderer Head_r3;
	private final ModelRenderer Head_r4;
	private final ModelRenderer part2;
	private final ModelRenderer InnerMouth_r2;
	private final ModelRenderer collar;
	private final ModelRenderer Neckpiece_r1;
	private final ModelRenderer legs;
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;

	public WhispererModel1(float modelSize) {
		super(modelSize);
		texWidth = 128;
		texHeight = 128;

		bone = new ModelRenderer(this);
		bone.setPos(0.5F, 3.5F, 0.0F);
		

		Body = new ModelRenderer(this);
		Body.setPos(0.0F, 5.75F, 0.0F);
		bone.addChild(Body);
		

		bodypart1 = new ModelRenderer(this);
		bodypart1.setPos(0.0F, -7.25F, 0.0F);
		Body.addChild(bodypart1);
		bodypart1.texOffs(48, 24).addBox(-4.5F, -7.0F, -2.0F, 9.0F, 14.0F, 4.0F, 0.0F, false);
		bodypart1.texOffs(92, 9).addBox(-4.5F, -7.0F, -2.0F, 9.0F, 14.0F, 4.0F, 0.25F, false);

		arms = new ModelRenderer(this);
		arms.setPos(0.5F, -12.75F, 0.0F);
		Body.addChild(arms);
		

		RightArm = new ModelRenderer(this);
		RightArm.setPos(-5.0F, 0.0F, 0.0F);
		arms.addChild(RightArm);
		RightArm.texOffs(4, 66).addBox(-5.0F, -1.25F, 1.5F, 6.0F, 11.0F, 0.0F, 0.0F, false);
		RightArm.texOffs(82, 116).addBox(-3.5F, -1.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		RightArm.texOffs(26, -1).addBox(-3.5F, -1.75F, -3.0F, 0.0F, 12.0F, 6.0F, 0.0F, false);
		RightArm.texOffs(102, 116).addBox(-3.5F, 9.75F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		RightArm.texOffs(25, 29).addBox(-0.5F, -1.5F, -2.5F, 0.0F, 12.0F, 5.0F, 0.0F, false);
		RightArm.texOffs(24, 65).addBox(-5.0F, -1.5F, -1.5F, 6.0F, 11.0F, 0.0F, 0.0F, false);

		RightArm_part1 = new ModelRenderer(this);
		RightArm_part1.setPos(-2.0F, 10.5833F, 0.0F);
		RightArm.addChild(RightArm_part1);
		RightArm_part1.texOffs(26, 10).addBox(-1.5F, -0.3333F, -3.0F, 0.0F, 11.0F, 6.0F, 0.0F, false);
		RightArm_part1.texOffs(102, 116).addBox(-1.5F, 9.4167F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		RightArm_part1.texOffs(5, 77).addBox(-2.0F, -0.8333F, 1.5F, 5.0F, 12.0F, 0.0F, 0.0F, false);
		RightArm_part1.texOffs(25, 41).addBox(1.5F, -0.0833F, -2.5F, 0.0F, 11.0F, 5.0F, 0.0F, false);
		RightArm_part1.texOffs(24, 76).addBox(-3.0F, -1.0833F, -1.5F, 6.0F, 12.0F, 0.0F, 0.0F, false);
		RightArm_part1.texOffs(102, 116).addBox(-1.5F, -0.0833F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

		LeftArm = new ModelRenderer(this);
		LeftArm.setPos(4.0F, 0.0F, 0.0F);
		arms.addChild(LeftArm);
		LeftArm.texOffs(4, 0).addBox(0.0F, -1.5F, -2.5F, 0.0F, 12.0F, 5.0F, 0.0F, false);
		LeftArm.texOffs(24, 98).addBox(-1.0F, -1.5F, 1.5F, 5.0F, 11.0F, 0.0F, 0.0F, false);
		LeftArm.texOffs(82, 116).addBox(0.0F, -1.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		LeftArm.texOffs(4, 98).addBox(-1.0F, -1.5F, -1.5F, 5.0F, 11.0F, 0.0F, 0.0F, false);
		LeftArm.texOffs(102, 116).addBox(0.0F, 9.5F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

		LeftArm_r1 = new ModelRenderer(this);
		LeftArm_r1.setPos(4.0F, 10.0F, 0.0F);
		LeftArm.addChild(LeftArm_r1);
		setRotationAngle(LeftArm_r1, 0.0F, 3.1416F, 0.0F);
		LeftArm_r1.texOffs(4, 27).addBox(1.0F, -11.5F, -3.0F, 0.0F, 12.0F, 6.0F, 0.0F, false);

		LeftArm_part1 = new ModelRenderer(this);
		LeftArm_part1.setPos(1.9167F, 10.5833F, 0.0F);
		LeftArm.addChild(LeftArm_part1);
		LeftArm_part1.texOffs(4, 109).addBox(-2.9167F, -1.0833F, -1.5F, 5.0F, 12.0F, 0.0F, 0.0F, false);
		LeftArm_part1.texOffs(102, 116).addBox(-1.9167F, -0.0833F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		LeftArm_part1.texOffs(102, 116).addBox(-1.9167F, 9.4167F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
		LeftArm_part1.texOffs(24, 109).addBox(-2.9167F, -1.0833F, 1.5F, 5.0F, 12.0F, 0.0F, 0.0F, false);
		LeftArm_part1.texOffs(4, 12).addBox(-1.9167F, -0.0833F, -2.5F, 0.0F, 11.0F, 5.0F, 0.0F, false);

		LeftArm_r2 = new ModelRenderer(this);
		LeftArm_r2.setPos(2.0833F, -0.5833F, 0.0F);
		LeftArm_part1.addChild(LeftArm_r2);
		setRotationAngle(LeftArm_r2, 0.0F, 3.1416F, 0.0F);
		LeftArm_r2.texOffs(4, 39).addBox(1.0F, 0.5F, -3.0F, 0.0F, 11.0F, 6.0F, 0.0F, false);

		Head = new ModelRenderer(this);
		Head.setPos(0.0F, -14.25F, 0.0F);
		Body.addChild(Head);
		

		part1 = new ModelRenderer(this);
		part1.setPos(0.0F, -1.75F, 4.75F);
		Head.addChild(part1);
		part1.texOffs(81, 69).addBox(-5.5F, -6.0F, -9.75F, 11.0F, 6.0F, 10.0F, 0.0F, false);

		InnerMouth_r1 = new ModelRenderer(this);
		InnerMouth_r1.setPos(0.0F, -3.25F, -4.75F);
		part1.addChild(InnerMouth_r1);
		setRotationAngle(InnerMouth_r1, 0.0F, 3.1416F, -3.1416F);
		InnerMouth_r1.texOffs(50, 95).addBox(-5.5F, 1.0F, -5.0F, 11.0F, 0.0F, 10.0F, 0.0F, false);

		Head_r1 = new ModelRenderer(this);
		Head_r1.setPos(0.0F, -6.0F, -2.75F);
		part1.addChild(Head_r1);
		setRotationAngle(Head_r1, -0.4363F, 3.1416F, 0.0F);
		Head_r1.texOffs(78, 30).addBox(-2.0F, -4.25F, 0.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		Head_r2 = new ModelRenderer(this);
		Head_r2.setPos(0.0F, -6.0F, -6.75F);
		part1.addChild(Head_r2);
		setRotationAngle(Head_r2, -0.4363F, 0.0F, 0.0F);
		Head_r2.texOffs(78, 30).addBox(-2.0F, -4.25F, 0.25F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		Head_r3 = new ModelRenderer(this);
		Head_r3.setPos(1.75F, -6.0F, -4.75F);
		part1.addChild(Head_r3);
		setRotationAngle(Head_r3, 0.0F, 0.0F, -0.4363F);
		Head_r3.texOffs(77, 37).addBox(-0.25F, -4.25F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, false);

		Head_r4 = new ModelRenderer(this);
		Head_r4.setPos(-1.5F, -6.0F, -4.75F);
		part1.addChild(Head_r4);
		setRotationAngle(Head_r4, 0.0F, 3.1416F, 0.4363F);
		Head_r4.texOffs(77, 37).addBox(-0.25F, -4.25F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, false);

		part2 = new ModelRenderer(this);
		part2.setPos(0.0F, -2.0F, 5.0F);
		Head.addChild(part2);
		part2.texOffs(81, 93).addBox(-5.5F, -2.0F, -10.0F, 11.0F, 4.0F, 10.0F, 0.0F, false);

		InnerMouth_r2 = new ModelRenderer(this);
		InnerMouth_r2.setPos(0.0F, 1.0F, -5.0F);
		part2.addChild(InnerMouth_r2);
		setRotationAngle(InnerMouth_r2, 0.0F, 3.1416F, 0.0F);
		InnerMouth_r2.texOffs(50, 95).addBox(-5.5F, 0.25F, -5.0F, 11.0F, 0.0F, 10.0F, 0.0F, false);

		collar = new ModelRenderer(this);
		collar.setPos(0.0F, 0.0F, -1.0F);
		Head.addChild(collar);
		

		Neckpiece_r1 = new ModelRenderer(this);
		Neckpiece_r1.setPos(0.0F, 0.0F, 0.5F);
		collar.addChild(Neckpiece_r1);
		setRotationAngle(Neckpiece_r1, 3.1416F, 0.0F, -3.1416F);
		Neckpiece_r1.texOffs(18, 69).addBox(-9.5F, 0.0F, -10.0F, 19.0F, 0.0F, 19.0F, 0.0F, false);

		legs = new ModelRenderer(this);
		legs.setPos(0.0F, 5.5F, 0.0F);
		bone.addChild(legs);
		

		RightLeg = new ModelRenderer(this);
		RightLeg.setPos(-2.0F, 0.4109F, 0.0F);
		legs.addChild(RightLeg);
		RightLeg.texOffs(102, 40).addBox(-1.5F, -0.1874F, -1.9729F, 3.0F, 15.0F, 4.0F, 0.0F, false);

		LeftLeg = new ModelRenderer(this);
		LeftLeg.setPos(2.0F, 0.2111F, 0.0F);
		legs.addChild(LeftLeg);
		LeftLeg.texOffs(93, 40).addBox(-1.5F, -0.2111F, -2.0F, 3.0F, 15.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bone.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}