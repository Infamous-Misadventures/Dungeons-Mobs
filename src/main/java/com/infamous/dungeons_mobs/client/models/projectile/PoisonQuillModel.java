package com.infamous.dungeons_mobs.client.models.projectile;


import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.projectiles.PoisonQuillEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PoisonQuillModel extends AnimatedGeoModel<PoisonQuillEntity> {

	@Override
	public ResourceLocation getAnimationResource(PoisonQuillEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/poison_quill.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(PoisonQuillEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/poison_quill.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(PoisonQuillEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, entity.isKelp() ? "textures/entity/projectile/water_poison_quill.png" : "textures/entity/projectile/poison_quill.png");
	}

	@Override
	public void setCustomAnimations(PoisonQuillEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		IBone everything = this.getAnimationProcessor().getBone("everything");

		everything.setRotationY(-1.5708F);
	}
}