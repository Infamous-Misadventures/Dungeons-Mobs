package com.infamous.dungeons_mobs.client.models.illager;
// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.infamous.dungeons_mobs.entities.illagers.ArchIllagerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ArchIllagerModel<T extends ArchIllagerEntity> extends BipedModel<T> {
	private final ModelRenderer amulet;

	public ArchIllagerModel(float modelSize) {
		super(modelSize);
		texWidth = 64;
		texHeight = 64;

		this.body = new ModelRenderer(this);
		this.body.setPos(0.0F, 16.0F, 0.0F);
		this.body.texOffs(0, 13).addBox(-4.0F, -5.875F, -1.1812F, 8.0F, 14.0F, 4.0F, 0.0F, false);

		this.head = new ModelRenderer(this);
		this.head.setPos(-4.0F, -23.0F, 1.0F);
		this.head.texOffs(0, 0).addBox(-2.0F, 10.125F, -3.1812F, 12.0F, 7.0F, 6.0F, 0.0F, false);
		this.head.texOffs(0, 0).addBox(3.0F, 14.125F, -4.1813F, 2.0F, 4.0F, 1.0F, 0.0F, false);
		this.head.texOffs(24, 24).addBox(2.0F, -0.875F, -2.1812F, 4.0F, 11.0F, 4.0F, 0.0F, false);

		this.leftLeg = new ModelRenderer(this);
		this.leftLeg.setPos(-3.0F, -15.0F, 2.0F);
		this.leftLeg.texOffs(0, 31).addBox(3.0F, 17.125F, -3.1812F, 3.0F, 3.0F, 3.0F, 0.0F, true);
		this.leftLeg.texOffs(0, 13).addBox(4.0F, 20.125F, -2.1812F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		this.leftLeg.texOffs(19, 13).addBox(4.0F, 23.125F, -4.1813F, 1.0F, 0.0F, 3.0F, 0.0F, false);

		this.rightLeg = new ModelRenderer(this);
		this.rightLeg.setPos(-5.0F, -15.0F, 2.0F);
		this.rightLeg.texOffs(0, 31).addBox(2.0F, 17.125F, -3.1812F, 3.0F, 3.0F, 3.0F, 0.0F, false);
		this.rightLeg.texOffs(0, 13).addBox(3.0F, 20.125F, -2.1812F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		this.rightLeg.texOffs(17, 13).addBox(3.0F, 23.125F, -4.1813F, 1.0F, 0.0F, 3.0F, 0.0F, false);

		this.rightArm = new ModelRenderer(this);
		this.rightArm.setPos(-8.0F, -22.0F, 2.0F);
		this.rightArm.texOffs(30, 0).addBox(0.0F, 16.125F, -2.6812F, 4.0F, 3.0F, 3.0F, 0.0F, true);
		this.rightArm.texOffs(24, 19).addBox(-3.0F, 16.125F, -1.6812F, 7.0F, 1.0F, 1.0F, 0.0F, true);

		this.amulet = new ModelRenderer(this);
		this.amulet.setPos(-4.0F, -21.0F, -1.0F);
		this.body.addChild(amulet);
		this.amulet.texOffs(9, 31).addBox(3.0F, 19.125F, -1.1812F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		this.amulet.texOffs(35, 13).addBox(2.0F, 17.125F, -0.2813F, 4.0F, 2.0F, 0.0F, 0.0F, false);

		this.leftArm = new ModelRenderer(this);
		this.leftArm.setPos(0.0F, -22.0F, 2.0F);
		this.leftArm.texOffs(30, 0).addBox(4.0F, 16.125F, -2.6812F, 4.0F, 3.0F, 3.0F, 0.0F, false);
		this.leftArm.texOffs(24, 19).addBox(4.0F, 16.125F, -1.6812F, 7.0F, 1.0F, 1.0F, 0.0F, false);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}