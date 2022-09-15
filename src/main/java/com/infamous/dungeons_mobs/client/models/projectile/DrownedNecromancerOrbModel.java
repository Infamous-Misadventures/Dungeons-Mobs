package com.infamous.dungeons_mobs.client.models.projectile;


import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.projectiles.DrownedNecromancerOrbEntity;
import com.infamous.dungeons_mobs.entities.projectiles.NecromancerOrbEntity;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DrownedNecromancerOrbModel extends AnimatedGeoModel<DrownedNecromancerOrbEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(DrownedNecromancerOrbEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/drowned_necromancer_orb.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(DrownedNecromancerOrbEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/drowned_necromancer_orb.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(DrownedNecromancerOrbEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/projectile/drowned_necromancer_orb_0.png"); //new ResourceLocation(DungeonsMobs.MODID, "textures/entity/projectile/drowned_necromancer_orb_" + entity.textureChange % 3 + ".png");
	}

	@Override
	public void setLivingAnimations(DrownedNecromancerOrbEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone everything = this.getAnimationProcessor().getBone("everything");

		everything.setRotationY(-1.5708F);
	}
}