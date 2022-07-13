package com.infamous.dungeons_mobs.client.models.undead;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.undead.ArmoredZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.DungeonsZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.JungleZombieEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DungeonZombieModel extends AnimatedGeoModel {

		@Override
		public ResourceLocation getAnimationFileLocation(Object entity) {
			return new ResourceLocation(DungeonsMobs.MODID, "animations/zombie.animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(Object entity) {
			return (entity instanceof ArmoredZombieEntity) ?
							new ResourceLocation(DungeonsMobs.MODID, "geo/armored_zombie.geo.json") :
					(entity instanceof DungeonsZombieEntity && ((DungeonsZombieEntity)entity).getIsVillager()) ?
							new ResourceLocation(DungeonsMobs.MODID, "geo/zombie_villager.geo.json") :
							new ResourceLocation(DungeonsMobs.MODID, "geo/zombie.geo.json") ;
		}

		@Override
		public ResourceLocation getTextureLocation(Object entity) {
			return (entity instanceof ArmoredZombieEntity) ?
							new ResourceLocation(DungeonsMobs.MODID, "textures/entity/zombie/armored_zombie.png") :
					(entity instanceof JungleZombieEntity) ?
							new ResourceLocation(DungeonsMobs.MODID, "textures/entity/zombie/jungle_zombie.png") :
							(entity instanceof DungeonsZombieEntity && ((DungeonsZombieEntity)entity).getIsVillager()) ?
									new ResourceLocation(DungeonsMobs.MODID, "textures/entity/zombie/zombie_villager.png") :
									new ResourceLocation(DungeonsMobs.MODID, "textures/entity/zombie/zombie.png") ;
		}

	@Override
	public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);

		LivingEntity entityIn = (LivingEntity) entity;

		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

		if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
			head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
			head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
		}
		if (!(entityIn instanceof JungleZombieEntity) && !(entityIn instanceof ArmoredZombieEntity)  && ((DungeonsZombieEntity)entity).getIsVillager()) {
			IBone rightEye = this.getAnimationProcessor().getBone("righteye");
			IBone leftEye = this.getAnimationProcessor().getBone("lefteye");
			IBone eyeBrow = this.getAnimationProcessor().getBone("head3");
			rightEye.setPositionX((float) Math.max(Math.min((extraData.netHeadYaw / 80)+Math.sin(leftEye.getPositionX() * Math.PI / 180F),1),0.1));
			leftEye.setPositionX((float) Math.min(Math.max((extraData.netHeadYaw / 80)+Math.sin(rightEye.getPositionX() * Math.PI / 180F),-1),-0.1));
			rightEye.setPositionY(Math.max(Math.min(extraData.headPitch / 80,0.15F),-0.2F));
			leftEye.setPositionY(Math.max(Math.min(extraData.headPitch / 80,0.15F),-0.2F));
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

