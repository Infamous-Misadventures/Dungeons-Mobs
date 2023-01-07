package com.infamous.dungeons_mobs.client.renderer.blaze;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.blaze.WildfireModel;
import com.infamous.dungeons_mobs.client.renderer.layers.PulsatingGlowLayer;
import com.infamous.dungeons_mobs.entities.blaze.WildfireEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

import javax.annotation.Nullable;

public class WildfireRenderer extends ExtendedGeoEntityRenderer<WildfireEntity> {
    public WildfireRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WildfireModel());
        this.addLayer(new PulsatingGlowLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/blaze/wildfire.png"), 0.1F, 1.0F, 0.25F));
    }

    @Override
    protected int getBlockLightLevel(WildfireEntity p_225624_1_, BlockPos p_225624_2_) {
        return 15;
    }

    @Override
    protected void applyRotations(WildfireEntity entityLiving, PoseStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        float scaleFactor = 1.25F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);

    }

    @Override
    public RenderType getRenderType(WildfireEntity animatable, float partialTicks, PoseStack stack,
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
    protected ResourceLocation getTextureForBone(String s, WildfireEntity windcallerEntity) {
        return null;
    }

    @Override
    protected ItemStack getHeldItemForBone(String boneName, WildfireEntity currentEntity) {
        return null;
    }

    @Override
    protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return TransformType.NONE;
    }

    @Override
    protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, WildfireEntity currentEntity, IBone bone) {
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
    protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, WildfireEntity currentEntity, IBone bone) {

    }

    @Override
    protected BlockState getHeldBlockForBone(String boneName, WildfireEntity currentEntity) {
        return null;
    }

    @Override
    protected void preRenderBlock(PoseStack matrixStack, BlockState block, String boneName,
                                  WildfireEntity currentEntity) {

    }

    @Override
    protected void postRenderBlock(PoseStack matrixStack, BlockState block, String boneName,
                                   WildfireEntity currentEntity) {

    }

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, WildfireEntity currentEntity) {
        switch (boneName) {
            case "armorHead":
                return helmet;
            default:
                return null;
        }
    }

    @Override
    protected EquipmentSlot getEquipmentSlotForArmorBone(String boneName, WildfireEntity currentEntity) {
        switch (boneName) {
            case "armorHead":
                return EquipmentSlot.HEAD;
            default:
                return null;
        }
    }

    @Override
    protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorBipedModel) {
        switch (name) {
            case "armorHead":
                return armorBipedModel.head;
            default:
                return null;
        }
    }
}