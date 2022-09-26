package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.jungle.PoisonQuillVineModel;
import com.infamous.dungeons_mobs.client.renderer.layers.GeoEyeLayer;
import com.infamous.dungeons_mobs.client.renderer.layers.PulsatingGlowLayer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PoisonQuillVineRenderer extends AbstractVineRenderer {
    @SuppressWarnings("unchecked")
	public PoisonQuillVineRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PoisonQuillVineModel());
        this.addLayer(new GeoEyeLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/jungle/poison_quill_vine_glow.png")));
    }
}