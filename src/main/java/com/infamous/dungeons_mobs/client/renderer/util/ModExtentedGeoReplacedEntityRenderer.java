package com.infamous.dungeons_mobs.client.renderer.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public abstract class ModExtentedGeoReplacedEntityRenderer<T extends IAnimatable> extends ModGeoReplacedEntityRenderer<T> {

    public ModExtentedGeoReplacedEntityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<IAnimatable> modelProvider, T animatable) {
        this(renderManager, modelProvider, animatable, 1, 1, 0);
    }

    public static interface IRenderCycle {
        public String name();
    }

    public static enum EModelRenderCycle implements IRenderCycle {
        INITIAL, REPEATED, SPECIAL /* For special use by the user */
    }

    protected float widthScale;
    protected float heightScale;

    protected final Queue<Tuple<GeoBone, ItemStack>> HEAD_QUEUE = new ArrayDeque<>();

    private software.bernie.geckolib3.util.EModelRenderCycle currentModelRenderCycle = software.bernie.geckolib3.util.EModelRenderCycle.INITIAL;

    protected ModExtentedGeoReplacedEntityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<IAnimatable> modelProvider, T animation,
                                         float widthScale, float heightScale, float shadowSize) {
        super(renderManager, modelProvider, animation);
        this.shadowRadius = shadowSize;
        this.widthScale = widthScale;
        this.heightScale = heightScale;
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {

        this.setCurrentModelRenderCycle(software.bernie.geckolib3.util.EModelRenderCycle.INITIAL);
        super.render(entity, animatable, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    protected void renderHeads(PoseStack stack, MultiBufferSource buffer, int packedLightIn) {
        while (!this.HEAD_QUEUE.isEmpty()) {
            Tuple<GeoBone, ItemStack> entry = this.HEAD_QUEUE.poll();

            GeoBone bone = entry.getA();
            ItemStack itemStack = entry.getB();

            stack.pushPose();

            this.moveAndRotateMatrixToMatchBone(stack, bone);

            GameProfile skullOwnerProfile = null;
            if (itemStack.hasTag()) {
                CompoundTag compoundnbt = itemStack.getTag();
                if (compoundnbt.contains("SkullOwner", 10)) {
                    skullOwnerProfile = NbtUtils.readGameProfile(compoundnbt.getCompound("SkullOwner"));
                } else if (compoundnbt.contains("SkullOwner", 8)) {
                    String s = compoundnbt.getString("SkullOwner");
                    if (!StringUtils.isBlank(s)) {
                        SkullBlockEntity.updateGameprofile(new GameProfile((UUID) null, s), (p_172560_) -> {
                            compoundnbt.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), p_172560_));
                        });
                    }
                }
            }
            float sx = 1;
            float sy = 1;
            float sz = 1;
            try {
                GeoCube firstCube = bone.childCubes.get(0);
                if (firstCube != null) {
                    // Calculate scale in relation to a vanilla head (8x8x8 units)
                    sx = firstCube.size.x() / 8;
                    sy = firstCube.size.y() / 8;
                    sz = firstCube.size.z() / 8;
                }
            } catch (IndexOutOfBoundsException ioobe) {
                // Ignore
            }
            stack.scale(1.1875F * sx, 1.1875F * sy, 1.1875F * sz);
            stack.translate(-0.5, 0, -0.5);
            SkullBlock.Type skullblock$type = ((AbstractSkullBlock) ((BlockItem) itemStack.getItem()).getBlock())
                    .getType();
            SkullModelBase skullmodelbase = SkullBlockRenderer
                    .createSkullRenderers(Minecraft.getInstance().getEntityModels()).get(skullblock$type);
            RenderType rendertype = SkullBlockRenderer.getRenderType(skullblock$type, skullOwnerProfile);
            SkullBlockRenderer.renderSkull((Direction) null, 0, 0, stack, buffer, packedLightIn, skullmodelbase,
                    rendertype);
            stack.popPose();

        }

    }

    @Override
    public void render(GeoModel model, Object animatable, float partialTicks, RenderType type, PoseStack matrixStackIn, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.setCurrentModelRenderCycle(software.bernie.geckolib3.util.EModelRenderCycle.REPEATED);
        // Now, render the heads
        this.renderHeads(matrixStackIn, renderTypeBuffer, packedLightIn);
    }

    protected float getWidthScale(T entity) {
        return this.widthScale;
    }

    protected float getHeightScale(T entity) {
        return this.heightScale;
    }

    @Override
    public void renderEarly(Object animatable, PoseStack stackIn, float partialTicks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.rtb = renderTypeBuffer;
        super.renderEarly(animatable, stackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        if (this.getCurrentModelRenderCycle() == software.bernie.geckolib3.util.EModelRenderCycle.INITIAL /* Pre-Layers */) {
            if (animatable instanceof IAnimatable) {
                float width = this.getWidthScale((T) animatable);
                float height = this.getHeightScale((T) animatable);
                stackIn.scale(width, height, width);
            }
        }
    }

    @Override
    public void renderLate(Object animatable, PoseStack stackIn, float partialTicks, MultiBufferSource renderTypeBuffer, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.renderLate(animatable, stackIn, partialTicks, renderTypeBuffer, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.currentVertexBuilderInUse = bufferIn;
        this.currentPartialTicks = partialTicks;
    }

    protected final HumanoidModel<LivingEntity> DEFAULT_BIPED_ARMOR_MODEL_INNER = new HumanoidModel<LivingEntity>(
            Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
    protected final HumanoidModel<LivingEntity> DEFAULT_BIPED_ARMOR_MODEL_OUTER = new HumanoidModel<LivingEntity>(
            Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));

    protected abstract boolean isArmorBone(final GeoBone bone);

    private VertexConsumer currentVertexBuilderInUse;
    private float currentPartialTicks;

    protected void moveAndRotateMatrixToMatchBone(PoseStack stack, GeoBone bone) {
        // First, let's move our render position to the pivot point...
        stack.translate(bone.getPivotX() / 16, bone.getPivotY() / 16, bone.getPivotZ() / 16);

        stack.mulPose(Vector3f.XP.rotationDegrees(bone.getRotationX()));
        stack.mulPose(Vector3f.YP.rotationDegrees(bone.getRotationY()));
        stack.mulPose(Vector3f.ZP.rotationDegrees(bone.getRotationZ()));
    }

    protected void handleArmorRenderingForBone(GeoBone bone, PoseStack stack, VertexConsumer bufferIn,
                                               int packedLightIn, int packedOverlayIn, ResourceLocation currentTexture) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final ItemStack armorForBone = this.getArmorForBone(bone.getName(), (T) currentAnimatable);
        final EquipmentSlot boneSlot = this.getEquipmentSlotForArmorBone(bone.getName(), (T) currentAnimatable);
        if (armorForBone != null && boneSlot != null) {
            // Standard armor
            if (armorForBone.getItem() instanceof ArmorItem) {
                final ArmorItem armorItem = (ArmorItem) armorForBone.getItem();
                final HumanoidModel<?> armorModel = (HumanoidModel<?>) ForgeHooksClient.getArmorModel(
                        (LivingEntity) currentEntity, armorForBone, boneSlot,
                        boneSlot == EquipmentSlot.LEGS ? DEFAULT_BIPED_ARMOR_MODEL_INNER
                                : DEFAULT_BIPED_ARMOR_MODEL_OUTER);
                final ModelPart sourceLimb = this.getArmorPartForBone(bone.getName(), armorModel);

                if (armorModel != null) {
                    if (armorForBone.getItem() instanceof GeoArmorItem) {
                        @SuppressWarnings("unchecked")
                        final GeoArmorRenderer<? extends GeoArmorItem> geoArmorRenderer = GeoArmorRenderer
                                .getRenderer(armorItem.getClass(), this.currentEntity);

                        if (sourceLimb != null) {
                            List<ModelPart.Cube> cubeList;
                            cubeList = sourceLimb.cubes;
                            if (!cubeList.isEmpty()){
                                // IMPORTANT: The first cube is used to define the armor part!!
                                stack.scale(-1, -1, 1);
                                stack.pushPose();

                                this.prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack, false,
                                        boneSlot == EquipmentSlot.CHEST);

                                geoArmorRenderer.setCurrentItem(this.currentEntity, armorForBone, boneSlot);
                                // Just to be safe, it does some modelprovider stuff in there too
                                geoArmorRenderer.applySlot(boneSlot);
                                this.handleGeoArmorBoneVisibility(geoArmorRenderer, sourceLimb, armorModel, boneSlot);

                                @SuppressWarnings("unchecked")
                                VertexConsumer ivb = ItemRenderer.getArmorFoilBuffer(rtb,
                                        RenderType.armorCutoutNoCull(GeoArmorRenderer
                                                .getRenderer(armorItem.getClass(), this.currentEntity)
                                                .getTextureLocation(armorItem)),
                                        false, armorForBone.hasFoil());

                                geoArmorRenderer.render(this.currentPartialTicks, stack, ivb, packedLightIn);

                                stack.popPose();
                            }
                        }
                    }else {
                        List<ModelPart.Cube> cubeList;
                        if (sourceLimb != null) {
                            cubeList = sourceLimb.cubes;
                            if (!cubeList.isEmpty()) {
                                // IMPORTANT: The first cube is used to define the armor part!!
                                this.prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack);
                                stack.scale(-1, -1, 1);

                                stack.pushPose();

                                ResourceLocation armorResource = this.getArmorResource(currentEntity, armorForBone,
                                        boneSlot, null);

                                this.renderArmorOfItem(armorItem, armorForBone, boneSlot, armorResource, sourceLimb, stack,
                                        packedLightIn, packedOverlayIn);

                                stack.popPose();

                            }
                        }
                    }
                    bufferIn = rtb.getBuffer(RenderType.entityTranslucent(currentTexture));
                }
            }
            // Head blocks
            else if (armorForBone.getItem() instanceof BlockItem
                    && ((BlockItem) armorForBone.getItem()).getBlock() instanceof AbstractSkullBlock) {
                this.HEAD_QUEUE.add(new Tuple<>(bone, armorForBone));
            }
        }
    }

    protected void handleGeoArmorBoneVisibility(GeoArmorRenderer<? extends GeoArmorItem> geoArmorRenderer,
                                                ModelPart sourceLimb, HumanoidModel<?> armorModel, EquipmentSlot slot) {
        IBone gbHead = geoArmorRenderer.getGeoModelProvider().getBone(geoArmorRenderer.headBone);
        IBone gbBody = geoArmorRenderer.getGeoModelProvider().getBone(geoArmorRenderer.bodyBone);
        IBone gbArmL = geoArmorRenderer.getGeoModelProvider().getBone(geoArmorRenderer.leftArmBone);
        IBone gbArmR = geoArmorRenderer.getGeoModelProvider().getBone(geoArmorRenderer.rightArmBone);
        IBone gbLegL = geoArmorRenderer.getGeoModelProvider().getBone(geoArmorRenderer.leftLegBone);
        IBone gbLegR = geoArmorRenderer.getGeoModelProvider().getBone(geoArmorRenderer.rightLegBone);
        IBone gbBootL = geoArmorRenderer.getGeoModelProvider().getBone(geoArmorRenderer.leftBootBone);
        IBone gbBootR = geoArmorRenderer.getGeoModelProvider().getBone(geoArmorRenderer.rightBootBone);

        gbHead.setHidden(true);
        gbBody.setHidden(true);
        gbArmL.setHidden(true);
        gbArmR.setHidden(true);
        gbLegL.setHidden(true);
        gbLegR.setHidden(true);
        gbBootL.setHidden(true);
        gbBootR.setHidden(true);

        if (sourceLimb == armorModel.head || sourceLimb == armorModel.hat) {
            gbHead.setHidden(false);
            return;
        }
        if (sourceLimb == armorModel.body) {
            gbBody.setHidden(false);
            return;
        }
        if (sourceLimb == armorModel.leftArm) {
            gbArmL.setHidden(false);
            return;
        }
        if (sourceLimb == armorModel.leftLeg) {
            if (slot == EquipmentSlot.FEET) {
                gbBootL.setHidden(false);
            } else {
                gbLegL.setHidden(false);
            }
            return;
        }
        if (sourceLimb == armorModel.rightArm) {
            gbArmR.setHidden(false);
            return;
        }
        if (sourceLimb == armorModel.rightLeg) {
            if (slot == EquipmentSlot.FEET) {
                gbBootR.setHidden(false);
            } else {
                gbLegR.setHidden(false);
            }
            return;
        }
    }

    protected void renderArmorOfItem(ArmorItem armorItem, ItemStack armorForBone, EquipmentSlot boneSlot,
                                     ResourceLocation armorResource, ModelPart sourceLimb, PoseStack stack, int packedLightIn,
                                     int packedOverlayIn) {
        if (armorItem instanceof DyeableArmorItem) {
            int i = ((DyeableArmorItem) armorItem).getColor(armorForBone);
            float r = (float) (i >> 16 & 255) / 255.0F;
            float g = (float) (i >> 8 & 255) / 255.0F;
            float b = (float) (i & 255) / 255.0F;

            renderArmorPart(stack, sourceLimb, packedLightIn, packedOverlayIn, r, g, b, 1, armorForBone, armorResource);
            renderArmorPart(stack, sourceLimb, packedLightIn, packedOverlayIn, 1, 1, 1, 1, armorForBone,
                    getArmorResource(currentEntity, armorForBone, boneSlot, "overlay"));
        } else {
            renderArmorPart(stack, sourceLimb, packedLightIn, packedOverlayIn, 1, 1, 1, 1, armorForBone, armorResource);
        }
    }


    protected void prepareArmorPositionAndScale(GeoBone bone, List<ModelPart.Cube> cubeList, ModelPart sourceLimb,
                                                PoseStack stack) {
        prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack, false, false);
    }

    protected void prepareArmorPositionAndScale(GeoBone bone, List<ModelPart.Cube> cubeList, ModelPart sourceLimb,
                                                PoseStack stack, boolean geoArmor, boolean modMatrixRot) {
        GeoCube firstCube = bone.childCubes.get(0);
        final ModelPart.Cube armorCube = cubeList.get(0);

        final float targetSizeX = firstCube.size.x();
        final float targetSizeY = firstCube.size.y();
        final float targetSizeZ = firstCube.size.z();

        final float sourceSizeX = Math.abs(armorCube.maxX - armorCube.minX);
        final float sourceSizeY = Math.abs(armorCube.maxY - armorCube.minY);
        final float sourceSizeZ = Math.abs(armorCube.maxZ - armorCube.minZ);

        float scaleX = targetSizeX / sourceSizeX;
        float scaleY = targetSizeY / sourceSizeY;
        float scaleZ = targetSizeZ / sourceSizeZ;

        // Modify position to move point to correct location, otherwise it will be off
        // when the sizes are different
        // Modifications of X and Z doon't seem to be necessary here, so let's ignore
        // them. For now.
        sourceLimb.setPos(-(bone.getPivotX() - ((bone.getPivotX() * scaleX) - bone.getPivotX()) / scaleX),
                -(bone.getPivotY() - ((bone.getPivotY() * scaleY) - bone.getPivotY()) / scaleY),
                (bone.getPivotZ() - ((bone.getPivotZ() * scaleZ) - bone.getPivotZ()) / scaleZ));


        if (!geoArmor) {
            sourceLimb.xRot = -bone.getRotationX();
            sourceLimb.yRot = -bone.getRotationY();
            sourceLimb.zRot = bone.getRotationZ();
        } else {
            // All those *= 2 calls ARE necessary, otherwise the geo armor will apply
            // rotations twice, so to have it only applied one time in the correct direction
            // we add 2x the negative rotation to it
            float xRot = -bone.getRotationX();
            xRot *= 2;
            float yRot = -bone.getRotationY();
            yRot *= 2;
            float zRot = bone.getRotationZ();
            zRot *= 2;
            GeoBone tmpBone = bone.parent;
            while (tmpBone != null) {
                xRot -= tmpBone.getRotationX();
                yRot -= tmpBone.getRotationY();
                zRot += tmpBone.getRotationZ();
                tmpBone = tmpBone.parent;
            }

            if (modMatrixRot) {
                xRot = (float) Math.toRadians(xRot);
                yRot = (float) Math.toRadians(yRot);
                zRot = (float) Math.toRadians(zRot);

                stack.mulPose(new Quaternion(0, 0, zRot, false));
                stack.mulPose(new Quaternion(0, yRot, 0, false));
                stack.mulPose(new Quaternion(xRot, 0, 0, false));
            } else {
                sourceLimb.xRot = xRot;
                sourceLimb.yRot = yRot;
                sourceLimb.zRot = zRot;
            }
        }

        stack.scale(scaleX, scaleY, scaleZ);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        ResourceLocation tfb = this.getCurrentModelRenderCycle() != software.bernie.geckolib3.util.EModelRenderCycle.INITIAL ? null
                : this.getTextureForBone(bone.getName(), (T) this.currentAnimatable);
        boolean customTextureMarker = tfb != null;
        ResourceLocation currentTexture = this.getTextureLocation(this.currentEntity);
        if (customTextureMarker) {
            currentTexture = tfb;

            if (this.rtb != null) {
                RenderType rt = this.getRenderTypeForBone(bone, (T) this.currentAnimatable,
                        this.currentPartialTicks, stack, bufferIn, this.rtb, packedLightIn, currentTexture);
                bufferIn = this.rtb.getBuffer(rt);
            }
        }
        if (this.getCurrentModelRenderCycle() == software.bernie.geckolib3.util.EModelRenderCycle.INITIAL) {
            stack.pushPose();

            // Render armor
            if (this.isArmorBone(bone)) {
                stack.pushPose();
                try {
                    this.handleArmorRenderingForBone(bone, stack, bufferIn, packedLightIn, packedOverlayIn, currentTexture);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                stack.popPose();
            } else {
                ItemStack boneItem = this.getHeldItemForBone(bone.getName(), (T) this.currentAnimatable);
                BlockState boneBlock = this.getHeldBlockForBone(bone.getName(), (T) this.currentAnimatable);
                if (boneItem != null || boneBlock != null) {

                    stack.pushPose();
                    this.handleItemAndBlockBoneRendering(stack, bone, boneItem, boneBlock, packedLightIn,
                            packedOverlayIn);
                    stack.popPose();

                    bufferIn = rtb.getBuffer(RenderType.entityTranslucent(currentTexture));
                }
            }
            stack.popPose();
        }
        this.customBoneSpecificRenderingHook(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue,
                alpha, customTextureMarker, currentTexture);
        // reset buffer
        if (customTextureMarker) {
            bufferIn = this.currentVertexBuilderInUse;
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
    /*
     * Gets called after armor and item rendering but in every render cycle. This
     * serves as a hook for modders to include their own bone specific rendering
     */
    protected void customBoneSpecificRenderingHook(GeoBone bone, PoseStack stack, VertexConsumer bufferIn,
                                                   int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha,
                                                   boolean customTextureMarker, ResourceLocation currentTexture) {
    }

    protected void handleItemAndBlockBoneRendering(PoseStack stack, GeoBone bone, @javax.annotation.Nullable ItemStack boneItem,
                                                   @javax.annotation.Nullable BlockState boneBlock, int packedLightIn, int packedOverlayIn) {
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        RenderUtils.rotate(bone, stack);
        RenderUtils.scale(bone, stack);
        RenderUtils.moveBackFromPivot(bone, stack);

        this.moveAndRotateMatrixToMatchBone(stack, bone);

        if (boneItem != null) {
            this.preRenderItem(stack, boneItem, bone.getName(), (T) this.currentAnimatable, bone);

            this.renderItemStack(stack, this.rtb, packedLightIn, boneItem, bone.getName());

            this.postRenderItem(stack, boneItem, bone.getName(), (T) this.currentAnimatable, bone);
        }
        if (boneBlock != null) {
            this.preRenderBlock(stack, boneBlock, bone.getName(), (T) this.currentAnimatable);

            this.renderBlock(stack, this.rtb, packedLightIn, boneBlock);

            this.postRenderBlock(stack, boneBlock, bone.getName(), (T) this.currentAnimatable);
        }
    }

    protected void renderItemStack(PoseStack stack, MultiBufferSource rtb, int packedLightIn, ItemStack boneItem,
                                   String boneName) {
        Minecraft.getInstance().getItemRenderer().renderStatic(currentEntity, boneItem,
                this.getCameraTransformForItemAtBone(boneItem, boneName), false, stack, rtb, null, packedLightIn,
                LivingEntityRenderer.getOverlayCoords(currentEntity, 0.0F),
                currentEntity.getId());
    }

    protected RenderType getRenderTypeForBone(GeoBone bone, T currentEntityBeingRendered2, float currentPartialTicks2,
                                              PoseStack stack, VertexConsumer bufferIn, MultiBufferSource currentRenderTypeBufferInUse2,
                                              int packedLightIn, ResourceLocation currentTexture) {
        return this.getRenderType(currentEntityBeingRendered2, currentPartialTicks2, stack,
                currentRenderTypeBufferInUse2, bufferIn, packedLightIn, currentTexture);
    }

    // Internal use only. Basically renders the passed "part" of the armor model on
    // a pre-setup location
    protected void renderArmorPart(PoseStack stack, ModelPart sourceLimb, int packedLightIn, int packedOverlayIn,
                                   float red, float green, float blue, float alpha, ItemStack armorForBone, ResourceLocation armorResource) {
        VertexConsumer ivb = ItemRenderer.getArmorFoilBuffer(rtb, RenderType.armorCutoutNoCull(armorResource), false,
                armorForBone.hasFoil());
        sourceLimb.render(stack, ivb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
    @javax.annotation.Nullable
    protected abstract ResourceLocation getTextureForBone(String boneName, T currentEntity);

    @SuppressWarnings("deprecation")
    protected void renderBlock(PoseStack matrixStack, MultiBufferSource rtb, int packedLightIn,
                               BlockState iBlockState) {
        if (iBlockState.getRenderShape() != RenderShape.MODEL) {
            return;
        }
        matrixStack.pushPose();
        matrixStack.translate(-0.25F, -0.25F, -0.25F);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(iBlockState, matrixStack, rtb, packedLightIn,
                OverlayTexture.NO_OVERLAY);
        matrixStack.popPose();
    }

    /*
     * Return null if there is no item
     */
    @javax.annotation.Nullable
    protected abstract ItemStack getHeldItemForBone(String boneName, T currentEntity);

    protected abstract ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName);

    /*
     * Return null if there is no held block
     */
    @javax.annotation.Nullable
    protected abstract BlockState getHeldBlockForBone(String boneName, T currentEntity);

    protected abstract void preRenderItem(PoseStack matrixStack, ItemStack item, String boneName, T currentEntity,
                                          IBone bone);

    protected abstract void preRenderBlock(PoseStack matrixStack, BlockState block, String boneName, T currentEntity);

    protected abstract void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, T currentEntity,
                                           IBone bone);

    protected abstract void postRenderBlock(PoseStack matrixStack, BlockState block, String boneName, T currentEntity);

    /*
     * Return null, if there is no armor on this bone
     *
     */
    @javax.annotation.Nullable
    protected ItemStack getArmorForBone(String boneName, T currentEntity) {
        return null;
    }

    @javax.annotation.Nullable
    protected EquipmentSlot getEquipmentSlotForArmorBone(String boneName, T currentEntity) {
        return null;
    }

    @javax.annotation.Nullable
    protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorModel) {
        return null;
    }

    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

    protected ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, String type) {
        ArmorItem item = (ArmorItem) stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture,
                (slot == EquipmentSlot.LEGS ? 2 : 1), type == null ? "" : String.format("_%s", type));

        s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = (ResourceLocation) ARMOR_TEXTURE_RES_MAP.get(s1);

        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
        }

        return resourcelocation;
    }

}
