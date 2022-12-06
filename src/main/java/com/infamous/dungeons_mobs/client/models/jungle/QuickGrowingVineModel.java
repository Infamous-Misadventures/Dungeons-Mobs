package com.infamous.dungeons_mobs.client.models.jungle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.jungle.AbstractVineEntity;
import net.minecraft.resources.ResourceLocation;

public class QuickGrowingVineModel extends AbstractVineModel {
	@Override
	public ResourceLocation getAnimationResource(AbstractVineEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/quick_growing_vine.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(AbstractVineEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/quick_growing_vine.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(AbstractVineEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/jungle/quick_growing_vine.png");
	}
}