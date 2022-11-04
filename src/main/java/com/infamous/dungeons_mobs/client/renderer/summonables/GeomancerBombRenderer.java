package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.summonables.GeomancerConstructModel;
import com.infamous.dungeons_mobs.client.renderer.layers.GeoEyeLayer;
import com.infamous.dungeons_mobs.client.renderer.layers.PulsatingGlowLayer;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.infamous.dungeons_mobs.entities.summonables.GeomancerBombEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GeomancerBombRenderer extends GeoEntityRenderer<GeomancerBombEntity> {
	public GeomancerBombRenderer(EntityRendererManager renderManager) {
		super(renderManager, new GeomancerConstructModel());
		this.addLayer(new PulsatingGlowLayer<GeomancerBombEntity>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/constructs/geomancer_bomb.png"), 0.5F, 0.6F, 0.2F) {
			@Override
			public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
							   GeomancerBombEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
							   float ageInTicks, float netHeadYaw, float headPitch) {

				if (entitylivingbaseIn.getLifeTicks() < 60 && entitylivingbaseIn.getLifeTicks() >= 30) {
					textureLocation = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/constructs/geomancer_bomb_eyes_1.png");
					super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
							ageInTicks, netHeadYaw, headPitch);
				} else if (entitylivingbaseIn.getLifeTicks() < 30 && entitylivingbaseIn.getLifeTicks() >= 0) {
					pulseSpeed = 0.8F;
					textureLocation = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/constructs/geomancer_bomb_eyes_2.png");
					super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
							ageInTicks, netHeadYaw, headPitch);
				}
			}
		});
	}
	
	protected void applyRotations(GeomancerBombEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
			float rotationYaw, float partialTicks) {

		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}

	@Override
	public RenderType getRenderType(GeomancerBombEntity animatable, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
