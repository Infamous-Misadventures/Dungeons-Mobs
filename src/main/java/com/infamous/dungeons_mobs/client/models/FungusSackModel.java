package com.infamous.dungeons_mobs.client.models;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class FungusSackModel<T extends LivingEntity> extends AgeableListModel<T> {
	private final ModelPart root;

	public FungusSackModel(ModelPart p_170955_) {
		this.root = p_170955_;
	}


	public static LayerDefinition createBackpackLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("backpack", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.1111F, -0.0556F, 8.0F, 9.0F, 7.0F), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void setupAnim(T wearer, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
		this.root.y = 4.3611F;
		if(wearer.isCrouching()){
			this.root.y += 3.0F;
		}
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.root);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}