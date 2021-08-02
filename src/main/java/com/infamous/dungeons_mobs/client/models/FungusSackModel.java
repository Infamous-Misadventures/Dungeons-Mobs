package com.infamous.dungeons_mobs.client.models;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class FungusSackModel<T extends LivingEntity> extends AgeableModel<T> {
	private final ModelRenderer backpack;

	public FungusSackModel() {
		texWidth = 32;
		texHeight = 16;

		this.backpack = new ModelRenderer(this);
		this.backpack.setPos(0.0F, 4.3611F, 2.0556F);
		this.backpack.texOffs(0, 0).addBox(-4.0F, -4.1111F, -0.0556F, 8.0F, 9.0F, 7.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T wearer, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
		this.backpack.y = 4.3611F;
		if(wearer.isCrouching()){
			this.backpack.y += 3.0F;
		}
	}

	@Override
	protected Iterable<ModelRenderer> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
		return ImmutableList.of(this.backpack);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}