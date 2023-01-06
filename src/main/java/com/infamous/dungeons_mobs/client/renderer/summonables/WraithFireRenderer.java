package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.client.models.summonables.WraithFireModel;
import com.infamous.dungeons_mobs.entities.summonables.WraithFireEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class WraithFireRenderer extends GeoProjectilesRenderer<WraithFireEntity> {
    public WraithFireRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WraithFireModel());
    }

    @Override
    public void renderEarly(WraithFireEntity animatable, PoseStack stackIn, float partialTicks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float alpha) {
        float scaleFactor = 1.0F;
        stackIn.scale(scaleFactor, scaleFactor, scaleFactor);
    }

    @Override
    protected int getBlockLightLevel(WraithFireEntity p_225624_1_, BlockPos p_225624_2_) {
        return 15;
    }

    @Override
    public RenderType getRenderType(WraithFireEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}