package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.WindcallerModel;
import com.infamous.dungeons_mobs.entities.illagers.WindcallerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
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
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

import javax.annotation.Nullable;
public class WindcallerRenderer extends ExtendedGeoEntityRenderer<WindcallerEntity> {
    public WindcallerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new WindcallerModel());
    }

    protected void applyRotations(WindcallerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        float scaleFactor = 0.9375F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);

    }

    @Override
    public RenderType getRenderType(WindcallerEntity animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return bone.getName().startsWith("armor");
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String s, WindcallerEntity windcallerEntity) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String s, WindcallerEntity windcallerEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack itemStack, String s) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String s, WindcallerEntity windcallerEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(MatrixStack matrixStack, ItemStack itemStack, String s, WindcallerEntity windcallerEntity, IBone iBone) {

    }

    @Override
    protected void preRenderBlock(MatrixStack matrixStack, BlockState blockState, String s, WindcallerEntity windcallerEntity) {

    }

    @Override
    protected void postRenderItem(MatrixStack matrixStack, ItemStack itemStack, String s, WindcallerEntity windcallerEntity, IBone iBone) {

    }

    @Override
    protected void postRenderBlock(MatrixStack matrixStack, BlockState blockState, String s, WindcallerEntity windcallerEntity) {

    }

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, WindcallerEntity currentEntity) {
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
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, WindcallerEntity currentEntity) {
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