package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.client.models.jungle.PoisonQuillVineModel;
import com.infamous.dungeons_mobs.client.models.jungle.PoisonQuillVineModel1;
import com.infamous.dungeons_mobs.entities.jungle.PoisonQuillVineEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class PoisonQuillVineRenderer<T extends PoisonQuillVineEntity> extends MobRenderer<T, PoisonQuillVineModel1<T>> {
   private static final ResourceLocation POISON_QUILL_VINE_TEXTURE = new ResourceLocation(MODID, "textures/entity/jungle/poison_quill_vine.png");

   public PoisonQuillVineRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new PoisonQuillVineModel1<>(), 0.5F);
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
      return POISON_QUILL_VINE_TEXTURE;
   }

   protected void setupRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
   }
}