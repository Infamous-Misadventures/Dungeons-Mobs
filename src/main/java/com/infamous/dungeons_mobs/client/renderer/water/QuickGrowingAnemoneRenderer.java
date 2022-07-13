package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.client.models.jungle.QuickGrowingVineModel;
import com.infamous.dungeons_mobs.client.renderer.jungle.QuickGrowingVineRenderer;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.entities.water.QuickGrowingAnemoneEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class QuickGrowingAnemoneRenderer<T extends QuickGrowingAnemoneEntity> extends GeoEntityRenderer<T> {

    public QuickGrowingAnemoneRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new QuickGrowingVineModel());
    }
}
