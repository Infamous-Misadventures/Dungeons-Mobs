package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.entities.undead.MossySkeletonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class CustomSkeletonRenderer extends SkeletonRenderer {
    private static final ResourceLocation MOSSY_SKELETON_TEXTURE = new ResourceLocation(MODID, "textures/entity/skeleton/mossy_skeleton.png");

    public CustomSkeletonRenderer(EntityRendererProvider.Context renderContext) {
        super(renderContext);
    }

    public ResourceLocation getTextureLocation(AbstractSkeleton abstractSkeletonEntity) {
        if (abstractSkeletonEntity instanceof MossySkeletonEntity) {
            return MOSSY_SKELETON_TEXTURE;
        } else {
            return super.getTextureLocation(abstractSkeletonEntity);
        }
    }
}
