package com.infamous.dungeons_mobs.client.renderer.layers;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;


public class SkeletonEyesLayer<T extends Entity, M extends EntityModel<T>> extends AbstractEyesLayer<T, M> {
   private static final RenderType EYES = EyesRenderState.eyes(new ResourceLocation(MODID,"textures/entity/eyes/skeleton_eyes.png"));


   public SkeletonEyesLayer(IEntityRenderer<T, M> p_i50921_1_) {
      super(p_i50921_1_);
   }

   public RenderType renderType() {
      return EYES;
   }
}