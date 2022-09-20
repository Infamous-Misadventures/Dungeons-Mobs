package com.infamous.dungeons_mobs.client.renderer.water;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.jungle.QuickGrowingVineModel;
import com.infamous.dungeons_mobs.client.renderer.layer.PulsatingGlowLayer;
import com.infamous.dungeons_mobs.entities.jungle.AbstractVineEntity;
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
public class QuickGrowingKelpRenderer extends GeoEntityRenderer<AbstractVineEntity> {

    private static final ResourceLocation QUICK_GROWING_ANEMONE_TEXTURE = new ResourceLocation(MODID, "textures/entity/ocean/quick_growing_kelp.png");
    
    @SuppressWarnings("rawtypes")
	public QuickGrowingKelpRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new QuickGrowingVineModel());
        this.addLayer(new PulsatingGlowLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/quick_growing_kelp_glow.png"), 0.1F, 0.75F, 0.75F));
    }
    
    @Override
    public ResourceLocation getTextureLocation(AbstractVineEntity entity) {
        return QUICK_GROWING_ANEMONE_TEXTURE;
    }
    
    @Override
 	public RenderType getRenderType(AbstractVineEntity animatable, float partialTicks, MatrixStack stack,
 			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
 			ResourceLocation textureLocation) {
 		return RenderType.entityTranslucent(textureLocation);
 	}
    
    @Override
 	protected float getDeathMaxRotation(AbstractVineEntity entityLivingBaseIn) {
 		return 0;
 	}
}