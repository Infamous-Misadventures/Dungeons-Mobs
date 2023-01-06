package com.infamous.dungeons_mobs.client.models.armor;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VindicatorHelmetModel<T extends LivingEntity> extends HumanoidModel<T> {
	private final LivingEntity entity;
	private final boolean isDiamond;
	private final ModelPart horn1;
	private final ModelPart horn2;

	public VindicatorHelmetModel(ModelPart modelPart, LivingEntity entity, boolean isDiamond) {
		super(modelPart);
		this.entity = entity;
		this.isDiamond = isDiamond;
		this.horn1 = head.getChild("horn1");
		this.horn2 = head.getChild("horn2");
	}

	public static LayerDefinition createHelmetLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition helmet = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -35.0F + 25.0F, -5.0F, 10.0F, 10.0F, 10.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
		helmet.addOrReplaceChild("horn1", CubeListBuilder.create().texOffs(46, 27).addBox(5.0F, -34.5F, -6.0F, 2.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 16.0F, -23.0F, -0.7854F, 0.0F, 0.0F));
		helmet.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(46, 27).addBox(-7.0F, -34.5F, -6.0F, 2.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, -4.0F, -0.7854F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha){
		matrixStackIn.pushPose();
		this.head.copyFrom(this.head);
		if (this.entity.isBaby()) {
			matrixStackIn.scale(0.8F, 0.8F, 0.8F);
			this.head.setPos(0.0F, 15.0F, 0.0F);
		}
		if(!this.isDiamond){
			horn1.visible = false;
			horn2.visible = false;
		}
		this.head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		matrixStackIn.popPose();
	}

}