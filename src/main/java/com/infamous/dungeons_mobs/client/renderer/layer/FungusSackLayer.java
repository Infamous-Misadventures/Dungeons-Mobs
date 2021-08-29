package com.infamous.dungeons_mobs.client.renderer.layer;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.FungusSackModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FungusSackLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
   private static final ResourceLocation SACK_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/fungus_sack.png");
   private final FungusSackModel<T> fungusSackModel = new FungusSackModel<>();

   public FungusSackLayer(IEntityRenderer<T, M> entityRenderer) {
      super(entityRenderer);
   }

   public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225628_3_, T wearer, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
      ItemStack itemstack = getSackItem(wearer);
      if (shouldRender(itemstack, wearer)) {
         ResourceLocation resourcelocation = getSackTexture(itemstack, wearer);

         matrixStack.pushPose();
         matrixStack.translate(0.0D, 0.0D, 0.0D);
         this.getParentModel().copyPropertiesTo(this.fungusSackModel);
         this.fungusSackModel.setupAnim(wearer, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
         IVertexBuilder ivertexbuilder = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, RenderType.armorCutoutNoCull(resourcelocation), false, false);
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