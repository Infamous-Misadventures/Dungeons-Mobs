package com.infamous.dungeons_mobs.client.models.jungle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.jungle.AbstractVineEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class PoisonQuillVineModel extends AbstractVineModel {
	@Override
	public ResourceLocation getAnimationResource(AbstractVineEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/poison_quill_vine.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(AbstractVineEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/poison_quill_vine.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(AbstractVineEntity entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/jungle/poison_quill_vine.png");
	}
	
	@Override
	public void setCustomAnimations(AbstractVineEntity entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);

		IBone head = this.getAnimationProcessor().getBone("head");
		IBone headRotator = this.getAnimationProcessor().getBone("headRotator");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
            head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
            
            headRotator.setRotationY(headRotator.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
        }
	}
}