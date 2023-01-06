package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.jungle.QuickGrowingVineModel;
import com.infamous.dungeons_mobs.client.renderer.layers.GeoEyeLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuickGrowingVineRenderer extends AbstractVineRenderer {
    @SuppressWarnings("unchecked")
    public QuickGrowingVineRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new QuickGrowingVineModel());
        this.addLayer(new GeoEyeLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/jungle/quick_growing_vine_glow.png")));
    }
}