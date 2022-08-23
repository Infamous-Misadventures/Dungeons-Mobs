package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.client.models.jungle.QuickGrowingVineModel;
import com.infamous.dungeons_mobs.client.renderer.jungle.QuickGrowingVineRenderer;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.infamous.dungeons_mobs.entities.water.QuickGrowingAnemoneEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class QuickGrowingAnemoneRenderer<T extends QuickGrowingAnemoneEntity> extends GeoEntityRenderer<T> {

    private static final ResourceLocation QUICK_GROWING_ANEMONE_TEXTURE = new ResourceLocation(MODID, "textures/entity/ocean/quick_growing_anemone.png");
    
    public QuickGrowingAnemoneRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new QuickGrowingVineModel());
    }
    
    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return QUICK_GROWING_ANEMONE_TEXTURE;
    }
    
    @Override
 	public RenderType getRenderType(T animatable, float partialTicks, MatrixStack stack,
 			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
 			ResourceLocation textureLocation) {
 		return RenderType.entityTranslucent(textureLocation);
 	}
}