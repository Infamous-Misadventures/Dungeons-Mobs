package com.infamous.dungeons_mobs.client.models.jungle;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.entities.jungle.LeapleafEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class LeapleafModel1<T extends LeapleafEntity> extends SegmentedModel<T> {
	private final ModelRenderer leapleaf;
	private final ModelRenderer torax;
	private final ModelRenderer torax2;
	private final ModelRenderer head;
	private final ModelRenderer head_r1;
	private final ModelRenderer head_r2;
	private final ModelRenderer flower;
	private final ModelRenderer petal1;
	private final ModelRenderer petal1_r1;
	private final ModelRenderer petal1_r2;
	private final ModelRenderer petal2;
	private final ModelRenderer petal2_r1;
	private final ModelRenderer petal2_r2;
	private final ModelRenderer petal3;
	private final ModelRenderer petal3_r1;
	private final ModelRenderer petal3_r2;
	private final ModelRenderer petal4;
	private final ModelRenderer petal4_r1;
	private final ModelRenderer petal4_r2;
	private final ModelRenderer arms;
	private final ModelRenderer arm1;
	private final ModelRenderer hand1;
	private final ModelRenderer hand1_r1;
	private final ModelRenderer hand1_r2;
	private final ModelRenderer hand1_r3;
	private final ModelRenderer arm2;
	private final ModelRenderer hand2;
	private final ModelRenderer hand2_r1;
	private final ModelRenderer hand2_r2;
	private final ModelRenderer hand2_r3;
	private final ModelRenderer abdomen;
	private final ModelRenderer legs;
	private final ModelRenderer leg1;
	private final ModelRenderer leg2;

	public LeapleafModel1() {
		texWidth = 256;
		texHeight = 256;

		leapleaf = new ModelRenderer(this);
		leapleaf.setPos(0.0F, 4.0F, -5.0F);
		

		torax = new ModelRenderer(this);
		torax.setPos(0.0F, -1.0F, 5.0F);
		leapleaf.addChild(torax);
		

		torax2 = new ModelRenderer(this);
		torax2.setPos(0.0F, -8.0F, 0.0F);
		torax.addChild(torax2);
		torax2.texOffs(44, 44).addBox(-10.0F, -9.0F, -6.0F, 20.0F, 18.0F, 14.0F, 0.0F, false);
		torax2.texOffs(0, 0).addBox(-9.0F, -8.0F, -3.0F, 18.0F, 16.0F, 10.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -9.0F, 0.0F);
		torax2.addChild(head);
		

		head_r1 = new ModelRenderer(this);
		head_r1.setPos(0.0F, -0.656F, 0.9955F);
		head.addChild(head_r1);
		setRotationAngle(head_r1, 1.5708F, 0.0F, 1.5708F);
		head_r1.texOffs(0, 0).addBox(0.0F, -13.0F, -14.75F, 0.0F, 26.0F, 29.0F, 0.0F, false);

		head_r2 = new ModelRenderer(this);
		head_r2.setPos(0.0F, -0.906F, 0.4955F);
		head.addChild(head_r2);
		setRotationAngle(head_r2, 0.0F, 1.5708F, 1.5708F);
		head_r2.texOffs(56, 0).addBox(-13.5F, -13.5F, 0.0F, 27.0F, 27.0F, 0.0F, 0.0F, false);

		flower = new ModelRenderer(this);
		flower.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(flower);
		flower.texOffs(0, 55).addBox(-1.0F, -9.0F, -0.5045F, 2.0F, 9.0F, 2.0F, 0.0F, false);

		petal1 = new ModelRenderer(this);
		petal1.setPos(0.0F, -1.0F, -5.5F);
		flower.addChild(petal1);
		

		petal1_r1 = new ModelRenderer(this);
		petal1_r1.setPos(0.0F, 0.1675F, -11.7016F);
		petal1.addChild(petal1_r1);
		setRotationAngle(petal1_r1, 1.5708F, 0.0F, -1.5708F);
		petal1_r1.texOffs(58, 23).addBox(0.0F, -3.0F, -6.75F, 0.0F, 6.0F, 13.0F, 0.0F, true);

		petal1_r2 = new ModelRenderer(this);
		petal1_r2.setPos(0.0F, 0.1675F, -4.2016F);
		petal1.addChild(petal1_r2);
		setRotationAngle(petal1_r2, 1.5708F, 0.0F, -1.5708F);
		petal1_r2.texOffs(58, 14).addBox(0.0F, -4.5F, -6.75F, 0.0F, 9.0F, 13.0F, 0.0F, true);

		petal2 = new ModelRenderer(this);
		petal2.setPos(-6.5F, -1.0F, 0.0F);
		flower.addChild(petal2);
		

		petal2_r1 = new ModelRenderer(this);
		petal2_r1.setPos(-11.9132F, -0.286F, 0.4955F);
		petal2.addChild(petal2_r1);
		setRotationAngle(petal2_r1, 0.0F, 0.0F, -1.5708F);
		petal2_r1.texOffs(58, 23).addBox(0.0F, -3.0F, -6.75F, 0.0F, 6.0F, 13.0F, 0.0F, true);

		petal2_r2 = new ModelRenderer(this);
		petal2_r2.setPos(-4.4132F, -0.286F, 0.4955F);
		petal2.addChild(petal2_r2);
		setRotationAngle(petal2_r2, 0.0F, 0.0F, -1.5708F);
		petal2_r2.texOffs(58, 14).addBox(0.0F, -4.5F, -6.75F, 0.0F, 9.0F, 13.0F, 0.0F, true);

		petal3 = new ModelRenderer(this);
		petal3.setPos(6.5F, -1.0F, 1.0F);
		flower.addChild(petal3);
		

		petal3_r1 = new ModelRenderer(this);
		petal3_r1.setPos(11.9132F, -0.286F, -0.5045F);
		petal3.addChild(petal3_r1);
		setRotationAngle(petal3_r1, 0.0F, 0.0F, 1.5708F);
		petal3_r1.texOffs(58, 23).addBox(0.0F, -3.0F, -6.75F, 0.0F, 6.0F, 13.0F, 0.0F, false);

		petal3_r2 = new ModelRenderer(this);
		petal3_r2.setPos(4.4132F, -0.286F, -0.5045F);
		petal3.addChild(petal3_r2);
		setRotationAngle(petal3_r2, 0.0F, 0.0F, 1.5708F);
		petal3_r2.texOffs(58, 14).addBox(0.0F, -4.5F, -6.75F, 0.0F, 9.0F, 13.0F, 0.0F, false);

		petal4 = new ModelRenderer(this);
		petal4.setPos(0.0F, -1.0F, 6.5F);
		flower.addChild(petal4);
		

		petal4_r1 = new ModelRenderer(this);
		petal4_r1.setPos(0.0F, 0.1594F, 11.7054F);
		petal4.addChild(petal4_r1);
		setRotationAngle(petal4_r1, -1.5708F, 0.0F, -1.5708F);
		petal4_r1.texOffs(58, 23).addBox(0.0F, -3.0F, -6.75F, 0.0F, 6.0F, 13.0F, 0.0F, true);

		petal4_r2 = new ModelRenderer(this);
		petal4_r2.setPos(0.0F, 0.1594F, 4.2054F);
		petal4.addChild(petal4_r2);
		setRotationAngle(petal4_r2, -1.5708F, 0.0F, -1.5708F);
		petal4_r2.texOffs(58, 14).addBox(0.0F, -4.5F, -6.75F, 0.0F, 9.0F, 13.0F, 0.0F, true);

		arms = new ModelRenderer(this);
		arms.setPos(0.0F, -11.0F, 0.0F);
		torax.addChild(arms);
		

		arm1 = new ModelRenderer(this);
		arm1.setPos(-10.0F, 1.0F, 2.0F);
		arms.addChild(arm1);
		arm1.texOffs(33, 76).addBox(-11.0F, -4.0F, -6.0F, 11.0F, 18.0F, 11.0F, 0.0F, false);
		arm1.texOffs(68, 101).addBox(-10.0F, -3.0F, -5.0F, 9.0F, 16.0F, 9.0F, 0.0F, false);

		hand1 = new ModelRenderer(this);
		hand1.setPos(-10.0F, 14.0F, -2.0F);
		arm1.addChild(hand1);
		hand1.texOffs(77, 77).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 12.0F, 12.0F, 0.0F, true);
		hand1.texOffs(104, 104).addBox(-5.0F, 1.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
		hand1.texOffs(0, 26).addBox(-6.0F, 12.0F, -9.0F, 12.0F, 0.0F, 3.0F, 0.0F, true);

		hand1_r1 = new ModelRenderer(this);
		hand1_r1.setPos(0.0F, 12.0F, 7.5F);
		hand1.addChild(hand1_r1);
		setRotationAngle(hand1_r1, 3.1416F, 0.0F, 0.0F);
		hand1_r1.texOffs(0, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 0.0F, 3.0F, 0.0F, false);

		hand1_r2 = new ModelRenderer(this);
		hand1_r2.setPos(7.5F, 12.0F, 0.0F);
		hand1.addChild(hand1_r2);
		setRotationAngle(hand1_r2, 0.0F, -1.5708F, 0.0F);
		hand1_r2.texOffs(24, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 0.0F, 3.0F, 0.0F, true);

		hand1_r3 = new ModelRenderer(this);
		hand1_r3.setPos(-7.5F, 12.0F, 0.0F);
		hand1.addChild(hand1_r3);
		setRotationAngle(hand1_r3, 0.0F, 1.5708F, 0.0F);
		hand1_r3.texOffs(24, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 0.0F, 3.0F, 0.0F, true);

		arm2 = new ModelRenderer(this);
		arm2.setPos(10.0F, 0.0F, 2.0F);
		arms.addChild(arm2);
		arm2.texOffs(0, 55).addBox(0.0F, -3.0F, -6.0F, 11.0F, 18.0F, 11.0F, 0.0F, false);
		arm2.texOffs(0, 96).addBox(1.0F, -2.0F, -5.0F, 9.0F, 16.0F, 9.0F, 0.0F, false);

		hand2 = new ModelRenderer(this);
		hand2.setPos(10.0F, 15.0F, -2.0F);
		arm2.addChild(hand2);
		hand2.texOffs(77, 77).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 12.0F, 12.0F, 0.0F, false);
		hand2.texOffs(104, 104).addBox(-5.0F, 1.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
		hand2.texOffs(0, 26).addBox(-6.0F, 12.0F, -9.0F, 12.0F, 0.0F, 3.0F, 0.0F, false);

		hand2_r1 = new ModelRenderer(this);
		hand2_r1.setPos(0.0F, 12.0F, 7.5F);
		hand2.addChild(hand2_r1);
		setRotationAngle(hand2_r1, 3.1416F, 0.0F, 0.0F);
		hand2_r1.texOffs(0, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 0.0F, 3.0F, 0.0F, true);

		hand2_r2 = new ModelRenderer(this);
		hand2_r2.setPos(-7.5F, 12.0F, 0.0F);
		hand2.addChild(hand2_r2);
		setRotationAngle(hand2_r2, 0.0F, 1.5708F, 0.0F);
		hand2_r2.texOffs(24, 26).addBox(-6.0F, 0.0F, -1.5F, 12.0F, 0.0F, 3.0F, 0.0F, false);

		hand2_r3 = new ModelRenderer(this);
		hand2_r3.setPos(7.0F, 13.0F, -2.0F);
		hand2.addChild(hand2_r3);
		setRotationAngle(hand2_r3, 0.0F, -1.5708F, 0.0F);
		hand2_r3.texOffs(24, 26).addBox(-4.0F, -1.0F, -2.0F, 12.0F, 0.0F, 3.0F, 0.0F, false);

		abdomen = new ModelRenderer(this);
		abdomen.setPos(0.0F, -1.0F, 5.0F);
		leapleaf.addChild(abdomen);
		abdomen.texOffs(98, 27).addBox(-7.0F, 0.0F, -4.5F, 14.0F, 13.0F, 10.0F, 0.0F, false);
		abdomen.texOffs(110, 0).addBox(-6.0F, 1.0F, -3.0F, 12.0F, 11.0F, 8.0F, 0.0F, false);

		legs = new ModelRenderer(this);
		legs.setPos(0.0F, 12.0F, 0.0F);
		abdomen.addChild(legs);
		

		leg1 = new ModelRenderer(this);
		leg1.setPos(-5.0F, 0.0F, 0.0F);
		legs.addChild(leg1);
		leg1.texOffs(0, 122).addBox(-7.0F, 1.0F, -4.0F, 8.0F, 7.0F, 8.0F, 0.0F, false);
		leg1.texOffs(112, 50).addBox(-8.0F, 0.0F, -5.0F, 10.0F, 9.0F, 10.0F, 0.0F, false);

		leg2 = new ModelRenderer(this);
		leg2.setPos(5.0F, 0.0F, 0.0F);
		legs.addChild(leg2);
		leg2.texOffs(26, 111).addBox(-2.0F, 0.0F, -5.0F, 10.0F, 9.0F, 10.0F, 0.0F, false);
		leg2.texOffs(113, 69).addBox(-1.0F, 1.0F, -4.0F, 8.0F, 7.0F, 8.0F, 0.0F, false);
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return null;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		leapleaf.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}