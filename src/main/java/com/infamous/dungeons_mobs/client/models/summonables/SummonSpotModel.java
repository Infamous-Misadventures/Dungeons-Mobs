package com.infamous.dungeons_mobs.client.models.summonables;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.summonables.SummonSpotEntity;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SummonSpotModel<T extends SummonSpotEntity> extends AnimatedGeoModel<T> {

	@Override
	public ResourceLocation getAnimationFileLocation(T entity) {
		if (entity.getSummonType() == 0) {
			return new ResourceLocation(DungeonsMobs.MODID, "animations/illusioner_summon_spot.animation.json");
		} else if (entity.getSummonType() == 1) {
			return new ResourceLocation(DungeonsMobs.MODID, "animations/wildfire_summon_spot.animation.json");
		} else {
			return new ResourceLocation(DungeonsMobs.MODID, "animations/illusioner_summon_spot.animation.json");
		}	
	}

	@Override
	public ResourceLocation getModelLocation(T entity) {
		if (entity.getSummonType() == 0) {
			return new ResourceLocation(DungeonsMobs.MODID, "geo/illusioner_summon_spot.geo.json");
		} else if (entity.getSummonType() == 1) {
			return new ResourceLocation(DungeonsMobs.MODID, "geo/wildfire_summon_spot.geo.json");
		} else {
			return new ResourceLocation(DungeonsMobs.MODID, "geo/illusioner_summon_spot.geo.json");
		}	
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		if (entity.getSummonType() == 0) {
			return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illusioner_summon_spot.png");
		} else if (entity.getSummonType() == 1) {
			return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/wildfire_summon_spot.png");
		} else {
			return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illusioner_summon_spot.png");
		}	
	}
}