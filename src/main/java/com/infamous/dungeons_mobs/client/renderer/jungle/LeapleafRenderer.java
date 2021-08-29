package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.client.models.jungle.LeapleafModel;
import com.infamous.dungeons_mobs.client.models.jungle.LeapleafModel2;
import com.infamous.dungeons_mobs.entities.jungle.LeapleafEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class LeapleafRenderer extends MobRenderer<LeapleafEntity, LeapleafModel2<LeapleafEntity>> {
   private static final ResourceLocation LEAPLEAF_TEXTURE = new ResourceLocation(MODID, "textures/entity/jungle/leapleaf.png");

   public LeapleafRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new LeapleafModel2<>(), 1.4F);
   }

   @Override
   protected void scale(LeapleafEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
      float scaleFactor = 1.25F;
      matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
      super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getTextureLocation(LeapleafEntity entity) {
      return LEAPLEAF_TEXTURE;
   }

}