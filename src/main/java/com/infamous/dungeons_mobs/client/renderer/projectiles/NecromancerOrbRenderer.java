package com.infamous.dungeons_mobs.client.renderer.projectiles;

import com.infamous.dungeons_mobs.client.models.projectile.NecromancerOrbModel;
import com.infamous.dungeons_mobs.entities.projectiles.NecromancerOrbEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class NecromancerOrbRenderer extends GeoProjectilesRenderer<NecromancerOrbEntity> {

    public NecromancerOrbRenderer(EntityRendererManager renderManager) {
        super(renderManager, new NecromancerOrbModel());
    }

	@Override
	public void renderEarly(NecromancerOrbEntity animatable, MatrixStack stackIn, float partialTicks,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
			float red, float green, float blue, float alpha) {
		if (animatable.lifeTime <= 3) {
			float scaleFactor = 0.0F;
			stackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		} else {
			
		}
	}
	
	@Override
	protected int getBlockLightLevel(NecromancerOrbEntity p_225624_1_, BlockPos p_225624_2_) {
		return 15;
	}

	
    @Override
    public void render(NecromancerOrbEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float scaleFactor = 1.0F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public RenderType getRenderType(NecromancerOrbEntity animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
