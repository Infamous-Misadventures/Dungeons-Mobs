package com.infamous.dungeons_mobs.client.models.jungle;

import com.infamous.dungeons_mobs.DungeonsMobs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class LeapleafModel extends AnimatedGeoModel {

	@Override
	public ResourceLocation getAnimationResource(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/leapleaf.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/leapleaf.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/jungle/leapleaf.png");
	}

	@Override
	public void setMolangQueries(IAnimatable animatable, double currentTick) {
		super.setMolangQueries(animatable, currentTick);

		MolangParser parser = GeckoLibCache.getInstance().parser;
		LivingEntity livingEntity = (LivingEntity) animatable;
		Vec3 velocity = livingEntity.getDeltaMovement();
		float groundSpeed = Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
		parser.setValue("query.ground_speed", () -> groundSpeed * 17.5);
	}
}
