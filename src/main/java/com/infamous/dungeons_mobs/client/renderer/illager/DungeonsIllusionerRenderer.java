package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.DungeonsIllusionerModel;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsIllusionerEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DungeonsIllusionerRenderer extends DefaultIllagerRenderer<DungeonsIllusionerEntity> {
	public DungeonsIllusionerRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new DungeonsIllusionerModel());
	}

	@Override
	public void render(DungeonsIllusionerEntity entity, float entityYaw, float partialTicks, PoseStack stack,
			MultiBufferSource bufferIn, int packedLightIn) {
		if (entity.appearDelay <= 0) {
			super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		} else {

		}
	}
}