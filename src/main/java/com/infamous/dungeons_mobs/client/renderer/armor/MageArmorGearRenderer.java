package com.infamous.dungeons_mobs.client.renderer.armor;

import com.infamous.dungeons_libraries.client.renderer.gearconfig.ArmorGearRenderer;
import com.infamous.dungeons_mobs.client.models.armor.MageArmorGearModel;
import com.infamous.dungeons_mobs.items.armor.MageArmorGear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MageArmorGearRenderer extends ArmorGearRenderer<MageArmorGear> {

	public MageArmorGearRenderer() {
		super(new MageArmorGearModel<>());
	}

	@Override
	public void render(float partialTicks, PoseStack stack, VertexConsumer bufferIn, int packedLightIn) {

		AnimatedGeoModel<MageArmorGear> geoModelProvider = getGeoModelProvider();
		if (geoModelProvider instanceof MageArmorGearModel) {
			((MageArmorGearModel<MageArmorGear>) geoModelProvider).setWearer(this.entityLiving);
		}
		super.render(partialTicks, stack, bufferIn, packedLightIn);
	}
}
