package com.infamous.dungeons_mobs.client.renderer.golem;

import com.infamous.dungeons_mobs.client.models.golem.SquallGolemModel;
import com.infamous.dungeons_mobs.client.models.redstone.RedstoneGolemModel;
import com.infamous.dungeons_mobs.entities.golem.SquallGolemEntity;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneGolemEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class SquallGolemRenderer extends MobRenderer<SquallGolemEntity, SquallGolemModel<SquallGolemEntity>> {
   private static final ResourceLocation SQUALL_GOLEM_TEXTURE = new ResourceLocation(MODID, "textures/entity/golem/squall_golem.png");

   public SquallGolemRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new SquallGolemModel<>(), 1.4F);
      //this.addLayer(new IronGolemCracksLayer(this));
      //this.addLayer(new IronGolenFlowerLayer(this));
   }

   @Override
   protected void scale(SquallGolemEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
      float scaleFactor = 1.00F;
      matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
      super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getTextureLocation(SquallGolemEntity entity) {
      return SQUALL_GOLEM_TEXTURE;
   }

   protected void setupRotations(SquallGolemEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
      if (!((double)entityLiving.animationSpeed < 0.01D)) {
         float f = 13.0F;
         float f1 = entityLiving.animationPosition - entityLiving.animationSpeed * (1.0F - partialTicks) + 6.0F;
         float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
         matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
      }
   }
}