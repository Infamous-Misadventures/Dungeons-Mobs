package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.client.models.jungle.QuickGrowingVineModel;
import com.infamous.dungeons_mobs.client.models.jungle.QuickGrowingVineModel1;
import com.infamous.dungeons_mobs.entities.jungle.QuickGrowingVineEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class QuickGrowingVineRenderer<T extends QuickGrowingVineEntity> extends MobRenderer<T, QuickGrowingVineModel1<T>> {
   private static final ResourceLocation QUICK_GROWING_VINE_TEXTURE = new ResourceLocation(MODID, "textures/entity/jungle/quick_growing_vine.png");

   public QuickGrowingVineRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new QuickGrowingVineModel1<>(), 0.5F);
   }

   @Override
   protected void scale(T entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
      float scaleFactor = 1.00F;
      matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
      super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getTextureLocation(T entity) {
      return QUICK_GROWING_VINE_TEXTURE;
   }

   protected void setupRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
   }
}