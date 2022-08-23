package com.infamous.dungeons_mobs.client.models.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlastlingBulletModel extends Model {
   protected final ModelRenderer head;

   public BlastlingBulletModel() {
      this(0, 0, 12, 6);
   }

   public BlastlingBulletModel(int p_i51060_1_, int p_i51060_2_, int p_i51060_3_, int p_i51060_4_) {
      super(RenderType::entityTranslucent);
      this.texWidth = p_i51060_3_;
      this.texHeight = p_i51060_4_;
      this.head = new ModelRenderer(this, p_i51060_1_, p_i51060_2_);
      this.head.addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F);
      this.head.setPos(0.0F, 0.0F, 0.0F);
   }

   public void setupAnim(float p_225603_1_, float p_225603_2_, float p_225603_3_) {
      this.head.yRot = p_225603_2_ * ((float)Math.PI / 180F);
      this.head.xRot = p_225603_3_ * ((float)Math.PI / 180F);
   }

   public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
      this.head.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
   }
}
