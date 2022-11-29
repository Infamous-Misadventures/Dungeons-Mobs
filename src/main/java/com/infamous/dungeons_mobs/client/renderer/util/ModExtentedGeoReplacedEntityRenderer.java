package com.infamous.dungeons_mobs.client.renderer.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.lang3.StringUtils;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class ModExtentedGeoReplacedEntityRenderer<T extends IAnimatable> extends ModGeoReplacedEntityRenderer<T> {

    public ModExtentedGeoReplacedEntityRenderer(EntityRendererManager renderManager, AnimatedGeoModel<IAnimatable> modelProvider, T animatable) {
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

    protected ModExtentedGeoReplacedEntityRenderer(EntityRendererManager renderManager, AnimatedGeoModel<IAnimatable> modelProvider, T animation,
                                         float widthScale, float heightScale, float shadowSize) {
        super(renderManager, modelProvider, animation);
        this.shadowRadius = shadowSize;
        this.widthScale = widthScale;
        this.heightScale = heightScale;
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {

        this.setCurrentModelRenderCycle(software.bernie.geckolib3.util.EModelRenderCycle.INITIAL);
        super.render(entity, animatable, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    protected void renderHeads(MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn) {
        while (!this.HEAD_QUEUE.isEmpty()) {
            Tuple<GeoBone, ItemStack> entry = this.HEAD_QUEUE.poll();

            GeoBone bone = entry.getA();
            ItemStack itemStack = entry.getB();

            stack.pushPose();

            this.moveAndRotateMatrixToMatchBone(stack, bone);

            GameProfile skullOwnerProfile = null;
            if (itemStack.hasTag()) {
                CompoundNBT compoundnbt = itemStack.getTag();
                if (compoundnbt.contains("SkullOwner", 10)) {
                    skullOwnerProfile = NBTUtil.readGameProfile(compoundnbt.getCompound("SkullOwner"));
                } else if (compoundnbt.contains("SkullOwner", 8)) {
                    String s = compoundnbt.getString("SkullOwner");
                    if (!StringUtils.isBlank(s)) {
                        skullOwnerProfile = SkullTileEntity.updateGameprofile(new GameProfile((UUID) null, s));
                        compoundnbt.put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), skullOwnerProfile));
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
            SkullTileEntityRenderer.renderSkull((Direction) null, 0.0F,
                    ((AbstractSkullBlock) ((BlockItem) itemStack.getItem()).getBlock()).getType(), skullOwnerProfile,
                    0F /* limbswing, controls rotation */, stack, buffer, packedLightIn);
            stack.popPose();

        }

    }

    @Override
    public void render(GeoModel model, Object animatable, float partialTicks, RenderType type, MatrixStack matrixStackIn, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
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
    public void renderEarly(Object animatable, MatrixStack stackIn, float partialTicks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
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
    public void renderLate(Object animatable, MatrixStack stackIn, float partialTicks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.renderLate(animatable, stackIn, partialTicks, renderTypeBuffer, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.currentVertexBuilderInUse = bufferIn;
        this.currentPartialTicks = partialTicks;
    }

    protected final BipedModel<LivingEntity> DEFAULT_BIPED_ARMOR_MODEL_INNER = new BipedModel<>(0.5F);
    protected final BipedModel<LivingEntity> DEFAULT_BIPED_ARMOR_MODEL_OUTER = new BipedModel<>(1.0F);

    protected abstract boolean isArmorBone(final GeoBone bone);

    private IVertexBuilder currentVertexBuilderInUse;
    private float currentPartialTicks;

    protected void moveAndRotateMatrixToMatchBone(MatrixStack stack, GeoBone bone) {
        // First, let's move our render position to the pivot point...
        stack.translate(bone.getPivotX() / 16, bone.getPivotY() / 16, bone.getPivotZ() / 16);

        stack.mulPose(Vector3f.XP.rotationDegrees(bone.getRotationX()));
        stack.mulPose(Vector3f.YP.rotationDegrees(bone.getRotationY()));
        stack.mulPose(Vector3f.ZP.rotationDegrees(bone.getRotationZ()));
    }

    protected void handleArmorRenderingForBone(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn,
                                               int packedLightIn, int packedOverlayIn, ResourceLocation currentTexture) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final ItemStack armorForBone = this.getArmorForBone(bone.getName(), (T) currentAnimatable);
        final EquipmentSlotType boneSlot = this.getEquipmentSlotForArmorBone(bone.getName(), (T) currentAnimatable);
        if (armorForBone != null && boneSlot != null) {
            // Standard armor
            if (armorForBone.getItem() instanceof ArmorItem) {
                final ArmorItem armorItem = (ArmorItem) armorForBone.getItem();
                final BipedModel<?> armorModel = (BipedModel<?>) ForgeHooksClient.getArmorModel(
                        (LivingEntity) currentEntity, armorForBone, boneSlot,
                        boneSlot == EquipmentSlotType.LEGS ? DEFAULT_BIPED_ARMOR_MODEL_INNER
                                : DEFAULT_BIPED_ARMOR_MODEL_OUTER);
                final ModelRenderer sourceLimb = this.getArmorPartForBone(bone.getName(), armorModel);

                if (armorModel != null) {
                    if (armorForBone.getItem() instanceof GeoArmorItem) {
                        @SuppressWarnings("unchecked")
                        final GeoArmorRenderer<? extends GeoArmorItem> geoArmorRenderer = GeoArmorRenderer
                                .getRenderer(armorItem.getClass(), this.currentEntity);

                        if (sourceLimb != null) {
                            List<ModelRenderer.ModelBox> cubeList;
                            cubeList = sourceLimb.cubes;
                            if (!cubeList.isEmpty()){
                                // IMPORTANT: The first cube is used to define the armor part!!
                                stack.scale(-1, -1, 1);
                                stack.pushPose();

                                this.prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack, false,
                                        boneSlot == EquipmentSlotType.CHEST);

                                geoArmorRenderer.setCurrentItem(this.currentEntity, armorForBone, boneSlot);
                                // Just to be safe, it does some modelprovider stuff in there too
                                geoArmorRenderer.applySlot(boneSlot);
                                this.handleGeoArmorBoneVisibility(geoArmorRenderer, sourceLimb, armorModel, boneSlot);

                                @SuppressWarnings("unchecked")
                                IVertexBuilder ivb = ItemRenderer.getArmorFoilBuffer(rtb,
                                        RenderType.armorCutoutNoCull(GeoArmorRenderer
                                                .getRenderer(armorItem.getClass(), this.currentEntity)
                                                .getTextureLocation(armorItem)),
                                        false, armorForBone.hasFoil());

                                geoArmorRenderer.render(this.currentPartialTicks, stack, ivb, packedLightIn);

                                stack.popPose();
                            }
                        }
                    }else {
                        List<ModelRenderer.ModelBox> cubeList;
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
                                                ModelRenderer sourceLimb, BipedModel<?> armorModel, EquipmentSlotType slot) {
        IBone gbHead = geoArmorRenderer.getAndHideBone(geoArmorRenderer.headBone);
        IBone gbBody = geoArmorRenderer.getAndHideBone(geoArmorRenderer.bodyBone);
        IBone gbArmL = geoArmorRenderer.getAndHideBone(geoArmorRenderer.leftArmBone);
        IBone gbArmR = geoArmorRenderer.getAndHideBone(geoArmorRenderer.rightArmBone);
        IBone gbLegL = geoArmorRenderer.getAndHideBone(geoArmorRenderer.leftLegBone);
        IBone gbLegR = geoArmorRenderer.getAndHideBone(geoArmorRenderer.rightLegBone);
        IBone gbBootL = geoArmorRenderer.getAndHideBone(geoArmorRenderer.leftBootBone);
        IBone gbBootR = geoArmorRenderer.getAndHideBone(geoArmorRenderer.rightBootBone);

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
            if (slot == EquipmentSlotType.FEET) {
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
            if (slot == EquipmentSlotType.FEET) {
                gbBootR.setHidden(false);
            } else {
                gbLegR.setHidden(false);
            }
            return;
        }
    }

    protected void renderArmorOfItem(ArmorItem armorItem, ItemStack armorForBone, EquipmentSlotType boneSlot,
                                     ResourceLocation armorResource, ModelRenderer sourceLimb, MatrixStack stack, int packedLightIn,
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


    protected void prepareArmorPositionAndScale(GeoBone bone, List<ModelRenderer.ModelBox> cubeList, ModelRenderer sourceLimb,
                                                MatrixStack stack) {
        prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack, false, false);
    }

    protected void prepareArmorPositionAndScale(GeoBone bone, List<ModelRenderer.ModelBox> cubeList, ModelRenderer sourceLimb,
                                                MatrixStack stack, boolean geoArmor, boolean modMatrixRot) {
        GeoCube firstCube = bone.childCubes.get(0);
        final ModelRenderer.ModelBox armorCube = cubeList.get(0);

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
            // xRot *= 1;
            float yRot = -bone.getRotationY();
            // yRot *= 1;
            float zRot = bone.getRotationZ();
            // zRot *= 1;
            /*
             * GeoBone tmpBone = bone.parent; while (tmpBone != null) { xRot -=
             * tmpBone.getRotationX(); yRot -= tmpBone.getRotationY(); zRot +=
             * tmpBone.getRotationZ(); tmpBone = tmpBone.parent; }
             */

            /*
             * if (modMatrixRot) { xRot = (float) Math.toRadians(xRot); yRot = (float)
             * Math.toRadians(yRot); zRot = (float) Math.toRadians(zRot);
             *
             * stack.mulPose(new Quaternion(0, 0, zRot, false)); stack.mulPose(new
             * Quaternion(0, yRot, 0, false)); stack.mulPose(new Quaternion(xRot, 0, 0,
             * false));
             *
             * } else {
             */
            sourceLimb.xRot = xRot;
            sourceLimb.yRot = yRot;
            sourceLimb.zRot = zRot;
            // }
        }

        stack.scale(scaleX, scaleY, scaleZ);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

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
    protected void customBoneSpecificRenderingHook(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn,
                                                   int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha,
                                                   boolean customTextureMarker, ResourceLocation currentTexture) {
    }

    protected void handleItemAndBlockBoneRendering(MatrixStack stack, GeoBone bone, @javax.annotation.Nullable ItemStack boneItem,
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

    protected void renderItemStack(MatrixStack stack, IRenderTypeBuffer rtb, int packedLightIn, ItemStack boneItem,
                                   String boneName) {
        Minecraft.getInstance().getItemInHandRenderer().renderItem(currentEntity, boneItem,
                this.getCameraTransformForItemAtBone(boneItem, boneName), false, stack, rtb, packedLightIn);
    }

    protected RenderType getRenderTypeForBone(GeoBone bone, T currentEntityBeingRendered2, float currentPartialTicks2,
                                              MatrixStack stack, IVertexBuilder bufferIn, IRenderTypeBuffer currentRenderTypeBufferInUse2,
                                              int packedLightIn, ResourceLocation currentTexture) {
        return this.getRenderType(currentEntityBeingRendered2, currentPartialTicks2, stack,
                currentRenderTypeBufferInUse2, bufferIn, packedLightIn, currentTexture);
    }

    // Internal use only. Basically renders the passed "part" of the armor model on
    // a pre-setup location
    protected void renderArmorPart(MatrixStack stack, ModelRenderer sourceLimb, int packedLightIn, int packedOverlayIn,
                                   float red, float green, float blue, float alpha, ItemStack armorForBone, ResourceLocation armorResource) {
        IVertexBuilder ivb = ItemRenderer.getArmorFoilBuffer(rtb, RenderType.armorCutoutNoCull(armorResource), false,
                armorForBone.hasFoil());
        sourceLimb.render(stack, ivb, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
    @javax.annotation.Nullable
    protected abstract ResourceLocation getTextureForBone(String boneName, T currentEntity);

    @SuppressWarnings("deprecation")
    protected void renderBlock(MatrixStack matrixStack, IRenderTypeBuffer rtb, int packedLightIn,
                               BlockState iBlockState) {
        if (iBlockState.getRenderShape() != BlockRenderType.MODEL) {
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

    protected abstract ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName);

    /*
     * Return null if there is no held block
     */
    @javax.annotation.Nullable
    protected abstract BlockState getHeldBlockForBone(String boneName, T currentEntity);

    protected abstract void preRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, T currentEntity,
                                          IBone bone);

    protected abstract void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, T currentEntity);

    protected abstract void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, T currentEntity,
                                           IBone bone);

    protected abstract void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName, T currentEntity);

    /*
     * Return null, if there is no armor on this bone
     *
     */
    @javax.annotation.Nullable
    protected ItemStack getArmorForBone(String boneName, T currentEntity) {
        return null;
    }

    @javax.annotation.Nullable
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, T currentEntity) {
        return null;
    }

    @javax.annotation.Nullable
    protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorModel) {
        return null;
    }

    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

    protected ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlotType slot, String type) {
        ArmorItem item = (ArmorItem) stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture,
                (slot == EquipmentSlotType.LEGS ? 2 : 1), type == null ? "" : String.format("_%s", type));

        s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = (ResourceLocation) ARMOR_TEXTURE_RES_MAP.get(s1);

        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
        }

        return resourcelocation;
    }

}
