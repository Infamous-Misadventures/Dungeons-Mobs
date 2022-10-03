package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.entities.undead.MossySkeletonEntity;
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

    public ResourceLocation getTextureLocation(AbstractSkeletonEntity abstractSkeletonEntity) {
        if(abstractSkeletonEntity instanceof MossySkeletonEntity){
            return MOSSY_SKELETON_TEXTURE;
        }
        else{
            return super.getTextureLocation(abstractSkeletonEntity);
        }
    }
}
