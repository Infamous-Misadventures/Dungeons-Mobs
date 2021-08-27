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
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class PillagerHelmetModel<T extends LivingEntity> extends BipedModel<T> {
	private final EquipmentSlotType slot;
	private final LivingEntity entity;
	private final ModelRenderer everything;
	private final ModelRenderer helmet;
	private final ModelRenderer flap1;
	private final ModelRenderer flap2;
	private final ModelRenderer flap3;

	public PillagerHelmetModel(float modelSize, EquipmentSlotType slot, LivingEntity entity) {
		super(modelSize, 0.0F, 64, 64);
		this.slot = slot;
		this.entity = entity;
		texWidth = 64;
		texHeight = 64;

		everything = new ModelRenderer(this);
		everything.setPos(0.0F, 12.0F, 0.0F);

		boolean testificate =
				this.entity instanceof AbstractIllagerEntity ||
						this.entity.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("savageandravage:skeleton_villager")) ||
						this.entity instanceof ZombieVillagerEntity ||
						this.entity instanceof AbstractVillagerEntity ||
						this.entity.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("guardvillagers:guard"));

		int testificateHelmetShiftUp = testificate ? 2 : 0;

		helmet = new ModelRenderer(this);
		helmet.setPos(0.0F, -24.0F, 0.0F);
		everything.addChild(helmet);
		helmet.texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 6.0F, 8.0F, 1.0F, false);

		flap1 = new ModelRenderer(this);
		flap1.setPos(-1.0F, 0.0F, -4.0F);
		helmet.addChild(flap1);
		setRotationAngle(flap1, 0.0F, 0.0F, 0.4363F);
		flap1.texOffs(46, 27).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 3.0F, 8.0F, 1.0F, false);

		flap2 = new ModelRenderer(this);
		flap2.setPos(1.0F, 0.0F, -4.0F);
		helmet.addChild(flap2);
		setRotationAngle(flap2, 0.0F, 0.0F, -0.4363F);
		flap2.texOffs(46, 27).addBox(4.0F, -3.0F, 0.0F, 1.0F, 3.0F, 8.0F, 1.0F, true);

		flap3 = new ModelRenderer(this);
		flap3.setPos(4.0F, -3.0F, 7.0F);
		helmet.addChild(flap3);
		setRotationAngle(flap3, 0.4363F, 0.0F, 0.0F);
		flap3.texOffs(45, 30).addBox(-8.0F, -3.0F, 0.0F, 8.0F, 3.0F, 1.0F, 1.0F, false);
	}

	@Override
	public void setupAnim(T  entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}@Override
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha){
		boolean testificate =
				this.entity instanceof AbstractIllagerEntity ||
						this.entity.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("savageandravage:skeleton_villager")) ||
						this.entity instanceof ZombieVillagerEntity ||
						this.entity instanceof AbstractVillagerEntity ||
						this.entity.getType() == ForgeRegistries.ENTITIES.getValue(new ResourceLocation("guardvillagers:guard"));

		boolean piglin =
				this.entity instanceof PiglinEntity ||
						this.entity instanceof ZombifiedPiglinEntity;



		if (this.slot == EquipmentSlotType.HEAD) {
		/*
			if (piglin) {
				matrixStackIn.push();
				this.piglin_helmet1.copyModelAngles(this.bipedHead);
				if (this.entity.isChild()) {
					matrixStack.scale(0.8F, 0.8F, 0.8F);
					this.piglin_helmet1.setRotationPoint(0.0F, 15.0F, 0.0F);
				}
				this.piglin_helmet1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				matrixStackIn.pop();

			} else if (testificate) {
				matrixStackIn.push();
				this.illager_helmet1.copyModelAngles(this.bipedHead);
				if (this.entity.isChild()) {
					matrixStackIn.scale(0.8F, 0.8F, 0.8F);
					this.illager_helmet1.setRotationPoint(0.0F, 15.0F, 0.0F);
				}
				this.illager_helmet1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				matrixStackIn.pop();
			} else {

		 */
			matrixStackIn.pushPose();
			this.helmet.copyFrom(this.head);
			if (this.entity.isBaby()) {
				matrixStackIn.scale(0.8F, 0.8F, 0.8F);
				this.helmet.setPos(0.0F, 15.0F, 0.0F);
			}
			this.helmet.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStackIn.popPose();
			//}
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}