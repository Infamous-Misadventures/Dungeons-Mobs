package com.infamous.dungeons_mobs.client.renderer.redstone;

import com.infamous.dungeons_mobs.client.models.redstone.RedstoneMineModel;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneMineEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class RedstoneMineRenderer extends GeoProjectilesRenderer<RedstoneMineEntity> {
   public RedstoneMineRenderer(EntityRendererManager renderManager) {
      super(renderManager, new RedstoneMineModel());
   }
   
   @Override
	protected int getBlockLightLevel(RedstoneMineEntity p_225624_1_, BlockPos p_225624_2_) {
		return 15;
	}

   @Override
   public void render(RedstoneMineEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
      if(entityIn.getLifeTicks() > RedstoneMineEntity.LIFE_TIME){
         return;
      }
      matrixStackIn.translate(0, 0.01F, 0);
      super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
   }

   @Override
   public RenderType getRenderType(RedstoneMineEntity animatable, float partialTicks, MatrixStack stack,
                                   IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                   ResourceLocation textureLocation) {
      return RenderType.entityTranslucent(getTextureLocation(animatable));
   }
}