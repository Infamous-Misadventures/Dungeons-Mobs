package com.infamous.dungeons_mobs.client.renderer.creeper;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

import com.infamous.dungeons_mobs.client.models.creeper.IcyCreeperModel;
import com.infamous.dungeons_mobs.client.renderer.layer.IcyCreeperChargeLayer;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IcyCreeperRenderer extends MobRenderer<IcyCreeperEntity, IcyCreeperModel<IcyCreeperEntity>> {
	private static final ResourceLocation ICY_CREEPER_TEXTURE = new ResourceLocation(MODID,"textures/entity/creeper/icy_creeper.png");

   public IcyCreeperRenderer(EntityRendererManager p_i46186_1_) {
      super(p_i46186_1_, new IcyCreeperModel<>(0.0F), 0.5F);
      this.addLayer(new IcyCreeperChargeLayer(this));
   }

   protected void scale(IcyCreeperEntity p_225620_1_, MatrixStack p_225620_2_, float p_225620_3_) {
      float f = p_225620_1_.getSwelling(p_225620_3_);
      float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
      f = MathHelper.clamp(f, 0.0F, 1.0F);
      f = f * f;
      f = f * f;
      float f2 = (1.0F + f * 0.4F) * f1;
      float f3 = (1.0F + f * 0.1F) / f1;
      p_225620_2_.scale(f2, f3, f2);
   }

   protected float getWhiteOverlayProgress(IcyCreeperEntity p_225625_1_, float p_225625_2_) {
      float f = p_225625_1_.getSwelling(p_225625_2_);
      return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
   }

   public ResourceLocation getTextureLocation(IcyCreeperEntity p_110775_1_) {
      return ICY_CREEPER_TEXTURE;
   }
}
