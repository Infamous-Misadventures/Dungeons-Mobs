package com.infamous.dungeons_mobs.client.models.projectile;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.projectiles.GeoOrbEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GeoOrbModel extends AnimatedGeoModel<GeoOrbEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(GeoOrbEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/necromancer_projectile.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(GeoOrbEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/necromancer_projectile.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(GeoOrbEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID,"textures/entity/projectile/necromancer_projectile.png");
	}

	@Override
	public void setLivingAnimations(GeoOrbEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
	}
}