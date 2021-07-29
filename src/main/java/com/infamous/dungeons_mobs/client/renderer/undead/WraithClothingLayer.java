package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.entities.undead.WraithEntity;
import com.infamous.dungeons_mobs.client.models.undead.WraithBipedModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class WraithClothingLayer<T extends WraithEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
   private static final ResourceLocation WRAITH_CLOTHES_TEXTURE = new ResourceLocation(MODID, "textures/entity/wraith/wraith_overlay.png");
   private final WraithBipedModel<T> layerModel = new WraithBipedModel<T>(0.25F, true);

   public WraithClothingLayer(IEntityRenderer<T, M> p_i50919_1_) {
      super(p_i50919_1_);
   }

   public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, WRAITH_CLOTHES_TEXTURE, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);
   }
}