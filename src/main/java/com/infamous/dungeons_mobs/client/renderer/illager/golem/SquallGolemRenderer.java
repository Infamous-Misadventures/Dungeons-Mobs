package com.infamous.dungeons_mobs.client.renderer.illager.golem;

import com.infamous.dungeons_mobs.client.models.golem.SquallGolemModel;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
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
public class SquallGolemRenderer extends GeoEntityRenderer<SquallGolemEntity> {

   public SquallGolemRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new SquallGolemModel());
      //this.addLayer(new IronGolemCracksLayer(this));
      //this.addLayer(new IronGolenFlowerLayer(this));
   }

   protected void applyRotations(SquallGolemEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
                                 float rotationYaw, float partialTicks) {
      float scaleFactor = 1.00F;
      matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
      super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
   }

   @Override
   public RenderType getRenderType(SquallGolemEntity animatable, float partialTicks, MatrixStack stack,
                                   IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                   ResourceLocation textureLocation) {
      return RenderType.entityTranslucent(getTextureLocation(animatable));
   }
}