package com.infamous.dungeons_mobs.client.models.armor;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ChefHatModel<T extends LivingEntity> extends BipedModel<T> {
	private final EquipmentSlotType slot;
	private final LivingEntity entity;
	private final ModelRenderer helmet;

	public ChefHatModel(float modelSize, EquipmentSlotType slot, LivingEntity entity) {
		super(modelSize, 0.0F, 64, 64);
		this.slot = slot;
		this.entity = entity;
		texWidth = 64;
		texHeight = 64;

		boolean testificate =
				this.entity instanceof AbstractIllagerEntity ||
						this.entity.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("savageandravage:skeleton_villager")) ||
						this.entity instanceof ZombieVillagerEntity ||
						this.entity instanceof AbstractVillagerEntity ||
						this.entity.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("guardvillagers:guard"));

		int testificateHelmetShiftUp = testificate ? 2 : 0;

		helmet = new ModelRenderer(this);
		helmet.setPos(0.0F, 0.0F, 0.0F);
		helmet.texOffs(0, 20).addBox(-5.0F, 11.0F - 20.0F - testificateHelmetShiftUp, -5.0F, 10.0F, 3.0F, 10.0F, 0.0F, false);
		helmet.texOffs(0, 0).addBox(-6.0F, 4.0F - 20.0F - testificateHelmetShiftUp, -6.0F, 12.0F, 7.0F, 12.0F, 0.0F, false);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		if (this.slot == EquipmentSlotType.HEAD) {
			matrixStack.pushPose();
			this.helmet.copyFrom(this.head);
			if (this.entity.isBaby()) {
				matrixStack.scale(0.8F, 0.8F, 0.8F);
				this.helmet.setPos(0.0F, 15.0F, 0.0F);
			}
			this.helmet.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			matrixStack.popPose();
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}