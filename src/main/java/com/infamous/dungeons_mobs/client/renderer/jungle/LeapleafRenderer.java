package com.infamous.dungeons_mobs.client.renderer.jungle;

import com.infamous.dungeons_mobs.client.models.jungle.LeapleafModel;
import com.infamous.dungeons_mobs.entities.jungle.LeapleafEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class LeapleafRenderer extends MobRenderer<LeapleafEntity, LeapleafModel<LeapleafEntity>> {
   private static final ResourceLocation LEAPLEAF_TEXTURE = new ResourceLocation(MODID, "textures/entity/jungle/leapleaf.png");

   public LeapleafRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new LeapleafModel<>(), 1.4F);
   }

   @Override
   protected void preRenderCallback(LeapleafEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
      float scaleFactor = 1.25F;
      matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
      super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(LeapleafEntity entity) {
      return LEAPLEAF_TEXTURE;
   }

   protected void applyRotations(LeapleafEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
      /*
      if (!((double)entityLiving.limbSwingAmount < 0.01D)) {
         float f = 13.0F;
         float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
         float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
         matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(6.5F * f2));
      }

       */
   }
}