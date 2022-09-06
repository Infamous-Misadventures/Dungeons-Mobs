package com.infamous.dungeons_mobs.client.models.golem;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class SquallGolemModel extends AnimatedGeoModel {

	@Override
	public ResourceLocation getAnimationFileLocation(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/squall_golem.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/squall_golem.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(Object entity) {
		//ChorusGormandizerEntity entityIn = (ChorusGormandizerEntity) entity;
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/golem/squall_golem.png");
	}

	@Override
	public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);

		SquallGolemEntity entityIn = (SquallGolemEntity) entity;

		IBone head = this.getAnimationProcessor().getBone("head");
		IBone eye = this.getAnimationProcessor().getBone("head2");
		IBone eyeBrow = this.getAnimationProcessor().getBone("head3");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
			eye.setPositionX((float) Math.max(Math.min((extraData.netHeadYaw / 80) + Math.sin(eye.getPositionX() * Math.PI / 180F), 1), -1));
			head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
			head.setRotationZ(head.getRotationZ() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
			eye.setPositionY(Math.max(Math.min(extraData.headPitch / 80, 0.15F), -0.2F));
			eyeBrow.setPositionY(Math.max(Math.min(extraData.headPitch / 80, 0.15F), -0.2F));
		}
	}

}
