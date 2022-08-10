package com.infamous.dungeons_mobs.client.renderer.illager;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.illager.GeomancerModel;
import com.infamous.dungeons_mobs.entities.illagers.GeomancerEntity;
import com.infamous.dungeons_mobs.entities.illagers.GeomancerEntity;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.Map;

public class GeomancerRenderer extends ExtendedGeoEntityRenderer<GeomancerEntity> {
	public GeomancerRenderer(EntityRendererManager renderManager) {
		super(renderManager, new GeomancerModel());
		//this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/enchanter/enchanter_eyes.png")));
		//this.addLayer(new GeoHeldItemLayer<>(this, 0.0, 0.0, 0.5));
	}

	protected void applyRotations(GeomancerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
								  float rotationYaw, float partialTicks) {
		float scaleFactor = 0.9375F;
		matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}

	@Override
	public RenderType getRenderType(GeomancerEntity animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Nullable
	@Override
	protected ResourceLocation getTextureForBone(String s, GeomancerEntity Entity) {
		return null;
	}

	@Nullable
	@Override
	protected ItemStack getHeldItemForBone(String s, GeomancerEntity Entity) {
		switch (s) {
			case "leftHand":
				return Entity.isLeftHanded() ? mainHand : offHand;
			case "rightHand":
				return Entity.isLeftHanded() ? offHand : mainHand;
			case DefaultBipedBoneIdents.POTION_BONE_IDENT:
				break;
		}
		return null;
	}

	protected boolean isArmorBone(GeoBone bone) {
		return bone.getName().startsWith("armor");
	}

	@Nullable
	@Override
	protected BlockState getHeldBlockForBone(String s, GeomancerEntity Entity) {
		return null;
	}



	@Override
	protected void postRenderItem(MatrixStack stack, ItemStack item, String boneName, GeomancerEntity currentEntity, IBone bone) {

	}

	@Override
	protected void postRenderBlock(MatrixStack matrixStack, BlockState blockState, String s, GeomancerEntity geomancerEntity) {

	}


	@Override
	protected void preRenderItem(MatrixStack stack, ItemStack item, String s, GeomancerEntity windcallerEntity, IBone iBone) {
	}

	@Override
	protected void preRenderBlock(MatrixStack matrixStack, BlockState blockState, String s, GeomancerEntity geomancerEntity) {

	}

	@Override
	protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack itemStack, String s) {
		switch (s) {
			case "leftHand":
				return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
			case "rightHand":
				return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
			default:
				return ItemCameraTransforms.TransformType.NONE;
		}
	}

	public Integer getUniqueID(GeomancerEntity animatable) {
		return animatable.getId();
	}

	@Nullable
	@Override
	protected ItemStack getArmorForBone(String boneName, GeomancerEntity currentEntity) {
		switch (boneName) {
			case "armorLeftFoot":
			case "armorRightFoot":
			case "armorLeftFoot2":
			case "armorRightFoot2":
				return boots;
			case "armorLeftLeg":
			case "armorRightLeg":
			case "armorLeftLeg2":
			case "armorRightLeg2":
				return leggings;
			case "armorBody":
			case "armorRightArm":
			case "armorLeftArm":
				return chestplate;
			case "armorHead":
				return helmet;
			default:
				return null;
		}
	}
	@Override
	protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, GeomancerEntity currentEntity) {
		switch (boneName) {
			case "armorLeftFoot":
			case "armorRightFoot":
			case "armorLeftFoot2":
			case "armorRightFoot2":
				return EquipmentSlotType.FEET;
			case "armorLeftLeg":
			case "armorRightLeg":
			case "armorLeftLeg2":
			case "armorRightLeg2":
				return EquipmentSlotType.LEGS;
			case "armorRightArm":
				return !currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
			case "armorLeftArm":
				return currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
			case "armorBody":
				return EquipmentSlotType.CHEST;
			case "armorHead":
				return EquipmentSlotType.HEAD;
			default:
				return null;
		}
	}

	@Override
	protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorModel) {
		switch (name) {
			case "armorLeftFoot":
			case "armorLeftLeg":
			case "armorLeftFoot2":
			case "armorLeftLeg2":
				return armorModel.leftLeg;
			case "armorRightFoot":
			case "armorRightLeg":
			case "armorRightFoot2":
			case "armorRightLeg2":
				return armorModel.rightLeg;
			case "armorRightArm":
				return armorModel.rightArm;
			case "armorLeftArm":
				return armorModel.leftArm;
			case "armorBody":
				return armorModel.body;
			case "armorHead":
				return armorModel.head;
			default:
				return null;
		}
	}
}
