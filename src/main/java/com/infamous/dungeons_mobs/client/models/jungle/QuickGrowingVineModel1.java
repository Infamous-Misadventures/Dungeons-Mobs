package com.infamous.dungeons_mobs.client.models.jungle;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class QuickGrowingVineModel1<T extends QuickGrowingVineEntity> extends EntityModel<T> {
	private final ModelRenderer bone;
	private final ModelRenderer body;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer cube_r7;
	private final ModelRenderer bodypart1;
	private final ModelRenderer cube_r8;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;
	private final ModelRenderer bodypart2;
	private final ModelRenderer cube_r12;
	private final ModelRenderer cube_r13;
	private final ModelRenderer cube_r14;
	private final ModelRenderer cube_r15;
	private final ModelRenderer leafs;
	private final ModelRenderer leaf1;
	private final ModelRenderer cube_r16;
	private final ModelRenderer leaf2;
	private final ModelRenderer cube_r17;
	private final ModelRenderer leaf3;
	private final ModelRenderer cube_r18;
	private final ModelRenderer leaf4;
	private final ModelRenderer cube_r19;

	public QuickGrowingVineModel1() {
		texWidth = 128;
		texHeight = 128;

		bone = new ModelRenderer(this);
		bone.setPos(0.2686F, 23.8743F, -0.0157F);
		

		body = new ModelRenderer(this);
		body.setPos(0.0F, 0.0F, 0.0F);
		bone.addChild(body);
		body.texOffs(96, 63).addBox(-8.0186F, -18.8743F, -4.2343F, 16.0F, 19.0F, 0.0F, 0.0F, false);
		body.texOffs(72, 98).addBox(-5.0186F, -18.1243F, -4.9843F, 10.0F, 0.0F, 10.0F, 0.0F, false);
		body.texOffs(32, 63).addBox(-8.0186F, -18.8743F, 4.2657F, 16.0F, 19.0F, 0.0F, 0.0F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(-7.0698F, -10.8952F, -5.2921F);
		body.addChild(cube_r1);
		setRotationAngle(cube_r1, 2.2689F, 0.7854F, 0.0F);
		cube_r1.texOffs(82, 103).addBox(1.0F, -2.75F, -4.75F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(-4.2686F, -8.8743F, -0.4843F);
		body.addChild(cube_r2);
		setRotationAngle(cube_r2, 3.1416F, 3.1416F, -3.1416F);
		cube_r2.texOffs(64, 48).addBox(0.0F, -9.0F, -6.5F, 0.0F, 18.0F, 16.0F, 0.0F, false);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setPos(-7.0698F, -10.8952F, -5.2921F);
		body.addChild(cube_r3);
		setRotationAngle(cube_r3, 3.1416F, 0.0873F, 1.5708F);
		cube_r3.texOffs(82, 103).addBox(1.0F, -1.5F, -3.25F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setPos(5.4474F, -13.4653F, 5.5497F);
		body.addChild(cube_r4);
		setRotationAngle(cube_r4, -0.9599F, -0.7854F, -3.1416F);
		cube_r4.texOffs(82, 103).addBox(0.0F, -3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r5 = new ModelRenderer(this);
		cube_r5.setPos(-0.0186F, -8.8743F, 0.0157F);
		body.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, -3.1416F);
		cube_r5.texOffs(0, 98).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 18.0F, 8.0F, 0.0F, false);

		cube_r6 = new ModelRenderer(this);
		cube_r6.setPos(5.5724F, -13.3743F, 5.6747F);
		body.addChild(cube_r6);
		setRotationAngle(cube_r6, -1.5708F, 0.0F, -1.8326F);
		cube_r6.texOffs(82, 103).addBox(0.0F, -2.75F, -3.25F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r7 = new ModelRenderer(this);
		cube_r7.setPos(4.9814F, -8.8743F, 0.0157F);
		body.addChild(cube_r7);
		setRotationAngle(cube_r7, 3.1416F, 0.0F, -3.1416F);
		cube_r7.texOffs(0, 48).addBox(0.75F, -9.0F, -7.5F, 0.0F, 18.0F, 16.0F, 0.0F, false);

		bodypart1 = new ModelRenderer(this);
		bodypart1.setPos(-0.1575F, -18.0965F, 0.1268F);
		body.addChild(bodypart1);
		bodypart1.texOffs(32, 26).addBox(-7.8611F, -36.7778F, 3.3889F, 16.0F, 37.0F, 0.0F, 0.0F, false);
		bodypart1.texOffs(96, 26).addBox(-7.8611F, -36.7778F, -3.6111F, 16.0F, 37.0F, 0.0F, 0.0F, false);
		bodypart1.texOffs(72, 98).addBox(-4.8611F, -35.7778F, -5.1111F, 10.0F, 0.0F, 10.0F, 0.0F, false);
		bodypart1.texOffs(33, 86).addBox(-2.8611F, -35.7778F, -3.1111F, 6.0F, 36.0F, 6.0F, 0.0F, false);

		cube_r8 = new ModelRenderer(this);
		cube_r8.setPos(-3.3611F, -17.7778F, -4.1111F);
		bodypart1.addChild(cube_r8);
		setRotationAngle(cube_r8, 3.1416F, -0.7854F, -3.1416F);
		cube_r8.texOffs(82, 103).addBox(0.0F, -4.5F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r9 = new ModelRenderer(this);
		cube_r9.setPos(-3.3611F, -17.7778F, -4.1111F);
		bodypart1.addChild(cube_r9);
		setRotationAngle(cube_r9, -3.0107F, 0.5236F, 2.138F);
		cube_r9.texOffs(82, 103).addBox(-1.25F, -3.75F, -2.25F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r10 = new ModelRenderer(this);
		cube_r10.setPos(-3.3611F, -18.2778F, -0.6111F);
		bodypart1.addChild(cube_r10);
		setRotationAngle(cube_r10, 3.1416F, 3.1416F, -3.1416F);
		cube_r10.texOffs(64, 11).addBox(0.0F, -18.5F, -6.5F, 0.0F, 37.0F, 16.0F, 0.0F, false);

		cube_r11 = new ModelRenderer(this);
		cube_r11.setPos(5.1389F, 9.2222F, -0.1111F);
		bodypart1.addChild(cube_r11);
		setRotationAngle(cube_r11, 3.1416F, 0.0F, -3.1416F);
		cube_r11.texOffs(0, 11).addBox(1.5F, -46.0F, -7.5F, 0.0F, 37.0F, 16.0F, 0.0F, false);

		bodypart2 = new ModelRenderer(this);
		bodypart2.setPos(0.1389F, -35.7778F, -0.1716F);
		bodypart1.addChild(bodypart2);
		bodypart2.texOffs(96, 0).addBox(-8.0F, -26.0F, -2.4395F, 16.0F, 26.0F, 0.0F, 0.0F, false);
		bodypart2.texOffs(58, 98).addBox(-2.0F, -25.0F, -1.9395F, 4.0F, 26.0F, 4.0F, 0.0F, false);
		bodypart2.texOffs(72, 98).addBox(-5.0F, -25.0F, -4.9395F, 10.0F, 0.0F, 10.0F, 0.0F, false);
		bodypart2.texOffs(32, 0).addBox(-8.0F, -26.0F, 2.5605F, 16.0F, 26.0F, 0.0F, 0.0F, false);

		cube_r12 = new ModelRenderer(this);
		cube_r12.setPos(-2.5F, -13.0F, -0.4395F);
		bodypart2.addChild(cube_r12);
		setRotationAngle(cube_r12, 0.0F, 1.5708F, 0.0F);
		cube_r12.texOffs(64, 0).addBox(-9.0F, -13.0F, 0.0F, 16.0F, 26.0F, 0.0F, 0.0F, false);

		cube_r13 = new ModelRenderer(this);
		cube_r13.setPos(3.2344F, -4.7481F, 2.7216F);
		bodypart2.addChild(cube_r13);
		setRotationAngle(cube_r13, 1.7453F, -2.8798F, 0.2182F);
		cube_r13.texOffs(82, 109).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);

		cube_r14 = new ModelRenderer(this);
		cube_r14.setPos(3.4127F, -4.3004F, 2.8549F);
		bodypart2.addChild(cube_r14);
		setRotationAngle(cube_r14, 2.8362F, 2.6616F, -0.5236F);
		cube_r14.texOffs(82, 109).addBox(-3.0F, -2.5F, 0.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);

		cube_r15 = new ModelRenderer(this);
		cube_r15.setPos(2.5F, -13.0F, -0.9395F);
		bodypart2.addChild(cube_r15);
		setRotationAngle(cube_r15, 0.0F, -1.5708F, 0.0F);
		cube_r15.texOffs(0, 0).addBox(-7.5F, -13.0F, 0.0F, 16.0F, 26.0F, 0.0F, 0.0F, false);

		leafs = new ModelRenderer(this);
		leafs.setPos(0.5F, -25.0F, 0.0605F);
		bodypart2.addChild(leafs);
		

		leaf1 = new ModelRenderer(this);
		leaf1.setPos(0.0F, 0.0F, 0.5F);
		leafs.addChild(leaf1);
		setRotationAngle(leaf1, 0.0F, 1.5708F, 0.0F);
		

		cube_r16 = new ModelRenderer(this);
		cube_r16.setPos(0.5F, 0.25F, -0.5F);
		leaf1.addChild(cube_r16);
		setRotationAngle(cube_r16, 0.2618F, 0.0F, 0.2618F);
		cube_r16.texOffs(76, 122).addBox(-5.8041F, 0.0117F, -0.1972F, 6.0F, 0.0F, 6.0F, 0.0F, false);

		leaf2 = new ModelRenderer(this);
		leaf2.setPos(-0.5F, 0.0F, -0.25F);
		leafs.addChild(leaf2);
		setRotationAngle(leaf2, 0.0F, -1.5708F, 0.0F);
		

		cube_r17 = new ModelRenderer(this);
		cube_r17.setPos(0.5F, 0.25F, -0.25F);
		leaf2.addChild(cube_r17);
		setRotationAngle(cube_r17, 0.2618F, 0.0F, 0.2618F);
		cube_r17.texOffs(76, 122).addBox(-6.0541F, 0.0117F, -0.1972F, 6.0F, 0.0F, 6.0F, 0.0F, false);

		leaf3 = new ModelRenderer(this);
		leaf3.setPos(-0.3748F, 0.0F, 0.179F);
		leafs.addChild(leaf3);
		

		cube_r18 = new ModelRenderer(this);
		cube_r18.setPos(-2.0F, -1.061F, 2.25F);
		leaf3.addChild(cube_r18);
		setRotationAngle(cube_r18, 0.2618F, 0.0F, 0.2618F);
		cube_r18.texOffs(77, 116).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);

		leaf4 = new ModelRenderer(this);
		leaf4.setPos(-0.2165F, 0.0F, -0.017F);
		leafs.addChild(leaf4);
		

		cube_r19 = new ModelRenderer(this);
		cube_r19.setPos(2.0F, -0.997F, -2.0F);
		leaf4.addChild(cube_r19);
		setRotationAngle(cube_r19, 0.2618F, 3.1416F, -0.2618F);
		cube_r19.texOffs(77, 116).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);
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