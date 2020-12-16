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
		textureWidth = 64;
		textureHeight = 64;

		this.bipedBody = new ModelRenderer(this);
		this.bipedBody.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.bipedBody.setTextureOffset(0, 13).addBox(-4.0F, -5.875F, -1.1812F, 8.0F, 14.0F, 4.0F, 0.0F, false);

		this.bipedHead = new ModelRenderer(this);
		this.bipedHead.setRotationPoint(-4.0F, -23.0F, 1.0F);
		this.bipedHead.setTextureOffset(0, 0).addBox(-2.0F, 10.125F, -3.1812F, 12.0F, 7.0F, 6.0F, 0.0F, false);
		this.bipedHead.setTextureOffset(0, 0).addBox(3.0F, 14.125F, -4.1813F, 2.0F, 4.0F, 1.0F, 0.0F, false);
		this.bipedHead.setTextureOffset(24, 24).addBox(2.0F, -0.875F, -2.1812F, 4.0F, 11.0F, 4.0F, 0.0F, false);

		this.bipedLeftLeg = new ModelRenderer(this);
		this.bipedLeftLeg.setRotationPoint(-3.0F, -15.0F, 2.0F);
		this.bipedLeftLeg.setTextureOffset(0, 31).addBox(3.0F, 17.125F, -3.1812F, 3.0F, 3.0F, 3.0F, 0.0F, true);
		this.bipedLeftLeg.setTextureOffset(0, 13).addBox(4.0F, 20.125F, -2.1812F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		this.bipedLeftLeg.setTextureOffset(19, 13).addBox(4.0F, 23.125F, -4.1813F, 1.0F, 0.0F, 3.0F, 0.0F, false);

		this.bipedRightLeg = new ModelRenderer(this);
		this.bipedRightLeg.setRotationPoint(-5.0F, -15.0F, 2.0F);
		this.bipedRightLeg.setTextureOffset(0, 31).addBox(2.0F, 17.125F, -3.1812F, 3.0F, 3.0F, 3.0F, 0.0F, false);
		this.bipedRightLeg.setTextureOffset(0, 13).addBox(3.0F, 20.125F, -2.1812F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		this.bipedRightLeg.setTextureOffset(17, 13).addBox(3.0F, 23.125F, -4.1813F, 1.0F, 0.0F, 3.0F, 0.0F, false);

		this.bipedRightArm = new ModelRenderer(this);
		this.bipedRightArm.setRotationPoint(-8.0F, -22.0F, 2.0F);
		this.bipedRightArm.setTextureOffset(30, 0).addBox(0.0F, 16.125F, -2.6812F, 4.0F, 3.0F, 3.0F, 0.0F, true);
		this.bipedRightArm.setTextureOffset(24, 19).addBox(-3.0F, 16.125F, -1.6812F, 7.0F, 1.0F, 1.0F, 0.0F, true);

		this.amulet = new ModelRenderer(this);
		this.amulet.setRotationPoint(-4.0F, -21.0F, -1.0F);
		this.bipedBody.addChild(amulet);
		this.amulet.setTextureOffset(9, 31).addBox(3.0F, 19.125F, -1.1812F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		this.amulet.setTextureOffset(35, 13).addBox(2.0F, 17.125F, -0.2813F, 4.0F, 2.0F, 0.0F, 0.0F, false);

		this.bipedLeftArm = new ModelRenderer(this);
		this.bipedLeftArm.setRotationPoint(0.0F, -22.0F, 2.0F);
		this.bipedLeftArm.setTextureOffset(30, 0).addBox(4.0F, 16.125F, -2.6812F, 4.0F, 3.0F, 3.0F, 0.0F, false);
		this.bipedLeftArm.setTextureOffset(24, 19).addBox(4.0F, 16.125F, -1.6812F, 7.0F, 1.0F, 1.0F, 0.0F, false);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}