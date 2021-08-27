package com.infamous.dungeons_mobs.client.renderer.redstone;

import com.infamous.dungeons_mobs.client.models.redstone.RedstoneCubeModel;
import com.infamous.dungeons_mobs.entities.redstone.RedstoneCubeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class RedstoneCubeRenderer extends MobRenderer<RedstoneCubeEntity, RedstoneCubeModel<RedstoneCubeEntity>> {
   private static final ResourceLocation REDSTONE_CUBE_TEXTURE = new ResourceLocation(MODID, "textures/entity/redstone/redstone_cube.png");

   public RedstoneCubeRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new RedstoneCubeModel<>(0), 0.25F);
   }

   public void render(RedstoneCubeEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
      this.shadowRadius = 0.5F;
      super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
   }

   @Override
   protected void setupRotations(RedstoneCubeEntity redstoneCubeEntity, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      super.setupRotations(redstoneCubeEntity, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
      if (redstoneCubeEntity.isRolling()) {
         float rotationPerTick = 360.0F / 20.0F;
         float rotationAmount = ((float)redstoneCubeEntity.tickCount + partialTicks) * -rotationPerTick;
         this.rollCube(matrixStackIn, rotationAmount);
      }
   }

   private void rollCube(MatrixStack matrixStackIn, float rotationAmount) {
      Vector3d offset = new Vector3d(0.0, 0.5, 0);
      matrixStackIn.translate(offset.x, offset.y, offset.z);
      matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(rotationAmount)); // Forward roll
      matrixStackIn.translate(-offset.x, -offset.y, -offset.z);
   }

   protected void scale(RedstoneCubeEntity redstoneCubeEntity, MatrixStack matrixStackIn, float partialTickTime) {
      matrixStackIn.translate(0.0D, (double)0.001F, 0.0D);
      float sizeScaleFactor = 2.0F; // Big slimes have a size of 2
      //float f2 = MathHelper.lerp(partialTickTime, redstoneCubeEntity.prevSquishFactor, redstoneCubeEntity.squishFactor) / (sizeScaleFactor * 0.5F + 1.0F);
      //float f3 = 1.0F / (f2 + 1.0F);
      matrixStackIn.scale(sizeScaleFactor, sizeScaleFactor, sizeScaleFactor);
   }

   /**
    * Returns the location of an entity's texture.
    */
   @Override
   public ResourceLocation getTextureLocation(RedstoneCubeEntity entity) {
      return REDSTONE_CUBE_TEXTURE;
   }
}