package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.illagers.EnchanterEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class EnchanterModel extends AnimatedGeoModel {
	   
		@Override
		public ResourceLocation getAnimationResource(Object entity) {
			return new ResourceLocation(DungeonsMobs.MODID, "animations/enchanter.animation.json");
		}

		@Override
		public ResourceLocation getModelResource(Object entity) {
			return new ResourceLocation(DungeonsMobs.MODID, "geo/enchanter.geo.json");
		}

		@Override
		public ResourceLocation getTextureResource(Object entity) {
			//ChorusGormandizerEntity entityIn = (ChorusGormandizerEntity) entity;
			return new ResourceLocation(DungeonsMobs.MODID, "textures/entity/enchanter/enchanter.png");
		}

		@Override
		public void setCustomAnimations(IAnimatable entity, int uniqueID, AnimationEvent customPredicate) {
			super.setCustomAnimations(entity, uniqueID, customPredicate);
			
			EnchanterEntity entityIn = (EnchanterEntity) entity;
			
			IBone head = this.getAnimationProcessor().getBone("head");

			EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
			//if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
			//head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			//head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
			//}
		}

		/*public IBone getArm(HandSide p_191216_1_) {
			return this.getAnimationProcessor().getBone("rightArm");
		}


		public void translateToHand(HandSide p_225599_1_, MatrixStack p_225599_2_) {
			this.translateAndRotate(this.getAnimationProcessor().getBone("body"), p_225599_2_, 0.0, 0.0, 0.0);
			this.translateAndRotate(this.getAnimationProcessor().getBone("rightArm"), p_225599_2_, 0.0, 0.0, 0.0);
		}
 
		public void translateAndRotate(IBone bone, MatrixStack p_228307_1_, double moveX, double moveY, double moveZ) {

			if (bone.getRotationZ() != 0.0F) {
				p_228307_1_.mulPose(Vector3f.ZP.rotation(bone.getRotationZ()));
			}

			if (bone.getRotationY() != 0.0F) {
				p_228307_1_.mulPose(Vector3f.YP.rotation(bone.getRotationY()));
			}

			if (bone.getRotationX() != 0.0F) {
				p_228307_1_.mulPose(Vector3f.XP.rotation(bone.getRotationX()));
			}
			
			p_228307_1_.translate((double)((bone.getPivotX() + moveX) / 16.0F), (double)((bone.getPivotY() + moveY) / 16.0F), (double)((bone.getPivotZ() + moveZ) / 16.0F));
	   
	      
		}*/
}

