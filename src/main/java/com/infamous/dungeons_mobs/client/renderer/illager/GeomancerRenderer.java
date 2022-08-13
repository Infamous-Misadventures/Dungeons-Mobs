package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.GeomancerModel;
import com.infamous.dungeons_mobs.entities.illagers.GeomancerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

import javax.annotation.Nullable;
public class GeomancerRenderer extends ExtendedGeoEntityRenderer<GeomancerEntity> {
	public GeomancerRenderer(EntityRendererManager renderManager) {
		super(renderManager, new GeomancerModel());
	}

	@Override
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

	@Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		if(this.isArmorBone(bone)) {
			bone.setCubesHidden(true);
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	protected boolean isArmorBone(GeoBone bone) {
		return bone.getName().startsWith("armor");
	}

	@Nullable
	@Override
	protected ResourceLocation getTextureForBone(String s, GeomancerEntity windcallerEntity) {
		return null;
	}

	@Nullable
	@Override
	protected ItemStack getHeldItemForBone(String s, GeomancerEntity windcallerEntity) {
		return null;
	}

	@Override
	protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack itemStack, String s) {
		return null;
	}

	@Nullable
	@Override
	protected BlockState getHeldBlockForBone(String s, GeomancerEntity windcallerEntity) {
		return null;
	}

	@Override
	protected void preRenderItem(MatrixStack matrixStack, ItemStack itemStack, String s, GeomancerEntity windcallerEntity, IBone iBone) {

	}

	@Override
	protected void preRenderBlock(MatrixStack matrixStack, BlockState blockState, String s, GeomancerEntity windcallerEntity) {

	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack itemStack, String s, GeomancerEntity windcallerEntity, IBone iBone) {

	}

	@Override
	protected void postRenderBlock(MatrixStack matrixStack, BlockState blockState, String s, GeomancerEntity windcallerEntity) {

	}

	@Nullable
	@Override
	protected ItemStack getArmorForBone(String boneName, GeomancerEntity currentEntity) {
		switch (boneName) {
			case "armorBipedLeftFoot":
			case "armorBipedRightFoot":
				return boots;
			case "armorBipedLeftLeg":
			case "armorBipedRightLeg":
				return leggings;
			case "armorBipedBody":
			case "armorBipedRightArm":
			case "armorBipedLeftArm":
				return chestplate;
			case "armorBipedHead":
				return helmet;
			default:
				return null;
		}
	}

	@Override
	protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, GeomancerEntity currentEntity) {
		switch (boneName) {
			case "armorBipedLeftFoot":
			case "armorBipedRightFoot":
				return EquipmentSlotType.FEET;
			case "armorBipedLeftLeg":
			case "armorBipedRightLeg":
				return EquipmentSlotType.LEGS;
			case "armorBipedRightHand":
				return !currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
			case "armorBipedLeftHand":
				return currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
			case "armorBipedRightArm":
			case "armorBipedLeftArm":
			case "armorBipedBody":
				return EquipmentSlotType.CHEST;
			case "armorBipedHead":
				return EquipmentSlotType.HEAD;
			default:
				return null;
		}
	}

	@Override
	protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorBipedModel) {
		switch (name) {
			case "armorBipedLeftFoot":
			case "armorBipedLeftLeg":
				return armorBipedModel.leftLeg;
			case "armorBipedRightFoot":
			case "armorBipedRightLeg":
				return armorBipedModel.rightLeg;
			case "armorBipedRightArm":
				return armorBipedModel.rightArm;
			case "armorBipedLeftArm":
				return armorBipedModel.leftArm;
			case "armorBipedBody":
				return armorBipedModel.body;
			case "armorBipedHead":
				return armorBipedModel.head;
			default:
				return null;
		}
	}

	@Override
	protected void prepareArmorPositionAndScale(GeoBone bone, ObjectList<ModelRenderer.ModelBox> cubeList, ModelRenderer sourceLimb, MatrixStack stack, boolean geoArmor, boolean modMatrixRot) {
		GeoCube firstCube = (GeoCube)bone.childCubes.get(0);
		ModelRenderer.ModelBox armorCube = (ModelRenderer.ModelBox)cubeList.get(0);
		float targetSizeX = firstCube.size.x();
		float targetSizeY = firstCube.size.y();
		float targetSizeZ = firstCube.size.z();
		float sourceSizeX = Math.abs(armorCube.maxX - armorCube.minX);
		float sourceSizeY = Math.abs(armorCube.maxY - armorCube.minY);
		float sourceSizeZ = Math.abs(armorCube.maxZ - armorCube.minZ);
		float scaleX = targetSizeX / sourceSizeX;
		float scaleY = targetSizeY / sourceSizeY;
		float scaleZ = targetSizeZ / sourceSizeZ;
		sourceLimb.setPos(-(bone.getPivotX() - (bone.getPivotX() * scaleX - bone.getPivotX()) / scaleX), -(bone.getPivotY() - (bone.getPivotY() * scaleY - bone.getPivotY()) / scaleY), bone.getPivotZ() - (bone.getPivotZ() * scaleZ - bone.getPivotZ()) / scaleZ);
		if (!geoArmor) {
			sourceLimb.xRot = -bone.getRotationX();
			sourceLimb.yRot = -bone.getRotationY();
			sourceLimb.zRot = bone.getRotationZ();
		} else {
			float xRot = -bone.getRotationX();
			float yRot = -bone.getRotationY();
			float zRot = bone.getRotationZ();


			for(GeoBone tmpBone = bone.parent; tmpBone != null; tmpBone = tmpBone.parent) {
				if(bone.getName().equals("armorBipedHead") && tmpBone.getName().equals("bipedBody")) {
					break;
				}
				xRot -= tmpBone.getRotationX();
				yRot -= tmpBone.getRotationY();
				zRot += tmpBone.getRotationZ();
			}

			if (modMatrixRot) {
				xRot = (float)Math.toRadians((double)xRot);
				yRot = (float)Math.toRadians((double)yRot);
				zRot = (float)Math.toRadians((double)zRot);
				stack.mulPose(new Quaternion(0.0F, 0.0F, zRot, false));
				stack.mulPose(new Quaternion(0.0F, yRot, 0.0F, false));
				stack.mulPose(new Quaternion(xRot, 0.0F, 0.0F, false));
			} else {
				sourceLimb.xRot = xRot;
				sourceLimb.yRot = yRot;
				sourceLimb.zRot = zRot;
			}
		}

		stack.scale(scaleX, scaleY, scaleZ);
	}
}