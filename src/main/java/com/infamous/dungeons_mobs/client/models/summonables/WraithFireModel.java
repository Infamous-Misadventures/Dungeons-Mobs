package com.infamous.dungeons_mobs.client.models.summonables;


import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.summonables.WraithFireEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WraithFireModel extends AnimatedGeoModel<WraithFireEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(WraithFireEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/wraith_fire.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(WraithFireEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/wraith_fire.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(WraithFireEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID,"textures/entity/wraith_fire/wraith_fire_" + entity.textureChange % 31 + ".png");
	}

	@Override
	public void setCustomAnimations(WraithFireEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);

	}
}