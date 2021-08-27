package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.undead.WraithModel;
import com.infamous.dungeons_mobs.client.renderer.layer.GeoEyeLayer;
import com.infamous.dungeons_mobs.client.renderer.layer.GeoHeldItemLayer;
import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WraithRenderer extends GeoEntityRenderer<WraithEntity> {
	public WraithRenderer(EntityRendererManager renderManager) {
		super(renderManager, new WraithModel());
		this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/wraith/wraith_eyes.png")));
		this.addLayer(new GeoHeldItemLayer<>(this, 0.0, 0.0, 0.5));
	}

	@Override
	public RenderType getRenderType(WraithEntity animatable, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
