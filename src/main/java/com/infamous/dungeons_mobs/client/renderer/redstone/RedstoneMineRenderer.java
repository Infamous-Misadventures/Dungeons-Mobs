package com.infamous.dungeons_mobs.client.renderer.redstone;

import com.infamous.dungeons_mobs.client.models.redstone.RedstoneMineModel;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneMineEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class RedstoneMineRenderer extends GeoProjectilesRenderer<RedstoneMineEntity> {
	public RedstoneMineRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new RedstoneMineModel());
	}

	@Override
	protected int getBlockLightLevel(RedstoneMineEntity p_225624_1_, BlockPos p_225624_2_) {
		return 15;
	}

	@Override
	public void render(RedstoneMineEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn,
			MultiBufferSource bufferIn, int packedLightIn) {
		if (entityIn.getLifeTicks() > RedstoneMineEntity.LIFE_TIME) {
			return;
		}
		matrixStackIn.translate(0, 0.01F, 0);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public RenderType getRenderType(RedstoneMineEntity animatable, float partialTicks, PoseStack stack,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}