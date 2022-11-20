package com.infamous.dungeons_mobs.client.models.projectile;


import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.projectiles.CobwebProjectileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CobwebProjectileModel extends AnimatedGeoModel<CobwebProjectileEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(CobwebProjectileEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/web_projectile.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(CobwebProjectileEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/web_projectile.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(CobwebProjectileEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/projectile/web_projectile.png");
	}

	@Override
	public void setCustomAnimations(CobwebProjectileEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		IBone everything = this.getAnimationProcessor().getBone("everything");

		everything.setRotationY(-1.5708F);
	}
}