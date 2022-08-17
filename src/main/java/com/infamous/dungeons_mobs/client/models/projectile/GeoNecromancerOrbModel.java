package com.infamous.dungeons_mobs.client.models.projectile;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.illagers.bosses.ArchIllagerEntity;
import com.infamous.dungeons_mobs.entities.projectiles.NecromancerOrbEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GeoNecromancerOrbModel extends AnimatedGeoModel {

	@Override
	public ResourceLocation getAnimationFileLocation(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/necromancer_proyectile.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/necromancer_proyectile.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID,"textures/geo_entity/necromancer_proyectile.png");
	}

	@Override
	public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
	}
}
