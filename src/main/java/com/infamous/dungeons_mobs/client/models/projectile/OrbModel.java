package com.infamous.dungeons_mobs.client.models.projectile;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.entities.projectiles.AbstractOrbEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class OrbModel<T extends AbstractOrbEntity> extends EntityModel<T> {
	private final ModelRenderer bone;
	private final ModelRenderer cube_r1;

	public OrbModel() {
		texWidth = 14;
		texHeight = 14;

		bone = new ModelRenderer(this);
		bone.setPos(0.0F, 22.5F, 0.0F);
		

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(0.0F, 0.0F, 0.0F);
		bone.addChild(cube_r1);
		setRotationAngle(cube_r1, 1.5708F, 0.0F, 0.0F);
		cube_r1.texOffs(1, 4).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, false);
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