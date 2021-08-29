package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.client.renderer.jungle.PoisonQuillVineRenderer;
import com.infamous.dungeons_mobs.entities.water.PoisonAnemoneEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class PoisonAnemoneRenderer<T extends PoisonAnemoneEntity> extends PoisonQuillVineRenderer<T> {
    private static final ResourceLocation POISON_ANEMONE_TEXTURE = new ResourceLocation(MODID, "textures/entity/ocean/poison_anemone.png");

    public PoisonAnemoneRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return POISON_ANEMONE_TEXTURE;
    }
}
