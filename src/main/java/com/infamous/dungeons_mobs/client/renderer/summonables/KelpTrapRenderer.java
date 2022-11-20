package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.client.models.summonables.KelpTrapModel;
import com.infamous.dungeons_mobs.entities.summonables.KelpTrapEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class KelpTrapRenderer extends GeoProjectilesRenderer<KelpTrapEntity> {
   public KelpTrapRenderer(EntityRendererProvider.Context renderManager) {
      super(renderManager, new KelpTrapModel<KelpTrapEntity>());
   }
   
	@Override
	public void renderEarly(KelpTrapEntity animatable, PoseStack stackIn, float partialTicks,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		float scaleFactor = 2.0F;
		stackIn.scale(scaleFactor, scaleFactor, scaleFactor);
	}

   @Override
   public RenderType getRenderType(KelpTrapEntity animatable, float partialTicks, PoseStack stack,
                                   MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                   ResourceLocation textureLocation) {
      return RenderType.entityTranslucent(getTextureLocation(animatable));
   }
}