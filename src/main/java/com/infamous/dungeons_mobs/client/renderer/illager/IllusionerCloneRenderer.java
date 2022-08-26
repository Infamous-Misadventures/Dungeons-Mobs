package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.DungeonsIllusionerModel;
import com.infamous.dungeons_mobs.entities.illagers.IllusionerCloneEntity;
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
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

import javax.annotation.Nullable;

public class IllusionerCloneRenderer extends ExtendedGeoEntityRenderer<IllusionerCloneEntity> {
    public IllusionerCloneRenderer(EntityRendererManager renderManager) {
        super(renderManager, new DungeonsIllusionerModel());
        //this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/enchanter/enchanter_eyes.png")));
        //this.addLayer(new GeoHeldItemLayer<>(this, 0.0, 0.0, 0.5));
    }

    protected void applyRotations(IllusionerCloneEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        float scaleFactor = 0.9375F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    @Override
    public RenderType getRenderType(IllusionerCloneEntity animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String s, IllusionerCloneEntity Entity) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String s, IllusionerCloneEntity Entity) {
        switch (s) {
            case "bipedHandLeft":
                return Entity.isLeftHanded() ? mainHand : offHand;
            case "bipedHandRight":
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
    protected BlockState getHeldBlockForBone(String s, IllusionerCloneEntity currentEntity) {
        return null;
    }



    @Override
    protected void postRenderItem(MatrixStack stack, ItemStack item, String boneName, IllusionerCloneEntity currentEntity, IBone bone) {

    }

    @Override
    protected void postRenderBlock(MatrixStack matrixStack, BlockState blockState, String s, IllusionerCloneEntity dungeonsIllusionerEntity) {

    }


    @Override
    protected void preRenderItem(MatrixStack stack, ItemStack item, String s, IllusionerCloneEntity windcallerEntity, IBone iBone) {
        if (item == this.mainHand || item == this.offHand) {
            stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            boolean shieldFlag = item.getItem() instanceof ShieldItem;
            if (item == this.mainHand) {
                if (shieldFlag) {
                    stack.translate(0.0, 0.125, -0.25);
                } else {

                }
            } else {
                if (shieldFlag) {
                    stack.translate(0, 0.125, 0.25);
                    stack.mulPose(Vector3f.YP.rotationDegrees(180));
                } else {

                }

            }
            // stack.mulPose(Vector3f.YP.rotationDegrees(180));

            // stack.scale(0.75F, 0.75F, 0.75F);
        }
    }

    @Override
    protected void preRenderBlock(MatrixStack matrixStack, BlockState blockState, String s, IllusionerCloneEntity dungeonsIllusionerEntity) {

    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack itemStack, String s) {
        switch (s) {
            case "bipedHandLeft":
                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            case "bipedHandRight":
                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            default:
                return ItemCameraTransforms.TransformType.NONE;
        }
    }

    public Integer getUniqueID(IllusionerCloneEntity animatable) {
        return animatable.getId();
    }

    @Override
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, IllusionerCloneEntity currentEntity) {
        switch (boneName) {
            case "armorLeftFoot":
            case "armorLeftFoot2":
            case "armorBipedLeftFoot":
            case "armorBipedRightFoot":
                return EquipmentSlotType.FEET;
            case "armorBipedLeftLeg":
            case "armorBipedRightLeg":
                return EquipmentSlotType.LEGS;
            case "armorBipedRightArm":
            case "armorIllagerRightArm":
            case "armorIllagerArm":
                return !currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armorBipedLeftArm":
            case "armorIllagerLeftArm":
                return currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armorBipedBody":
                return EquipmentSlotType.CHEST;
            case "armorBipedHead":
                return EquipmentSlotType.HEAD;
            default:
                return null;
        }
    }
    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, IllusionerCloneEntity currentEntity) {
        switch (boneName) {
            case "armorLeftFoot":
            case "armorBipedLeftFoot":
            case "armorLeftFoot2":
            case "armorBipedRightFoot":
                return boots;
            case "armorBipedLeftLeg":
            case "armorBipedRightLeg":
                return leggings;
            case "armorBipedBody":
            case "armorIllagerArm":
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
    protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorModel) {
        switch (name) {
            case "armorBipedLeftLeg":
            case "armorBipedLeftFoot":
                return armorModel.leftLeg;
            case "armorBipedRightLeg":
            case "armorBipedRightFoot":
                return armorModel.rightLeg;
            case "armorBipedRightArm":
            case "armorIllagerRightArm":
                return armorModel.rightArm;
            case "armorBipedLeftArm":
            case "armorIllagerLeftArm":
                return armorModel.leftArm;
            case "armorBipedBody":
                return armorModel.body;
            case "armorBipedHead":
                return armorModel.head;
            default:
                return null;
        }
    }
}