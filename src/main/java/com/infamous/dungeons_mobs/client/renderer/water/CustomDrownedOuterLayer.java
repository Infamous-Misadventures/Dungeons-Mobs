package com.infamous.dungeons_mobs.client.renderer.water;

import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import com.infamous.dungeons_mobs.DungeonsMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CustomDrownedOuterLayer<T extends Drowned> extends RenderLayer<T, DrownedModel<T>> {
   private static final ResourceLocation DROWNED_OUTER_LAYER_LOCATION = new ResourceLocation("textures/entity/zombie/drowned_outer_layer.png");
   private static final ResourceLocation SEAWEED_ARMORED_DROWNED_OUTER_LAYER_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/seaweed_armored_drowned_outer_layer.png");
   private static final ResourceLocation PALE_ARMORED_DROWNED_OUTER_LAYER_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/ocean/pale_armored_drowned_outer_layer.png");
   private static final List<ResourceLocation> ARMORED_DROWNED_LOCATIONS = Arrays.asList(SEAWEED_ARMORED_DROWNED_OUTER_LAYER_LOCATION, PALE_ARMORED_DROWNED_OUTER_LAYER_LOCATION);
   private final DrownedModel<T> model;

   public CustomDrownedOuterLayer(RenderLayerParent<T, DrownedModel<T>> p_i50943_1_, DrownedModel<T> model) {
      super(p_i50943_1_);
      this.model = model;
   }

   public void render(PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int p_225628_3_, T drowned, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
      coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, getLayerTexture(drowned), matrixStack, renderTypeBuffer, p_225628_3_, drowned, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_, p_225628_7_, 1.0F, 1.0F, 1.0F);
   }

   protected ResourceLocation getLayerTexture(T drowned) {
      EliteMob cap = EliteMobHelper.getEliteMobCapability(drowned);
      if(cap != null && cap.isElite()){
         return ARMORED_DROWNED_LOCATIONS.get(drowned.getId() % ARMORED_DROWNED_LOCATIONS.size());
      } else{
         return DROWNED_OUTER_LAYER_LOCATION;
      }
   }
}