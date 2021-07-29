package com.infamous.dungeons_mobs.client.models.armor;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkeletonVanguardShieldModel extends Model {
	private final ModelRenderer plate;

	public SkeletonVanguardShieldModel() {
		super(RenderType::entitySolid);
		texWidth = 32;
		texHeight = 32;

		plate = new ModelRenderer(this);
		//plate.setRotationPoint(1.0F, -3.0F, -11.0F);

		// tops
		plate.texOffs(0, 26).addBox(-8.0F + 5.0F, -1.0F - 12.0F, 0.0F - 2.0F, 6.0F, 2.0F, 1.0F, 0.0F, false);

		//sides
		plate.texOffs(24, 26).addBox(-13.0F + 5.0F, 5.0F - 12.0F, 0.0F - 2.0F, 3.0F, 5.0F, 1.0F, 0.0F, false);
		plate.texOffs(16, 26).addBox(0.0F + 5.0F, 5.0F - 12.0F, 0.0F - 2.0F, 3.0F, 5.0F, 1.0F, 0.0F, false);

		// base
		plate.texOffs(0, 0).addBox(-10.0F + 5.0F, 6.0F - 12.0F, 0.0F - 2.0F, 10.0F, 13.0F, 1.0F, 0.0F, false);
		plate.texOffs(0, 17).addBox(-11.0F + 5.0F, 1.0F - 12.0F, 0.0F - 2.0F, 12.0F, 5.0F, 1.0F, 0.0F, false);
	}

	public ModelRenderer getPlate() {
		return this.plate;
	}

	/*
	public ModelRenderer getHandle() {
		return this.handle;
	}

	 */

	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.plate.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		/*
		this.handle.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

		 */
	}
}