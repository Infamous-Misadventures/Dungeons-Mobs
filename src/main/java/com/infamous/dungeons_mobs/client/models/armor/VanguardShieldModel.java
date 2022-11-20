package com.infamous.dungeons_mobs.client.models.armor;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VanguardShieldModel extends Model {
	private final ModelPart root;

	public VanguardShieldModel(ModelPart modelPart) {
		super(RenderType::entitySolid);
		this.root = modelPart;
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("plate_top", CubeListBuilder
				.create().texOffs(0, 26).addBox(-8.0F + 5.0F, -1.0F - 12.0F, 0.0F - 2.0F, 6.0F, 2.0F, 1.0F), PartPose.ZERO);
		partdefinition.addOrReplaceChild("plate_side_1", CubeListBuilder
				.create().texOffs(24, 26).addBox(-13.0F + 5.0F, 5.0F - 12.0F, 0.0F - 2.0F, 3.0F, 5.0F, 1.0F), PartPose.ZERO);
		partdefinition.addOrReplaceChild("plate_side_2", CubeListBuilder
				.create().texOffs(16, 26).addBox(0.0F + 5.0F, 5.0F - 12.0F, 0.0F - 2.0F, 3.0F, 5.0F, 1.0F), PartPose.ZERO);
		partdefinition.addOrReplaceChild("plate_base_1", CubeListBuilder
				.create().texOffs(0, 0).addBox(-10.0F + 5.0F, 6.0F - 12.0F, 0.0F - 2.0F, 10.0F, 13.0F, 1.0F), PartPose.ZERO);
		partdefinition.addOrReplaceChild("plate_base_2", CubeListBuilder
				.create().texOffs(0, 17).addBox(-11.0F + 5.0F, 1.0F - 12.0F, 0.0F - 2.0F, 12.0F, 5.0F, 1.0F), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	public ModelPart getRoot() {
		return this.root;
	}
	public void renderToBuffer(PoseStack p_103703_, VertexConsumer p_103704_, int p_103705_, int p_103706_, float p_103707_, float p_103708_, float p_103709_, float p_103710_) {
		this.root.render(p_103703_, p_103704_, p_103705_, p_103706_, p_103707_, p_103708_, p_103709_, p_103710_);
	}
}