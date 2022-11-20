package com.infamous.dungeons_mobs.client.models.summonables;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.summonables.KelpTrapEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KelpTrapModel<T extends KelpTrapEntity> extends AnimatedGeoModel<T> {

	@Override
	public ResourceLocation getAnimationFileLocation(T entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/trap.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(T entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/trap.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/kelp_trap.png");
	}
}