package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.redstone.RedstoneGolemModel;
import com.infamous.dungeons_mobs.client.models.summonables.DungeonsVexModel;
import com.infamous.dungeons_mobs.client.models.summonables.IceCloudModel;
import com.infamous.dungeons_mobs.client.renderer.layer.GeoEyeLayer;
import com.infamous.dungeons_mobs.entities.illagers.ArmoredVindicatorEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.infamous.dungeons_mobs.entities.summonables.DungeonsVexEntity;
import com.infamous.dungeons_mobs.entities.summonables.IceCloudEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class DungeonsVexRenderer extends GeoEntityRenderer<DungeonsVexEntity> {
	public DungeonsVexEntity thisMage;
	public DungeonsVexRenderer(EntityRendererManager renderManager) {
		super(renderManager, new DungeonsVexModel());

		this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/redstone/redstone_golem_light.png")));
		//this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/enchanter/enchanter_eyes.png")));
		//this.addLayer(new GeoHeldItemLayer<>(this, 0.0, 0.0, 0.5));
	}

	protected void applyRotations(DungeonsVexEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
								  float rotationYaw, float partialTicks) {
		float scaleFactor = 0.425f;
		matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}

	@Override
	public RenderType getRenderType(DungeonsVexEntity animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void render(DungeonsVexEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
					   IRenderTypeBuffer bufferIn, int packedLightIn) {

		thisMage = entity;
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

	}
	@Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		bufferIn = rtb.getBuffer(RenderType.entityTranslucent(
						new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/vex.png")));

		if (bone.getName().equals("rightItems")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			stack.pushPose();
			//You'll need to play around with these to get item to render in the correct orientation
			stack.mulPose(Vector3f.XP.rotationDegrees(-75));
			stack.mulPose(Vector3f.YP.rotationDegrees(0));
			stack.mulPose(Vector3f.ZP.rotationDegrees(0));
			//You'll need to play around with this to render the item in the correct spot.
			stack.translate(0.4D, 0.4D, 0.8D);
			//Sets the scaling of the item.
			stack.scale(1.0f, 1.0f, 1.0f);
			// Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
			Minecraft.getInstance().getItemRenderer().renderStatic(offHand, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
			stack.popPose();

		}


		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}

