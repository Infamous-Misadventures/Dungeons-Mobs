package com.infamous.dungeons_mobs.client.models.jungle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.infamous.dungeons_mobs.entities.water.WavewhispererEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class WhispererModel<T extends WhispererEntity> extends AnimatedGeoModel<T> {

	@Override
	public ResourceLocation getAnimationFileLocation(T entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/whisperer.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(T entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/whisperer.geo.json") ;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return entity instanceof WavewhispererEntity ?
				new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/wavewhisperer.png"):
				new ResourceLocation(DungeonsMobs.MODID, "textures/entity/jungle/whisperer.png");
	}

	@Override
	public void setLivingAnimations(T entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);

		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

		if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
			head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
			head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
		}
	}
}
