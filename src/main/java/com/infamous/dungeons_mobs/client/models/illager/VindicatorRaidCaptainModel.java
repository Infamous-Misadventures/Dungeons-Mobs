package com.infamous.dungeons_mobs.client.models.illager;

import com.infamous.dungeons_mobs.DungeonsMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class VindicatorRaidCaptainModel extends AnimatedGeoModel {

		@Override
		public ResourceLocation getAnimationFileLocation(Object entity) {
			return new ResourceLocation(DungeonsMobs.MODID, "animations/vindicator_geo.animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(Object entity) {
			return new ResourceLocation(DungeonsMobs.MODID, "geo/illager_geo.geo.json") ;
		}

		@Override
		public ResourceLocation getTextureLocation(Object entity) {
			return new ResourceLocation(DungeonsMobs.MODID, "textures/geo_entity/illager/diamond_armored_vindicator_leader.png");
		}

	@Override
	public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);

		LivingEntity entityIn = (LivingEntity) entity;

		IBone leftEye = this.getAnimationProcessor().getBone("lefteye");
		IBone rightEye = this.getAnimationProcessor().getBone("righteye");
		IBone eyeBrow = this.getAnimationProcessor().getBone("head3");
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

		if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
			rightEye.setPositionX(Math.max(Math.min(extraData.netHeadYaw / 80,1),0));
			leftEye.setPositionX(Math.min(Math.max(extraData.netHeadYaw / 80,-1),0));
			rightEye.setPositionY(Math.max(Math.min(extraData.headPitch / 80,1F),-0F));
			leftEye.setPositionY(Math.max(Math.min(extraData.headPitch / 80,1F),-0F));
			eyeBrow.setPositionY(Math.max(Math.min(extraData.headPitch / 80,1F),-0F));
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

