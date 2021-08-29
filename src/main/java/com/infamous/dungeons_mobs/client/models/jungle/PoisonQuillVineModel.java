package com.infamous.dungeons_mobs.client.models.jungle;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PoisonQuillVineModel<T extends PoisonQuillVineEntity> extends EntityModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer neck;
	private final ModelRenderer flowerHead;
	private final ModelRenderer petals;
	private final ModelRenderer body_r1;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;

	public PoisonQuillVineModel() {
		texWidth = 128;
		texHeight = 128;

		body = new ModelRenderer(this);
		body.setPos(1.0F, 15.0F, 0.0F);
		body.texOffs(0, 87).addBox(-6.0F, -20.5F, -5.0F, 11.0F, 29.0F, 11.0F, 0.0F, true);
		body.texOffs(92, 90).addBox(-5.0F, -20.0F, -3.5F, 9.0F, 28.0F, 8.0F, 0.0F, true);

		head = new ModelRenderer(this);
		head.setPos(1.0F, -4.0F, -4.0F);
		setRotationAngle(head, 0.4363F, 0.0F, 0.0F);
		head.texOffs(0, 97).addBox(-5.0F, -18.0F, 0.0F, 9.0F, 21.0F, 9.0F, 0.0F, false);
		head.texOffs(98, 92).addBox(-3.0F, -18.0F, 1.0F, 5.0F, 20.0F, 7.0F, 0.0F, false);

		neck = new ModelRenderer(this);
		neck.setPos(-1.3333F, -18.1239F, 8.4115F);
		head.addChild(neck);
		setRotationAngle(neck, 0.7854F, 0.0F, 0.0F);
		neck.texOffs(3, 106).addBox(-2.6667F, -11.8761F, -7.4115F, 7.0F, 12.0F, 7.0F, 0.0F, false);
		neck.texOffs(102, 101).addBox(-1.6667F, -11.8761F, -6.4115F, 5.0F, 12.0F, 5.0F, 0.0F, false);

		flowerHead = new ModelRenderer(this);
		flowerHead.setPos(0.4583F, -12.4097F, -2.0482F);
		neck.addChild(flowerHead);
		setRotationAngle(flowerHead, 0.3491F, 0.0F, 0.0F);
		flowerHead.texOffs(110, 42).addBox(-1.125F, -9.4664F, -3.3634F, 3.0F, 8.0F, 3.0F, 0.0F, false);
		flowerHead.texOffs(4, 2).addBox(-14.125F, 0.5336F, -19.3634F, 29.0F, 1.0F, 31.0F, 0.0F, false);
		flowerHead.texOffs(105, 1).addBox(-2.125F, -14.4664F, -4.3634F, 5.0F, 5.0F, 5.0F, 0.0F, false);

		petals = new ModelRenderer(this);
		petals.setPos(0.875F, 88.5336F, -2.3634F);
		flowerHead.addChild(petals);

		body_r1 = new ModelRenderer(this);
		body_r1.setPos(-0.5F, -88.9449F, 0.5815F);
		petals.addChild(body_r1);
		setRotationAngle(body_r1, 1.5708F, 0.0F, 0.0F);
		body_r1.texOffs(89, 59).addBox(-7.5F, -8.0F, -1.0F, 15.0F, 16.0F, 2.0F, 0.0F, true);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(-6.5F, -88.0F, 1.5F);
		petals.addChild(cube_r1);
		setRotationAngle(cube_r1, 1.8762F, -1.5708F, -3.1416F);
		cube_r1.texOffs(3, 51).addBox(-8.5F, -2.0F, -28.5F, 15.0F, 0.0F, 27.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(5.5F, -89.0F, -1.5F);
		petals.addChild(cube_r2);
		setRotationAngle(cube_r2, 1.8762F, 1.5708F, -3.1416F);
		cube_r2.texOffs(3, 52).addBox(-9.5F, -1.5176F, -27.4319F, 15.0F, 0.0F, 27.0F, 0.0F, false);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setPos(-0.5F, -93.0F, -9.5F);
		petals.addChild(cube_r3);
		setRotationAngle(cube_r3, 1.8762F, 0.0F, -3.1416F);
		cube_r3.texOffs(3, 52).addBox(-7.5F, 3.0F, -24.5F, 15.0F, 0.0F, 27.0F, 0.0F, false);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setPos(0.5F, -89.0F, 7.5F);
		petals.addChild(cube_r4);
		setRotationAngle(cube_r4, 1.8762F, 3.1416F, -3.1416F);
		cube_r4.texOffs(3, 52).addBox(-8.5F, -1.0F, -27.5F, 15.0F, 0.0F, 27.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
		head.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}