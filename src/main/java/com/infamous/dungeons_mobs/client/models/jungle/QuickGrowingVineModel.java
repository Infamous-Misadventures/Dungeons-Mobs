package com.infamous.dungeons_mobs.client.models.jungle;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class QuickGrowingVineModel<T extends QuickGrowingVineEntity> extends EntityModel<T> {
	private final ModelRenderer body;

	public QuickGrowingVineModel() {
		textureWidth = 128;
		textureHeight = 128;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 15.0F, 0.0F);
		body.setTextureOffset(0, 91).addBox(-6.0F, -17.5F, -5.0F, 11.0F, 26.0F, 11.0F, 0.0F, true);
		body.setTextureOffset(92, 93).addBox(-5.0F, -16.0F, -3.5F, 9.0F, 24.0F, 8.0F, 0.0F, true);

		ModelRenderer middle = new ModelRenderer(this);
		middle.setRotationPoint(0.0F, -20.0F, -4.0F);
		body.addChild(middle);
		middle.setTextureOffset(0, 101).addBox(-5.0F, -15.0F, 0.0F, 9.0F, 18.0F, 9.0F, 0.0F, false);
		middle.setTextureOffset(98, 100).addBox(-4.0F, -14.0F, 1.0F, 7.0F, 16.0F, 7.0F, 0.0F, false);

		ModelRenderer top = new ModelRenderer(this);
		top.setRotationPoint(-1.3333F, -15.1239F, 8.4115F);
		middle.addChild(top);
		top.setTextureOffset(0, 109).addBox(-2.6667F, -11.8761F, -7.4115F, 7.0F, 12.0F, 7.0F, 0.0F, false);
		top.setTextureOffset(100, 96).addBox(-1.6667F, -10.8761F, -6.4115F, 5.0F, 10.0F, 5.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}