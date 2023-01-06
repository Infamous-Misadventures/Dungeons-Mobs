package com.infamous.dungeons_mobs.client.models.summonables;// Made with Blockbench 3.6.6

// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.infamous.dungeons_mobs.entities.summonables.GeomancerBombEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GeomancerConstructModel extends AnimatedGeoModel {

	@Override
	public ResourceLocation getAnimationResource(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "animations/geomancer_pillar.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(Object entity) {
		return new ResourceLocation(DungeonsMobs.MODID, "geo/geomancer_pillar.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Object entity) {
		// ChorusGormandizerEntity entityIn = (ChorusGormandizerEntity) entity;
		return entity instanceof GeomancerBombEntity
				? new ResourceLocation(DungeonsMobs.MODID, "textures/entity/constructs/geomancer_bomb.png")
				: new ResourceLocation(DungeonsMobs.MODID, "textures/entity/constructs/geomancer_wall.png");
	}

	@Override
	public void setCustomAnimations(IAnimatable entity, int uniqueID, AnimationEvent customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);

		ConstructEntity entityIn = (ConstructEntity) entity;

	}

	/*
	 * public IBone getArm(HandSide p_191216_1_) { return
	 * this.getAnimationProcessor().getBone("rightArm"); }
	 * 
	 * 
	 * public void translateToHand(HandSide p_225599_1_, MatrixStack p_225599_2_) {
	 * this.translateAndRotate(this.getAnimationProcessor().getBone("body"),
	 * p_225599_2_, 0.0, 0.0, 0.0);
	 * this.translateAndRotate(this.getAnimationProcessor().getBone("rightArm"),
	 * p_225599_2_, 0.0, 0.0, 0.0); }
	 * 
	 * public void translateAndRotate(IBone bone, MatrixStack p_228307_1_, double
	 * moveX, double moveY, double moveZ) {
	 * 
	 * if (bone.getRotationZ() != 0.0F) {
	 * p_228307_1_.mulPose(Vector3f.ZP.rotation(bone.getRotationZ())); }
	 * 
	 * if (bone.getRotationY() != 0.0F) {
	 * p_228307_1_.mulPose(Vector3f.YP.rotation(bone.getRotationY())); }
	 * 
	 * if (bone.getRotationX() != 0.0F) {
	 * p_228307_1_.mulPose(Vector3f.XP.rotation(bone.getRotationX())); }
	 * 
	 * p_228307_1_.translate((double)((bone.getPivotX() + moveX) / 16.0F),
	 * (double)((bone.getPivotY() + moveY) / 16.0F), (double)((bone.getPivotZ() +
	 * moveZ) / 16.0F));
	 * 
	 * 
	 * }
	 */
}