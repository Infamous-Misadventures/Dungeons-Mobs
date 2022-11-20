package com.infamous.dungeons_mobs.client.renderer.water;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.renderer.jungle.WhispererRenderer;
import com.infamous.dungeons_mobs.client.renderer.layers.GeoEyeLayer;
import com.infamous.dungeons_mobs.entities.jungle.AbstractVineEntity;
import com.infamous.dungeons_mobs.entities.jungle.WhispererEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class WavewhispererRenderer extends WhispererRenderer {

    private static final ResourceLocation WAVEWHISPERER_TEXTURE = new ResourceLocation(MODID, "textures/entity/ocean/wavewhisperer.png");
    
    @SuppressWarnings("unchecked")
	public WavewhispererRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.addLayer(new GeoEyeLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/wavewhisperer_glow.png")));
    }
    
	public boolean isShaking(WhispererEntity p_230495_1_) {
		return p_230495_1_.isInWrongHabitat();
	}

	@Override
	protected void applyRotations(WhispererEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
			float rotationYaw, float partialTicks) {
		if (this.isShaking(entityLiving)) {
			rotationYaw += (float) (Math.cos((double) entityLiving.tickCount * 3.25D) * Math.PI * (double) 0.4F);
		}
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}
    
    @Override
    public ResourceLocation getTextureLocation(WhispererEntity entity) {
        return WAVEWHISPERER_TEXTURE;
    }
}