package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.RampartCaptainModel;
import com.infamous.dungeons_mobs.client.models.illager.VindicatorRaidCaptainModel;
import com.infamous.dungeons_mobs.entities.illagers.RampartCaptainEntity;
import com.infamous.dungeons_mobs.entities.illagers.VindicatorRaidCaptainEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RampartCaptainRenderer extends GeoEntityRenderer<RampartCaptainEntity> {
	public RampartCaptainRenderer(EntityRendererManager renderManager) {
		super(renderManager, new RampartCaptainModel());
		//this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/enchanter/enchanter_eyes.png")));
		//this.addLayer(new GeoHeldItemLayer<>(this, 0.0, 0.0, 0.5));
	}

	protected void applyRotations(RampartCaptainEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
								  float rotationYaw, float partialTicks) {
		float scaleFactor = 0.9375F;
		matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}

	@Override
	public RenderType getRenderType(RampartCaptainEntity animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
