package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.client.renderer.jungle.PoisonQuillVineRenderer;
import com.infamous.dungeons_mobs.entities.water.PoisonAnemoneEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
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
    
    @Override
 	public RenderType getRenderType(T animatable, float partialTicks, MatrixStack stack,
 			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
 			ResourceLocation textureLocation) {
 		return RenderType.entityTranslucent(textureLocation);
 	}
}
