package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.jungle.QuickGrowingVineModel;
import com.infamous.dungeons_mobs.client.models.undead.NecromancerModel;
import com.infamous.dungeons_mobs.client.renderer.layer.PulsatingGlowLayer;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class QuickGrowingVineRenderer extends GeoEntityRenderer<QuickGrowingVineEntity> {

    @SuppressWarnings("unchecked")
	public QuickGrowingVineRenderer(EntityRendererManager renderManager) {
        super(renderManager, new QuickGrowingVineModel());
        this.addLayer(new PulsatingGlowLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/jungle/quick_growing_vine_glow.png"), 0.1F, 0.75F, 0.75F));
    }
    
   @Override
	public RenderType getRenderType(QuickGrowingVineEntity animatable, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(textureLocation);
	}
   
   @Override
	protected float getDeathMaxRotation(QuickGrowingVineEntity entityLivingBaseIn) {
		return 0;
	}
}