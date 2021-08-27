package com.infamous.dungeons_mobs.client.renderer.summonables;

import com.infamous.dungeons_mobs.client.models.summonables.GeomancerConstructModel;
import com.infamous.dungeons_mobs.entities.summonables.ConstructEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class GeomancerConstructRenderer<T extends ConstructEntity> extends EntityRenderer<T> {

   protected final GeomancerConstructModel<T> pillarModel = new GeomancerConstructModel<T>();

   public GeomancerConstructRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn);
   }

   @Override
   public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
      matrixStackIn.pushPose();
      matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180));
      matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entityIn.yRot));
      ResourceLocation resourceLocation = this.getTextureLocation(entityIn);
      RenderType renderType = this.pillarModel.renderType(resourceLocation);
      IVertexBuilder ivertexbuilder = bufferIn.getBuffer(renderType);
      this.pillarModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      matrixStackIn.popPose();
      super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
   }
}