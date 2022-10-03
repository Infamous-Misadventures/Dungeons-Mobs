package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.client.models.summonables.SummonSpotModel;
import com.infamous.dungeons_mobs.entities.summonables.SummonSpotEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SummonSpotRenderer extends GeoProjectilesRenderer<SummonSpotEntity> {
   public SummonSpotRenderer(EntityRendererManager renderManager) {
      super(renderManager, new SummonSpotModel<SummonSpotEntity>());
   }
   
	@Override
	public void renderEarly(SummonSpotEntity animatable, MatrixStack stackIn, float partialTicks,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		if (animatable.lifeTime <= 1) {
			float scaleFactor = 0.0F;
			stackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		} else {
			
		}
	}
	
	@Override
		protected int getBlockLightLevel(SummonSpotEntity p_225624_1_, BlockPos p_225624_2_) {
			return 15;
		}

   @Override
   public RenderType getRenderType(SummonSpotEntity animatable, float partialTicks, MatrixStack stack,
                                   IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                   ResourceLocation textureLocation) {
      return RenderType.entityTranslucent(getTextureLocation(animatable));
   }
}