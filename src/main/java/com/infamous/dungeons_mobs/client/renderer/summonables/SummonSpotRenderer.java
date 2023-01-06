package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.client.models.summonables.SummonSpotModel;
import com.infamous.dungeons_mobs.entities.summonables.SummonSpotEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class SummonSpotRenderer extends GeoProjectilesRenderer<SummonSpotEntity> {
    public SummonSpotRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SummonSpotModel<SummonSpotEntity>());
    }

    @Override
    public void renderEarly(SummonSpotEntity animatable, PoseStack stackIn, float partialTicks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
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
    public RenderType getRenderType(SummonSpotEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}