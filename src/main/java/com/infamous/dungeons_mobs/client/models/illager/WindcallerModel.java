package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class WindcallerModel extends AnimatedGeoModel<WindcallerEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(WindcallerEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/windcaller.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(WindcallerEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/windcaller.geo.json") ;
	}

	@Override
	public ResourceLocation getTextureLocation(WindcallerEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/windcaller.png");
	}

	@Override
	public void setLivingAnimations(WindcallerEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);

		LivingEntity entityIn = (LivingEntity) entity;

		IBone head = this.getAnimationProcessor().getBone("bipedHead");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

		if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
			head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
			head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
		}
	}
}
