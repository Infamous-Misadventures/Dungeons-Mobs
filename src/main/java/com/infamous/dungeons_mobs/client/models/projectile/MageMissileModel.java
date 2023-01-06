package com.infamous.dungeons_mobs.client.models.projectile;


import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.projectiles.MageMissileEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MageMissileModel extends AnimatedGeoModel<MageMissileEntity> {

	@Override
	public ResourceLocation getAnimationResource(MageMissileEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/mage_missile.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MageMissileEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/mage_missile.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MageMissileEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/projectile/mage_missile.png");
	}

	@Override
	public void setCustomAnimations(MageMissileEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		IBone everything = this.getAnimationProcessor().getBone("everything");

		everything.setRotationY(-1.5708F);
	}
}