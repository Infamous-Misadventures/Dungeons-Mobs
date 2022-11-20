package com.infamous.dungeons_mobs.client.renderer.layers;

import com.infamous.dungeons_mobs.client.models.creeper.IcyCreeperModel;
import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IcyCreeperChargeLayer extends EnergyLayer<IcyCreeperEntity, IcyCreeperModel<IcyCreeperEntity>> {
   private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
   private final EntityModel<IcyCreeperEntity> model = new IcyCreeperModel<>(1.5F);

   public IcyCreeperChargeLayer(IEntityRenderer<IcyCreeperEntity, IcyCreeperModel<IcyCreeperEntity>> p_i50947_1_) {
      super(p_i50947_1_);
   }

   protected float xOffset(float p_225634_1_) {
      return p_225634_1_ * 0.01F;
   }

   protected ResourceLocation getTextureLocation() {
      return POWER_LOCATION;
   }

   protected EntityModel<IcyCreeperEntity> model() {
      return this.model;
   }
}