package com.infamous.dungeons_mobs.client.renderer.water;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.jungle.QuickGrowingVineModel;
import com.infamous.dungeons_mobs.client.renderer.jungle.AbstractVineRenderer;
import com.infamous.dungeons_mobs.client.renderer.layers.GeoEyeLayer;
import com.infamous.dungeons_mobs.entities.jungle.AbstractVineEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuickGrowingKelpRenderer extends AbstractVineRenderer {
	
    private static final ResourceLocation QUICK_GROWING_KELP_TEXTURE = new ResourceLocation(MODID, "textures/entity/ocean/quick_growing_kelp.png");
    
    @SuppressWarnings("unchecked")
	public QuickGrowingKelpRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new QuickGrowingVineModel());
        this.addLayer(new GeoEyeLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/quick_growing_kelp_glow.png")));
    }
    
    @Override
    public ResourceLocation getTextureLocation(AbstractVineEntity entity) {
        return QUICK_GROWING_KELP_TEXTURE;
    }
}