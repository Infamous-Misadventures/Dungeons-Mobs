package com.infamous.dungeons_mobs.client.models.jungle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.water.WavewhispererEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class WhispererModel extends AnimatedGeoModel {

	@Override
	public ResourceLocation getAnimationFileLocation(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/whisperer.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/whisperer.geo.json") ;
	}

	@Override
	public ResourceLocation getTextureLocation(Object entity) {
		return entity instanceof WavewhispererEntity ?
				new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/wavewhisperer.png"):
				new ResourceLocation(DungeonsMobs.MODID, "textures/entity/jungle/whisperer.png");
	}

	@Override
	public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);

		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

		if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
			head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
			head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
		}
	}
}
