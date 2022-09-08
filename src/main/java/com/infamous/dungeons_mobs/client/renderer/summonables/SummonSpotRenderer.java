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
import net.minecraft.world.LightType;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SummonSpotRenderer extends GeoProjectilesRenderer<SummonSpotEntity> {
   public SummonSpotRenderer(EntityRendererManager renderManager) {
      super(renderManager, new SummonSpotModel<SummonSpotEntity>());
   }
   
	@Override
	protected int getBlockLightLevel(SummonSpotEntity p_114496_, BlockPos p_114497_) {
		return p_114496_.level.getBrightness(LightType.BLOCK, p_114497_) > 15
				? p_114496_.level.getBrightness(LightType.BLOCK, p_114497_)
				: 15;
	}

   @Override
   public RenderType getRenderType(SummonSpotEntity animatable, float partialTicks, MatrixStack stack,
                                   IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                   ResourceLocation textureLocation) {
      return RenderType.entityTranslucent(getTextureLocation(animatable));
   }
}