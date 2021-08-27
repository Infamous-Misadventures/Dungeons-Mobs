package com.infamous.dungeons_mobs.client.models.redstone;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RedstoneCubeModel<T extends Entity> extends EntityModel<T> {
   private final ModelRenderer cubeBody;

   public RedstoneCubeModel(int cubeBodyTexOffY) {
      this.cubeBody = new ModelRenderer(this, 0, cubeBodyTexOffY);
      if (cubeBodyTexOffY > 0) {
         this.cubeBody.addBox(-3.0F, 17.0F, -3.0F, 6.0F, 6.0F, 6.0F);
      } else {
         this.cubeBody.addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F);
      }

   }

   /**
    * Sets this entity's model rotation angles
    */
   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   }

   @Override
   public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
      this.cubeBody.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
   }
}