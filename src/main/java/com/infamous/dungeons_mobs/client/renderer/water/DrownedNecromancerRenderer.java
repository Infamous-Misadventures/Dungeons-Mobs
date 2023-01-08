package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.ocean.DrownedNecromancerModel;
import com.infamous.dungeons_mobs.client.renderer.layers.PulsatingGlowLayer;
import com.infamous.dungeons_mobs.entities.water.DrownedNecromancerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import javax.annotation.Nullable;

public class DrownedNecromancerRenderer extends ExtendedGeoEntityRenderer<DrownedNecromancerEntity> {

    @SuppressWarnings("unchecked")
    public DrownedNecromancerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DrownedNecromancerModel());
        this.addLayer(new PulsatingGlowLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/drowned_necromancer_eyes.png"), 0.2F, 0.5F, 1.0F));
    }

    @Override
    protected void applyRotations(DrownedNecromancerEntity entityLiving, PoseStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        float scaleFactor = 1.5F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);

    }

    @Override
    public RenderType getRenderType(DrownedNecromancerEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.isArmorBone(bone)) {
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
    protected ResourceLocation getTextureForBone(String s, DrownedNecromancerEntity windcallerEntity) {
        return null;
    }

    @Override
    protected ItemStack getHeldItemForBone(String boneName, DrownedNecromancerEntity currentEntity) {
        switch (boneName) {
            case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
                return currentEntity.isLeftHanded() ? mainHand : offHand;
            case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
                return currentEntity.isLeftHanded() ? offHand : mainHand;
            case DefaultBipedBoneIdents.POTION_BONE_IDENT:
                break;
        }
        return null;
    }

    @Override
    protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        switch (boneName) {
            case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
                return TransformType.THIRD_PERSON_RIGHT_HAND;
            case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
                return TransformType.THIRD_PERSON_RIGHT_HAND;
            default:
                return TransformType.NONE;
        }
    }

    @Override
    protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, DrownedNecromancerEntity currentEntity, IBone bone) {
        if (item == this.mainHand) {
            stack.mulPose(Vector3f.XP.rotationDegrees(-90f));

            if (item.getItem() instanceof ShieldItem)
                stack.translate(0, 0.125, -0.25);
        }
        else if (item == this.offHand) {
            stack.mulPose(Vector3f.XP.rotationDegrees(-90f));

            if (item.getItem() instanceof ShieldItem) {
                stack.translate(0, 0.125, 0.25);
                stack.mulPose(Vector3f.YP.rotationDegrees(180));
            }
        }
    }

    @Override
    protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, DrownedNecromancerEntity currentEntity, IBone bone) {

    }

    @Override
    protected BlockState getHeldBlockForBone(String boneName, DrownedNecromancerEntity currentEntity) {
        return null;
    }

    @Override
    protected void preRenderBlock(PoseStack matrixStack, BlockState block, String boneName,
                                  DrownedNecromancerEntity currentEntity) {

    }

    @Override
    protected void postRenderBlock(PoseStack matrixStack, BlockState block, String boneName,
                                   DrownedNecromancerEntity currentEntity) {

    }

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, DrownedNecromancerEntity currentEntity) {
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
            case "armorIllagerRightArm":
            case "armorIllagerLeftArm":
                return chestplate;
            case "armorBipedHead":
                return helmet;
            default:
                return null;
        }
    }

    @Override
    protected EquipmentSlot getEquipmentSlotForArmorBone(String boneName, DrownedNecromancerEntity currentEntity) {
        switch (boneName) {
            case "armorBipedLeftFoot":
            case "armorBipedRightFoot":
                return EquipmentSlot.FEET;
            case "armorBipedLeftLeg":
            case "armorBipedRightLeg":
                return EquipmentSlot.LEGS;
            case "armorBipedRightHand":
                return !currentEntity.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            case "armorBipedLeftHand":
                return currentEntity.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            case "armorBipedRightArm":
            case "armorBipedLeftArm":
            case "armorIllagerRightArm":
            case "armorIllagerLeftArm":
            case "armorBipedBody":
                return EquipmentSlot.CHEST;
            case "armorBipedHead":
                return EquipmentSlot.HEAD;
            default:
                return null;
        }
    }

    @Override
    protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorBipedModel) {
        switch (name) {
            case "armorBipedLeftFoot":
            case "armorBipedLeftLeg":
                return armorBipedModel.leftLeg;
            case "armorBipedRightFoot":
            case "armorBipedRightLeg":
                return armorBipedModel.rightLeg;
            case "armorBipedRightArm":
            case "armorIllagerRightArm":
                return armorBipedModel.rightArm;
            case "armorBipedLeftArm":
            case "armorIllagerLeftArm":
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
    protected void setLimbBoneVisible(GeoArmorRenderer<? extends GeoArmorItem> armorRenderer, ModelPart limb, HumanoidModel<?> armorModel, EquipmentSlot slot) {
        if (limb == armorModel.head || limb == armorModel.hat) {
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.headBone).setHidden(false);
        }
        else if (limb == armorModel.body) {
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.bodyBone).setHidden(false);
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.leftArmBone).setHidden(true);
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.rightArmBone).setHidden(true);
        }
        else if (limb == armorModel.leftArm) {
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.bodyBone).setHidden(true);
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.leftArmBone).setHidden(false);
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.rightArmBone).setHidden(true);
        }
        else if (limb == armorModel.leftLeg) {
            armorRenderer.getGeoModelProvider().getBone((slot == EquipmentSlot.FEET ? armorRenderer.leftBootBone : armorRenderer.leftLegBone)).setHidden(false);
            armorRenderer.getGeoModelProvider().getBone((slot == EquipmentSlot.FEET ? armorRenderer.leftLegBone : armorRenderer.leftBootBone)).setHidden(true);
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.rightBootBone).setHidden(true);
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.rightLegBone).setHidden(true);
        }
        else if (limb == armorModel.rightArm) {
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.bodyBone).setHidden(true);
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.leftArmBone).setHidden(true);
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.rightArmBone).setHidden(false);
        }
        else if (limb == armorModel.rightLeg) {
            armorRenderer.getGeoModelProvider().getBone((slot == EquipmentSlot.FEET ? armorRenderer.rightBootBone : armorRenderer.rightLegBone)).setHidden(false);
            armorRenderer.getGeoModelProvider().getBone((slot == EquipmentSlot.FEET ? armorRenderer.rightLegBone : armorRenderer.rightBootBone)).setHidden(true);
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.leftBootBone).setHidden(true);
            armorRenderer.getGeoModelProvider().getBone(armorRenderer.leftLegBone).setHidden(true);
        }
    }
}