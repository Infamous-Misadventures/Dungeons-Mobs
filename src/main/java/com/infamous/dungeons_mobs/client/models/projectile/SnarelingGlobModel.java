package com.infamous.dungeons_mobs.client.models.projectile;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.LlamaSpitModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SnarelingGlobModel<T extends Entity> extends SegmentedModel<T> {
   private final ModelRenderer main = new ModelRenderer(this).setTexSize(32, 16);

   public SnarelingGlobModel() {
      this(0.0F);
   }

   public SnarelingGlobModel(float p_i47225_1_) {
      int i = 2;
      this.main.texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, p_i47225_1_);
      this.main.setPos(0.0F, 0.0F, 0.0F);
   }

   public void setupAnim(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
   }

   public Iterable<ModelRenderer> parts() {
      return ImmutableList.of(this.main);
   }
}
