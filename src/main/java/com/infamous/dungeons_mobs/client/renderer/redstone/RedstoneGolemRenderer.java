package com.infamous.dungeons_mobs.client.renderer.redstone;

import com.infamous.dungeons_mobs.client.models.illager.GeomancerModel;
import com.infamous.dungeons_mobs.client.models.redstone.RedstoneGolemModel;
import com.infamous.dungeons_mobs.entities.illagers.GeomancerEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class RedstoneGolemRenderer extends GeoEntityRenderer<RedstoneGolemEntity> {
   public RedstoneGolemRenderer(EntityRendererManager renderManager) {
      super(renderManager, new RedstoneGolemModel());
      //this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/enchanter/enchanter_eyes.png")));
      //this.addLayer(new GeoHeldItemLayer<>(this, 0.0, 0.0, 0.5));
   }

   protected void applyRotations(RedstoneGolemEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
                                 float rotationYaw, float partialTicks) {
      float scaleFactor = 1.0f;
      matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
      super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
   }

   @Override
   public RenderType getRenderType(RedstoneGolemEntity animatable, float partialTicks, MatrixStack stack,
                                   IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                   ResourceLocation textureLocation) {
      return RenderType.entityTranslucent(getTextureLocation(animatable));
   }
}