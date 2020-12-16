package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.entities.undead.ArmoredSkeletonEntity;
import com.infamous.dungeons_mobs.entities.undead.MossySkeletonEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.util.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class CustomSkeletonRenderer extends SkeletonRenderer {
    private static final ResourceLocation MOSSY_SKELETON_TEXTURE = new ResourceLocation(MODID, "textures/entity/skeleton/mossy_skeleton.png");

    public CustomSkeletonRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }


    @Override
    protected void preRenderCallback(AbstractSkeletonEntity abstractSkeletonEntity, MatrixStack matrixStack, float v) {
        if(abstractSkeletonEntity instanceof ArmoredSkeletonEntity){
            float scaleFactor = 1.1F;
            matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        }
        super.preRenderCallback(abstractSkeletonEntity, matrixStack, v);
    }

    public ResourceLocation getEntityTexture(AbstractSkeletonEntity abstractSkeletonEntity) {
        if(abstractSkeletonEntity instanceof MossySkeletonEntity){
            return MOSSY_SKELETON_TEXTURE;
        }
        else{
            return super.getEntityTexture(abstractSkeletonEntity);
        }
    }
}
