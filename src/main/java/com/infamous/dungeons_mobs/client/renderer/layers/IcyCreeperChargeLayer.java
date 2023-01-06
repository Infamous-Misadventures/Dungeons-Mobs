package com.infamous.dungeons_mobs.client.renderer.layers;

import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IcyCreeperChargeLayer extends EnergySwirlLayer<IcyCreeperEntity, CreeperModel<IcyCreeperEntity>> {
	private static final ResourceLocation POWER_LOCATION = new ResourceLocation(
			"textures/entity/creeper/creeper_armor.png");
	private final EntityModel<IcyCreeperEntity> model;

	public IcyCreeperChargeLayer(RenderLayerParent<IcyCreeperEntity, CreeperModel<IcyCreeperEntity>> p_i50947_1_,
			CreeperModel<IcyCreeperEntity> model) {
		super(p_i50947_1_);
		this.model = model;
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