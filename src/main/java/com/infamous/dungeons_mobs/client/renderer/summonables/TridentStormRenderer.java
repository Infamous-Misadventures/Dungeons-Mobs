package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.client.models.summonables.TridentStormModel;
import com.infamous.dungeons_mobs.entities.summonables.TridentStormEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class TridentStormRenderer extends GeoProjectilesRenderer<TridentStormEntity> {
    public TridentStormRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TridentStormModel());
    }

    @Override
    public void renderEarly(TridentStormEntity animatable, PoseStack stackIn, float partialTicks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float alpha) {

        stackIn.mulPose(Vector3f.YP.rotationDegrees(animatable.getYRot() * ((float) Math.PI / 180F)));

        if (animatable.lifeTime <= 1) {
            float scaleFactor = 0.0F;
            stackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        } else {

        }
    }

    @Override
    protected int getBlockLightLevel(TridentStormEntity p_225624_1_, BlockPos p_225624_2_) {
        return 15;
    }

    @Override
    public RenderType getRenderType(TridentStormEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}