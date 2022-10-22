package com.infamous.dungeons_mobs.client.renderer.projectiles;

import com.infamous.dungeons_mobs.client.models.projectile.MageMissileModel;
import com.infamous.dungeons_mobs.entities.projectiles.MageMissileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class MageMissileRenderer extends GeoProjectilesRenderer<MageMissileEntity> {

    public MageMissileRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MageMissileModel());
    }

	protected int getBlockLightLevel(MageMissileEntity p_225624_1_, BlockPos p_225624_2_) {
		return 15;
	};
	
    @Override
    public void render(MageMissileEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float scaleFactor = 1.0F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public RenderType getRenderType(MageMissileEntity animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
