package com.infamous.dungeons_mobs.client.models.summonables;


import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.summonables.SimpleTrapEntity;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SimpleTrapModel<T extends SimpleTrapEntity> extends AnimatedGeoModel<T> {

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
		if (entity.getTrapType() == 0) {
			return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/web_trap.png");
		} else if (entity.getTrapType() == 1) {
			return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/vine_trap.png");
		} else {
			return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/web_trap.png");
		}	
	}
}