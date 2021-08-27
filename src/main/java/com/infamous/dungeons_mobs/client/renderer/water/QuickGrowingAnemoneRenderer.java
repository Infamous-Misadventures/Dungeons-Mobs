package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.client.renderer.jungle.QuickGrowingVineRenderer;
import com.infamous.dungeons_mobs.entities.water.QuickGrowingAnemoneEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class QuickGrowingAnemoneRenderer<T extends QuickGrowingAnemoneEntity> extends QuickGrowingVineRenderer<T> {
    private static final ResourceLocation QUICK_GROWING_ANEMONE_TEXTURE = new ResourceLocation(MODID, "textures/entity/ocean/quick_growing_anemone.png");

    public QuickGrowingAnemoneRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return QUICK_GROWING_ANEMONE_TEXTURE;
    }
}
