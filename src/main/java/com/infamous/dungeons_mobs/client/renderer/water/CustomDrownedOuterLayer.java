package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.interfaces.IArmoredMob;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.DrownedModel;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomDrownedOuterLayer<T extends DrownedEntity> extends LayerRenderer<T, DrownedModel<T>> {
   private static final ResourceLocation DROWNED_OUTER_LAYER_LOCATION = new ResourceLocation("textures/entity/zombie/drowned_outer_layer.png");
   private static final ResourceLocation SEAWEED_ARMORED_DROWNED_OUTER_LAYER_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/seaweed_armored_drowned_outer_layer.png");
   private static final ResourceLocation PALE_ARMORED_DROWNED_OUTER_LAYER_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/pale_armored_drowned_outer_layer.png");
   private final DrownedModel<T> model = new DrownedModel<>(0.25F, 0.0F, 64, 64);

   public CustomDrownedOuterLayer(IEntityRenderer<T, DrownedModel<T>> p_i50943_1_) {
      super(p_i50943_1_);
   }

   public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225628_3_, T drowned, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
      coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, getLayerTexture(drowned), matrixStack, renderTypeBuffer, p_225628_3_, drowned, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_, p_225628_7_, 1.0F, 1.0F, 1.0F);
   }

   protected ResourceLocation getLayerTexture(T drowned) {
      if(drowned instanceof IArmoredMob){
         if(((IArmoredMob) drowned).hasStrongArmor()){
            return PALE_ARMORED_DROWNED_OUTER_LAYER_LOCATION;
         } else{
            return SEAWEED_ARMORED_DROWNED_OUTER_LAYER_LOCATION;
         }
      } else{
         return DROWNED_OUTER_LAYER_LOCATION;
      }
   }
}