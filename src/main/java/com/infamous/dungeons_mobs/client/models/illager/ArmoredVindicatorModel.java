package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.illagers.ArmoredVindicatorEntity;

import com.infamous.dungeons_mobs.entities.illagers.MageEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ArmoredVindicatorModel extends AnimatedGeoModel {

		@Override
		public ResourceLocation getAnimationFileLocation(Object entity) {
			return new ResourceLocation(DungeonsMobs.MODID, "animations/vindicator.animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(Object entity) {
			return (entity instanceof ArmoredVindicatorEntity && ((ArmoredVindicatorEntity)entity).isDiamond()) ? new ResourceLocation(DungeonsMobs.MODID, "geo/diamond_vindicator.geo.json") : new ResourceLocation(DungeonsMobs.MODID, "geo/gold_iron_vindicator.geo.json");
		}

		@Override
		public ResourceLocation getTextureLocation(Object entity) {
			return (entity instanceof ArmoredVindicatorEntity && ((ArmoredVindicatorEntity)entity).isDiamond()) ? new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/diamond_armored_vindicator.png") : (entity instanceof ArmoredVindicatorEntity && ((ArmoredVindicatorEntity)entity).isGold()) ? new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/gold_armored_vindicator.png"): new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/iron_armored_vindicator.png");
		}

	@Override
	public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);

		LivingEntity entityIn = (LivingEntity) entity;

		IBone rightEye = this.getAnimationProcessor().getBone("righteye");
		IBone leftEye = this.getAnimationProcessor().getBone("lefteye");
		IBone eyeBrow = this.getAnimationProcessor().getBone("head3");
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {/*
			leftEye.setPositionX((float) (Math.max(
					extraData.netHeadYaw / 80 + Math.min(
							Math.sin(
									leftEye.getPositionX()/16 + 90 * Math.PI / 180
							),
							1
					),
					0
			)));
			rightEye.setPositionX((float) (Math.min(
					extraData.netHeadYaw / 80 + Math.max(
							Math.sin(
									rightEye.getPositionX()/16 + 90 * Math.PI / 180
							),
							-1
					),
					0
			)));
			leftEye.setPositionY((float) (Math.max(
					extraData.headPitch / 80 - 0.4,
					0
			)));
			rightEye.setPositionY((float) (Math.max(
					extraData.headPitch / 80 - 0.4,
					0
			)));
			eyeBrow.setPositionY((float) (Math.max(
					extraData.headPitch / 80 - 0.4,
					0
			)));*/
			head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
			head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
		}
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

