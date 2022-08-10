package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.armor.IllagerArmorModel;
import com.infamous.dungeons_mobs.client.models.illager.DungeonsIllusionerModel;
import com.infamous.dungeons_mobs.client.models.illager.IllagerBipedModel;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsIllusionerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
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

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class CustomIllusionerRenderer extends ExtendedGeoEntityRenderer<DungeonsIllusionerEntity> {
    public CustomIllusionerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new DungeonsIllusionerModel());
        //this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/enchanter/enchanter_eyes.png")));
        //this.addLayer(new GeoHeldItemLayer<>(this, 0.0, 0.0, 0.5));
    }

    protected void applyRotations(DungeonsIllusionerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        float scaleFactor = 0.9375F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    @Override
    public RenderType getRenderType(DungeonsIllusionerEntity animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String s, DungeonsIllusionerEntity Entity) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String s, DungeonsIllusionerEntity Entity) {
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
    protected BlockState getHeldBlockForBone(String s, DungeonsIllusionerEntity currentEntity) {
        return null;
    }



    @Override
    protected void postRenderItem(MatrixStack stack, ItemStack item, String boneName, DungeonsIllusionerEntity currentEntity, IBone bone) {

    }

    @Override
    protected void postRenderBlock(MatrixStack matrixStack, BlockState blockState, String s, DungeonsIllusionerEntity dungeonsIllusionerEntity) {

    }


    @Override
    protected void preRenderItem(MatrixStack stack, ItemStack item, String s, DungeonsIllusionerEntity windcallerEntity, IBone iBone) {
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
    protected void preRenderBlock(MatrixStack matrixStack, BlockState blockState, String s, DungeonsIllusionerEntity dungeonsIllusionerEntity) {

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

    public Integer getUniqueID(DungeonsIllusionerEntity animatable) {
        return animatable.getId();
    }

    @Override
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, DungeonsIllusionerEntity currentEntity) {
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
            case "armorRightArm2":
                return !currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armorLeftArm":
            case "armorLeftArm2":
                return currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armorBody":
                return EquipmentSlotType.CHEST;
            case "armorHead":
                return EquipmentSlotType.HEAD;
            default:
                return null;
        }
    }
    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, DungeonsIllusionerEntity currentEntity) {
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
            case "armorRightArm2":
            case "armorLeftArm":
            case "armorLeftArm2":
                return chestplate;
            case "armorHead":
                return helmet;
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
            case "armorRightArm2":
                return armorModel.rightArm;
            case "armorLeftArm":
            case "armorLeftArm2":
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
