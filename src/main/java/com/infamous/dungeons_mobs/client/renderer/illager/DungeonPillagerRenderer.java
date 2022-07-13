package com.infamous.dungeons_mobs.client.renderer.illager;

import com.google.common.collect.Maps;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.illager.DungeonPillagerModel;
import com.infamous.dungeons_mobs.client.models.illager.MountaineerModel;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsPillagerEntity;
import com.infamous.dungeons_mobs.entities.illagers.MountaineerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.Map;

public class DungeonPillagerRenderer extends GeoEntityRenderer<DungeonsPillagerEntity> {

	private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
	public DungeonsPillagerEntity thisMage;
	public DungeonPillagerRenderer(EntityRendererManager renderManager) {
		super(renderManager, new DungeonPillagerModel());
		//this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/enchanter/enchanter_eyes.png")));
		//this.addLayer(new GeoHeldItemLayer<>(this, 0.0, 0.0, 0.5));
	}

	protected void applyRotations(DungeonsPillagerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
								  float rotationYaw, float partialTicks) {
		float scaleFactor = 0.9375F;
		matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}

	@Override
	public RenderType getRenderType(DungeonsPillagerEntity animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
	@Override
	public void render(DungeonsPillagerEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
					   IRenderTypeBuffer bufferIn, int packedLightIn) {

		thisMage = entity;

		if (this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null) != null) {
			Minecraft.getInstance().textureManager.bind(this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null));
		}

		if (this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.HEAD), EquipmentSlotType.HEAD, null) != null) {
			Minecraft.getInstance().textureManager.bind(this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.HEAD), EquipmentSlotType.HEAD, null));
		}

		if (this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null) != null) {
			Minecraft.getInstance().textureManager.bind(this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null));
		}

		if (this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null) != null) {
			Minecraft.getInstance().textureManager.bind(this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null));
		}

		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

	}

	@Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

		bufferIn = rtb.getBuffer(RenderType.entityTranslucent(new ResourceLocation(
				DungeonsMobs.MODID, "textures/geo_entity/illager/pillager.png")));

		if (bone.getName().equals("armourLeftArm")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
			if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null) != null) {
				bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null))));
			}
		}

		if (bone.getName().equals("armourRightArm")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
			if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null) != null) {
				bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null))));
			}

		}

		if (bone.getName().equals("armourBody")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
			if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null) != null) {
				bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null))));
			}

		}

		if (bone.getName().equals("innerArmourBody")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
			if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null) != null) {
				bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null))));
			}

		}

		if (bone.getName().equals("innerArmourLeftLeg")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
			if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null) != null) {
				bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null))));
			}

		}

		if (bone.getName().equals("innerArmourRightLeg")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
			if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null) != null) {
				bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null))));
			}

		}

		if (bone.getName().equals("armourLeftLeg")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
			if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null) != null) {
				bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null))));
			}

		}

		if (bone.getName().equals("armourRightLeg")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
			if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null) != null) {
				bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null))));
			}

		}

		if (bone.getName().equals("armourHead")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
			if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.HEAD), EquipmentSlotType.HEAD, null) != null) {
				bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.HEAD), EquipmentSlotType.HEAD, null))));
			}

		}
/*
		if (bone.getName().equals("headWear") && !(thisMage.getItemBySlot(EquipmentSlotType.HEAD).getItem() instanceof ArmorItem)) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
			stack.pushPose();
			//You'll need to play around with these to get item to render in the correct orientation
			stack.mulPose(Vector3f.XP.rotationDegrees(0));
			stack.mulPose(Vector3f.YP.rotationDegrees(0));
			stack.mulPose(Vector3f.ZP.rotationDegrees(0));
			//You'll need to play around with this to render the item in the correct spot.
			stack.translate(0.0D, 1.75D, 0.0D);
			//Sets the scaling of the item.
			stack.scale(0.6F, 0.6F, 0.6F);
			// Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
			Minecraft.getInstance().getItemRenderer().renderStatic(helmet, ItemCameraTransforms.TransformType.HEAD, packedLightIn, packedOverlayIn, stack, this.rtb);
			stack.popPose();
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		}

 */



		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	/**
	 * More generic ForgeHook version of the above function, it allows for Items to have more control over what texture they provide.
	 *
	 * @param entity Entity wearing the armor
	 * @param stack ItemStack for the armor
	 * @param slot Slot ID that the item is in
	 * @param type Subtype, can be null or "overlay"
	 * @return ResourceLocation pointing at the armor's texture
	 */
	public ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, EquipmentSlotType slot, @Nullable String type) {
		if (stack.getItem() instanceof ArmorItem) {
			ArmorItem item = (ArmorItem)stack.getItem();
			String texture = item.getMaterial().getName();
			String domain = "dungeons_mobs";
			int idx = texture.indexOf(':');
			if (idx != -1) {
				domain = texture.substring(0, idx);
				texture = texture.substring(idx + 1);
			}

			String s1 = null;
			if (slot == EquipmentSlotType.HEAD) {
				s1 = String.format("%s:textures/geo_armor/geo_%s_helmet.png", domain, texture, type == null ? "" : String.format("_%s", type));
			} else if (slot == EquipmentSlotType.CHEST) {
				s1 = String.format("%s:textures/geo_armor/geo_%s_chestplate.png", domain, texture, type == null ? "" : String.format("_%s", type));
			} else if (slot == EquipmentSlotType.LEGS) {
				s1 = String.format("%s:textures/geo_armor/geo_%s_leggings.png", domain, texture, type == null ? "" : String.format("_%s", type));
			} else if (slot == EquipmentSlotType.FEET) {
				s1 = String.format("%s:textures/geo_armor/geo_%s_boots.png", domain, texture, type == null ? "" : String.format("_%s", type));
			}

			s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
			ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

			if (resourcelocation == null) {
				resourcelocation = new ResourceLocation(s1);
				ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
			}

			return resourcelocation;
		} else {

			return null;

		}
	}

	public Integer getUniqueID(DungeonsPillagerEntity animatable) {
		return animatable.getId();
	}
}
