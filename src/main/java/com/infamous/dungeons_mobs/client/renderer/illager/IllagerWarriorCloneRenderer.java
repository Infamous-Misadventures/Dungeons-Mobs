package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.IllagerWarriorModel;
import com.infamous.dungeons_mobs.client.models.illager.MountaineerModel;
import com.infamous.dungeons_mobs.entities.illagers.IllagerWarriorCloneEntity;
import com.infamous.dungeons_mobs.entities.illagers.IllagerWarriorEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class IllagerWarriorCloneRenderer extends GeoEntityRenderer<IllagerWarriorCloneEntity> {
	public IllagerWarriorCloneRenderer(EntityRendererManager renderManager) {
		super(renderManager, new IllagerWarriorModel());
		//this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/enchanter/enchanter_eyes.png")));
		//this.addLayer(new GeoHeldItemLayer<>(this, 0.0, 0.0, 0.5));
	}

	protected void applyRotations(IllagerWarriorCloneEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
								  float rotationYaw, float partialTicks) {
		float scaleFactor = 0.9375F;
		matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}

	@Override
	public RenderType getRenderType(IllagerWarriorCloneEntity animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
