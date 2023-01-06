package com.infamous.dungeons_mobs.client.models.summonables;// Made with Blockbench 3.6.6

// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.summonables.WindcallerTornadoEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class WindcallerTornadoModel extends AnimatedGeoModel {

	@Override
	public ResourceLocation getAnimationResource(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/windcaller_tornado.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/windcaller_tornado.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/windcaller_tornado.png");
	}

	@Override
	public void setCustomAnimations(IAnimatable entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);

		IBone everything = this.getAnimationProcessor().getBone("everything");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

		if (((WindcallerTornadoEntity) entity).isBlast() && (extraData.headPitch != 0 || extraData.netHeadYaw != 0)) {
			// everything.setRotationX(((Entity)entity).getXRot());
			// everything.setRotationY(((Entity)entity).yRot);
		}
	}
}