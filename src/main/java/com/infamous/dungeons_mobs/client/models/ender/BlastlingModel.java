package com.infamous.dungeons_mobs.client.models.ender;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.ender.BlastlingEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BlastlingModel extends AnimatedGeoModel {
	   
		@Override
		public ResourceLocation getAnimationResource(Object entity) {
			return new ResourceLocation(DungeonsMobs.MODID, "animations/blastling.animation.json");
		}

		@Override
		public ResourceLocation getModelResource(Object entity) {
			return new ResourceLocation(DungeonsMobs.MODID, "geo/blastling.geo.json");
		}

		@Override
		public ResourceLocation getTextureResource(Object entity) {
			return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ender/blastling" + ( 1 + ((int)((BlastlingEntity)entity).flameTicks) % 3) + ".png");
		}

		@Override
		public void setCustomAnimations(IAnimatable entity, int uniqueID, AnimationEvent customPredicate) {
			super.setCustomAnimations(entity, uniqueID, customPredicate);
			IBone head = this.getAnimationProcessor().getBone("head");

			LivingEntity entityIn = (LivingEntity) entity;
			EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
			if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
				head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
				head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
			}
		}
	}

