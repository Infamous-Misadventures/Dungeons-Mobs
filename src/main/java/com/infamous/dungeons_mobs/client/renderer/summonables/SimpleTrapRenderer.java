package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.client.models.summonables.SimpleTrapModel;
import com.infamous.dungeons_mobs.entities.summonables.SimpleTrapEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SimpleTrapRenderer extends GeoProjectilesRenderer<SimpleTrapEntity> {
    public SimpleTrapRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SimpleTrapModel<SimpleTrapEntity>());
    }

    @Override
    public void renderEarly(SimpleTrapEntity animatable, PoseStack stackIn, float partialTicks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
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
    public RenderType getRenderType(SimpleTrapEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}