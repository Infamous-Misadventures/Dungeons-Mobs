package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.DungeonsIllusionerModel;
import com.infamous.dungeons_mobs.entities.illagers.DungeonsIllusionerEntity;
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

public class DungeonsIllusionerRenderer extends ExtendedGeoEntityRenderer<DungeonsIllusionerEntity> {
    public DungeonsIllusionerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new DungeonsIllusionerModel());
    }
    
    @Override
    public void render(DungeonsIllusionerEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
    		IRenderTypeBuffer bufferIn, int packedLightIn) {
    	if (entity.appearDelay <= 0) {
    		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    	} else {
    		
    	}
    }

    @Override
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
    protected ResourceLocation getTextureForBone(String s, DungeonsIllusionerEntity currentEntity) {
        return null;
    }

    @Override
    protected ItemStack getHeldItemForBone(String boneName, DungeonsIllusionerEntity currentEntity) {
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
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        switch (boneName) {
            case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            default:
                return ItemCameraTransforms.TransformType.NONE;
        }
    }

    @Override
    protected void preRenderItem(MatrixStack stack, ItemStack item, String boneName, DungeonsIllusionerEntity currentEntity, IBone bone) {
        if(item == this.mainHand || item == this.offHand) {
            stack.scale(1.1F, 1.1F, 1.1F);
            stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            boolean shieldFlag = item.getItem() instanceof ShieldItem;
            if(item == this.mainHand) {
                if(shieldFlag) {
                    stack.translate(0.0, 0.125, -0.25);
                } else {

                }
            } else {
                if(shieldFlag) {
                    stack.translate(-0.15, 0.125, 0.05);
                    stack.mulPose(Vector3f.YP.rotationDegrees(90));
                } else {

                }


            }
        }
    }

    @Override
    protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, DungeonsIllusionerEntity currentEntity, IBone bone) {

    }

    @Override
    protected BlockState getHeldBlockForBone(String boneName, DungeonsIllusionerEntity currentEntity) {
        return null;
    }

    @Override
    protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName,
                                  DungeonsIllusionerEntity currentEntity) {

    }

    @Override
    protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName,
                                   DungeonsIllusionerEntity currentEntity) {

    }

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, DungeonsIllusionerEntity currentEntity) {
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
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, DungeonsIllusionerEntity currentEntity) {
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
            case "armorIllagerRightArm":
            case "armorIllagerLeftArm":
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

//    protected void handleArmorRenderingForBone(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn,
//                                               int packedLightIn, int packedOverlayIn, ResourceLocation currentTexture) {
//        final ItemStack armorForBone = this.getArmorForBone(bone.getName(), currentEntityBeingRendered);
//        final EquipmentSlotType boneSlot = this.getEquipmentSlotForArmorBone(bone.getName(),
//                currentEntityBeingRendered);
//        // Armor and geo armor
//        if (armorForBone != null && boneSlot != null) {
//            // Geo armor
//            if (armorForBone.getItem() instanceof ArmorItem) {
//                final ArmorItem armorItem = (ArmorItem) armorForBone.getItem();
//                if (armorForBone.getItem() instanceof IAnimatable) {
//                    @SuppressWarnings("unchecked")
//                    final GeoArmorRenderer<? extends GeoArmorItem> geoArmorRenderer = GeoArmorRenderer
//                            .getRenderer(armorItem.getClass(), this.currentEntityBeingRendered);
//                    final BipedModel<?> armorModel = (BipedModel<?>) geoArmorRenderer;
//
//                    if (armorModel != null) {
//                        ModelRenderer sourceLimb = this.getArmorPartForBone(bone.getName(), armorModel);
//                        if (sourceLimb != null) {
//                            ObjectList<ModelRenderer.ModelBox> cubeList = sourceLimb.cubes;
//                            if (cubeList != null && !cubeList.isEmpty()) {
//                                // IMPORTANT: The first cube is used to define the armor part!!
//                                stack.scale(-1, -1, 1);
//                                stack.pushPose();
//
//                                this.prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack, true,
//                                        boneSlot == EquipmentSlotType.CHEST);
//
//                                geoArmorRenderer.setCurrentItem(this.currentEntityBeingRendered, armorForBone,
//                                        boneSlot);
//                                //Just to be safe, it does some modelprovider stuff in there too
//                                geoArmorRenderer.applySlot(boneSlot);
//                                this.handleGeoArmorBoneVisibility(geoArmorRenderer, sourceLimb, armorModel, boneSlot);
//
//                                @SuppressWarnings("unchecked")
//                                IVertexBuilder ivb = ItemRenderer.getArmorFoilBuffer(rtb,
//                                        RenderType.armorCutoutNoCull(GeoArmorRenderer
//                                                .getRenderer(armorItem.getClass(), this.currentEntityBeingRendered)
//                                                .getTextureLocation(armorItem)),
//                                        false, armorForBone.hasFoil());
//
//                                geoArmorRenderer.render(this.currentPartialTicks, stack, ivb, packedLightIn);
//
//                                stack.popPose();
//
//                                bufferIn = rtb.getBuffer(RenderType.entityTranslucent(currentTexture));
//                            }
//                        }
//                    }
//                }
//                // Normal Armor
//                else {
//                    final BipedModel<?> armorModel = ForgeHooksClient.getArmorModel(currentEntityBeingRendered,
//                            armorForBone, boneSlot, boneSlot == EquipmentSlotType.LEGS ? DEFAULT_BIPED_ARMOR_MODEL_INNER
//                                    : DEFAULT_BIPED_ARMOR_MODEL_OUTER);
//                    if (armorModel != null) {
//                        ModelRenderer sourceLimb = this.getArmorPartForBone(bone.getName(), armorModel);
//                        if (sourceLimb != null) {
//                            ObjectList<ModelRenderer.ModelBox> cubeList = sourceLimb.cubes;
//                            if (cubeList != null && !cubeList.isEmpty()) {
//                                // IMPORTANT: The first cube is used to define the armor part!!
//                                this.prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack);
//                                stack.scale(-1, -1, 1);
//
//                                stack.pushPose();
//
//                                ResourceLocation armorResource = this.getArmorResource(currentEntityBeingRendered,
//                                        armorForBone, boneSlot, null);
//
//                                this.renderArmorOfItem(armorItem, armorForBone, boneSlot, armorResource, sourceLimb,
//                                        stack, packedLightIn, packedOverlayIn);
//
//                                stack.popPose();
//
//                                bufferIn = rtb.getBuffer(RenderType.entityTranslucent(currentTexture));
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
}