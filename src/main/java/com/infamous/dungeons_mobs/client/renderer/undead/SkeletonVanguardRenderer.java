package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.client.models.undead.SkeletonVanguardModel;
import com.infamous.dungeons_mobs.entities.undead.SkeletonVanguardEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class SkeletonVanguardRenderer extends BipedRenderer<SkeletonVanguardEntity, SkeletonVanguardModel<SkeletonVanguardEntity>> {
    private static final ResourceLocation SKELETON_VANGUARD_TEXTURE = new ResourceLocation(MODID, "textures/entity/skeleton/skeleton_vanguard.png");

    public SkeletonVanguardRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SkeletonVanguardModel<>(), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new SkeletonVanguardModel<>(0.5F), new SkeletonVanguardModel<>(1.0F)));
    }

    @Override
    protected void scale(SkeletonVanguardEntity skeletonVanguardEntity, MatrixStack matrixStack, float v) {
        float scaleFactor = 1.1F;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.scale(skeletonVanguardEntity, matrixStack, v);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(SkeletonVanguardEntity entity) {
        return SKELETON_VANGUARD_TEXTURE;
    }
}