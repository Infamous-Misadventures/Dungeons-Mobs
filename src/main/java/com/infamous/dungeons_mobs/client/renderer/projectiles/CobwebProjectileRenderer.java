package com.infamous.dungeons_mobs.client.renderer.projectiles;

import com.infamous.dungeons_mobs.client.models.projectile.CobwebProjectileModel;
import com.infamous.dungeons_mobs.entities.projectiles.CobwebProjectileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class CobwebProjectileRenderer extends GeoProjectilesRenderer<CobwebProjectileEntity> {

    public CobwebProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CobwebProjectileModel());
    }
	
    @Override
    public void render(CobwebProjectileEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        float scaleFactor = 1.0F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public RenderType getRenderType(CobwebProjectileEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
