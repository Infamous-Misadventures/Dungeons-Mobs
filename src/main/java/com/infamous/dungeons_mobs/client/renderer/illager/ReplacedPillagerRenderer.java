package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.ReplacedPillagerModel;
import com.infamous.dungeons_mobs.client.renderer.util.ModExtentedGeoReplacedEntityRenderer;
import com.infamous.dungeons_mobs.entities.illagers.ReplacedPillagerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import javax.annotation.Nullable;
import java.util.List;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class ReplacedPillagerRenderer extends ModExtentedGeoReplacedEntityRenderer<ReplacedPillagerEntity> {
    public ReplacedPillagerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedPillagerModel(),
                new ReplacedPillagerEntity()
        );
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
    protected ResourceLocation getTextureForBone(String s, ReplacedPillagerEntity currentEntity) {
        return null;
    }

    @Override
    protected ItemStack getHeldItemForBone(String boneName, ReplacedPillagerEntity currentEntity) {
        switch (boneName) {
            case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
                return currentEntity.getMob().isLeftHanded() ? mainHand : offHand;
            case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
                return currentEntity.getMob().isLeftHanded() ? offHand : mainHand;
            case DefaultBipedBoneIdents.POTION_BONE_IDENT:
                break;
        }
        return null;
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        switch (boneName) {
            case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
                return ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
                return ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            default:
                return ItemTransforms.TransformType.NONE;
        }
    }

    @Override
    protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, ReplacedPillagerEntity currentEntity, IBone bone) {
        if (item == this.mainHand || item == this.offHand) {
            stack.scale(1.1F, 1.1F, 1.1F);
            stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            boolean shieldFlag = item.getItem() instanceof ShieldItem;
            if (item == this.mainHand) {
                if (shieldFlag) {
                    stack.translate(0.0, 0.125, -0.25);
                } else {

                }
            } else {
                if (shieldFlag) {
                    stack.translate(-0.15, 0.125, 0.05);
                    stack.mulPose(Vector3f.YP.rotationDegrees(90));
                } else {

                }


            }
        }
    }

    @Override
    protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, ReplacedPillagerEntity currentEntity, IBone bone) {

    }

    @Override
    protected BlockState getHeldBlockForBone(String boneName, ReplacedPillagerEntity currentEntity) {
        return null;
    }

    @Override
    protected void preRenderBlock(PoseStack matrixStack, BlockState block, String boneName,
                                  ReplacedPillagerEntity currentEntity) {

    }

    @Override
    protected void postRenderBlock(PoseStack matrixStack, BlockState block, String boneName,
                                   ReplacedPillagerEntity currentEntity) {

    }

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, ReplacedPillagerEntity currentEntity) {
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
    protected EquipmentSlot getEquipmentSlotForArmorBone(String boneName, ReplacedPillagerEntity currentEntity) {
        switch (boneName) {
            case "armorBipedLeftFoot":
            case "armorBipedRightFoot":
                return EquipmentSlot.FEET;
            case "armorBipedLeftLeg":
            case "armorBipedRightLeg":
                return EquipmentSlot.LEGS;
            case "armorBipedRightHand":
                return !currentEntity.getMob().isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            case "armorBipedLeftHand":
                return currentEntity.getMob().isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
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
    protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorModel) {
        switch (name) {
            case "armorBipedLeftFoot":
            case "armorBipedLeftLeg":
                return armorModel.leftLeg;
            case "armorBipedRightFoot":
            case "armorBipedRightLeg":
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

    @Override
    protected void prepareArmorPositionAndScale(GeoBone bone, List<ModelPart.Cube> cubeList, ModelPart sourceLimb, PoseStack stack, boolean geoArmor, boolean modMatrixRot) {
        super.prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack, geoArmor, modMatrixRot);
        if (bone.getName().equals("armorBipedHead") && geoArmor && ForgeRegistries.ITEMS.getKey(helmet.getItem()).getNamespace().equals(MODID)) {
            stack.translate(0, 0.125, 0); // 1y is 1 cube up, we want 2/16
        }
    }
}
