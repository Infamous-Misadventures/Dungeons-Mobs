package com.infamous.dungeons_mobs.client.renderer.redstone;

import com.infamous.dungeons_mobs.client.models.redstone.RedstoneGolemModel;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class RedstoneGolemRenderer extends MobRenderer<RedstoneGolemEntity, RedstoneGolemModel<RedstoneGolemEntity>> {
   private static final ResourceLocation REDSTONE_GOLEM_TEXTURE = new ResourceLocation(MODID, "textures/entity/redstone/redstone_golem.png");

   public RedstoneGolemRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new RedstoneGolemModel<>(), 1.4F);
      //this.addLayer(new IronGolemCracksLayer(this));
      //this.addLayer(new IronGolenFlowerLayer(this));
   }

   @Override
   protected void scale(RedstoneGolemEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
      float scaleFactor = 1.00F;
      matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
      super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getTextureLocation(RedstoneGolemEntity entity) {
      return REDSTONE_GOLEM_TEXTURE;
   }

   protected void setupRotations(RedstoneGolemEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
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