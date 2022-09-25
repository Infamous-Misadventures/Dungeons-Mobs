package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.client.models.summonables.SimpleTrapModel;
import com.infamous.dungeons_mobs.entities.summonables.SimpleTrapEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SimpleTrapRenderer extends GeoProjectilesRenderer<SimpleTrapEntity> {
   public SimpleTrapRenderer(EntityRendererManager renderManager) {
      super(renderManager, new SimpleTrapModel<SimpleTrapEntity>());
   }
   
	@Override
	public void renderEarly(SimpleTrapEntity animatable, MatrixStack stackIn, float partialTicks,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		float scaleFactor = 2.0F;
		if (animatable.lifeTime <= 1) {
			scaleFactor = 0.0F;
		} else {
			scaleFactor = 2.0F;
		}
		stackIn.scale(scaleFactor, scaleFactor, scaleFactor);
	}

   @Override
   public RenderType getRenderType(SimpleTrapEntity animatable, float partialTicks, MatrixStack stack,
                                   IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                   ResourceLocation textureLocation) {
      return RenderType.entityTranslucent(getTextureLocation(animatable));
   }
}