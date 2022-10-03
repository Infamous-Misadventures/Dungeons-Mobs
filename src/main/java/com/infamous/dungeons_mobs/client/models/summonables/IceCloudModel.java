package com.infamous.dungeons_mobs.client.models.summonables;

import com.infamous.dungeons_mobs.DungeonsMobs;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class IceCloudModel extends AnimatedGeoModel {

	@Override
	public ResourceLocation getAnimationFileLocation(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/ice_chunk.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/ice_chunk.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ice_chunk.png");
	}
}