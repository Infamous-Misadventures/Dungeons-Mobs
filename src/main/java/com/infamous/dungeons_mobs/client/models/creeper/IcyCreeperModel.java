package com.infamous.dungeons_mobs.client.models.creeper;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

// Made with Blockbench 4.3.1
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class IcyCreeperModel<T extends Entity> extends SegmentedModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer leg1;
	private final ModelRenderer leg1_r1;
	private final ModelRenderer leg2;
	private final ModelRenderer leg2_r1;
	private final ModelRenderer leg3;
	private final ModelRenderer leg4;

	public IcyCreeperModel() {
		texWidth = 64;
		texHeight = 32;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 18.0F, 0.0F);
		body.texOffs(16, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
		body.texOffs(40, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.25F, true);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -12.0F, 0.0F);
		body.addChild(head);
		head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		head.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.25F, true);

		leg1 = new ModelRenderer(this);
		leg1.setPos(-2.0F, 18.0F, 2.0F);
		leg1.texOffs(0, 16).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		leg1_r1 = new ModelRenderer(this);
		leg1_r1.setPos(0.0F, 3.0F, 2.0F);
		leg1.addChild(leg1_r1);
		setRotationAngle(leg1_r1, 0.0F, 3.1416F, 0.0F);
		leg1_r1.texOffs(48, 0).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.25F, false);

		leg2 = new ModelRenderer(this);
		leg2.setPos(2.0F, 18.0F, 2.0F);
		leg2.texOffs(0, 16).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		leg2_r1 = new ModelRenderer(this);
		leg2_r1.setPos(0.0F, 2.0F, 2.0F);
		leg2.addChild(leg2_r1);
		setRotationAngle(leg2_r1, 0.0F, 3.1416F, 0.0F);
		leg2_r1.texOffs(48, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.25F, true);

		leg3 = new ModelRenderer(this);
		leg3.setPos(-2.0F, 18.0F, -2.0F);
		leg3.texOffs(0, 16).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		leg3.texOffs(48, 0).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 4.0F, 4.0F, 0.25F, true);

		leg4 = new ModelRenderer(this);
		leg4.setPos(2.0F, 18.0F, -2.0F);
		leg4.texOffs(0, 16).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		leg4.texOffs(48, 0).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 3.0F, 4.0F, 0.25F, false);
	}
	
	   public Iterable<ModelRenderer> parts() {
		      return ImmutableList.of(this.head, this.body, this.leg1, this.leg2, this.leg3, this.leg4);
		   }

		   public void setupAnim(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
		      this.head.yRot = p_225597_5_ * ((float)Math.PI / 180F);
		      this.head.xRot = p_225597_6_ * ((float)Math.PI / 180F);
		      this.leg1.xRot = MathHelper.cos(p_225597_2_ * 0.6662F) * 1.4F * p_225597_3_;
		      this.leg2.xRot = MathHelper.cos(p_225597_2_ * 0.6662F + (float)Math.PI) * 1.4F * p_225597_3_;
		      this.leg3.xRot = MathHelper.cos(p_225597_2_ * 0.6662F + (float)Math.PI) * 1.4F * p_225597_3_;
		      this.leg4.xRot = MathHelper.cos(p_225597_2_ * 0.6662F) * 1.4F * p_225597_3_;
		   }

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		leg1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		leg2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		leg3.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		leg4.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}