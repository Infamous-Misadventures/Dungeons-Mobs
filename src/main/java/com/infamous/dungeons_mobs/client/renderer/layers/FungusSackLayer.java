package com.infamous.dungeons_mobs.client.renderer.layers;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.FungusSackModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FungusSackLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   private static final ResourceLocation SACK_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/fungus_sack.png");
   private final FungusSackModel<T> fungusSackModel;

   public FungusSackLayer(RenderLayerParent<T, M> entityRenderer, FungusSackModel<T> fungusSackModel) {
      super(entityRenderer);
      this.fungusSackModel = fungusSackModel;
   }

   public void render(PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int p_225628_3_, T wearer, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
      ItemStack itemstack = getSackItem(wearer);
      if (shouldRender(itemstack, wearer)) {
         ResourceLocation resourcelocation = getSackTexture(itemstack, wearer);

         matrixStack.pushPose();
         matrixStack.translate(0.0D, 0.0D, 0.0D);
         this.getParentModel().copyPropertiesTo(this.fungusSackModel);
         this.fungusSackModel.setupAnim(wearer, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
         VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(resourcelocation), false, false);
         this.fungusSackModel.renderToBuffer(matrixStack, ivertexbuilder, p_225628_3_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
         matrixStack.popPose();
      }
   }

   private ItemStack getSackItem(T wearer) {
      return ItemStack.EMPTY;
   }

   public boolean shouldRender(ItemStack stack, T entity) {
      return true;
   }

   public ResourceLocation getSackTexture(ItemStack stack, T entity) {
      return SACK_LOCATION;
   }
}