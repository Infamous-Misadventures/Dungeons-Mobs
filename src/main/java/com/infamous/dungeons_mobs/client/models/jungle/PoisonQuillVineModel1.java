package com.infamous.dungeons_mobs.client.models.jungle;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PoisonQuillVineModel1<T extends PoisonQuillVineEntity> extends EntityModel<T> {
	private final ModelRenderer bone;
	private final ModelRenderer body;
	private final ModelRenderer stem1;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer cube_r7;
	private final ModelRenderer bodypart1;
	private final ModelRenderer stem2;
	private final ModelRenderer cube_r8;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;
	private final ModelRenderer bodypart2;
	private final ModelRenderer stem3;
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
	private final ModelRenderer head;
	private final ModelRenderer cube_r20;
	private final ModelRenderer cube_r21;
	private final ModelRenderer cube_r22;
	private final ModelRenderer cube_r23;
	private final ModelRenderer petals;
	private final ModelRenderer petal1;
	private final ModelRenderer cube_r24;
	private final ModelRenderer petal2;
	private final ModelRenderer cube_r25;
	private final ModelRenderer petal3;
	private final ModelRenderer cube_r26;
	private final ModelRenderer petal4;
	private final ModelRenderer cube_r27;

	public PoisonQuillVineModel1() {
		texWidth = 168;
		texHeight = 168;

		bone = new ModelRenderer(this);
		bone.setPos(0.2686F, 23.8743F, -0.0157F);
		

		body = new ModelRenderer(this);
		body.setPos(0.0F, 0.0F, 0.0F);
		bone.addChild(body);
		

		stem1 = new ModelRenderer(this);
		stem1.setPos(0.0F, 0.0F, 0.0F);
		body.addChild(stem1);
		stem1.texOffs(96, 60).addBox(-8.0186F, -21.8743F, -4.2343F, 16.0F, 22.0F, 0.0F, 0.0F, false);
		stem1.texOffs(72, 98).addBox(-5.0186F, -22.1243F, -4.9843F, 10.0F, 0.0F, 10.0F, 0.0F, false);
		stem1.texOffs(32, 60).addBox(-8.0186F, -21.8743F, 4.2657F, 16.0F, 22.0F, 0.0F, 0.0F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(5.4474F, -13.4653F, 5.5497F);
		stem1.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.9599F, -0.7854F, -3.1416F);
		cube_r1.texOffs(82, 103).addBox(0.0F, -3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(-7.0698F, -10.8952F, -5.2921F);
		stem1.addChild(cube_r2);
		setRotationAngle(cube_r2, 3.1416F, 0.0873F, 1.5708F);
		cube_r2.texOffs(82, 103).addBox(1.0F, -1.5F, -3.25F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setPos(-7.0698F, -10.8952F, -5.2921F);
		stem1.addChild(cube_r3);
		setRotationAngle(cube_r3, 2.2689F, 0.7854F, 0.0F);
		cube_r3.texOffs(82, 103).addBox(1.0F, -2.75F, -4.75F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setPos(-0.0186F, -8.8743F, 0.0157F);
		stem1.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.0F, -3.1416F);
		cube_r4.texOffs(0, 103).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 22.0F, 8.0F, 0.0F, false);

		cube_r5 = new ModelRenderer(this);
		cube_r5.setPos(5.5724F, -13.3743F, 5.6747F);
		stem1.addChild(cube_r5);
		setRotationAngle(cube_r5, -1.5708F, 0.0F, -1.8326F);
		cube_r5.texOffs(82, 103).addBox(0.0F, -2.75F, -3.25F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r6 = new ModelRenderer(this);
		cube_r6.setPos(4.9814F, -8.8743F, 0.0157F);
		stem1.addChild(cube_r6);
		setRotationAngle(cube_r6, 3.1416F, 0.0F, -3.1416F);
		cube_r6.texOffs(0, 44).addBox(0.75F, -13.0F, -7.5F, 0.0F, 22.0F, 16.0F, 0.0F, false);

		cube_r7 = new ModelRenderer(this);
		cube_r7.setPos(-4.2686F, -8.8743F, -0.4843F);
		stem1.addChild(cube_r7);
		setRotationAngle(cube_r7, 3.1416F, 3.1416F, -3.1416F);
		cube_r7.texOffs(64, 44).addBox(0.0F, -13.0F, -6.5F, 0.0F, 22.0F, 16.0F, 0.0F, false);

		bodypart1 = new ModelRenderer(this);
		bodypart1.setPos(-0.1575F, -21.8465F, 0.1268F);
		body.addChild(bodypart1);
		setRotationAngle(bodypart1, 0.2182F, 0.0F, 0.0F);
		

		stem2 = new ModelRenderer(this);
		stem2.setPos(0.0F, 0.0F, 0.0F);
		bodypart1.addChild(stem2);
		stem2.texOffs(96, 26).addBox(-7.8611F, -33.0278F, -3.6111F, 16.0F, 33.0F, 0.0F, 0.0F, false);
		stem2.texOffs(32, 26).addBox(-7.8611F, -33.0278F, 3.3889F, 16.0F, 33.0F, 0.0F, 0.0F, false);
		stem2.texOffs(72, 98).addBox(-4.8611F, -32.0278F, -5.1111F, 10.0F, 0.0F, 10.0F, 0.0F, false);
		stem2.texOffs(33, 91).addBox(-2.8611F, -32.0278F, -3.1111F, 6.0F, 32.0F, 6.0F, 0.0F, false);

		cube_r8 = new ModelRenderer(this);
		cube_r8.setPos(-3.3611F, -14.5278F, -0.6111F);
		stem2.addChild(cube_r8);
		setRotationAngle(cube_r8, 3.1416F, 3.1416F, -3.1416F);
		cube_r8.texOffs(64, 11).addBox(0.0F, -18.5F, -6.5F, 0.0F, 33.0F, 16.0F, 0.0F, false);

		cube_r9 = new ModelRenderer(this);
		cube_r9.setPos(-3.3611F, -14.0278F, -4.1111F);
		stem2.addChild(cube_r9);
		setRotationAngle(cube_r9, -3.0107F, 0.5236F, 2.138F);
		cube_r9.texOffs(82, 103).addBox(-1.25F, -3.75F, -2.25F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		cube_r10 = new ModelRenderer(this);
		cube_r10.setPos(5.1389F, 12.9722F, -0.1111F);
		stem2.addChild(cube_r10);
		setRotationAngle(cube_r10, 3.1416F, 0.0F, -3.1416F);
		cube_r10.texOffs(0, 11).addBox(1.5F, -46.0F, -7.5F, 0.0F, 33.0F, 16.0F, 0.0F, false);

		cube_r11 = new ModelRenderer(this);
		cube_r11.setPos(-3.3611F, -14.0278F, -4.1111F);
		stem2.addChild(cube_r11);
		setRotationAngle(cube_r11, 3.1416F, -0.7854F, -3.1416F);
		cube_r11.texOffs(82, 103).addBox(0.0F, -4.5F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		bodypart2 = new ModelRenderer(this);
		bodypart2.setPos(0.1389F, -32.0278F, -0.1716F);
		bodypart1.addChild(bodypart2);
		setRotationAngle(bodypart2, 0.3927F, 0.0F, 0.0F);
		

		stem3 = new ModelRenderer(this);
		stem3.setPos(0.0F, 0.0F, 0.0F);
		bodypart2.addChild(stem3);
		stem3.texOffs(96, 0).addBox(-8.0F, -26.0F, -2.4395F, 16.0F, 26.0F, 0.0F, 0.0F, false);
		stem3.texOffs(58, 98).addBox(-2.0F, -25.0F, -1.9395F, 4.0F, 26.0F, 4.0F, 0.0F, false);
		stem3.texOffs(64, 98).addBox(-5.0F, -25.0F, -4.9395F, 10.0F, 0.0F, 10.0F, 0.0F, false);
		stem3.texOffs(32, 0).addBox(-8.0F, -26.0F, 2.5605F, 16.0F, 26.0F, 0.0F, 0.0F, false);

		cube_r12 = new ModelRenderer(this);
		cube_r12.setPos(-2.5F, -13.0F, -0.4395F);
		stem3.addChild(cube_r12);
		setRotationAngle(cube_r12, 0.0F, 1.5708F, 0.0F);
		cube_r12.texOffs(64, 0).addBox(-9.0F, -13.0F, 0.0F, 16.0F, 26.0F, 0.0F, 0.0F, false);

		cube_r13 = new ModelRenderer(this);
		cube_r13.setPos(3.2344F, -4.7481F, 2.7216F);
		stem3.addChild(cube_r13);
		setRotationAngle(cube_r13, 1.7453F, -2.8798F, 0.2182F);
		cube_r13.texOffs(82, 109).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);

		cube_r14 = new ModelRenderer(this);
		cube_r14.setPos(3.4127F, -4.3004F, 2.8549F);
		stem3.addChild(cube_r14);
		setRotationAngle(cube_r14, 2.8362F, 2.6616F, -0.5236F);
		cube_r14.texOffs(82, 109).addBox(-3.0F, -2.5F, 0.0F, 6.0F, 6.0F, 0.0F, 0.0F, false);

		cube_r15 = new ModelRenderer(this);
		cube_r15.setPos(2.5F, -13.0F, -0.9395F);
		stem3.addChild(cube_r15);
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
		setRotationAngle(cube_r16, 0.0436F, 0.0F, 0.0436F);
		cube_r16.texOffs(76, 122).addBox(-5.8041F, 0.0117F, -0.1972F, 6.0F, 0.0F, 6.0F, 0.0F, false);

		leaf2 = new ModelRenderer(this);
		leaf2.setPos(-0.5F, 0.0F, -0.25F);
		leafs.addChild(leaf2);
		setRotationAngle(leaf2, 0.0F, -1.5708F, 0.0F);
		

		cube_r17 = new ModelRenderer(this);
		cube_r17.setPos(0.5F, 0.25F, -0.25F);
		leaf2.addChild(cube_r17);
		setRotationAngle(cube_r17, 0.0436F, 0.0F, 0.0873F);
		cube_r17.texOffs(76, 122).addBox(-6.0541F, 0.0117F, -0.1972F, 6.0F, 0.0F, 6.0F, 0.0F, false);

		leaf3 = new ModelRenderer(this);
		leaf3.setPos(-0.3748F, 0.0F, 0.179F);
		leafs.addChild(leaf3);
		setRotationAngle(leaf3, -0.2182F, 0.0F, -0.2182F);
		

		cube_r18 = new ModelRenderer(this);
		cube_r18.setPos(-2.0F, -1.061F, 2.25F);
		leaf3.addChild(cube_r18);
		setRotationAngle(cube_r18, 0.2618F, 0.0F, 0.2618F);
		cube_r18.texOffs(77, 116).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);

		leaf4 = new ModelRenderer(this);
		leaf4.setPos(-0.2165F, 0.0F, -0.017F);
		leafs.addChild(leaf4);
		setRotationAngle(leaf4, 0.2182F, 0.0F, 0.2182F);
		

		cube_r19 = new ModelRenderer(this);
		cube_r19.setPos(2.0F, -0.997F, -2.0F);
		leaf4.addChild(cube_r19);
		setRotationAngle(cube_r19, 0.2618F, 3.1416F, -0.2618F);
		cube_r19.texOffs(77, 116).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 0.0F, 5.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -25.0F, -4.0F);
		bodypart2.addChild(head);
		setRotationAngle(head, 1.2217F, 0.0F, 0.0F);
		head.texOffs(3, 136).addBox(-15.5F, 0.0F, -14.9395F, 31.0F, 0.0F, 30.0F, 0.0F, false);
		head.texOffs(132, 87).addBox(-1.0F, -8.0F, -1.4395F, 2.0F, 8.0F, 2.0F, 0.0F, false);

		cube_r20 = new ModelRenderer(this);
		cube_r20.setPos(-2.25F, 0.0F, -0.4395F);
		head.addChild(cube_r20);
		setRotationAngle(cube_r20, -0.1309F, 1.5708F, 0.0F);
		cube_r20.texOffs(134, 120).addBox(-2.5F, -13.0F, 0.0F, 5.0F, 13.0F, 0.0F, 0.0F, false);

		cube_r21 = new ModelRenderer(this);
		cube_r21.setPos(2.5F, 0.0F, -0.4395F);
		head.addChild(cube_r21);
		setRotationAngle(cube_r21, 0.1309F, 1.5708F, 0.0F);
		cube_r21.texOffs(134, 120).addBox(-2.5F, -13.0F, 0.0F, 5.0F, 13.0F, 0.0F, 0.0F, false);

		cube_r22 = new ModelRenderer(this);
		cube_r22.setPos(0.0F, 0.0F, -2.4395F);
		head.addChild(cube_r22);
		setRotationAngle(cube_r22, -0.1309F, 0.0F, 0.0F);
		cube_r22.texOffs(134, 120).addBox(-2.5F, -13.0F, -0.25F, 5.0F, 13.0F, 0.0F, 0.0F, false);

		cube_r23 = new ModelRenderer(this);
		cube_r23.setPos(0.0F, 0.0F, 1.5605F);
		head.addChild(cube_r23);
		setRotationAngle(cube_r23, 0.1309F, 0.0F, 0.0F);
		cube_r23.texOffs(134, 120).addBox(-2.5F, -13.0F, 0.25F, 5.0F, 13.0F, 0.0F, 0.0F, false);

		petals = new ModelRenderer(this);
		petals.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(petals);
		

		petal1 = new ModelRenderer(this);
		petal1.setPos(0.0F, 0.0F, 7.0605F);
		petals.addChild(petal1);
		

		cube_r24 = new ModelRenderer(this);
		cube_r24.setPos(0.0F, 0.0F, 0.0F);
		petal1.addChild(cube_r24);
		setRotationAngle(cube_r24, 0.3491F, 0.0F, 0.0F);
		cube_r24.texOffs(61, 134).addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.0F, 31.0F, 0.0F, false);

		petal2 = new ModelRenderer(this);
		petal2.setPos(7.5F, 0.0F, -0.4395F);
		petals.addChild(petal2);
		

		cube_r25 = new ModelRenderer(this);
		cube_r25.setPos(0.0F, 0.0F, 0.0F);
		petal2.addChild(cube_r25);
		setRotationAngle(cube_r25, 0.3491F, 1.5708F, 0.0F);
		cube_r25.texOffs(61, 134).addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.0F, 31.0F, 0.0F, false);

		petal3 = new ModelRenderer(this);
		petal3.setPos(0.0F, 0.0F, -7.9395F);
		petals.addChild(petal3);
		

		cube_r26 = new ModelRenderer(this);
		cube_r26.setPos(0.0F, 0.0F, 0.0F);
		petal3.addChild(cube_r26);
		setRotationAngle(cube_r26, 0.3491F, 3.1416F, 0.0F);
		cube_r26.texOffs(61, 134).addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.0F, 31.0F, 0.0F, false);

		petal4 = new ModelRenderer(this);
		petal4.setPos(-7.5F, 0.0F, -0.4395F);
		petals.addChild(petal4);
		

		cube_r27 = new ModelRenderer(this);
		cube_r27.setPos(0.0F, 0.0F, 0.0F);
		petal4.addChild(cube_r27);
		setRotationAngle(cube_r27, 0.3491F, -1.5708F, 0.0F);
		cube_r27.texOffs(61, 134).addBox(-7.5F, 0.0F, 0.0F, 15.0F, 0.0F, 31.0F, 0.0F, false);
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